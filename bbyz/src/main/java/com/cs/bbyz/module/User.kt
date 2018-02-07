package com.cs.bbyz.module

/**
 * @author chenshuai12619
 * @date 2018-02-07
 */
data class User(
		/* id */
		var id: String?,
		/* 工号 */
		var WrokerNo: String,
		/* 名字 */
		var WorkerName: String,
		/* 名字拼写 */
		var Spell: String,
		/* 密码 */
		var Password: String
)