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

package com.cosync.cosyncjwt.repository

import com.cosync.cosyncjwt.api.ChangePasswordBody
import com.cosync.cosyncjwt.api.CosyncJWTService
import com.cosync.cosyncjwt.api.DeleteAccountBody
import com.cosync.cosyncjwt.api.ResetPasswordBody
import com.cosync.cosyncjwt.api.Routes
import com.cosync.cosyncjwt.api.SetPhoneBody
import com.cosync.cosyncjwt.api.SetTwoFactorVerificationBody
import com.cosync.cosyncjwt.api.SetUserMetadataBody
import com.cosync.cosyncjwt.api.SetUserNameBody
import com.cosync.cosyncjwt.api.VerifyPhoneBody
import com.cosync.cosyncjwt.di.DaggerInjector
import com.cosync.cosyncjwt.model.User
import com.google.gson.JsonObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository {
	@Inject
	lateinit var apiService: CosyncJWTService

	init {
		DaggerInjector.component.inject(this)
	}

	suspend fun getUser(baseUrl: String, accessToken: String): Response<User> {
		val url = "$baseUrl${Routes.getUser}"
		return apiService.getUser(url, accessToken)
	}

	suspend fun resetPassword(
		baseUrl: String,
		accessToken: String,
		handle: String,
		password: String,
		code: String
	): Response<String> {
		val url = "$baseUrl${Routes.resetPassword}"
		return apiService.resetPassword(url, accessToken, ResetPasswordBody(handle, password.md5, code))
	}

	suspend fun changePassword(
		baseUrl: String,
		accessToken: String,
		newPassword: String,
		password: String,
	): Response<String> {
		val url = "$baseUrl${Routes.changePassword}"
		return apiService.changePassword(url, accessToken, ChangePasswordBody(newPassword.md5, password.md5))
	}

	suspend fun setPhone(
		baseUrl: String,
		accessToken: String,
		phoneNumber: String
	): Response<String> {
		val url = "$baseUrl${Routes.setPhone}"
		return apiService.setPhone(url, accessToken, SetPhoneBody(phoneNumber))
	}

	suspend fun verifyPhone(
		baseUrl: String,
		accessToken: String,
		code: String
	): Response<String> {
		val url = "$baseUrl${Routes.verifyPhone}"
		return apiService.verifyPhone(url, accessToken, VerifyPhoneBody(code))
	}

	suspend fun setTwoFactorPhoneVerification(
		baseUrl: String,
		accessToken: String,
		enable: Boolean
	): Response<String> {
		val url = "$baseUrl${Routes.setTwoFactorPhoneVerification}"
		return apiService.setTwoFactorPhoneVerification(
			url,
			accessToken,
			SetTwoFactorVerificationBody(enable.toString())
		)
	}

	suspend fun setTwoFactorGoogleVerification(
		baseUrl: String,
		accessToken: String,
		enable: Boolean
	): Response<String> {
		val url = "$baseUrl${Routes.setTwoFactorGoogleVerification}"
		return apiService.setTwoFactorGoogleVerification(
			url,
			accessToken,
			SetTwoFactorVerificationBody(enable.toString())
		)
	}

	suspend fun setUserMetadata(baseUrl: String, accessToken: String, metaData: String): Response<String> {
		val url = "$baseUrl${Routes.setUserMetadata}"
		return apiService.setUserMetadata(url, accessToken, SetUserMetadataBody(metaData))
	}

	suspend fun userNameAvailable(baseUrl: String, accessToken: String, userName: String): Response<JsonObject> {
		val url = "$baseUrl${Routes.userNameAvailable}"
		return apiService.userNameAvailable(url, accessToken, userName)
	}

	suspend fun setUserName(baseUrl: String, accessToken: String, userName: String): Response<String> {
		val url = "$baseUrl${Routes.setUserName}"
		return apiService.setUserName(url, accessToken, SetUserNameBody(userName))
	}

	suspend fun deleteAccount(baseUrl: String, accessToken: String, handle: String, password: String): Response<String> {
		val url = "$baseUrl${Routes.deleteAccount}"
		return apiService.deleteAccount(url, accessToken, DeleteAccountBody(handle, password.md5))
	}
}