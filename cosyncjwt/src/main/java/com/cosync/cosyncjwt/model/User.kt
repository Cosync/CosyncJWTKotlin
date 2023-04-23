package com.cosync.cosyncjwt.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class User(
	@SerializedName("handle")
	val handle: String? = "",
	@SerializedName("userName")
	val userName: String? = "",
	@SerializedName("twoFactorPhoneVerification")
	var twoFactorPhoneVerification: Boolean? = false,
	@SerializedName("twoFactorGoogleVerification")
	var twoFactorGoogleVerification: Boolean? = false,
	@SerializedName("appId")
	var appId: String? = "",
	@SerializedName("phone")
	var phone: String? = "",
	@SerializedName("phoneVerified")
	var phoneVerified: Boolean? = false,
	@SerializedName("lastLogin")
	var lastLogin: String? = "",
	@SerializedName("metaData")
	var metaData: JsonObject? = JsonObject(),
)
