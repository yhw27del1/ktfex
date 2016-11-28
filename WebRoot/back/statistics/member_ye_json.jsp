<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglib.jsp"%>
<%
String username = request.getParameter("username");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH,-7);
Date startDate = cal.getTime();
cal.add(Calendar.DAY_OF_MONTH,6);
Date endDate = cal.getTime();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<title></title>
		<style>
			body{padding:0;margin:0;overflow:hidden !important;}
			.combo{height:20px;background-color: #fff;}
		</style>
	</head>
	<body>
	 	<table id="ye_dg" width="100%" style="height:436px;" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            method:'post',
            url:'/back/stcsMemBaseAction!member_ye_list?username=<%=username %>&startDate=<%=sdf.format(startDate)%>&endDate=<%=sdf.format(endDate)%>',
            toolbar:'#ye_dg_tb',
            showFooter: true,
            width:878
            ">
	        <thead>
	            <tr>
		        	<th data-options="field:'date',width:80,formatter:date_formatter">清算日期</th>
		        	<th data-options="field:'balance',width:100,formatter:float_number_formatter">帐户可用余额</th>
		        	<th data-options="field:'frozen',width:100,formatter:float_number_formatter">帐户冻结余额</th>
	            </tr>
	        </thead>
	    </table>
	    <div id="ye_dg_tb" style="padding:5px;height:auto">
	        <div>
	           查询时间区间: <input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" value="<%=sdf.format(startDate)%>"/>
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期" value="<%=sdf.format(endDate)%>">
	            <a href="javascript:;" id="search_ye" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	        </div>
	    </div>
	  	<div id="pwin"></div>
	</body>
</html>
<script>
  	
  	/*浮点数格式化*/
function float_number_formatter(val,row,index){
	if(val==null){
		return null;
	}else if(isNaN(val)){
		return val;
	}else{
		return val.toFixed(2);
	}
}



/**
还款状态翻译
*/
function p_state_formatter(val,row,index){
	switch(val){
		case 0:
		return "未还款";break;
		case 1:
		return "正常还款";break;
		case 2:
		return "提前还款";break;
		case 3:
		return "逾期还款";break;
		case 4:
		return "担保代偿";
	}
}

function sub_datagrid_operator_formatter(val,row,index){
	if(row.FINANCIER_MAKE_SURE_DATE == '合计') return null;
	return '<a href="javascript:;" onclick="showdialog(\'pwin\',\'/back/statistics/m_p_statistics.jsp?investrecord_id='+row.ID+'\',700,420)">查看详细</a>';
}

function showdialog(ele,url,width,height){
	$('#'+ele).dialog({
		title:'业务详细',
	    width:width,
	    height:height,
	    modal:true,
	    href:url
	});
	//showModalDialog(url,'','dialogWidth:'+width+'px;dialogHeight:500px;center:yes;help:no;status:no;'); 
	event.stopPropagation();
}


function date_formatter(val,row,index){
	if(val == '合计'){
		return val;
	}else if(row.year == null){
		return null;
	}else{
		return row.year+"-"+row.month+"-"+row.date;
	}
}

Date.prototype.format =function(format){
	var o = {
		"M+" : this.getMonth()+1, //month
		"d+" : this.getDate(), //day
		"h+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter
		"S" : this.getMilliseconds() //millisecond
	}
	if(/(y+)/.test(format)){
		format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
	}
	for(var k in o)if(new RegExp("("+ k +")").test(format)){
		format = format.replace(RegExp.$1, RegExp.$1.length==1? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
	}
	return format;
}

	$(function(){
		$("#search_ye").click(function(){
    		var source_url = '/back/stcsMemBaseAction!member_ye_list?username=<%=username %>';
               	
               var startDate = $("[name='startDate']").val();
               var endDate = $("[name='endDate']").val();
               if(startDate == null || startDate == '' || endDate == null || endDate == '') return;
               var url_ = source_url+
                   "&startDate=" + startDate + 
                   "&endDate=" + endDate ;
               $("#ye_dg").datagrid({
                   url: url_
               });
    	});
    	$(window).resize(function(){
			$("#ye_dg").datagrid('resize',{
				width:'100%'
			});
		});
	})
</script>

