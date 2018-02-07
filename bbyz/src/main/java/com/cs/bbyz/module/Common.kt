package com.cs.bbyz.module

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
data class Common(
		var appId: String,
		var usId: String,
		var loginStatus: Int,
		var terminalType: String,
		var imei: String,
		var appVer: String,
		var mobileVer: String,
		var channelVer: String,
		var platformCode: String
)