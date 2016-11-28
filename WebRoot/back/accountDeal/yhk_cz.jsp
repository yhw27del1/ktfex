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
            url:'/back/accountDealAction!yhk_cz_data',
            singleSelect:true,
            method:'get',
            toolbar:'#tb'
            ">
	        <thead data-options="frozen:true">
	        	<tr>
	        		<th data-options="field:'TARGET_ACCOUNT_USER_USERTYPE',width:60,formatter:usertype_formatter">会员类型</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_REALNAME',width:60,formatter:name_formatter">会员名称</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_USERNAME',width:75">交易账号</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_BANK',width:105,formatter:bankcard_formatter">银行账号</th>
		            <th data-options="field:'TYPE',width:60">交易类型</th>
		            <th data-options="field:'MONEY_SUBTRACT',width:100,align:'right',formatter:subtract_formatter">提现额</th>
		            <th data-options="field:'CHANNEL',width:60,formatter:qd_formatter">专户</th>
		        </tr>
	        </thead>
	         <thead>
	        	<tr>
		            <th data-options="field:'CHECKDATE',width:120,formatter:date_formatter">审核日期</th>
		            <th data-options="field:'HKDATE',width:120,formatter:date_formatter">划款日期</th>
		            <th data-options="field:'MEMO',width:100">备注</th>
		            <th data-options="field:'HK_USER',width:100">划款员</th>
		            <th data-options="field:'STATE',width:90,formatter:state_formatter">状态</th>
		            <th data-options="field:'OP',width:90,formatter:cz_formatter">操作</th>
		        </tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
			划款日期:<input class="easyui-datebox" name="startDate" style="width:100px" placeholder="划款日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
			提现金额：<input id="chargeAmount" type="text" name="chargeAmount" size="15" class="combo" title="提现金额"  placeholder="提现金额" />
			交易账号：<input id="userName" type="text" name="userName" size="15" class="combo" title="交易账号"  placeholder="交易账号" />
			会员名称：<input id="realName" type="text" name="realName" size="15" class="combo" title="会员名称"  placeholder="会员名称" />
	        <input type="hidden" name="load" value="true" />
	        <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
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
	
	function fs_formatter(val,row,index){
		if(val=='0'){
			return "单笔";
		}else if(val=='1'){
			return "批量";
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
			}else if(f1=='1'||(f1=='4'&&f2=='0')||f1=='24'||f1=='26'||f1=='37'){
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
		str += '<a href="javascript:;" onclick="docz(\''+row.ID+'\')">冲正</a>';
		return str;
	}
	
	function docz(id){
		$.messager.prompt('你确定要冲正此笔已划款提现吗？', '请输入冲正备注', function(r){
			if(r&&id){
				var memo = encodeURI(r);
				var url = "/back/accountDeal/accountDealAction!yhk_cz_do?time="+new Date().getTime()+"&id="+id+"&memo="+memo;
				$.getJSON(url,function(data){
					alert(data.message);
					$("#search").trigger("click");
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
			var source_url = '/back/accountDealAction!yhk_cz_data';
			var startDate = $("[name='startDate']").val();
			var userName = $("[name='userName']").val();
			var realName = $("[name='realName']").val();
			var chargeAmount = $("[name='chargeAmount']").val();
			var time = new Date();
			var url_ = source_url+
		   		"?startDate=" + startDate +
		   		"&load=true" +
		   		"&userName=" + userName +
		   		"&realName=" + realName +
		   		"&chargeAmount=" + chargeAmount +
		   		"&time=" + time.getTime()
			$("#dg").datagrid({
		   		url: url_
			});
		});
		
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>