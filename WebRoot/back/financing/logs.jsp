<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<title>融资项目日志</title>
		<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css"/>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery-ui-1.7.2.custom.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery.chromatable.js"></script>
		<script>
			$(function(){
				$("#tt").chromatable({
					width: "100%",
					height: "auto",
					scrolling: "yes"
				});
			});
		</script>
	</head>
	<body style="padding:0;margin:0;">
		<table id="tt" class="easyui-datagrid" data-options="remoteSort:false,
                singleSelect:true">
			<thead>
				<tr>
				<th data-options="field:'username',width:250">用户帐号/用户名</th>
				<th data-options="field:'ip',width:120">IP</th>
				<th data-options="field:'time',width:140">时间</th>
				<th data-options="field:'operator',width:140">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="item">
					<tr>
						<td>${item.username}/${item.realname}</td>
						<td>${item.ip}</td>
						<td>${item.time}</td>
						<td>${item.operate}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>
