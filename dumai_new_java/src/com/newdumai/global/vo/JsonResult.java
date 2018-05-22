package com.newdumai.global.vo;


/**
 * 请求json结果定义<br/>
 * code <=-1 系统异常,0 成功 ,>=1 其他错误,错误描述体现在error字段<br/>
 * message 提示信息<br/>
 * error 错误信息<br/>
 * body 返回数据<br/>
 * <hr/>
 * 注:<br/>
 * -1 系统异常<br/>
 * 0 成功<br/>
 * 1-99 错误,部分成功<br/>
 * 100 通用失败码<br/>
 * 101 参数错误<br/>
 * 102 无权限<br/>
 * 103-199 系统预留<br/>
 * >=200 其他失败码，自定义<br/>
 */
public class JsonResult {
	private int code;
	private String message;
	private String error;
	private Object body;
	
	
	private JsonResult(int code, String message, String error, Object body) {
		super();
		this.code = code;
		this.message = message;
		this.error = error;
		this.body = body;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
	
	public static JsonResult successResult(Object body,String message){
		return new JsonResult(0, message, null, body);
	}
	public static JsonResult successResult(Object body){
		return new JsonResult(0, "成功", null, body);
	}
	public static JsonResult failResult(String error){
		return new JsonResult(100, null, error, null);
	}
	public static JsonResult failResult(){
		return new JsonResult(100, null, "失败", null);
	}

	public static JsonResult failResult(Object body){
		return new JsonResult(0, "失败", null, body);
	}
	public static JsonResult failResult(int code,String message){
		return new JsonResult(code, message, null, null);
	}
	

}
