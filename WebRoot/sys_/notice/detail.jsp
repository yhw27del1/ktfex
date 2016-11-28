<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<html>
	<head>
		<title>商金融个人通知</title>
	</head>
	<body>
		<table align="center" width="800">
 			<tr><td align="center"><p style="margin-top:10px;">${notice.title}(<span style='color:red;'>个人通知</span>)</p></td></tr>
 			<tr><td align="left"><p style="margin-top:10px;">${notice.content}</p></td></tr>
			<tr><td align="right" style="padding-top:10px"><fmt:formatDate value="${notice.addtime}" pattern="yyyy年MM月dd日"/></td></tr>
		</table>
	</body>
</html>
