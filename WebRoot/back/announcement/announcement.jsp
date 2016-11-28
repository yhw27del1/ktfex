<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/kindeditor/kindeditor-min.js"></script>

<script>

$(function(){
	var global_height = $(document).height();
	//alert(global_height);
	$(".autoheight").val(global_height+250);
	 //项目详细信息
	KE.show({
	        id : 'announcement_content',
	        width:'700px',
	        height:'300px',
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
	//时间控件
    $("#announcement_endtime").datepicker({
        showOn: 'button',
        buttonImageOnly: false,
        //changeMonth: true,
        //changeYear: true,
        numberOfMonths: 2,
        dateFormat: "yy-mm-dd",
        minDate: 0 
        //maxDate: "+1M"
    });
    $("#ui-datepicker-div").css({'display':'none'});
   	$('#submit_button').click(function(){
		$('#form1').submit();
    });
})	
		
</script>
<body>
<form action='/back/announcement/announcementAction!edit' method="post" id="form1">
<input type='hidden' class='autoheight' value="auto"/> 

<input type="hidden" value="${announcement_id}" name='announcement_id'/>
	<table>
		<tr><td>公告标题:</td><td><input type="text" name="announcement.title" value="${announcement.title}" />(公告内容中不包含该标题)</td></tr>
		<tr><td>过期时间:</td><td><input type="text" id="announcement_endtime" name="announcement.endtime" value="${fn:substring(announcement.endtime,0,10)}" /></td></tr>
		<tr><td>公告对象:</td><td><s:select list="membertypes" name="announcement.target" headerKey="A" headerValue="全部" listKey="code" listValue="name" value="announcement.target" cssStyle="width:157px;padding:3px;margin-left:2px;"></s:select></td></tr>
		<tr><td valign='top'>公告内容:</td><td><textarea name="announcement.content" id="announcement_content" >${announcement.content}</textarea> </td></tr>
		<tr><td colspan='2' align='left'><button  class='ui-state-default' id="submit_button">提交</button></td></tr>
	</table>
</form>
</body>
