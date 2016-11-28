<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/common/taglib.jsp"%>
<%
	String username = "53010100170";
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title></title>
		 
	</head>
	<body> 
		<div class="easyui-tabs" id="member-tabs" style="height:414px" data-options="tabWidth:130">
			<div title="授权中心" style="padding:3px" data-options="onResize:tabsResize">
				<iframe scrolling="yes" frameborder="0"  src="member_o_json.jsp" style="width:100%;height:100%;"></iframe>
			</div>
			<div title="担保公司" style="padding: 3px">
				<iframe scrolling="yes" frameborder="0"  src="member_d_json.jsp" style="width:100%;height:100%;"></iframe>
			</div>  
			<div title="融资方" style="padding: 3px">
				<iframe scrolling="yes" frameborder="0"  src="member_r_json.jsp" style="width:100%;height:100%;"></iframe>
			</div>
			<!--
			<div title="投资人" style="padding: 3px">
				<iframe scrolling="yes" frameborder="0"  src="member_t_json.jsp" style="width:100%;height:100%;"></iframe>
			</div>
			--->
		</div>
		
	</body>
</html>


		


			