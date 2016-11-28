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
			body{padding:0;margin:0;}
			.combo{background:#fff;height:22px;width:80px;}
		</style>
	</head>
	<body>

		<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            url:'/back/financingCostAction!list?startDate=<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>',
            singleSelect:true,
            method:'post',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
            
            <thead data-options="frozen:true">
	        	<tr>
		        	<th data-options="field:'FB_CODE',width:80" rowspan="2">项目编号</th>
		        	<th data-options="field:'CBO_CODING',width:60" rowspan="2">担保方</th>
		        	<th data-options="field:'FM_NAME',width:80,formatter:name_formatter" rowspan="2">融资方</th>
		        	<th data-options="field:'FB_CURRENYAMOUNT',width:80,align:'right',formatter:FB_CURRENYAMOUNT_FORMATTER" rowspan="2">融资额(￥)</th>
		        	<th data-options="field:'FBT_TERM',width:40,align:'right',formatter:FBT_TERM_FORMATTER" rowspan="2">期限</th>
		        	<th data-options="field:'FB_GUARANTEE',width:60,formatter:FB_GUARANTEE_FORMATTER" rowspan="2">有无担保</th>
		        </tr>
	        </thead>
	        <thead>
	        	<tr>
		        	<th colspan="7">费用</th>
		        	<th data-options="field:'FC_NOTE',width:80" rowspan="2">备注</th>
		        	<th data-options="field:'FC_REALAMOUNT',width:80,align:'right',halign:'center',formatter:FC_REALAMOUNT_FORMATTER" rowspan="2">融资结余</th>
		        	<th data-options="field:'FC_CREATEDATE',width:80,formatter:date_formatter" rowspan="2">创建日期</th>
		        	<th data-options="field:'OPERATE',width:100,align:'right',halign:'center',formatter:OPERATE_FORMATTER" rowspan="2">可用操作</th>
		        </tr>
		        <tr>
		        	<th data-options="field:'FC_FXGLF',width:140,align:'right',halign:'center',formatter:array_formatter">风险管理费</th>
		        	<th data-options="field:'FC_RZFWF',width:140,align:'right',halign:'center',formatter:array_formatter">融资服务费</th>
		        	<th data-options="field:'FC_DBF',width:140,align:'right',halign:'center',formatter:array_formatter">担保费</th>
		        	<th data-options="field:'FC_BZJ',width:90,align:'right',halign:'center',formatter:array_formatter">还款保证金</th>
		        	<th data-options="field:'FC_XWF',width:60,align:'right',halign:'center',formatter:float_foramtter">席位费</th>
		        	<!--  <th data-options="field:'FC_XYGLF',width:80,align:'right',halign:'center',formatter:float_foramtter">信用管理费</th>
		        	<th data-options="field:'FC_OTHER',width:80,align:'right',halign:'center',formatter:array_formatter">其他费用</th>-->
		        </tr>
	        </thead>
	       
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        	确认时间区间: <input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期" value="<fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd"/>">
	            <input type="text" id="keyWord" name="keyWord" class="combo" title="项目编号、担保方"  placeholder="项目信息" />
	            <input type="text" id="queryEname" name="queryEname" class="combo" title="融资方名称、交易帐号" placeholder="融资方信息" />
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <a href="#" id="print" class="easyui-linkbutton" iconCls="icon-print">列表打印</a>
	        </div>
	    </div>
	   

	</body> 
</html>

<script>


	function OPERATE_FORMATTER(val,row,index){
		if(row.FC_ID == null) return ;
		var result = '';
		
		<c:if test="${menuMap['financingcost_operator']=='inline'}">
		if(row.FC_STATE == 0 ){
			result += '<a href="javascript:;" onclick="toURL(\'/back/financingCostAction!feiyongqueren?id='+row.FC_ID+'&action=pass\',\''+row.FB_CODE+'\',true,this);return false;">确认</a>';
			result += '<a href="javascript:;" onclick="toURL(\'/back/financingCostAction!feiyongqueren?id='+row.FC_ID+'&action=ignore\',\''+row.FB_CODE+'\',false,this);return false;" style="margin-left:5px;">驳回</a>';
		}else{
			result += '<a href="javascript:;" style="color:red">已确认</a>'
		}
			
		</c:if>
		result += '<a href="/back/financingCostAction!cost_print?id='+row.FC_ID+'" target="_blank" style="margin-left:5px;">打印</a>';
		return result;
		
	}
	
	function toURL(url,code,action,obj){
		$.messager.confirm('费用确认与驳回', (action?"确认":"驳回")+'融资项目['+code+']费用项', function(r){
            if (r){
            	$.messager.progress({text:'拼命加载中...',interval:700}); 
                $.post(url,{},function(data,state){
                	$.messager.progress('close');
	   				$.messager.alert('操作结果',data.info+'!','info');
	   			},'json');
            }
        });
	}

	function FBT_TERM_FORMATTER(val,row, index){
		if(val == null) return null;
		if(row.FB_INTERESTDAY != 0){
			return row.FB_INTERESTDAY+"天";
		}
		return val + "月";
	}

	function array_formatter(val,row,index){
		if(val == null) return;
		if(val.toString().indexOf('|') == -1) return val;
		var arr = val.split("|");
		var value = float_foramtter(parseFloat(arr[0]));
		var fs_bl = "<div style='float:left;'>" + "[" + parseFloat(arr[2]) + "%]" + (parseInt(arr[1]) == 0 ?"":"[按月]") +"</div>";
		var result = value == 0 ? value : fs_bl+value ;
		return result;
	}

	function date_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return new Date(val.time).format('yyyy-MM-dd');
		}
	}

	function FB_CURRENYAMOUNT_FORMATTER(val,row, index){
	
		if(!isNaN(val)) return float_foramtter(val);
	}
	function FC_REALAMOUNT_FORMATTER(val,row, index){
	
		if(!isNaN(val)) return float_foramtter(val);
	}
	
	

	function float_foramtter(val){
		if(val == null || isNaN(val)) return;
		return val.toFixed(2);
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

	function FB_GUARANTEE_FORMATTER(val,row,index){
		if(row.FC_ID == null) return null;
		return (val == null)?"无担保":"有担保";
	}
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
		
		$("#search").click(function(){
			var source_url = '/back/financingCostAction!list';
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var keyWord = $("[name='keyWord']").val();
			var queryEname = $("[name='queryEname']").val();
			var url_ = source_url+
		   		"?startDate=" + startDate +
		   		"&endDate=" + endDate + 
		   		"&keyWord=" + keyWord +
		   		"&queryEname=" + queryEname;
			$("#dg").datagrid({
		   		url: url_
			});
		});
		
		$("#print").click(function(){
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var keyWord = $("[name='keyWord']").val();
			var queryEname = $("[name='queryEname']").val();
            window.open("/back/financingCostAction!list?keyWord="+keyWord+"&queryEname="+queryEname+"&startDate="+startDate+"&endDate="+endDate+"&userType=print","print");           
     	}); 
	});
</script>