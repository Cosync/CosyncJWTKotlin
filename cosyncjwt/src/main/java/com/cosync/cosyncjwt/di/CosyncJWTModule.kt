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
package com.cosync.cosyncjwt.di

import android.content.Context
import com.cosync.cosyncjwt.R
import com.cosync.cosyncjwt.api.CosyncJWTService
import com.cosync.cosyncjwt.common.Prefs
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class CosyncJWTModule {
	@Provides
	@Singleton
	fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
		.addInterceptor(
			HttpLoggingInterceptor()
				.apply {
					setLevel(HttpLoggingInterceptor.Level.BODY)
				},
		)
		.build()

	@Singleton
	@Provides
	fun provideCosyncJWTService(
		context: Context,
		okhttpCallFactory: Call.Factory,
	): CosyncJWTService {
		return Retrofit.Builder()
			.baseUrl(context.getString(R.string.defaultCosyncRestAddress))
			.callFactory(okhttpCallFactory)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(CosyncJWTService::class.java)
	}

	@Singleton
	@Provides
	fun providePrefs(context: Context): Prefs {
		return Prefs(context)
	}
}