<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/common/taglib.jsp"%>
<%
	String date = request.getParameter("date");
	String username = request.getParameter("username");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title></title>
	</head>
	<body>

		<table id="invest_detail_dg" class="easyui-datagrid"
		            data-options="rownumbers:true,
		            singleSelect:true,
		            method:'post',
		            showFooter: true,
		         	height:364,
		            url:'/back/stcsMemBaseAction!m_i_m_detail?username=<%=username %>&date=<%=date%>',
		            ">
			        <thead>
			            <tr>
				        	<th data-options="field:'INVEST_DATE',width:80,formatter:date_formatter">投标日期</th>
				        	<th data-options="field:'F_M_S_D',width:80,formatter:F_M_S_D_date_formatter">签约日期</th>
				        	<th data-options="field:'INVEST_AMOUNT',width:80,formatter:float_number_formatter">投标金额</th>
				        	<th data-options="field:'FINANCBASECODE',width:80">项目编号</th>
			            </tr>
			           
			        </thead>
			    </table>
	</body>
</html>


			