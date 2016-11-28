<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<html>
	<head>
		<title>商金融公告</title>
	</head>
	<body>
		<table align="center" width="800">
			<!-- <tr><td align="center" style="font-size:22px;font-weight:bold">${announcement.title}</td></tr> -->
			<tr><td align="left"><p style="margin-top:10px;">${announcement.content}</p></td></tr>
			<tr><td align="right" style="padding-top:10px"><fmt:formatDate value="${announcement.addtime}" pattern="yyyy年MM月dd日"/></td></tr>
		</table>
	</body>
</html>
