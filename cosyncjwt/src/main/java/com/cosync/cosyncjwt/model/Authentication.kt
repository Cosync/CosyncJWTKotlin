package com.cosync.cosyncjwt.model

import com.google.gson.annotations.SerializedName

data class Authentication(
	@SerializedName("jwt")
	val jwt: String? = "",
	@SerializedName("access-token")
	val accessToken: String? = "",
	@SerializedName("login-token")
	var loginToken: String? = ""
)
