<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/common/taglib.jsp"%>
<%
	String investrecord_id = request.getParameter("investrecord_id");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title></title>
	</head>
	<body>

		<table id="dg" width="100%" class="easyui-datagrid"
		            data-options="rownumbers:true,
		            singleSelect:true,
		            method:'post',
		            showFooter: true,
		         	height:383,
		            url:'/back/stcsMemBaseAction!m_p_detail?investrecord_id=<%=investrecord_id %>',
		            ">
			        <thead>
			            <tr>
				        	<th data-options="field:'YHDATE',width:80,formatter:date_formatter" rowspan="2">应还日期</th>
				        	<th data-options="field:'SHDATE',width:80,formatter:date_formatter" rowspan="2">实还日期</th>
				        	<th colspan="2">应还款额</th>
				        	<th colspan="3">实还款额</th>
				        	<th data-options="field:'STATE',width:80,formatter:p_state_formatter" rowspan="2">状态</th>
				        	<th data-options="field:'REMARK_',width:160" rowspan="2">备注</th>
			            </tr>
			            <tr>
			            	<th data-options="field:'XYBJ',width:60,formatter:float_number_formatter">本金</th>
			            	<th data-options="field:'XYLX',width:60,formatter:float_number_formatter">利息</th>
			            	<th data-options="field:'SHBJ',width:60,formatter:float_number_formatter">本金</th>
			            	<th data-options="field:'SHLX',width:60,formatter:float_number_formatter">利息</th>
			            	<th data-options="field:'FJ',width:50,formatter:float_number_formatter">罚息</th>
			            </tr>
			        </thead>
			    </table>
	</body>
</html>


			