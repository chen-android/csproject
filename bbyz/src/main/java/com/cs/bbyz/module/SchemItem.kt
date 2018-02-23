package com.cs.bbyz.module

/**
 * @author chenshuai12619
 * @date 2018-02-23
 */
data class SchemItem(
		/** 车次id */
		var SchemID: String,
		/** 发车日期 */
		var DriveDate: String,
		/** 隶属站编号 */
		var StationNo: String,
		/** 隶属站名称 */
		var StationName: String,
		/** 车次号 */
		var SchemNo: Int,
		/** 班次类别 */
		var SchemType: String,
		/** 1正班 2加班 */
		var SchemTypeCode: Int,
		/** 发车时间 */
		var DriveTime: String,
		/** 线路名称 */
		var RouteName: String,
		/** 车型名称 */
		var BusTypeName: String,
		/** 承运单位 */
		var CarrayCompanyName: String,
		/** 车牌号 */
		var LicenseNo: String,
		/** 检票口 */
		var CheckGateNo: String,
		/** 是否开班 1开班  0停班  2停班有售 */
		var IsRun: Int,
		/** 是否强制门检放行 1不验证合法性，强制放行  0正常状态 */
		var IsForcePass: Int,
		/** 终点站码 */
		var EndStopNo: String,
		/** 终点站名 */
		var EndStopName: String,
		/** 起座号 */
		var StartSeatNo: Int,
		/** 止座号 */
		var EndSeatNo: Int,
		/** 座位数 */
		var TotalSeatNum: Int,
		/** 余座 */
		var LeastSeatNum: Int,
		/** 已售票数 */
		var SaledNum: Int,
		/** 预定张数 */
		var ReserveNum: Int,
		/** 价格 */
		var Price: String,
		/** 是否始发站 */
		var IsDeparture: Int
)