<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html>
	<head>
		<title></title>
		<%@ include file="/common/taglib.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/kindeditor/kindeditor-min.js"></script>     
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
 
 	</head> 
<script>  
	function setNotice_targetUserValue(id,username,realname,orgCode){
	    var str=$("#notice_targetUser").val();     
	    var vaStr="";    
	    if (str!="") {      
	         vaStr=","+realname+"("+username+")";  
	    }else{
	         vaStr=realname+"("+username+")" ;  
	    }
	    $("#notice_targetUser").val(str+vaStr); 
     };  
		
		
		function showDias(ele,url,width,height)  
			{
			 
    			$('#'+ele).dialog({
					title:'选择通知目标对象',
				    width:width,
				    height:height,
				    modal:true,
				    href:url,
				    maximizable:true 
				   
				}); 
			} 
			 
$(function(){
 
	
	KE.show({
	        id : 'notice_content',
	        width:'700px',
	        height:'250px',
			resizeMode : 1,
			allowFileManager : true,
			/*图片上传的SERVLET路径*/
			imageUploadJson : "/upload/uploadImage.html", 
			/*图片管理的SERVLET路径*/     
			fileManagerJson : "/upload/uploadImgManager.html",
			/*允许上传的附件类型*/
			accessoryTypes : "gif|jpg|png|bmp",
			/*附件上传的SERVLET路径*/  
			accessoryUploadJson : "/upload/uploadAccessory.html"
	}); 

			
	
});	 
 function tabsResize(width, height){}
 
 
function dialogResize(width, height){
	if($("#member-tabs").length==0){
		return;
	} else {
		var tabs = $("#member-tabs").tabs('tabs');
		$.each(tabs,function(index,obj){
			obj.panel({
				width:'100%'
			}).panel('resize');
		})
	}
}
function okForm(){   
   if ($("#noticeTitle").val()=="") { 
     jQuery.messager.alert('操作提示','请输入通知标题！','info'); 
     $("#noticeTitle").focus();       
	 return false;    
   } 
   
   var noticeEndtimeStr=$("#noticeEndtime").datebox('getValue');   
   if (noticeEndtimeStr=="") {    
     jQuery.messager.alert('操作提示','请选择过期时间！','info');         
     $("#noticeEndtime").focus();       
	 return false;          
   }  
   if ($("#notice_targetUser").val()=="") { 
     jQuery.messager.alert('操作提示','请选择通知对象！','info');    
     $("#notice_targetUser").focus();    
	 return false;       
   }
   
   if ($("#notice_content").val()=="") { 
     jQuery.messager.alert('操作提示','请填写内容！','info');    
     $("#notice_content").focus();    
	 return false;       
   }
   $("#form1").submit();   
}		
	function showdialog(ele,url,width,height,titleStr){
    			$('#'+ele).dialog({
					title:titleStr,  
				    width:width,
				    height:height,
				    modal:true,
				    href:url
				});
				//showModalDialog(url,'','dialogWidth:'+width+'px;dialogHeight:500px;center:yes;help:no;status:no;'); 
	}   
		  
</script>
<body>
<form  action='/sys_/notice/noticeAc!edit'  method="post" id="form1">
<input type='hidden' class='autoheight' value="auto"/> 

<input type="hidden" value="${id}" name='id'/>
	<table id="tabaleHi">
		<tr><td>标题:</td><td><input type="text" name="notice.title"  id="noticeTitle"value="${notice.title}" />(通知内容中不包含该标题)</td></tr>
		<tr><td>过期时间:</td><td><input type="text" class="easyui-datebox" name="notice.endtime"  id="noticeEndtime"   value="${fn:substring(notice.endtime,0,10)}" /></td></tr>
		<tr><td>通知对象:</td><td>  
		<textarea name="notice.targetUser" id="notice_targetUser" style="width:360;overflow-y:visible;" >${notice.targetUser}</textarea>  <a href="javascript:;" class="easyui-linkbutton" iconCls="icon-search"  onclick="showDias('win','/sys_/notice/userTabs.jsp?id=${id}',900,450)" >选择</a> 	    
	    <div id="win"></div> 
		</td></tr>
		<tr><td colspan='2' align='left' style='color:red;'>通知对象格式如下:授权中心测试(533300),多个对象用逗号相隔</td></tr>  
		<tr><td valign='top'>内容:</td><td><textarea name="notice.content" id="notice_content"  >${notice.content}</textarea> </td></tr>
		<tr><td colspan='2' align='left'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="okForm()" class="easyui-linkbutton" iconCls="icon-add">确认提交</a></td></tr>
	</table> 
</form>
</body>
