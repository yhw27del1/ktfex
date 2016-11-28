
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%
	Date now = new Date();
%>
<html>
	<head>
		<title></title>
		<%@ include file="/common/taglib.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
				body{
				 overflow:auto !important;padding;0;margin:0;
				}
		</style>
 	</head>
	<body>
		<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            url:'/back/stcsAuthorityAction!getAuthoritysList',
            singleSelect:true,
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
	        <thead data-options="frozen:true">
	        	<tr>
		        	<th data-options="field:'NAME_',width:80,sortable:true,formatter:name_formatter">名称</th>
		            <th data-options="field:'SHOWCODING',width:80,sortable:true">编码</th>
		            <th data-options="field:'TYPENAME',width:80,sortable:true">类型</th>
		            </tr>
		    </thead>
		    <thead>
                <tr>
		            <th data-options="field:'MOBILE_',width:80,sortable:true,formatter:phone_formatter">手机</th>
		            <th data-options="field:'ADDRESS_',width:250,sortable:true">地址</th>
		            <th data-options="field:'USERREALNAME',width:80,sortable:true">介绍人</th> 
		            <th data-options="field:'CREATDATE',width:80,formatter:date_formatter" >开户日期</th>
		            <th data-options="field:'STATENAME',width:80,sortable:true">状态</th>
		            <th data-options="field:'SHOWOK',formatter:operator_formatter">操作</th>
		        </tr> 
	        </thead>
	         
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        	时间: <input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" >
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期" >
 	            <input type="text" name="queryByOrgCode" placeholder="机构编码" class="combo"  id="queryByOrgCode"/>
	            <input id="qkeyWord" type="text" name="qkeyWord" class="combo" title="用户名"  placeholder="关键字" />
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a> 
	        </div>
	    </div>
	    <div id="win"></div>
		<%@ include file="/common/messageTip.jsp" %>
	</body> 
</html>



<script>
	function date_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return new Date(val.time).format('yyyy-MM-dd');
		}
	}
	 
	function operator_formatter(val,row,index){ 
		    var str = '';
		str += '<a href="javascript:;" onclick="showdialog(\'win\',\'/back/stcsAuthorityAction!m_a_jyl?queryByOrgCode='+row.SHOWCODING+'\',900,410,\'授权中心交易跟踪--交易量统计\')">交易量统计</a>';    
		str += '&nbsp;<a href="javascript:;" onclick="showdialog(\'win\',\'/back/stcsAuthorityAction!m_a_hk?queryByOrgCode='+row.SHOWCODING+'\',900,410,\'授权中心交易跟踪--还款统计\')">还款统计</a>';   
	    str += '&nbsp;<a href="javascript:;" onclick="showdialog(\'win\',\'/back/stcsAuthorityAction!m_a_hy?queryByOrgCode='+row.SHOWCODING+'\',900,410,\'授权中心交易跟踪--投资会员活跃度统计\')">投资会员活跃度统计</a>'; 
           return str;
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
	
	function showdialog(ele,url,width,height,titleStr){
    			$('#'+ele).dialog({
					title:titleStr,  
				    width:width,
				    height:height,
				    modal:true,
				    maximizable:true,
				    href:url
				});
				//showModalDialog(url,'','dialogWidth:'+width+'px;dialogHeight:500px;center:yes;help:no;status:no;'); 
	}   
	
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$("#search").click(function(){
			var source_url = '/back/stcsAuthorityAction!getAuthoritysList'; 
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var queryByOrgCode = $("[name='queryByOrgCode']").val();
			var qkeyWord = $("[name='qkeyWord']").val();
			var url_ = source_url+
		   		"?startDate=" + startDate + 
		   		"&endDate=" + endDate +
		   		"&queryByOrgCode=" + queryByOrgCode +
		   		"&qkeyWord=" + qkeyWord;
			$("#dg").datagrid({
		   		url: url_
			});
		});
		
		
		$("#empExcel").click(function(){
			var options = $("#dg").datagrid('options');
			var url = options.url;
			var index = url.indexOf("?");
			var parameters = "";
			if(index != -1){
				parameters = url.substring(index);
			}
			$(this).attr("href","/back/financingBaseAction!EmpList"+parameters);
		});
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>