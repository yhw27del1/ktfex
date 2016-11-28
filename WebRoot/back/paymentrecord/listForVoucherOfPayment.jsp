<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%
	Date now = new Date();
%>
<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd" var="startDate"/>
<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd" var="endDate"/>
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
				.combo{background-color: #fff;}
			-->
		</style>
 	</head>
	<body>
		<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            url:'/back/paymentRecordAction!listForVoucherOfPayment?startDate=${startDate }&endDate=${endDate }',
            singleSelect:false,
            method:'post',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
	        <thead>
	            <tr>
	            	<th data-options="field:'ck',checkbox:true"></th>
		        	<th data-options="field:'FINANCBASECODE',width:80">项目编号</th>
		            <th data-options="field:'FSHORTNAME',width:100,formatter:financingbase_name_formatter">项目简称</th>
		            <th data-options="field:'FREALNAME',width:80,formatter:financer_name_formatter">融资方</th>
		            <th data-options="field:'FORGNAME',width:90,formatter:creater_formatter">发包方</th>
	                <th data-options="field:'CURRENYAMOUNT',width:80,formatter:maxamount_formatter">融资额</th>
		            <th data-options="field:'STATE',width:80,formatter:state_formatter">还款状态</th>
	                <th data-options="field:'SHDATE',width:80,formatter:date_formatter">实还日期</th>
	                <th data-options="field:'QS',width:60,formatter:qs_formatter,align:'center'">期数<br/>[总/当前]</th>
	                <th data-options="field:'GROUP_',width:40,align:'center'">组别</th>
	                <th data-options="field:'TIMES',width:40,align:'center'">人次</th>
	                <th data-options="field:'SHJE',width:80,align:'center',formatter:maxamount_formatter">实还金额</th>
	                <th data-options="field:'REMARK',width:100,align:'center',formatter:remark_formatter">备注</th>
	                <th data-options="field:'showok',width:80,formatter:operator_formatter">操作</th>
	            </tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        	实还时间区间: <input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" value="${startDate }">
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期" value="${endDate }">
	        	<input class="easyui-combogrid" style="width:120px" id="state" name="state" data-options="
			            panelWidth: 120,
			            multiple: true,
			            idField: 'key',
			            textField: 'value',
			            columns: [[
			                {field:'ck',checkbox:true},
			                {field:'value',title:'状态',width:120}
			            ]],
			            fitColumns: true
			        "/>
	            <input id="qkeyWord" type="text" name="qkeyWord" class="combo" title="融资方名称,融资包名称" style="width: 100px;height:20px;line-height:20px;padding:10px 2px;" placeholder="融资名称" />
	            <input type="text" name="fbcode" title="多个值用半角逗号(,)隔开" placeholder="融资包编号" class="combo" style="height:20px;line-height:20px;padding:10px 2px;width:90px;" id="fbcode"/>
	            <input type="text" name="orgcode" placeholder="担保机构编码" class="combo" style="height:20px;line-height:20px;padding:10px 2px;width:90px;" id="orgcode"/>
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <c:if test="${menuMap['printForVoucherOfPayment']=='inline'}"><a href="#" id="batch-print" class="easyui-linkbutton" iconCls="icon-print">打印凭证</a></c:if>
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
	
	function maxamount_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return val.toFixed(2);
		}
	}
	function state_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			switch(val){
				case 1:
					return "正常还款";
					break;
				case 2:
					return "提前还款";
					break;
				case 3:
					return "逾期还款";
					break;
				case 4:
					return "担保代偿";
			}
		}
	}
	function operator_formatter(val,row,index){
		var f_id = row.FINANCBASEID;
		var qs = row.QS;
		var group_ = row.GROUP_;
		var data = escape('{rows:[{f_id:"' + f_id + '",qs:' + qs + ',group_:' + group_ + '}]}');
		var str = '<a href="javascript:;" onclick="showdialog(\'/back/paymentRecordAction!printForVoucherOfPayment?data='+data+'\',800)">打印</a>';
		return '<a href="javascript:;" onclick="showdialog(\'/back/paymentRecord/paymentRecordAction!list_paymentRecord?id='+f_id+'&succession='+qs+'\',1000)">还款详情</a>';
	}
	
	function qs_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return row.RETURNTIMES+"/"+val;
		}
	}
	
	function remark_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return '<span title="'+val+'">'+val+'</span>';
		}
	}
	
	function creater_formatter(val,row,index){
		return val+"/"+row.FORGNO;
	}
	
	function financingbase_name_formatter(val,row,index){
		if(val==null) return null;
		return '<a href="javascript:;" title="'+val+'" onclick="showdialog(\'/back/financingBaseAction!detail?id='+row.FINANCBASEID+'\',1000)">' + val + '</a>';
	}
	function financer_name_formatter(val,row,index){
		if(val==null) return null;
		return '<a href="javascript:;" onclick="showdialog(\'/back/member/memberBaseAction!memberDetails?id='+row.FINANCIERID+'\',700)">' + name(val) + '</a>';
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
	
	
	
	function showdialog(url,width){
		showModalDialog(url,'','dialogWidth:'+width+'px;dialogHeight:500px;center:yes;help:no;status:no;'); 
		event.stopPropagation();
	}
	
	
	
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$("#search").click(function(){
			var source_url = '/back/paymentRecordAction!listForVoucherOfPayment';
			var states = $('#state').combogrid('getValues');
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var orgcode = $("[name='orgcode']").val();
			var qkeyWord = $("[name='qkeyWord']").val();
			var fbcode = $("[name='fbcode']").val();
			
			var url_ = source_url+
		   		"?states=" + states +
		   		"&startDate=" + startDate + 
		   		"&endDate=" + endDate +
		   		"&org_code=" + orgcode +
		   		"&qkeyWord=" + qkeyWord +
		   		"&fbcode=" + fbcode;
		   	//alert(url_);
		   	
			$("#dg").datagrid({
		   		url: url_
			});
			
		});
		
		
		$("#batch-print").click(function(){
			var selectrows = $("#dg").datagrid('getSelections');
			if(selectrows.length == 0){
				$.messager.alert('操作错误','至少要选一项!','error');
				return;
			}
			var data = '{rows:[';
			for(var x = 0; x < selectrows.length; x++){
				if( x != 0 ) data += ',';
				var f_id = selectrows[x].FINANCBASEID;
				var qs = selectrows[x].QS;
				var group_ = selectrows[x].GROUP_;
				data += '{f_id:"' + f_id + '",qs:' + qs + ',group_:' + group_ + '}';
			}
			data += ']}';
			data = escape(data);
			showdialog('/back/paymentRecordAction!printForVoucherOfPayment?data='+data,800);
		});
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
		$('#state').combogrid('grid').datagrid({
			data: [
				{key:1, value:'正常还款',ck:'true'},
				{key:2, value:'提前还款'},
				{key:3, value:'逾期还款'},
				{key:4, value:'担保代偿'}
			]
		});
	});
		
</script>