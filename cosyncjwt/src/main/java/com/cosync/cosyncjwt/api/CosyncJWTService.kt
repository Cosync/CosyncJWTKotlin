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
package com.cosync.cosyncjwt.api

import com.cosync.cosyncjwt.model.App
import com.cosync.cosyncjwt.model.Authentication
import com.cosync.cosyncjwt.model.User
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

data class LoginBody(val handle: String, val password: String)
data class LoginCompleteBody(val loginToken: String, val code: String)
data class LoginAnonymousBody(val handle: String)
data class SignupBody(val handle: String, val password: String, val metaData: String?)
data class SignupCompleteBody(val handle: String, val code: String)
data class InviteBody(val handle: String, val metaData: String?, val senderUserId: String?)
data class RegisterBody(val handle: String, val password: String, val metaData: String?, val code: String)
data class ForgotPasswordBody(val handle: String)
data class ResetPasswordBody(val handle: String, val password: String, val code: String)
data class ChangePasswordBody(val newPassword: String, val password: String)
data class SetPhoneBody(val phone: String)
data class VerifyPhoneBody(val code: String)
data class SetTwoFactorVerificationBody(val twoFactor: String)
data class SetUserMetadataBody(val metaData: String)
data class SetUserNameBody(val userName: String)
data class DeleteAccountBody(val handle: String, val password: String)

interface CosyncJWTService {
	@POST//("/api/appuser/login")
	suspend fun login(
		@Url url: String,
		@Header("app-token") appToken: String,
		@Body body: LoginBody
	): Response<Authentication>

	@POST//("/api/appuser/loginComplete")
	suspend fun loginComplete(
		@Url url: String,
		@Header("app-token") appToken: String,
		@Body body: LoginCompleteBody
	): Response<Authentication>

	@POST//("/api/appuser/loginAnonymous")
	suspend fun loginAnonymous(
		@Url url: String,
		@Header("app-token") appToken: String,
		@Body body: LoginAnonymousBody
	): Response<Authentication>

	@POST//("/api/appuser/forgotPassword")
	suspend fun forgotPassword(
		@Url url: String,
		@Header("app-token") appToken: String,
		@Body body: ForgotPasswordBody
	): Response<String>

	@GET//("/api/appuser/getApplication")
	suspend fun getApplication(
		@Url url: String,
		@Header("app-token") appToken: String
	): Response<App>

	@POST//("/api/appuser/signup")
	suspend fun signup(
		@Url url: String,
		@Header("app-token") appToken: String,
		@Body body: SignupBody
	): Response<Authentication>

	@POST//("/api/appuser/completeSignup")
	suspend fun completeSignup(
		@Url url: String,
		@Header("app-token") appToken: String,
		@Body body: SignupCompleteBody
	): Response<Authentication>

	@POST//("/api/appuser/invite")
	suspend fun invite(
		@Url url: String,
		@Header("app-token") appToken: String,
		@Body body: InviteBody
	): Response<String>

	@POST//("/api/appuser/register")
	suspend fun register(
		@Url url: String,
		@Header("app-token") appToken: String,
		@Body body: RegisterBody
	): Response<Authentication>

	@GET//("/api/appuser/getUser")
	suspend fun getUser(
		@Url url: String,
		@Header("access-token") accessToken: String
	): Response<User>

	@POST//("/api/appuser/resetPassword")
	suspend fun resetPassword(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: ResetPasswordBody
	): Response<String>

	@POST//("/api/appuser/changePassword")
	suspend fun changePassword(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: ChangePasswordBody
	): Response<String>

	@POST//("/api/appuser/setPhone")
	suspend fun setPhone(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: SetPhoneBody
	): Response<String>

	@POST//("/api/appuser/verifyPhone")
	suspend fun verifyPhone(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: VerifyPhoneBody
	): Response<String>

	@POST//("/api/appuser/setTwoFactorPhoneVerification")
	suspend fun setTwoFactorPhoneVerification(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: SetTwoFactorVerificationBody
	): Response<String>

	@POST//("/api/appuser/setTwoFactorGoogleVerification")
	suspend fun setTwoFactorGoogleVerification(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: SetTwoFactorVerificationBody
	): Response<String>

	@POST//("/api/appuser/setUserMetadata")
	suspend fun setUserMetadata(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: SetUserMetadataBody
	): Response<String>

	@GET//("/api/appuser/userNameAvailable")
	suspend fun userNameAvailable(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Query("userName") userName: String
	): Response<JsonObject>

	@POST//("/api/appuser/setUserName")
	suspend fun setUserName(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: SetUserNameBody
	): Response<String>

	@POST//("/api/appuser/deleteAccount")
	suspend fun deleteAccount(
		@Url url: String,
		@Header("access-token") accessToken: String,
		@Body body: DeleteAccountBody
	): Response<String>
}