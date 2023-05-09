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

import com.cosync.cosyncjwt.api.CosyncJWTService
import com.cosync.cosyncjwt.api.Routes
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CosyncJWTServiceTest {
	private lateinit var cosyncJWTService: CosyncJWTService

	@Before
	@Throws(Exception::class)
	fun setUp() {
//		val okHttpCallFactory: Call.Factory = OkHttpClient.Builder()
//			.addInterceptor(
//				HttpLoggingInterceptor()
//					.apply {
//						setLevel(HttpLoggingInterceptor.Level.BODY)
//					},
//			)
//			.build()

		val gson = GsonBuilder()
			.setLenient()
			.create()

		cosyncJWTService = Retrofit.Builder()
			.baseUrl("https://sandbox.cosync.net")
			//.callFactory(okHttpCallFactory)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()
			.create(CosyncJWTService::class.java)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun testGetApplication() = runTest {
		val response = cosyncJWTService.getApplication(
			url = "https://sandbox.cosync.net${Routes.getApplication}",
			appToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBJZCI6IjFiZDg2ZDIxZjY5MTQxMjc5MWRhMzQ1OTRmMjI5NGYyIiwic2NvcGUiOiJhcHAiLCJpYXQiOjE2NzkwNzQxNjR9.Aote9QdLkMGwq3-i0oRuoFxwzNh7l2CGdBhuvLBdbcnBnXXtXZ18w16WMH23--dU6r5HWiUHJsK1c0PX_aXqlysBVMPQ19u3jujCsDGyoEaOD9edm3ic-DyoobPaug5Uku5Pu6o-jLTOiuMt5LVEdnr67xsiR5cICPYFyvKLl6hTLGoE3pKVVe9xc5xVoIdU4P8gty8RoMI-JclL4OnAf-9EL7Mria27KT4TLMc8HVJo710F9jEDgFiWpAzGF07qOapvnObv10g1S8oj7RbRXYdG3gauHp86M_5qagVmU3EhGBRqVWH4T_v6YThWtQpQVCxPD1KYQjtG7Alf0T4lAVaVfe84WqNCotlVJRA3YafVIWE4NEaCKJAnTVww_CcSRSJmB_UnGS47uu8w8I9tEGjE7-ow3pN1bOeFfnrVZeDwS4bBMi7L6L3o0rDOgrGT5xtrGwWCwh-cL9SZunIf5HwiIGqfKsSVYZK9Tav_CVPqoNdRNxZcnVjRswYw3sxi"
		)
		assertEquals(response.isSuccessful, true)
	}
}