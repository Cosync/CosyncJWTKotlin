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
	fun configure(appToken: String, cosyncRestAddress: String? = "") {
		this.appToken = appToken
		this.cosyncRestAddress = cosyncRestAddress.takeUnless { it.isNullOrEmpty() } ?: context.getString(
			R.string.defaultCosyncRestAddress)
	}

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
					loginToken = it.loginToken.orEmpty()
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
				val response = authRepository.getApplication(cosyncRestAddress, accessToken)
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

	fun logout() {
		jwt = ""
		loginToken = ""
		accessToken = ""
	}
}