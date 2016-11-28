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
		<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
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
            url:'/back/accountDealAction!checkXYD_data',
            singleSelect:true,
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            onLoadSuccess:loadSuccess,
            pageList:[15,30,50,100]
            ">
	        <thead data-options="frozen:true">
	        	<tr>
	        		<th data-options="field:'TARGET_ACCOUNT_USER_USERTYPE',width:60,formatter:usertype_formatter">会员类型</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_REALNAME',width:180,formatter:name_formatter">会员名称</th>
		            <th data-options="field:'TARGET_ACCOUNT_USER_USERNAME',width:75">交易账号</th>
		            <th data-options="field:'TYPE',width:60">交易类型</th>
		            <th data-options="field:'MONEY',width:100,align:'right',formatter:money_formatter">金额</th>
		        </tr>
	        </thead>
	         <thead>
	        	<tr>
		            <th data-options="field:'CREATEDATE',width:120,formatter:date_formatter">划拨日期</th>
		            <th data-options="field:'OPERAT_USER',width:100">操作员</th>
		            <th data-options="field:'MEMO',width:100">备注</th>
		        </tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        	<c:if test="${menuMap['charge_check_pass']=='inline'}">
	            	<input type="button" id="pass" value="批量审核通过" />
	            </c:if>
	            <c:if test="${menuMap['charge_check_nopass']=='inline'}">
	           		<input type="button" id="nopass" value="批量审核驳回" />
	            </c:if>
	            <span id="tip" style="color:red;"></span>
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
	
	function money_formatter(val,row,index){
		if(row.TYPE=='合计'){
			return val;
		}else{
			var m = row.MONEY;
			if(m!=0){
				return "<span style='color:#D72323;'>"+m.toFixed(2)+"</span>";
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
	
	function loadSuccess(data){
		$('#tip').html(data['tip']);
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
		
		$("#pass").click(function(){
	   		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行批量审核通过吗？",
				ok:function(){
					$("#pass").attr("disabled",true);
					window.location.href = "/back/accountDeal/accountDealAction!checkXYD_pass?time="+new Date().getTime();
				},
				cancelVal:'关闭',cancel:true
			});
    	});
    
	    $("#nopass").click(function(){
	   		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行批量审核驳回吗？",
				ok:function(){
					$("#nopass").attr("disabled",true);
					window.location.href = "/back/accountDeal/accountDealAction!checkXYD_nopass?time="+new Date().getTime();
				},
				cancelVal:'关闭',cancel:true
			});
	    });
		
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>