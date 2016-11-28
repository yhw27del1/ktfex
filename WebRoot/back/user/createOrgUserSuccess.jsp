<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<title>开户成功页面</title>
		<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
	</head>
	<body>
		<table align="center" cellpadding="5px;">
			<tr>
				<td>
					<span class="title" style="color: red">成功为“${user.realname}”开立“${user.org.showCoding}”机构的操作员账户，请牢记用户名和密码！</span>
				</td>
			</tr>
			<tr>
				<td style="margin-left: 50px;">
					<span class="title">用户名为：</span><span style="font-size: 16px;">${user.username}</span>
				</td>
			</tr>
			<tr>
				<td style="margin-left: 50px;">
					<span class="title">初始密码为：</span><span style="font-size: 16px;"> 
					     123456
					</span>
				</td>
			</tr>
			<%--<tr align="center">
				<td>
					<input class="ui-state-default" type="button"
						onclick="javascript:window.location.href='/back/member/memberBaseAction!list'"
						value="确定" />
				</td>
			</tr>--%>
		</table>
	</body>
</html>
