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
	 	<table id="dg2" class="easyui-datagrid"
			            data-options="rownumbers:true,
			            singleSelect:true,
			            url:'/back/stcsMemBaseAction!getPaymentRecordJson?uid=<%=username%>',
			            method:'get',
			            toolbar:'#tb2',
			            pagination:true,
			            showFooter: true,
			            sortName:'SHDATE',
			            sortOrder:'desc',
			            pageList:[15,30,50,100],
			            height:437,
			            width:878
			            ">
			            <thead>
			                <tr>
			                	 <th data-options="field:'SHDATE',width:180,sortable:true" >实还日期</th>
			                	 <th data-options="field:'SH',width:190,formatter:float_number_formatter">当日实还金额汇总</th>
			                    <th data-options="field:'YH',width:190,formatter:float_number_formatter">对应债权应还金额汇总</th>
			                    <th data-options="field:'BS',width:60">还款笔数</th>
			                    <th data-options="field:'SHOWOK',width:190,formatter:operator_formatter_repayment_detail">操作</th>
			                </tr>
			            </thead>
			    </table>
			    <div id="tb2" style="padding:5px;height:auto">
			        <div>
			           
			           实还时间区间: <input class="easyui-datebox" name="startDate_repay" style="width:100px" placeholder="开始日期">
			            <input class="easyui-datebox" name="endDate_repay" style="width:100px" placeholder="结束日期">
			            <a href="#" id="search_repayment" class="easyui-linkbutton" iconCls="icon-search" title="">查询</a>
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
		return "是";
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


function yhtotal_formatter(val,row,index){
				if(row.SHDATE== null) return null;
				return (row.SHB+row.SHX+row.FJ).toFixed(2);
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
	event.stopPropagation();
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

 function operator_formatter_repayment_detail(val,row,index){
			    	if(row.SHDATE == '合计') return null;
	                return '<a href="javascript:;" onclick="showdialog(\'pwin\',\'/back/statistics/memberbase_repayment_detail_json.jsp?shdate='+row.SHDATE+'&uid='+<%=username%>+'\',700,400)">查看详细</a>';
	            }

	$(function(){
    	$("#search_repayment").click(function(){
             var source_url = '/back/stcsMemBaseAction!getPaymentRecordJson';
             var startDate = $("[name='startDate_repay']").val();
             var endDate = $("[name='endDate_repay']").val();
             var url_ = source_url+
                 "?startDate=" + startDate + 
                 "&uid=" + <%=username%> + 
                 "&endDate=" + endDate;
             $("#dg2").datagrid({
                 url: url_
             });
         });
         
         $(window).resize(function(){
			$("#dg2").datagrid('resize',{
				width:'100%'
			});
		});
	})
</script>

