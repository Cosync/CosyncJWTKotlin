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

class Routes {
	companion object {
		private const val apiRoute = "/api/appuser"
		
		const val login = "$apiRoute/login"
		const val loginComplete = "$apiRoute/loginComplete"
		const val loginAnonymous = "$apiRoute/loginAnonymous"
		const val forgotPassword = "$apiRoute/forgotPassword"
		const val signup = "$apiRoute/signup"
		const val register = "$apiRoute/register"
		const val completeSignup = "$apiRoute/completeSignup"
		const val getApplication = "$apiRoute/getApplication"

		const val getUser = "$apiRoute/getUser"
		const val resetPassword = "$apiRoute/resetPassword"
		const val changePassword = "$apiRoute/changePassword"
		const val setPhone = "$apiRoute/setPhone"
		const val verifyPhone = "$apiRoute/verifyPhone"
		const val setTwoFactorPhoneVerification = "$apiRoute/setTwoFactorPhoneVerification"
		const val setTwoFactorGoogleVerification = "$apiRoute/setTwoFactorGoogleVerification"
		const val setUserMetadata = "$apiRoute/setUserMetadata"
		const val userNameAvailable = "$apiRoute/userNameAvailable"
		const val setUserName = "$apiRoute/setUserName"
		const val deleteAccount = "$apiRoute/deleteAccount"
		const val invite = "$apiRoute/invite"
	}
}