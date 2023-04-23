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
import com.cosync.cosyncjwt.repository.AuthRepository
import com.cosync.cosyncjwt.repository.UserRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CosyncJWTModule::class])
interface CosyncJWTComponent {
	@Component.Builder
	interface Builder {
		@BindsInstance
		fun context(context: Context): Builder

		fun build(): CosyncJWTComponent
	}

	fun inject(cosyncJWTModule: CosyncJWTModule)
	fun inject(authRepository: AuthRepository)
	fun inject(userRepository: UserRepository)
}
