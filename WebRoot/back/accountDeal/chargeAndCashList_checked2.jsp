<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%
	Date now = new Date();
%>
<html>
	<head>
		<title></title>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<%@ include file="/common/taglib.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
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
            url:'/back/accountDealAction!charge_cash_data',
            singleSelect:true,
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
	        <thead data-options="frozen:true">
	        	<tr>
		            <th data-options="field:'TARGET_ACCOUNT_USER_REALNAME',width:100,formatter:name_formatter">会员名称</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_USERNAME',width:80">交易账号</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_USERTYPE',width:60,formatter:usertype_formatter">会员类型</th>
		            <th data-options="field:'TYPE',width:60">交易类型</th>
		            <th data-options="field:'MONEY_ADD',width:100,align:'right',formatter:add_formatter">充值额</th>
		            <th data-options="field:'MONEY_SUBTRACT',width:100,align:'right',formatter:subtract_formatter">提现额</th>
		        </tr>
	        </thead>
	         <thead>
	        	<tr>
		            <th data-options="field:'STATE',width:90,formatter:state_formatter">状态</th>
		            <th data-options="field:'CREATEDATE',width:120,formatter:date_formatter">申请日期</th>
		            <th data-options="field:'CHECKDATE',width:120,formatter:date_formatter">审核日期</th>
		            <th data-options="field:'HKDATE',width:120,formatter:date_formatter">划款日期</th>
		            <th data-options="field:'MEMO',width:100">备注</th>
		        </tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        	会员类型：
				<select id="userType" name="userType">
					<option value="T">投资人</option>
					<option value="R">融资方</option>
					<option value="all">全部</option>
				</select>
				交易类型:
				<select id="jyType" name="jyType">
					<option value="charge">现金充值</option>
					<option value="cash">提现</option>
					<option value="lx">活期利息</option>
					<option value="charge_hx">银转商(入金)</option>
					<option value="cash_hx">商转银(出金)</option>
					<option value="all">全部</option>
				</select>
	        	申请日期:<input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
	            <input id="keyWord" type="text" name="keyWord" size="25" class="combo" title="会员名称,交易账号,交易类型"  placeholder="会员名称,交易账号,交易类型" />
	            <input type="hidden" name="load" value="true" />
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <a href="#" id="empExcel" class="easyui-linkbutton" iconCls="icon-save">导出EXCEL</a>
	        </div>
	    </div>
	    <div id="win"></div>
		<%@ include file="/common/messageTip.jsp" %>
	</body> 
</html>



<script>
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
	
	function state_formatter(val,row,index){
		if(row.TYPE=='合计'){
			return val;
		}else if(row.CHECKFLAG==null){
			return null;
		}else if(row.CHECKFLAG2==null){
			return null;
		}else{
			var f1 = row.CHECKFLAG;
			var f2 = row.CHECKFLAG2;
			if(f1=='0'||f1=='3'||f1=='25'||f1=='36'||f1=='2.5'||f1=='2.9'||f1=='2.4'){
				return "<span style='color:#4169E1;'>待审核</span>";
			}else if(f1=='1'||f1=='24'||f1=='26'||f1=='37'){
				return "<span style='color:green;'>已审核</span>";
			}else if(f1=='2'||f1=='5'||f1=='27'||f1=='38'){
				return "<span style='color:red;'>已驳回</span>";
			}else if(f1=='4'&&f2=='0'){
				return "<span style='color:4169E1;'>待划款</span>";
			}else if(f1=='4'&&f2=='1'){
				return "<span style='color:green;'>已划款</span>";
			}else if(f1=='4'&&f2=='2'){
				return "<span style='color:red;'>转账异常</span>";
			}else if(f1=='4'&&f2=='3'){
				return "<span style='color:red;'>提现错误</span>";
			}else if(f1=='4'&&f2=='4'){
				return "<span style='color:green;'>提现冲正</span>";
			}else{
				return f1+'&'+f2;
			}
		}
	}
	
	function add_formatter(val,row,index){
		if(row.TYPE=='合计'){
			return val;
		}else if(row.MONEY_ADD==null){
			return null;
		}else{
			var m = row.MONEY_ADD;
			if(m!=0){
				return "<span style='color:#D72323;'>"+m.toFixed(2)+"</span>";
			}else{
				return "-";
			}
		}
	}
	
	function subtract_formatter(val,row,index){
		if(row.TYPE=='合计'){
			return val;
		}else if(row.MONEY_SUBTRACT==null){
			return null;
		}else{
			var m = row.MONEY_SUBTRACT;
			if(m!=0){
				return "<span style='color:green;'>"+m.toFixed(2)+"</span>";
			}else{
				return "-";
			}
		}
	}
	
	function date_formatter(val,row,index){
		if(val==null){
			return "";
		}else{
			return new Date(val.time).format('yyyy-MM-dd hh:mm:ss');
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
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$("#search").click(function(){
			var source_url = '/back/accountDealAction!charge_cash_data';
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var userType = $("[name='userType']").val();
			var jyType = $("[name='jyType']").val();
			var keyWord = $("[name='keyWord']").val();
			var time = new Date();
			var url_ = source_url+
		   		"?startDate=" + startDate +
		   		"&load=true" +
		   		"&endDate=" + endDate +
		   		"&userType=" + userType +
		   		"&jyType=" + jyType +
		   		"&time=" + time.getTime() +
		   		"&keyWord=" + keyWord;
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
			$(this).attr("href","/back/accountDealAction!charge_cash_excel"+parameters);
		});
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>