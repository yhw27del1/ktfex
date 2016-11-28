<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ include file="/common/taglib.jsp"%>
<%
String username = request.getParameter("username");
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
	 	<table id="invest_dg" class="easyui-datagrid"
		            data-options="rownumbers:true,
		            singleSelect:true,
		            url:'/back/stcsMemBaseAction!m_i_detail?username=<%=username%>',
		            method:'get',
		            toolbar:'#invest_tb',
		            pagination:true,
		            showFooter: true,
		            pageList:[15,30,50,100],
		            height:437,
		            width:878
		            ">
		        	
		            <thead>
		                <tr>
		                	<th data-options="field:'INVEST_DATE',width:80,formatter:sort_date_formatter" >日期</th>
		                    <th data-options="field:'INVEST_TIMES',width:80">投标次数</th>
		                    <th data-options="field:'INVEST_AMOUNT',width:120,formatter:float_number_formatter">投标金额</th>
		                    <th data-options="field:'SHOWOK',width:80,formatter:operator_formatter_invest_detail">操作</th>
		                </tr>
		            </thead>
			    </table>
			    <div id="invest_tb" style="padding:5px;height:auto">
			        <div>
			           投标时间: <input class="easyui-datebox" name="startDate_invest" style="width:100px" data-options="formatter:date_picker_formatter" placeholder="开始日期">
			            <input class="easyui-datebox" name="endDate_invest" style="width:100px" data-options="formatter:date_picker_formatter" placeholder="结束日期">
			            <a href="#" id="search_invest" class="easyui-linkbutton" iconCls="icon-search" title="">查询</a>
			        </div>
			    </div>
			    
			    
	  	<div id="pwin"></div>
	</body>
</html>
<script>



function sort_date_formatter(val,row,index){
				if(val==null){
					return null;
				}else{
					return new Date(val.time).format('yyyy-MM');
				}
			}
function date_picker_formatter(date){
	            var y = date.getFullYear();
	            var m = date.getMonth()+1;
	            var d = date.getDate();
	            return y+'-'+(m<10?('0'+m):m);
	        }
  	
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


function F_M_S_D_date_formatter(val,row,index){
	if(val==null){
		return "未签约";
	}else{
		return new Date(val.time).format('yyyy-MM-dd');
	}
}
function date_formatter(val,row,index){
	if(val == '合计'){
		return val;
	}else if(val==null){
		return null;
	}else{
		return new Date(val.time).format('yyyy-MM-dd');
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

function operator_formatter_invest_detail(val,row,index){
	if(row.INVEST_TIMES == '合计') return null;
	return '<a href="javascript:;" onclick="showdialog(\'pwin\',\'/back/statistics/memberbase_invest_detail_json.jsp?date='+sort_date_formatter(row.INVEST_DATE)+'&username='+<%=username%>+'\',400,400)">查看详细</a>';
}

	$(function(){
    	
    	$("#search_invest").click(function(){
    		var start_date = $('[name="startDate_invest"]').val()+'-01';
    		var end_date = $('[name="endDate_invest"]').val()+'-01';
    		if(start_date == null || start_date == '' || end_date == null || end_date == '') return;
    		$("#invest_dg").datagrid({
	   			url: '/back/stcsMemBaseAction!m_i_detail?username=<%=username%>&startDate='+start_date+'&endDate='+end_date
			});
    	});
    	 $(window).resize(function(){
			$("#invest_dg").datagrid('resize',{
				width:'100%'
			});
		});
	})
</script>

