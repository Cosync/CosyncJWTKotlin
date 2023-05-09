/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Copyright @ 2023 cosync. All rights reserved.
 */
package com.cosync.cosyncjwt

import android.content.Context
import com.cosync.cosyncjwt.common.CosyncJWTException
import com.cosync.cosyncjwt.common.SingletonHolder
import com.cosync.cosyncjwt.di.CosyncJWTModule
import com.cosync.cosyncjwt.di.DaggerInjector
import com.cosync.cosyncjwt.model.App
import com.cosync.cosyncjwt.model.Authentication
import com.cosync.cosyncjwt.model.User
import com.cosync.cosyncjwt.repository.AuthRepository
import com.cosync.cosyncjwt.repository.UserRepository

/**
 * Main class used for handling CosyncJWT operations
 *
 * @param	context	[Context] context used for construction
 */
class CosyncJWT constructor(private val context: Context) {
	init {
		DaggerInjector.buildComponent(context)
		DaggerInjector.component.inject(CosyncJWTModule())
	}

	companion object : SingletonHolder<CosyncJWT, Context>(::CosyncJWT)

	private var authRepository: AuthRepository = AuthRepository()
	private var userRepository: UserRepository = UserRepository()

	// Configuration
	private var appToken = ""
	private var cosyncRestAddress = ""

	// Login Credentials
	private var jwt = ""
	private var accessToken = ""
	private var loginToken = ""

	// Signup Credentials
	private var signedUserToken = ""

	/**
	 * Initialization for [CosyncJWT]
	 *
	 * This should be the first function called for initialization.
	 *
	 * @param	appToken			App Token
	 * @param	cosyncRestAddress	Cosync Rest Address
	 */
	@Synchronized
	fun configure(appToken: String, cosyncRestAddress: String? = "") {
		this.appToken = appToken
		this.cosyncRestAddress = cosyncRestAddress.takeUnless { it.isNullOrEmpty() } ?: context.getString(
			R.string.defaultCosyncRestAddress)
	}

	@Synchronized
	fun isLoggedIn(): Boolean {
		return jwt.isNotEmpty() && accessToken.isNotEmpty()
	}

