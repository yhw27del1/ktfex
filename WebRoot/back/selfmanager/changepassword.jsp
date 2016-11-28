<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script>
$(function(){
	$("#form1").validate({
        rules: { 
          "password_current":{ required:true},
          "password_target":{ required:true},
          "password_confirm":{ required:true}
        },  
        messages: {      
          "password_current":{ required:"请输入当前密码"},
          "password_target":{ required:"请输入新密码"},
          "password_confirm":{ required:"请输入确认密码"}
        }    
	});  
	$("#submitForm").click(function(){ 
		$('#form1').submit();
	});

	if($("#info").text().indexOf("密码修改成功，新密码将在下次登录时生效")!=-1){
		alert("密码修改成功，请重新登录");
		window.location.href="/";
	}
})
		
</script> 
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action='/back/selfmanager/changePasswordAction!save' id="form1">
	<table>
		<tr><td colspan="2" id="info"><s:actionerror cssStyle="font-size:13px;color:#B82525"/><s:actionmessage cssStyle="font-size:16px;;color:#DC1818"/></td></tr>
		<tr><td>当前密码:</td><td><input type='password' name='password_current'/></td></tr>
		<tr><td>新密码:</td><td><input type='password' name='password_target'/></td></tr>
		<tr><td>确认密码:</td><td><input type='password' name='password_confirm'/></td></tr>
		<tr><td colspan='2' align='left'><button  class='ui-state-default' id="submitForm">提交</button></td></tr>
	</table>
</form>
</body>