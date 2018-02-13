package com.cs.bbyz.module;

/**
 * @author chenshuai12619
 * @date 2018-02-13
 */

public class HttpResponse<T> {
	private String returnInfo;
	private int returnNo;
	private String secure;
	private String content;
	private T obj;
	private boolean success;
	
	public String getReturnInfo() {
		return returnInfo;
	}
	
	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}
	
	public int getReturnNo() {
		return returnNo;
	}
	
	public void setReturnNo(int returnNo) {
		this.returnNo = returnNo;
	}
	
	public String getSecure() {
		return secure;
	}
	
	public void setSecure(String secure) {
		this.secure = secure;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public T getObj() {
		return obj;
	}
	
	public void setObj(T obj) {
		this.obj = obj;
	}
	
	public boolean isSuccess() {
		return returnNo == 0;
	}
}

//data class HttpResponse<T>(var returnInfo: String?, var returnNo: Int?, var secure: String?,
//		var content: String?, var obj: T?, var listObj: List<T>?) {
//		var success: Boolean = false
//		get() = returnNo == 0
//		}
