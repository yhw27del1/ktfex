<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script>
$(function(){
	
	$("#groupRestrainId").val('${group.groupRestrainId}');  
	
	$("#form2").validate({
        rules:{ 
          "group.name":{required:true} 
        },  
        messages: {      
          "group.name":{ required:"请输入组名称"} 
        }
	});   
	
});
function form_submit(){
	$("#form2").submit();
	return false;
}  
		
</script>
<body><input type='hidden' class='autoheight' value="auto" /> 
<form action='/back/userGroupAction!edit' id="form2">
<input type="hidden" value="${id}" name='id'/>
	<table style="font-size:14px;" >
		<tr><td>组名称:</td><td><input type='text' name="group.name" value="${group.name}" style='width:300px;'/></td></tr>
		<tr><td>约束规则:</td><td>
		<select name="group.groupRestrainId" id='groupRestrainId'>
		    <option value=''>无约束</option>
		    <c:forEach items="${restrainList}" var="entry">
		        <option value='${entry.entityId}'> ${entry.name} </option>
		    </c:forEach>
		</select>
		
		    
		
		
		 
		 </td></tr>
		<tr><td>备注:</td><td><textarea name="group.note" style='width:400px;'>${group.note}</textarea></td></tr> 
		<tr><td colspan='2' align='left'><button  class='ui-state-default' onclick="return form_submit()">提交</button></td></tr>
	</table>
</form>
</body>