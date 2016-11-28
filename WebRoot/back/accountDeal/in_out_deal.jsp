<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
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
            url:'/back/accountDealAction!in_out_deal_data',
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
		            <th data-options="field:'TARGET_ACCOUNT_USER_USERTYPE',width:80,formatter:usertype_formatter">会员类型</th>
		            <th data-options="field:'SIGNBANK',width:100,formatter:signbank_formatter">签约行</th>
		            <th data-options="field:'SIGNTYPE',width:90,formatter:signtype_formatter">签约类型</th>
		            <th data-options="field:'TYPE',width:90">交易类型</th>
		            <th data-options="field:'TXOPT',width:90,formatter:txopt_formatter">发起方</th>
		            <th data-options="field:'MONEY_ADD',width:90,align:'right',formatter:add_formatter">入金额</th>
		            <th data-options="field:'MONEY_SUBTRACT',width:90,align:'right',formatter:subtract_formatter">出金额</th>
		        </tr>
	        </thead>
	        <thead>
	        	<tr>
	        		<th data-options="field:'CREATEDATE',width:120,formatter:date_formatter">申请日期</th>
		            <th data-options="field:'CHECKDATE',width:120,formatter:date_formatter">审核日期</th>
		            <th data-options="field:'OPERAT_USER',width:90">操作员</th>
		            <th data-options="field:'CHECK_USER',width:90">审核员</th>
		            <th data-options="field:'STATE',width:90,formatter:state_formatter">状态</th>
	        	</tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        	签约行：
				<select name="bank" id="bank">
					<option value="2">招商</option>
					<option value="1">华夏</option>
					<option value="-1">全部</option>
				</select>
	        	业务日期:<input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
	            <input id="keyWord" type="text" name="keyWord" size="25" class="combo" title="会员名称,交易账号,交易类型"  placeholder="会员名称,交易账号,交易类型" />
	            <input type="hidden" name="load" value="true" />
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <a href="javascript:;" pre-href="/back/accountDealAction!in_out_deal_data" id="print" class="easyui-linkbutton" iconCls="icon-print">打印</a>
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
	function txdir_formatter(val,row,index){
		if(val==1){
			return "资金增加";
		}else if(val==2){
			return "资金减少";
		}else{
			return val;
		}
	}
	function txopt_formatter(val,row,index){
		if(val==1){
			return "银行";
		}else if(val==0){
			return "微交所";
		}else{
			return val;
		}
	}
	function signbank_formatter(val,row,index){
		if(val==1){
			return "<span style='color:#D72323'>华夏</span>";
		}else if(val==2){
			return "<span style='color:#D72323'>招商</span><br /><span style='color:#D72323'>871903469010901</span>";
		}else{
			return val;
		}
	}
	function signtype_formatter(val,row,index){
		if(val==1){
			return "<span style='color:#D72323'>本行</span>";
		}else if(val==2){
			return "<span style='color:#D72323'>他行</span>";
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
	function add_formatter(val,row,index){
		if(row.TYPE=='合计'){
			return val;
		}else if(row.MONEY_ADD==null){
			return null;
		}else{
			var m = row.MONEY_ADD;
			if(m!=0){
				return "<span style='color:#D72323'>"+m.toFixed(2)+"</span>";
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
	
	function state_formatter(val,row,index){
		if(row.TYPE=='合计'){
			return val;
		}else if(row.CHECKFLAG==null){
			return null;
		}else{
			var f1 = row.CHECKFLAG;
			if(f1=='24'){
				return "<span style='color:green;'>已审核</span>";
			}else if(f1=='25'){
				return "<span style='color:#4169E1;'>待审核</span>";
			}else if(f1=='26'){
				return "<span style='color:green;'>已审核</span>";
			}else if(f1=='27'){
				return "<span style='color:red;'>已驳回</span>";
			}else{
				return f1;
			}
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
			var source_url = '/back/accountDealAction!in_out_deal_data';
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var keyWord = $("[name='keyWord']").val();
			var bank = $("[name='bank']").val();
			var url_ = source_url+
		   		"?startDate=" + startDate +
		   		"&load=true" +
		   		"&endDate=" + endDate +
		   		"&bank=" + bank +
		   		"&keyWord=" + keyWord; 
			$("#dg").datagrid({
		   		url: url_
			});
		});
		$("#print").click(function(){
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var bank = $("[name='bank']").val();
			var keyWord = $("[name='keyWord']").val();
			var href = $(this).attr("pre-href");
			var url_ = href+
		   		"?action=1&startDate=" + startDate +
		   		"&load=true" +
		   		"&endDate=" + endDate +
		   		"&bank=" + bank+
		   		"&keyWord=" + keyWord; 
			window.showModalDialog(url_,null,"dialogWidth=1000px;dialogHeight=400px");
		});
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>