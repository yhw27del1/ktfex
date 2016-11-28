<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglib.jsp"%>
<%
	Date now = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
	<head>
		<title>融资项目列表</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
	   	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			body{padding:0;margin:0;}
			.combo{height:22px;line-height:22px;background-color: #fff;padding:0 2px;width:100px;}
			.easyui-datebox{width:100px;}
		</style>
	</head>
	<body>

		<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            url:'/sheets/paymentRecordSheets/paymentRecordAction!financeList',
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
            <!-- data-options="frozen:true" -->
	        <thead data-options="frozen:true">
	        	<tr>
		        	<th rowspan="2" data-options="field:'FINANCBASECODE',width:80">项目编号</th>
		        	<th rowspan="2" data-options="field:'QS',width:60,formatter:qs_formatter">还款期数</th>
		        	<th rowspan="2" data-options="field:'QIANYUEDATE',width:80,formatter:date_formatter">签约日期</th>
		        	<th rowspan="2" data-options="field:'FREALNAME',width:70,formatter:name_formatter">融资方姓名</th>
		        	<th rowspan="2" data-options="field:'FBALANCE',width:60,align:'right',formatter:float_formatter">可用余额</th>
		        	<th rowspan="2" data-options="field:'FROZENAMOUNT',width:60,align:'right',formatter:float_formatter">冻结余额</th>
		        	<th rowspan="2" data-options="field:'FORGNAME',width:70">所属机构</th>
	        	</tr>
	        </thead>
	        <thead>
	        	<tr>
	        		<th colspan="5">应还款项</th>
	        		<th colspan="9">实还款项</th> 
	        		<th rowspan="2" data-options="field:'SSFEE4',width:70,align:'right',formatter:float_formatter">交易手续费</th>
	        		<th rowspan="2" data-options="field:'detail',width:70,formatter:detail_formatter">查看详细</th>
	        	</tr>
	        	<tr>
		        	<th data-options="field:'YHBJ',width:70,align:'right',formatter:float_formatter">本金</th>
		        	<th data-options="field:'YHLX',width:70,align:'right',formatter:float_formatter">利息</th>
		        	<th data-options="field:'YHFEE1',width:70,align:'right',formatter:float_formatter">服务费</th>
		        	<th data-options="field:'YHFEE2',width:70,align:'right',formatter:float_formatter">担保费</th>
		        	<th data-options="field:'YHFEE3',width:70,align:'right',formatter:float_formatter">风险管理费</th>
		        	<th data-options="field:'SHBJ',width:70,align:'right',formatter:float_formatter">本金</th>
		        	<th data-options="field:'SHLX',width:70,align:'right',formatter:float_formatter">利息</th>
		        	<th data-options="field:'SHFJ',width:50,align:'right',formatter:float_formatter">罚金</th>
		        	<th data-options="field:'SHFEE1',width:70,align:'right',formatter:float_formatter">服务费</th>
		        	<th data-options="field:'FJ1',width:50,align:'right',formatter:float_formatter">罚金</th>
		        	<th data-options="field:'SHFEE2',width:70,align:'right',formatter:float_formatter">担保费</th>
		        	<th data-options="field:'FJ2',width:50,align:'right',formatter:float_formatter">罚金</th>
		        	<th data-options="field:'SHFEE3',width:70,align:'right',formatter:float_formatter">风险管理费</th>
		        	<th data-options="field:'FJ3',width:50,align:'right',formatter:float_formatter">罚金</th>
		        </tr>
	        </thead>
	       
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	             日期条件<select name="selectby" style="width:80" id="selectby" class="easyui-combobox" data-options="editable:false">
	        		<option value="yhdate" selected="selected">应还日期</option>
	        		<option value="shdate">实还日期</option>
	        	</select>
	            <input name="startDate" class="easyui-datebox" data-options="editable:false" value="<%=sdf.format(now) %>" id="startDate"/>
	            <input name="endDate" class="easyui-datebox" data-options="editable:false" value="<%=sdf.format(now) %>" id="endDate"/>
	           	还款状态<select name="state" style="width:80" id="state" class="easyui-combobox" data-options="editable:false">
	        		<option value="0" selected="selected">未还款</option>
	        		<option value="1">正常还款</option>
	        		<option value="2">提前还款</option>
	        		<option value="3">逾期还款</option>
	        		<option value="4">担保代偿</option>
	        		<option value="5">全部已还</option>
	        	</select>
	            <input class="combo" name="orgCode" id="queryByOrgCode" placeholder="担保机构"/>
	            <input class="combo" name="fcode" id="fcode" placeholder="项目编号"/>
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <a href="javascript:;" pre-href="/sheets/paymentRecordSheets/paymentRecordAction!financeList?action=1" id="print" class="easyui-linkbutton" iconCls="icon-print">打印列表</a>
	            <a href="/sheets/paymentRecordSheets/paymentRecordAction!financeList?action=2" id="export" class="easyui-linkbutton" iconCls="icon-save">导出EXCEL</a>
	        </div>
	    </div>
  
  		<div id="dialog"></div>
  </body>
</html>


<script>
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		
		$("#search").click(function(){
			var source_url = '/sheets/paymentRecordSheets/paymentRecordAction!financeList';
			var selectby = $("[name='selectby']").val();
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var state = $("[name='state']").val();
			var orgCode = $("[name='orgCode']").val();
			var fcode = $("[name='fcode']").val();
			var url_ = source_url+
		   		"?selectby="+selectby+
		   		"&startDate=" + startDate +
		   		"&endDate=" + endDate +
		   		"&state=" + state +
		   		"&orgCode=" + orgCode +
		   		"&fcode=" + fcode; 
			$("#dg").datagrid({
		   		url: url_
			});
			$("#export").attr("href",url_+"&action=2");
			$("#print").attr("pre-href",url_+"&action=1");
		});
		
		$("#print").click(function(){
			var href = $(this).attr("pre-href");
			window.showModalDialog(href,null,"dialogWidth=1000px;dialogHeight=400px");
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
	
	function showwindow(fid,qs){
		var width = window.screen.width;
		var height = window.screen.height;
		window.open('back/paymentRecord/paymentRecordAction!list_paymentRecord?id='+fid+'&succession='+qs); 
	}
	
	function detail_formatter(val,row,index){
		<c:if test="${menuMap['investrecord_detail']=='inline'}">
			if(row.FINANCBASECODE == null) return null;
			return '<a href="javascript:void(0)" onclick="showwindow(\''+row.FINANCBASEID+'\','+row.QS+')">查看明细</a>';
		</c:if>
		return null;
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
	    }else if(isNaN(val)){
	    	return val;
	    }else{
	        return val.toFixed(2);
	    }
	}
		
		
	
</script>
