<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%
	Date now = new Date();
%>
<html>
	<head>
		<title>工行专户变更</title>
		<%@ include file="/common/taglib.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			<!--
				body{
				 overflow:auto !important;padding;0;margin:0;
				}
			-->
		</style>
 	</head>
	<body>
		<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            url:'/back/accountDealAction!icbc_biangeng_query',
            singleSelect:true,
            method:'get',
            toolbar:'#tb'
            ">
	        <thead data-options="frozen:true">
	        	<tr>
		            <th data-options="field:'REALNAME',width:100,formatter:name_formatter">会员名称</th>
		            <th data-options="field:'USERNAME',width:80">交易账号</th>
		            <th data-options="field:'TYPE',width:80,formatter:usertype_formatter">会员类型</th>
		            <th data-options="field:'CAPTION',width:100">当前开户行</th>
		            <th data-options="field:'CHANNEL',width:100,formatter:channel_formatter">当前专户</th>
		            <th data-options="field:'BALANCE',width:90,align:'right',formatter:money_formatter">可用余额</th>
		            <th data-options="field:'FROZEN',width:90,align:'right',formatter:money_formatter">冻结余额</th>
		            <th data-options="field:'ALL',width:90,align:'right',formatter:all_formatter">总余额</th>
		            <th data-options="field:'OP',width:120,formatter:cz_formatter">操作</th>
		        </tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	            交易账号：<input id="userName" type="text" name="userName" size="25" class="combo" title="交易账号"  placeholder="交易账号" />
	            会员名称：<input id="keyWord" type="text" name="keyWord" size="25" class="combo" title="会员名称"  placeholder="会员名称" />
	            <input type="hidden" name="load" value="true" />
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	        </div>
	    </div>
	    <div id="win"></div>
		<%@ include file="/common/messageTip.jsp" %>
	</body> 
</html>



<script>
	function cz_formatter(val,row,index){
		var str = "";
		if(row.ID==null) return null;
		str += "<a uid='"+row.ID+"' href='javascript:;' onclick='dobiangeng("+row.ID+")'>变更专户为工行</a>";
		return str;
	}
	function usertype_formatter(val,row,index){
		if(val=='T'){
			return "投资人";
		}else if(val=='R'){
			return "融资方";
		}else if(val=='D'){
			return "担保方";
		}else{
			return val;
		}
	}
	function channel_formatter(val,row,index){
		if(val==1){
			return "<span style='color:#D72323'>招行</span><br /><span style='color:#D72323'>871903469010608</span>";
		}else if(val==2){
			return "<span style='color:#D72323'>工行</span><br /><span style='color:#D72323'>2502110419024503160</span>";
		}else{
			return val;
		}
	}
	function date_formatter(val,row,index){
		if(val==null){
			return "";
		}else{
			return new Date(val.time).format('yyyy-MM-dd hh:mm:ss');
		}
	}
	function money_formatter(val,row,index){
		return "<span style='color:#D72323'>"+val.toFixed(2)+"</span>";
	}
	function all_formatter(val,row,index){
		return "<span style='color:#D72323'>"+(row.BALANCE+row.FROZEN).toFixed(2)+"</span>";
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
	
	function dobiangeng(val){
		$.messager.confirm('变更专户为工行','你确定要将此会员的专户变更为工行吗？',function(r){
			if(r){
				var url = "/back/accountDeal/accountDealAction!icbc_biangeng_do?userId="+val;
				var d = "&time="+new Date().getTime();
				$.ajax({
			   		type:"post",
			   		url:url,
			   		data:d,
			   		dataType:"json",
			   		success:function(data){
			   			alert(data.msg);
			   			location.reload();
			   		},
			   		error:function(data){
			   			alert(data.msg);
			   			location.reload();
			   		}
			   	});
			}
		});  
	}
	
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$("#search").click(function(){
			var source_url = '/back/accountDealAction!icbc_biangeng_query';
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var keyWord = $("[name='keyWord']").val();
			var userName = $("[name='userName']").val();
			var url_ = source_url+
		   		"?startDate=" + startDate +
		   		"&load=true" +
		   		"&endDate=" + endDate +
		   		"&userName=" + userName +
		   		"&keyWord=" + keyWord; 
			$("#dg").datagrid({
		   		url: url_
			});
		});
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>