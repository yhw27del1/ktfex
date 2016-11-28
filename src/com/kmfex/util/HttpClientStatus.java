package com.kmfex.util;

import org.apache.log4j.Logger;

public class HttpClientStatus {
	private static Logger logger = Logger.getLogger(HttpClientStatus.class);
	public static void verifyStatus(int code) {
		String info = "状态码：" + code + " 状态信息：";
		switch(code) {
			case 201 :logger.info(info + " 正常；紧接 POST 命令。 ");break;
			case 202 :logger.info(info + " 正常；已接收用于处置，但处理尚未完成。 ");break;
			case 203 :logger.info(info + " 正常；部分信息― 返回的信息只是一部分。");break;
			case 204 :logger.info(info + " 正常；无响应― 已接受请求，但不存在要回送的信息。");break;
			
			case 303 :logger.info(info + " 请参阅其它― 可在另一 URI 下找到对恳求的响应，且应应用 GET 方式检索此响应。   ");break;
			case 304 :logger.info(info + " 未修正― 未按预期修改文档。 ");break;
			case 305 :logger.info(info + " 使用代理― 必需通过地位字段中提供的代理来访问请求的资源。  ");break;
			case 306 :logger.info(info + " 未使用― 不再使用；保存此代码以便将来使用。 ");break;
			
			case 400 :logger.info(info + " 错误请求― 请求中有语法问题，或不能满足请求。 ");break;
			case 401 :logger.info(info + " 未授权― 未授权客户机拜访数据。  ");break;
			case 402 :logger.info(info + " 需要付款― 表示计费体系已有效。 ");break;
			case 403 :logger.info(info + " 制止― 即使有授权也不须要拜访。  ");break;
			case 404 :logger.info(info + " 找不到― 服务器找不到给定的资源；文档不存在。  ");break;
			case 407 :logger.info(info + " 代理认证恳求― 客户机首先必需使用代理认证自身。  ");break;
			case 415 :logger.info(info + " 介质类型不受支撑― 服务器谢绝服务请求，由于不支撑请求实体的格局。 ");break;
			
			case 500 :logger.info(info + " 内部错误― 由于意外情况，服务器不能完成请求。 ");break;
			case 501 :logger.info(info + " 未履行― 服务器不支撑请求的工具。 ");break;
			case 502 :logger.info(info + " 错误网关― 服务器接受到来自上游服务器的无效响应。  ");break;
			case 503 :logger.info(info + " 错误请求― 请求中有语法问题，或不能满足请求。 ");break;
			default:break;
		}
	}
	public static void main(String[] args) {
		verifyStatus(500);
	}
}
