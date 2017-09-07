package tech.lihx.demo.core.common.response;

import tech.lihx.demo.core.common.code.CodeConstants;


/**
 * 应答报文
 * <p>
 * { "head" : { "statusCode" : "" , "msg" : "" , } , "body" : { ... } }
 * 
 * @author lihx
 * @date 2015年3月9日
 * @version 1.0.0
 */
public class AppResponse {

	private AppResponseHead head;

	private Object body = "";


	public AppResponse( String statusCode, String msg ) {
		head = new AppResponseHead();
		head.setMsg(msg);
		head.setStatusCode(statusCode);
	}


	public AppResponse( Object body ) {
		head = new AppResponseHead();
		head.setStatusCode(CodeConstants.SUCCESS);
		this.body = body;
	}


	public AppResponseHead getHead() {
		return head;
	}


	public void setHead( AppResponseHead head ) {
		this.head = head;
	}


	public Object getBody() {
		return body;
	}


	public void setBody( Object body ) {
		this.body = body;
	}

	/**
	 * 请求头部报文
	 */
	public class AppResponseHead {

		/**
		 * 应答码
		 */
		private String statusCode = "";

		/**
		 * 提示消息
		 */
		private String msg = "";


		public String getStatusCode() {
			return statusCode;
		}


		public void setStatusCode( String statusCode ) {
			this.statusCode = statusCode;
		}


		public String getMsg() {
			return msg;
		}


		public void setMsg( String msg ) {
			this.msg = msg;
		}
	}

}
