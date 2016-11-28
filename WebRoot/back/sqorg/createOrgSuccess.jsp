<%--
2012-08-10 aora在此文件末加入“确定”按钮，方便用户返回到“授权服务中心列表”查看刚开户成功的
授权服务中心。
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<title>开服务中心成功页面</title>
		<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
	</head>
	<body>
		<table align="center" cellpadding="5px;">
			<tr>
				<td>
					<span class="title" style="color: red">成功创建了“${orgName}”服务中心<span style="color: blue"></span>，请牢记用户名和密码！</span>
				</td>
			</tr>
			<tr>
				<td style="margin-left: 50px;">
					<span class="title">用户名为：</span><span style="font-size: 16px;">${username}</span>
				</td>
			</tr>
			<tr>
				<td style="margin-left: 50px;">
					<span class="title">初始密码为：</span><span style="font-size: 16px;"> 
					     ${password}
					</span>
				</td>
			</tr> 
		</table>
		<div style="margin: 20px;"><input class="ui-state-default" type="button" value="确定" onclick="javascript:window.location.href='/back/sqorg/sqOrgAction!sqList';"/></div>
	</body>
</html>
