<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%
	Date now = new Date();
	String orgcode = request.getParameter("orgcode");
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
%>
<html>
	<head>
		<title></title>
		<%@ include file="/common/taglib.jsp"%>
 	</head>
	<body>
		<table id="detail_dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            showFooter: true,
            height:440,
            url:'/back/jysxFeeAction!detail_list?orgcode=<%=orgcode %>&startDate=<%=startDate %>&endDate=<%=endDate %>',
            pagination:true,
            pageList:[15,30,50,100],
            border:false
            ">
		    <thead>
                <tr>
		            <th data-options="field:'USERNAME',width:80,align:'left'">投资人帐号</th>
		        	<th data-options="field:'CODE',width:80">融资项目号</th>
		            <th data-options="field:'SHDATE',width:80,formatter:date_formatter" >还款日期</th>
		            <th data-options="field:'SY',width:80,align:'right'">收益金额</th>
		            <th data-options="field:'II_FEE',width:100,align:'right'">产生交易手续费</th>
		        </tr> 
	        </thead>
	    </table>
	</body> 
</html>


