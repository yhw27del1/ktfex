<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglib.jsp"%>
<%
	Date now = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
	<head>
		<title></title>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			body{padding:0;margin:0;}
			.combo{height:22px;line-height:22px;background-color: #fff;padding:0 2px;width:160px;}
			.easyui-datebox{width:100px;}
		</style>
	</head>
	<body>
		<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            url:'/back/paymentRecord/paymentRecordAction!list_jqhktx',
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
	        <thead data-options="frozen:true">
	        	<tr>
		        	<th data-options="field:'QIANYUEDATE',width:80,formatter:date_formatter" >签约日期</th>
		        	<th data-options="field:'FINANCBASECODE',width:80" >项目编号</th>
		        	<th data-options="field:'FSHORTNAME',width:160" >项目简称</th>
		        	<th data-options="field:'CURRENYAMOUNT',width:80,align:'right',formatter:float_formatter" >融资额(元)</th>
		        	<th data-options="field:'FINANCIERNAME',width:80" >融资方帐号</th>
		        	<th data-options="field:'FREALNAME',width:60,formatter:name_formatter" >融资方</th>
		        </tr>
	        </thead>
	        <thead>
	        	<tr>
		        	<th data-options="field:'FBANKACCOUNT',width:120,formatter:bankcard_formatter" >融资方银行帐号</th>
		        	<th data-options="field:'FBALANCE',width:100,align:'right',formatter:float_formatter" >融资方帐户余额</th>
		        	<th data-options="field:'TOTALYH',width:80,align:'right',formatter:float_formatter" >当期应还金额</th>
		        	<th data-options="field:'DBHSNAME',width:160" >担保方</th>
		        	<th data-options="field:'YHDATE',width:100,formatter:date_formatter" >应还日期</th>
		        	<th data-options="field:'QS',width:40,formatter:qs_formatter" >期次</th>
		        </tr>
	        </thead>
	       
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	            
	            <input class="combo" name="fbcode" id="fbcode" placeholder="融资项目帐号或简称"/>
	            <input class="combo" name="qkeyWord" id="qkeyWord" placeholder="融资方账号或名称"/>
	             应还款日期<select name="bjStr" style="width:60" id="bjStr" >
	        		<option value="=" selected="selected">=</option>
	        		<option value="<"><</option>
	        		<option value=">">></option>
	        		<option value="<="><=</option>
	        		<option value=">=">>=</option>
	        	</select>
	            <input name="startDate" class="easyui-datebox" data-options="editable:false" value="<%=sdf.format(now) %>" id="startDate"/>
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <a href="/back/paymentRecord/paymentRecordAction!list_jqhktx?excelFlag=1" id="export" class="easyui-linkbutton" iconCls="icon-save">导出EXCEL</a>
	        </div>
	    </div>
	</body>
</html>
<script type="text/javascript">
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		
		$("#search").click(function(){
			var source_url = '/back/paymentRecord/paymentRecordAction!list_jqhktx';
			var fbcode = $("[name='fbcode']").val();
			var qkeyWord = $("[name='qkeyWord']").val();
			var bjStr = $("[name='bjStr']").val();
			var startDate = $("[name='startDate']").val();
			var url_ = source_url+
		   		"?fbcode="+fbcode+
		   		"&qkeyWord=" + qkeyWord +
		   		"&bjStr=" + bjStr +
		   		"&startDate=" + startDate; 
			$("#dg").datagrid({
		   		url: url_
			});
			$("#export").attr("href",url_+"&excelFlag=1");
		});
		
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
	
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
	
	function date_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return new Date(val.time).format('yyyy-MM-dd');
		}
	}
	function qs_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return row.RETURNTIMES+"/"+val;
		}
	}
	function float_formatter(val,row,index){
	    if(val==null){
	        return null;
	    }else{
	        return val.toFixed(2);
	    }
	}
</script>
 

