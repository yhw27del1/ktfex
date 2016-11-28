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
	 	<table id="ye_dg" width="100%" style="height:436px;" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:false,
            method:'post',
            url:'/sys_/notice/noticeAc!member_json?userType=D&first=1',
            toolbar:'#ye_dg_tb',  
            pagination:true,
            showFooter: true,
            height:376,
            sortName:'REGTIME',
            onSelect:selectedRow,       
            onSelectAll:onSelectAllRow, 
            sortOrder:'asc',
            pageList:[15,30,50,100],
            width:878
            ">
	        <thead>
	            <tr>
	                            <th data-options="field:'ck',checkbox:true">选择</th>
			                    <th data-options="field:'USERNAME',width:190">用户名</th>
			                    <th data-options="field:'REALNAME',width:190">姓名</th>
			                    <th data-options="field:'SHOWCODING',width:190">所属机构编码</th> 
			                    <th data-options="field:'NAME_',width:60">所属机构</th>
			                	<th data-options="field:'REGTIME',width:160,sortable:true,formatter:date_formatter" >开户日期</th>
	            </tr>
	        </thead>
	    </table>
	    <div id="ye_dg_tb" style="padding:5px;height:auto">
	        <div>
	            <input type="text" name="queryByOrgCode" placeholder="机构名称或编码" class="combo"  id="queryByOrgCode"/>
                <input id="qkeyWord" type="text" name="qkeyWord" class="combo"   placeholder="用户名或姓名" />  
                <input id="userType" type="hidden" name="userType" value="D" />  
	            <a href="javascript:;" id="search_ye" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	        </div>
	    </div>
	  	<div id="pwin"></div>
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
     function selectedRow(rowIndex, row) {  
           parent.setNotice_targetUserValue(row.ID,row.USERNAME,row.REALNAME,row.SHOWCODING);  
            jQuery.messager.alert('操作提示','加入成功！','info');         
        } 
        
     function onSelectAllRow() { 
		    //var ids = [];
		    var rows = $('#ye_dg').datagrid('getSelections');  
		    for(var i=0; i<rows.length; i++){ 
		      parent.setNotice_targetUserValue(rows[i].ID,rows[i].USERNAME,rows[i].REALNAME,rows[i].SHOWCODING);       
 		    } 
 		    
 		    if(rows.length>0){
 		       jQuery.messager.alert('操作提示','批量加入成功！','info');    
 		    }
 		      
		    //alert(ids.join('\n')); 
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
		$("#search_ye").click(function(){
            var source_url = '/sys_/notice/noticeAc!member_json';
			var userType = $("#userType").val();     
			var queryByOrgCode = $("[name='queryByOrgCode']").val();  
			var qkeyWord = $("[name='qkeyWord']").val();
			var url_ = source_url+
		   		"?userType=" + userType +    
		   		"&queryByOrgCode=" + queryByOrgCode +  
		   		"&qkeyWord=" + qkeyWord;       
               $("#ye_dg").datagrid({
                   url: url_
               });
    	});
    	$(window).resize(function(){
			$("#ye_dg").datagrid('resize',{
				width:'100%'
			});
		});
	})
function operator_formatter(val,row,index){ 
	var str = ''; 
	str += '<input name="selectUser" type="checkbox" value="'+row.ID+'" />';    
	return str;
}	
</script>

