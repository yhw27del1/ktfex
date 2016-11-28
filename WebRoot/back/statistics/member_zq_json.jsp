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
	 	<table id="contract_dg" width="100%" style="height:436px;" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            method:'post',
            url:'/back/stcsMemBaseAction!m_b_detail?username=<%=username %>',
            pagination:true,
            toolbar:'#contract_dg_tb',
            showFooter: true,
            pageList:[15,30,50,100]
            ">
	        <thead>
	            <tr>
		        	<th data-options="field:'FINANCBASECODE',width:80">项目编号</th>
		        	<th data-options="field:'FSHORTNAME',width:180">项目简称</th>
		        	<th data-options="field:'CREATEDATE',width:80,formatter:date_formatter">投标日期</th>
		        	<th data-options="field:'FINANCIER_MAKE_SURE_DATE',width:80,formatter:date_formatter">签约日期</th>
		        	<th data-options="field:'INVESTAMOUNT',width:80,formatter:float_number_formatter">投标金额</th>
		        	<th data-options="field:'BUSINESSTYPE',width:60,formatter:businesstype_formatter">融资期限</th>
		        	<th data-options="field:'RETURNPATTERN',width:160">还款方式</th>
		        	<th data-options="field:'TERMINAL',width:60,formatter:termial_formatter">结清状态</th>
		        	<th data-options="field:'operator',width:160,formatter:sub_datagrid_operator_formatter">操作</th>
	            </tr>
	        </thead>
	    </table>
	    <div id="contract_dg_tb" style="padding:5px;height:auto">
	        <div>
	        	<input placeholder="项目编号" style="width:100px" class="combo" name="fcode_contract" id="fcode_contract" />
	           签约时间区间: <input class="easyui-datebox" name="startDate_contract" style="width:100px" placeholder="开始日期">
	            <input class="easyui-datebox" name="endDate_contract" style="width:100px" placeholder="结束日期">
	            结清状态:<select class="easyui-combobox" data-options="editable:false" name="state_contract" id="state">
	        		<option value="">不限</option>
	        		<option value="0">未结清</option>
	        		<option value="1">已结清</option>
	        	</select>
	            <a href="#" id="search_contract" class="easyui-linkbutton" iconCls="icon-search" title="">查询</a>
	            <span style="color:red">*只显示生效债权</span>
	        </div>
	    </div>
	  	<div id="pwin"></div>
	</body>
</html>
<script>
function termial_formatter(val,row,index){
	if(val== null){
		return null;
	}if(val == 0){
		return "否";
	}else{
		return "<span style='color:#C31C1C'>是</span>";
	}
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
function sort_date_formatter(val,row,index){
	if(val==null){
		return null;
	}else{
		return new Date(val.time).format('yyyy-MM');
	}
}


/**
    		还款状态翻译
    		*/
    		function p_state_formatter(val,row,index){
    			switch(val){
    				case 0:
    				return "未还款";break;
    				case 1:
    				return "正常还款";break;
    				case 2:
    				return "提前还款";break;
    				case 3:
    				return "逾期还款";break;
    				case 4:
    				return "担保代偿";
    			}
    		}

function sub_datagrid_operator_formatter(val,row,index){
	if(row.FINANCIER_MAKE_SURE_DATE == '合计') return null;
	return '<a href="javascript:;" onclick="showdialog(\'pwin\',\'/back/statistics/m_p_statistics.jsp?investrecord_id='+row.ID+'\',700,420)">查看详细</a>';
}
		/**
 		借款期限格式化
 		*/
function businesstype_formatter(val,row,index){
	if(val==null){
		return null;
	}else{     
		if(row.INTERESTDAY != 0){
			return row.INTERESTDAY+"天";
		}else{
			return val+"个月";
		}
		
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
	event.cancelBubble=true;
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

	$(function(){
		$("#search_contract").click(function(){
    		var source_url = '/back/stcsMemBaseAction!m_b_detail?username=<%=username %>';
               var fcode = $("[name='fcode_contract']").val();
               var startDate = $("[name='startDate_contract']").val();
               var endDate = $("[name='endDate_contract']").val();
               var state = $("[name='state_contract']").val();
               var url_ = source_url+
                   "&startDate=" + startDate + 
                   "&endDate=" + endDate +
                   "&state=" + state +
                   "&fcode=" + fcode ;
               $("#contract_dg").datagrid({
                   url: url_
               });
    	});
    	$(window).resize(function(){
			$("#contract_dg").datagrid('resize',{
				width:'100%'
			});
		});
	})
</script>

