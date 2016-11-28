<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script>
$(function(){
	$("#form1").validate({
        rules: { 
          "banklibrary.caption":{ required:true},
          "banklibrary.code":{ required:true}
        },  
        messages: {      
          "banklibrary.caption":{ required:"请输入名称)"},
          "banklibrary.code":{ required:"请输入编码"}
        }    
	});   
})
		
</script>
<form action='/back/banklibrary/bankLibraryAction!edit' id="form1">
<input type="hidden" value="${banklibrary_id}" name='banklibrary_id'/>
	<table>
		<tr><td>名称:</td><td><input type="text" name="banklibrary.caption" value="${banklibrary.caption}" /></td></tr>
		<tr><td>编码:</td><td><input type="text" name="banklibrary.code" value="${banklibrary.code}" /></td></tr>
		<tr><td colspan='2' align='left'><button  class='ui-state-default'>提交</button></td></tr>
	</table>
</form>