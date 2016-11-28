
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
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
            url:'/sys_/notice/noticeAc!list_data',   
            singleSelect:true,
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
	        <thead data-options="frozen:true">
	        	<tr> 
		        	<th data-options="field:'TITLE',width:80,sortable:true,formatter:title_formatter">标题</th>
		        	<th data-options="field:'CONTENT',width:80,formatter:content_formatter">内容</th>
		            <th data-options="field:'ADDTIME',width:80,formatter:date_formatter">添加时间</th>
		            <th data-options="field:'ENDTIME',width:80,formatter:date_formatter">过期时间</th>
		            <th data-options="field:'TARGETUSER',width:550,sortable:true,formatter:targetuser_formatter">目标用户</th>  
		            <th data-options="field:'STATE',width:80,formatter:state_formatter">审核状态</th>
		            <th data-options="field:'SHOWOK',width:120,formatter:operator_formatter">操作</th>
		        </tr> 
	        </thead>
	         
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	        	增加时间: <input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" >
	            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期" >
 	           <!-- <input type="text" name="queryByOrgCode" placeholder="机构编码" class="combo"  id="queryByOrgCode"/> --> 
	            <input id="qkeyWord" type="text" name="qkeyWord" class="combo" title="用户名"  placeholder="关键字" />
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp; &nbsp;
	            <a href="#" onclick="toURL('/sys_/notice/noticeAc!ui');return false;"  class="easyui-linkbutton" iconCls="icon-add">新增</a>
	        </div>
	    </div>
	    <div id="win"></div>
		<%@ include file="/common/messageTip.jsp" %>
	</body> 
</html>



<script>
function del_html_tags(str)
{
    var words = '';
    words = str.replace(/<[^>]+>/g,"");
    return words;
}
function content_formatter(val,row,index){
    var val=del_html_tags(val);
	return '<a title="'+val+'">'+val+'</a>';
}

function targetuser_formatter(val,row,index){
	return '<a title="'+val+'">'+val+'</a>';
}
function title_formatter(val,row,index){
	return '<a title="'+val+'">'+val+'</a>';
}

function toURL(url){ 
   window.location.href = url; 
}

	function date_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return new Date(val.time).format('yyyy-MM-dd');
		}
	}
	
	function confirmInfo(msg,url){   
	    jQuery.messager.confirm('确认提示',msg,function(event){        
			if(event){   
			    window.location.href=url;     
			}  
		});   
     } 
     

 
	function operator_formatter(val,row,index){ 
		   var str = '';  
		   if(row.STATE == '0'){  
			  str += '<a href="javascript:;" onclick="toURL(\'/sys_/notice/noticeAc!ui?id='+row.ID+'\')">修改</a>&nbsp;&nbsp;&nbsp;';          
			  str += '<a href="javascript:;" onclick="javascript:confirmInfo(\'您确定要申请审核吗?\',\'/sys_/notice/noticeAc!state_normal?id='+row.ID+'&notice.audit_state=1\');return false;" >申请审核</a>'; 
		   }
		   else if (row.STATE == '1' ){
			  str += '等待审核结果';
		   }
		   else if (row.STATE == '2'){
			  str += '无可用操作';
		   }
		   else if (row.STATE == '3' ){
			  str += '<a href="javascript:;" onclick="toURL(\'/sys_/notice/noticeAc!ui?id='+row.ID+'\')">修改</a>&nbsp;&nbsp;&nbsp;';         
			  str += '<a href="javascript:;" onclick="javascript:confirmInfo(\'您确定要重新申请审核吗?\',\'/sys_/notice/noticeAc!state_normal?id='+row.ID+'&notice.audit_state=1\');return false;" >重新申请审核</a>'; 
		   }   
		   else{
			  str += '未处理';   
		   }
           return str;   
		}
		
	function state_formatter(val,row,index){    
		   var str = '';
		   if(row.STATE == '0' )
			  str += '预备审核';
		   else if (row.STATE == '1' )
			  str += '待审核';
		   else if (row.STATE == '2' )
			  str += '审核通过';
		   else if (row.STATE == '3' )
			  str += '驳回';
		   else
			  str += '未处理';   
           return str;  
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
		$("#search").click(function(){
			var source_url = '/sys_/notice/noticeAc!list_data';  
			var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val(); 
			var qkeyWord = $("[name='qkeyWord']").val();
			var url_ = source_url+
		   		"?startDate=" + startDate + 
		   		"&endDate=" + endDate + 
		   		"&qkeyWord=" + qkeyWord;
			    $("#dg").datagrid({
		   		url: url_
			});
		});
		
		
		$("#addNotice").click(function(){
			 
		});
		
	 
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>