<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String username = request.getParameter("username");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>投资会员月度理财报告</title>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<style>
			body{padding:0;margin:0;overflow:hidden !important;}
		</style>
		
		
	</head>
	<body>
    	<select class="easyui-combobox" data-options="
    		onSelect:year_changed,
    		onLoadSuccess:initMonth,
    		width:80,
    		editable:false,
    		url:'/back/statistics/stcsMouthLiCaiAction!year_json_for_mouth_lc_report?username=<%=username%>'"
    		name="year_select" id="year_select">
        </select>
        &nbsp;&nbsp;
        <select class="easyui-combobox" data-options="
    		width:80,
    		editable:false,
    		valueField:'value',
           	textField:'text'" name="month" id="month">
        </select>
        &nbsp;&nbsp;
        <a href="javascript:;" onclick="openDialog()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        <script>
			function year_changed(record){
				$("#month").combobox('reload','/back/statistics/stcsMouthLiCaiAction!month_json_for_mouth_lc_report?username=<%=username%>&year='+record['value']);
			}
			function initMonth(){
				var year_str = $("[name='year_select']").val();
				$("#month").combobox({url:'/back/statistics/stcsMouthLiCaiAction!month_json_for_mouth_lc_report?username=<%=username%>&year='+year_str});
			}
    		
    		function openDialog(){
    		
    			var shortdate = $("[name='year_select']").val()+$("[name='month']").val();
				window.showModalDialog("/back/statistics/stcsMouthLiCaiAction!html_for_mouth_lc_report?username=<%=username%>&shortdate="+shortdate,"html_for_mouth_lc_report","dialogWidth=900px;dialogHeight=600px;");
    		}
		</script>
	</body>
</html>
