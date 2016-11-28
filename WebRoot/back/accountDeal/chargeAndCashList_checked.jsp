<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%
	Date now = new Date();
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
            url:'/back/accountDealAction!checkAndCashList_data',
            singleSelect:true,
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
	        <thead data-options="frozen:true">
	        	<tr>
	        		<th data-options="field:'TARGET_ACCOUNT_USER_USERTYPE',width:60,formatter:usertype_formatter">会员类型</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_REALNAME',width:60,formatter:name_formatter">会员名称</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_USERNAME',width:75">交易账号</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_BANK',width:105,formatter:bankcard_formatter">银行账号</th>
		            <th data-options="field:'HUIDAN',width:105">回单号</th>
		            <th data-options="field:'TYPE',width:60">交易类型</th>
		            <th data-options="field:'MONEY_ADD',width:100,align:'right',formatter:add_formatter">充值额</th>
		            <th data-options="field:'MONEY_SUBTRACT',width:100,align:'right',formatter:subtract_formatter">提现额</th>
		            <th data-options="field:'CHANNEL',width:60,formatter:qd_formatter">专户</th>
		        </tr>
	        </thead>
	         <thead>
	        	<tr>
		            <th data-options="field:'CREATEDATE',width:120,formatter:date_formatter">充值/提现时间</th>
		            <th data-options="field:'CHECKDATE',width:120,formatter:date_formatter">审核日期</th>
		            <th data-options="field:'HKDATE',width:120,formatter:date_formatter">划款日期</th>
		            <th data-options="field:'MEMO',width:100">备注</th>
		            <th data-options="field:'OPERAT_USER',width:100">操作员</th>
		            <th data-options="field:'CHECK_USER',width:100">审核员</th>
		            <th data-options="field:'BATCHFLAG',width:100,formatter:fs_formatter">方式</th>
		            <th data-options="field:'STATE',width:90,formatter:state_formatter">状态</th>
		            <th data-options="field:'OP',width:90,formatter:cz_formatter">操作</th>
		        </tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        	会员类型：
				<select id="userType" name="userType">
					<option value="T">投资人</option>
					<option value="R">融资方</option>
					<option value="D">担保方</option>
					<option value="Q">其他</option>
					<option value="all">全部</option>
				</select>
				交易类型:
				<select id="jyType" name="jyType">
					<option value="charge">现金充值</option>
					<option value="cash">提现</option>
					<option value="lx">活期利息</option>
					<option value="hb">资金划拨</option>
					<option value="all">全部</option>
				</select>
				方式&nbsp;
				<select id="chargeStyle" name="chargeStyle">
					<option value="single">单笔</option>
					<option value="batch">批量</option>
					<option value="all">全部</option>
				</select>	
				专户&nbsp;
				<select id="qudao" name="qudao">
					<option value="0">全部</option>
					<option value="1">招商银行</option>
					<option value="2">工商银行</option>
				</select>	
	        	审核日期:<input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
	            <input id="keyWord" type="text" name="keyWord" size="25" class="combo" title="会员名称,交易账号,交易类型"  placeholder="会员名称,交易账号,交易类型" />
	            <input type="hidden" name="load" value="true" />
	            <br />
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <a href="#" id="print" class="easyui-linkbutton" iconCls="icon-print">打印</a>
	            <a href="#" id="save" class="easyui-linkbutton" iconCls="icon-save">导出EXCEL</a>
	            <a href="#" id="excel" class="easyui-linkbutton" iconCls="icon-save">按日汇总</a>
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
		}else if(val=='Q'){
			return "其他";
		}else{
			return val;
		}
	}
	
	function fs_formatter(val,row,index){
		if(val=='0'){
			return "单笔";
		}else if(val=='1'){
			return "批量";
		}else{
			return val;
		}
	}
	
	function qd_formatter(val,row,index){
		if(val==0){
			return "无";
		}else if(val==1){
			return "招商银行";
		}else if(val==2){
			return "工商银行";
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
			if(f1=='1'&&f2=='1'){
				return "<span style='color:#4169E1;'>已审核</span>";
			}else if(f1=='37'||f1=='41'||f1=='43'){
				return "<span style='color:#4169E1;'>已审核</span>";
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
	
	function cz_formatter(val,row,index){
		var str = '';
		if(row.ID==null) return null;
		str += '<a href="javascript:;" onclick="window.showModalDialog(\'/back/accountDeal/accountDealAction!print_voucher?ids='+row.ID+ '\',\'\',\'dialogWidth=900px;dialogHeight=600px;\')">打印凭证</a>';
		return str;
	}
	
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$("#search").click(function(){
			var source_url = '/back/accountDealAction!checkAndCashList_data';
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var userType = $("[name='userType']").val();
			var jyType = $("[name='jyType']").val();
			var chargeStyle = $("[name='chargeStyle']").val();
			var qudao = $("[name='qudao']").val();
			var keyWord = $("[name='keyWord']").val();
			var time = new Date();
			var url_ = source_url+
		   		"?startDate=" + startDate +
		   		"&load=true" +
		   		"&endDate=" + endDate +
		   		"&userType=" + userType +
		   		"&jyType=" + jyType +
		   		"&chargeStyle=" + chargeStyle +
		   		"&qudao=" + qudao +
		   		"&time=" + time.getTime() +
		   		"&keyWord=" + keyWord;
			$("#dg").datagrid({
		   		url: url_
			});
		});
		$("#print").click(function(){
			var source_url = '/back/accountDealAction!checkAndCashList_data';
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var userType = $("[name='userType']").val();
			var jyType = $("[name='jyType']").val();
			var chargeStyle = $("[name='chargeStyle']").val();
			var qudao = $("[name='qudao']").val();
			var keyWord = $("[name='keyWord']").val();
			var time = new Date();
			var url_ = source_url+
		   		"?action=1&startDate=" + startDate +
		   		"&load=true" +
		   		"&endDate=" + endDate +
		   		"&userType=" + userType +
		   		"&jyType=" + jyType +
		   		"&chargeStyle=" + chargeStyle +
		   		"&qudao=" + qudao +
		   		"&time=" + time.getTime() +
		   		"&keyWord=" + keyWord;
			window.showModalDialog(url_,null,"dialogWidth=1200px;dialogHeight=600px");
		});
		
		$("#save").click(function(){
			var source_url = '/back/accountDealAction!checkAndCashList_data';
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var userType = $("[name='userType']").val();
			var jyType = $("[name='jyType']").val();
			var chargeStyle = $("[name='chargeStyle']").val();
			var qudao = $("[name='qudao']").val();
			var keyWord = $("[name='keyWord']").val();
			var time = new Date();
			var url_ = source_url+
		   		"?action=2&startDate=" + startDate +
		   		"&load=true" +
		   		"&endDate=" + endDate +
		   		"&userType=" + userType +
		   		"&jyType=" + jyType +
		   		"&chargeStyle=" + chargeStyle +
		   		"&qudao=" + qudao +
		   		"&time=" + time.getTime() +
		   		"&keyWord=" + keyWord;
		   	$(this).attr("href",url_);
		});
		
		$("#excel").click(function(){
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
	        var url = "/back/accountDealAction!importCashAndChargeExcel?time="+new Date().getTime();
			window.location.href = '/back/accountDealAction!cashFlow?startDate='+startDate+"&endDate="+endDate;
	    });
    
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>