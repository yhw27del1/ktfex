<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
			body{padding:0;margin:0;}
		</style>
	</head>
	<body>
		
	<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            url:'/back/financingBaseAction!getFinishedFinancingBaseList',
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
        <thead data-options="frozen:true">
        	<tr>
	        	<th data-options="field:'FINANCECODE',width:80" rowspan="2">项目编号</th>
	            <th data-options="field:'FINANCENAME',width:100" rowspan="2">项目简称</th>
	            <th data-options="field:'FINANCERREALNAME',width:80,formatter:name_formatter" rowspan="2">融资方</th>
	            <th data-options="field:'CREATEORG_SHORT',width:90,formatter:creater_formatter" rowspan="2">担保方</th>
	        </tr>
        </thead>
        <thead>
            <tr>
              	
                <th colspan="3">融资额</th>
                <th colspan="2">融资类型</th>
                <th data-options="field:'HAVEINVESTNUM',align:'right',width:60" rowspan="2">投标人数</th>
                <th data-options="field:'ENDDATE',width:80,formatter:date_formatter" rowspan="2">投标截止</th>
                <th data-options="field:'STATE_ZH',width:60,formatter:state_formatter" rowspan="2">状态</th>
                <th data-options="field:'showok',width:80,formatter:operator_formatter" rowspan="2">操作</th>
            </tr>
            <tr>
            	<th data-options="field:'MAXAMOUNT',align:'right',width:80,formatter:float_formatter">总融资额</th>
            	<th data-options="field:'CURCANINVEST',align:'right',width:80,formatter:float_formatter">可融资额</th>
            	<th data-options="field:'CURRENYAMOUNT',align:'right',width:80,formatter:float_formatter">已融资额</th>
            	<th data-options="field:'TERM',width:50,formatter:term_formatter">期限</th>
            	<th data-options="field:'RETURNPATTERN',width:80">还款方式</th>
            </tr>
        </thead>
    </table>
    <div id="tb" style="padding:5px;height:auto">
        <div>
            
              挂单时间区间: <input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期">
            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期">
            <select class="easyui-combobox" name="states" id="states">
        		<option value="">项目状态</option>
        		<option value="2">投标中</option>
        		<option value="4">已满标</option>
        	</select>
            <input type="text" name="queryByOrgCode" placeholder="担保机构编码" class="combo"  id="queryByOrgCode"/>
            <input id="qkeyWord" type="text" name="qkeyWord" class="combo" title="项目编号、融资方名称,融资方用户名" placeholder="关键字" />
            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    	<div id="win">
    		 Dialog Content.
    	</div>
				
		<%@ include file="/common/messageTip.jsp"%>
	</body>
</html>
<script>
	function term_formatter(val,row){
		if(val == null) return null;
		if(row.INTERESTDAY != 0){
			return row.INTERESTDAY+"天";
		}else{
			return val+"个月";
		}
	}
	function date_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return new Date(val.time).format('yyyy-MM-dd');
		}
	}
	
	function operator_formatter(val,row,index){
		if(val==null) return null;
		if(val=="合计") return val;
		var str = '<a href="javascript:;" onclick="showLogs(\'/back/financingBaseAction!logs?_id=' + row.ID + '\',700)">日志</a>';
		str += '&nbsp;<a href="javascript:;" onclick="dialog(\'/back/financingBaseAction!hesuanUI?id=' + row.ID + '\',1000)">确认</a>';
		return str;
	}
	
	function state_formatter(val,row,index){
		if(val=="投标中"){
			return "<span style='color:#D72323'>投标中</span>";
		}else if(val=="已满标"){
			return "<span style='color:#43C71F'>已满标</span>";
		}
	}
	function creater_formatter(val,row,index){
		if(val=="合计") return "合计";
		return val+"/"+row.CREATEORG;
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
	function showLogs(url_){ 
		$('#win').dialog({
			title:'日志',
		    width:700,
		    height:500,
		    modal:true,
		    href:url_
		});
	        
	}
	
	
	function toURL(url){ 
	   window.location.href = url;   
	}
	
	function dialog(url,width){
		showModalDialog(url,'','dialogWidth:'+width+'px;dialogHeight:500px;center:yes;help:no;status:no'); 
	}
	function float_formatter(val,row,index){
		    if(val==null){
		        return null;
		    }else{
		        return val.toFixed(2);
		    }
		}
	
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$("#search").click(function(){
			var source_url = '/back/financingBaseAction!getFinishedFinancingBaseList';
			var containstr = $("[name='containstr']").val();
			var queryCode = $("[name='queryCode']").val();
			var states = $("[name='states']").val();
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var queryByOrgCode = $("[name='queryByOrgCode']").val();
			var qkeyWord = $("[name='qkeyWord']").val();
			var url_ = source_url+
		   		"?containstr="+containstr+
		   		"&queryCode=" + queryCode +
		   		"&states=" + states +
		   		"&startDate=" + startDate + 
		   		"&endDate=" + endDate +
		   		"&queryByOrgCode=" + queryByOrgCode +
		   		"&qkeyWord=" + qkeyWord;
			$("#dg").datagrid({
		   		url: url_
			});
		});
	});
		
</script>