<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<title>会员开户成功页面</title>
		<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
	</head>
	<body>
		<table align="center" cellpadding="5px;">
			<tr>
				<td>
					<span class="title" style="color: red">成功为“${memberBase.user.realname}”开立<span style="color: blue"><s:if test="\"0\"==memberBase.category">机构</s:if><s:if test="\"1\"==memberBase.category">个人</s:if>${memberBase.memberType.name}</span>账户，请牢记用户名和密码！</span>
				</td>
			</tr>
			<tr>
				<td style="margin-left: 50px;">
					<span class="title">用户名为：</span><span style="font-size: 16px;">${memberBase.user.username}</span>
				</td>
			</tr>
			<tr>
				<td style="margin-left: 50px;">
					<span class="title">初始密码为：</span><span style="font-size: 16px;"> 
					     ${password}
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
