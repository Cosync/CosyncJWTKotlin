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

import com.cosync.cosyncjwt.api.CosyncJWTService
import com.cosync.cosyncjwt.api.ForgotPasswordBody
import com.cosync.cosyncjwt.api.LoginAnonymousBody
import com.cosync.cosyncjwt.api.LoginBody
import com.cosync.cosyncjwt.api.LoginCompleteBody
import com.cosync.cosyncjwt.api.Routes
import com.cosync.cosyncjwt.di.DaggerInjector
import com.cosync.cosyncjwt.model.App
import com.cosync.cosyncjwt.model.Authentication
import retrofit2.Response
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

val String.md5: String
	get() {
		val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
		return bytes.joinToString("") {
			"%02x".format(it)
		}
	}

@Singleton
class AuthRepository {
	@Inject
	lateinit var apiService: CosyncJWTService

	init {
		DaggerInjector.component.inject(this)
	}

	suspend fun login(baseUrl: String, appToken: String, handle: String, password: String): Response<Authentication> {
		val url = "$baseUrl${Routes.login}"
		return apiService.login(url, appToken, LoginBody(handle, password.md5))
	}

	suspend fun loginComplete(baseUrl: String, appToken: String, loginToken: String, code: String): Response<Authentication> {
		val url = "$baseUrl${Routes.loginComplete}"
		return apiService.loginComplete(url, appToken, LoginCompleteBody(loginToken, code))
	}

	suspend fun loginAnonymous(baseUrl: String, appToken: String, handle: String): Response<Authentication> {
		val url = "$baseUrl${Routes.loginAnonymous}"
		return apiService.loginAnonymous(url, appToken, LoginAnonymousBody(handle))
	}

	suspend fun forgotPassword(baseUrl: String, accessToken: String, handle: String): Response<String> {
		val url = "$baseUrl${Routes.forgotPassword}"
		return apiService.forgotPassword(url, accessToken, ForgotPasswordBody(handle))
	}

	suspend fun getApplication(baseUrl: String, appToken: String): Response<App> {
		val url = "$baseUrl${Routes.getApplication}"
		return apiService.getApplication(url, appToken)
	}
}