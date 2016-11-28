<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/common/taglib.jsp"%>
<%
	String username = request.getParameter("username");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title></title>
	</head>
	<body>

		<div class="easyui-tabs" id="member-tabs" style="height:474px" data-options="tabWidth:130">
			<div title="债权明细" style="padding:3px" data-options="onResize:tabsResize">
				<iframe scrolling="yes" frameborder="0"  src="member_zq_json.jsp?username=<%=username%>" style="width:100%;height:100%;"></iframe>
			</div>
			<div title="回款情况" style="padding: 3px">
				<iframe scrolling="yes" frameborder="0"  src="member_hk_json.jsp?username=<%=username%>" style="width:100%;height:100%;"></iframe>
			</div>
			<div title="投标情况" style="padding: 3px">
				<iframe scrolling="yes" frameborder="0"  src="member_sg_json.jsp?username=<%=username%>" style="width:100%;height:100%;"></iframe>
			</div>
			<div title="余额信息" style="padding: 3px">
				<iframe scrolling="yes" frameborder="0"  src="member_ye_json.jsp?username=<%=username%>" style="width:100%;height:100%;"></iframe>
			</div>
			<div title="风险分析" style="padding: 3px">
				<iframe scrolling="yes" frameborder="0"  src="member_fx_json.jsp?username=<%=username%>" style="width:100%;height:100%;"></iframe>
			</div>
		</div>
		
	</body>
</html>


		


			