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

package com.cosync.cosyncjwt.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class App(
	@SerializedName("name")
	val name: String? = "",
	@SerializedName("signupFlow")
	val signupFlow: String? = "code", // 'code', 'none', 'link'
	@SerializedName("anonymousLoginEnabled")
	val anonymousLoginEnabled: Boolean? = false,
	@SerializedName("userNamesEnabled")
	val userNamesEnabled: Boolean? = false,
	@SerializedName("twoFactorVerification")
	val twoFactorVerification: String? = "none", // 'phone', 'google', 'none'
	@SerializedName("passwordFilter")
	val passwordFilter: Boolean? = false,
	@SerializedName("passwordMinLength")
	val passwordMinLength: Int? = 8,
	@SerializedName("passwordMinUpper")
	val passwordMinUpper: Int? = 1,
	@SerializedName("passwordMinLower")
	val passwordMinLower: Int? = 1,
	@SerializedName("passwordMinDigit")
	val passwordMinDigit: Int? = 1,
	@SerializedName("passwordMinSpecial")
	val passwordMinSpecial: Int? = 1,
	@SerializedName("appData")
	var appData: JsonObject? = JsonObject(),
)