	suspend fun login(handle: String, password: String): Result<Authentication> {
		return try {
			if (appToken.isEmpty() || cosyncRestAddress.isEmpty()) {
				Result.failure(CosyncJWTException("Not configured yet"))
			} else {
				val response = authRepository.login(cosyncRestAddress, appToken, handle, password)
				if (response.isSuccessful) {
					response.body()?.let {
						jwt = it.jwt.orEmpty()
						accessToken = it.accessToken.orEmpty()
						loginToken = it.loginToken.orEmpty()
						Result.success(it)
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun loginComplete(code: String): Result<Authentication> {
		return try {
			val response = authRepository.loginComplete(cosyncRestAddress, appToken, loginToken, code)
			if (response.isSuccessful) {
				response.body()?.let {
					jwt = it.jwt.orEmpty()
					accessToken = it.accessToken.orEmpty()
					Result.success(it)
				} ?: Result.failure(CosyncJWTException("Something went wrong"))
			} else {
				Result.failure(CosyncJWTException("Something went wrong"))
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun loginAnonymous(handle: String): Result<Authentication> {
		return try {
			if (appToken.isEmpty() || cosyncRestAddress.isEmpty()) {
				Result.failure(CosyncJWTException("Not configured yet"))
			} else if (!handle.startsWith("ANON_")) {
				Result.failure(CosyncJWTException("Invalid credentials"))
			} else {
				val response = authRepository.loginAnonymous(cosyncRestAddress, appToken, handle)
				if (response.isSuccessful) {
					response.body()?.let {
						jwt = it.jwt.orEmpty()
						accessToken = it.accessToken.orEmpty()
						Result.success(it)
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun forgotPassword(handle: String): Result<Boolean> {
		return try {
			if (appToken.isEmpty() || cosyncRestAddress.isEmpty()) {
				Result.failure(CosyncJWTException("Not configured yet"))
			} else {
				val response = authRepository.forgotPassword(cosyncRestAddress, appToken, handle)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun getApplication(): Result<App> {
		return try {
			if (appToken.isEmpty() || cosyncRestAddress.isEmpty()) {
				Result.failure(CosyncJWTException("Not configured yet"))
			} else {
				val response = authRepository.getApplication(cosyncRestAddress, appToken)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it)
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	private fun isPasswordValid(password: String, app: App): Boolean {
		val minLength = app.passwordMinLength!! // minimum length of password
		val minUpperCase = app.passwordMinUpper!! // minimum number of uppercase letters
		val minLowerCase = app.passwordMinLower!! // minimum number of lowercase letters
		val minDigits = app.passwordMinDigit!! // minimum number of digits
		val minSpecialChars = app.passwordMinSpecial!! // minimum number of special characters

		val upperCaseRegex = Regex("[A-Z]")
		val lowerCaseRegex = Regex("[a-z]")
		val digitRegex = Regex("\\d")
		val specialCharRegex = Regex("[@%/\\\\'!#\$^?:()\\[\\]~`\\-_.,]")

		return password.length >= minLength &&
				password.count { upperCaseRegex.containsMatchIn(it.toString()) } >= minUpperCase &&
				password.count { lowerCaseRegex.containsMatchIn(it.toString()) } >= minLowerCase &&
				password.count { digitRegex.containsMatchIn(it.toString()) } >= minDigits &&
				password.count { specialCharRegex.containsMatchIn(it.toString()) } >= minSpecialChars
	}

	suspend fun signup(handle: String, password: String, metaData: String?): Result<Authentication> {
		var result: Result<Authentication> =
			Result.failure(CosyncJWTException("Something went wrong"))

		try {
			if (appToken.isEmpty() || cosyncRestAddress.isEmpty()) {
				result = Result.failure(CosyncJWTException("Not configured yet"))
			} else {
				val appResponse = authRepository.getApplication(cosyncRestAddress, appToken)
				if (appResponse.isSuccessful) {
					appResponse.body()?.let { app ->
						var passwordValid = true
						if (app.passwordFilter == true) {
							passwordValid = isPasswordValid(password, app)
						}
						if (passwordValid) {
							val response = authRepository.signup(cosyncRestAddress, appToken, handle, password, metaData)
							if (response.isSuccessful) {
								if (app.signupFlow == "none") {
									response.body()?.let { auth ->
										jwt = auth.jwt.orEmpty()
										accessToken = auth.accessToken.orEmpty()
										result = Result.success(auth)
									}
								}
							}
						} else {
							result = Result.failure(CosyncJWTException("Password invalid"))
						}
					}
				}
			}
		} catch (e: Exception) {
			result = Result.failure(e)
		}
		return result
	}

	suspend fun completeSignup(handle: String, code: String): Result<Authentication> {
		return try {
			val response = authRepository.completeSignup(cosyncRestAddress, appToken, handle, code)
			if (response.isSuccessful) {
				response.body()?.let {
					jwt = it.jwt.orEmpty()
					accessToken = it.accessToken.orEmpty()
					Result.success(it)
				} ?: Result.failure(CosyncJWTException("Something went wrong"))
			} else {
				Result.failure(CosyncJWTException("Something went wrong"))
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun invite(handle: String, metaData: String?, senderUserId: String?): Result<Boolean> {
		return try {
			val response = authRepository.invite(cosyncRestAddress, appToken, handle, metaData, senderUserId)
			if (response.isSuccessful) {
				response.body()?.let {
					Result.success(it.toBoolean())
				} ?: Result.failure(CosyncJWTException("Something went wrong"))
			} else {
				Result.failure(CosyncJWTException("Something went wrong"))
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun register(handle: String, password: String, metaData: String?, code: String): Result<Authentication> {
		var result: Result<Authentication> =
			Result.failure(CosyncJWTException("Something went wrong"))

		try {
			if (appToken.isEmpty() || cosyncRestAddress.isEmpty()) {
				result = Result.failure(CosyncJWTException("Not configured yet"))
			} else {
				val appResponse = authRepository.getApplication(cosyncRestAddress, appToken)
				if (appResponse.isSuccessful) {
					appResponse.body()?.let { app ->
						var passwordValid = true
						if (app.passwordFilter == true) {
							passwordValid = isPasswordValid(password, app)
						}
						if (passwordValid) {
							val response = authRepository.register(cosyncRestAddress, appToken, handle, password, metaData, code)
							if (response.isSuccessful) {
								if (app.signupFlow == "none") {
									response.body()?.let { auth ->
										jwt = auth.jwt.orEmpty()
										accessToken = auth.accessToken.orEmpty()
										result = Result.success(auth)
									}
								}
							}
						} else {
							result = Result.failure(CosyncJWTException("Password invalid"))
						}
					}
				}
			}
		} catch (e: Exception) {
			result = Result.failure(e)
		}
		return result
	}

	suspend fun getUser(): Result<User> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.getUser(cosyncRestAddress, accessToken)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it)
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun resetPassword(handle: String, password: String, code: String): Result<Boolean> {
		return try {
			if (appToken.isEmpty() || cosyncRestAddress.isEmpty()) {
				Result.failure(CosyncJWTException("Not configured yet"))
			} else {
				val response = userRepository.resetPassword(
					cosyncRestAddress,
					appToken,
					handle,
					password,
					code
				)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun changePassword(newPassword: String, password: String): Result<Boolean> {
		return try {
			if (appToken.isEmpty() || cosyncRestAddress.isEmpty()) {
				Result.failure(CosyncJWTException("Not configured yet"))
			} else {
				val response = userRepository.changePassword(
					cosyncRestAddress,
					appToken,
					newPassword,
					password
				)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun setPhone(phoneNumber: String): Result<Boolean> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.setPhone(cosyncRestAddress, accessToken, phoneNumber)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun verifyPhone(code: String): Result<Boolean> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.verifyPhone(cosyncRestAddress, accessToken, code)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun setTwoFactorPhoneVerification(enable: Boolean): Result<Boolean> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.setTwoFactorPhoneVerification(cosyncRestAddress, accessToken, enable)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun setTwoFactorGoogleVerification(enable: Boolean): Result<Boolean> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.setTwoFactorGoogleVerification(cosyncRestAddress, accessToken, enable)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun setUserMetadata(metaData: String): Result<Boolean> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.setUserMetadata(cosyncRestAddress, accessToken, metaData)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun userNameAvailable(userName: String): Result<Boolean> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.userNameAvailable(cosyncRestAddress, accessToken, userName)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.asBoolean.or(false))
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun setUserName(userName: String): Result<Boolean> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.setUserName(cosyncRestAddress, accessToken, userName)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	suspend fun deleteAccount(handle: String, password: String): Result<Boolean> {
		return try {
			if (accessToken.isEmpty()) {
				Result.failure(CosyncJWTException("No access token"))
			} else {
				val response = userRepository.deleteAccount(cosyncRestAddress, accessToken, handle, password)
				if (response.isSuccessful) {
					response.body()?.let {
						Result.success(it.toBoolean())
					} ?: Result.failure(CosyncJWTException("Something went wrong"))
				} else {
					Result.failure(CosyncJWTException("Something went wrong"))
				}
			}
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	fun logout() {
		jwt = ""
		loginToken = ""
		accessToken = ""
		signedUserToken = ""
	}
}