package com.cs.bbyz.constant

/**
 * @author chenshuai12619
 * @date 2018-02-06
 */
object Constant {
	const val BUSSINESS_TYPE_COMMON: String = "common"

	const val HTTP_SECURE_TAG: String = "http_secure_tag"
	const val PUBLIC_KEY: String = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1k10wulc/MjjWJqVHgrfGJDCYlIn0dpGM9bp/wRmHt17DErh0RVWevqYrVOIcOqsX6ij5np3wKjKBtXczWTrqBvKwj5mDeJkJnTOa1iTDr1sNPAhcU6HnQ1hkQy9HkdsOL2AqkgvuBUgNvF2ldQF2lSjnvTrtWareHnCNA9gT5wIDAQAB"

	const val COMMAND_NO: String = "command_no"
	val rsaKeyCache: MutableMap<String, String> = mutableMapOf()

	const val DB_USER_ACCOUNT: String = "db_user_account"
	const val DB_USER_PWD: String = "db_user_pwd"


	val COMMAND_LOGIN: Pair<String, String> = Pair("0x0008", "登录")
}