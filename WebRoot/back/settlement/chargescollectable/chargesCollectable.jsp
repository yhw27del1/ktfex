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
          "costbase.name":{ required:true},
          "costbase.code":{ required:true}
        },  
        messages: {      
          "costbase.name":{ required:"请输入项目名称"},
          "costbase.code":{ required:"请输入项目编码"}
        }    
	}); 
	var tariff = '${costbase.tariff}';
	$("option[value='"+tariff+"']",$("#tariff")).attr("selected",true);
})
		
</script>
<form action='/back/costCategoryAction!save' id="form1">
<input type="hidden" value="${costbase_id}" name='costbase_id'/>
	<table>
		<tr><td>名称:</td><td><input type='text' name='costbase.name' value="${costbase.name }"/></td></tr>
		<tr><td>编码:</td><td><input type='text' name='costbase.code' value="${costbase.code }"/></td></tr>
		<tr><td>收费方式:</td><td><select id="tariff" name='costbase.tariff'><option value='0'>一次收费</option><option value='1'>按月收费</option></select></td></tr>
		<tr><td>备注:</td><td><input type='text' size="40" name='costbase.memo' value="${costbase.memo }"/></td></tr>
		<tr><td colspan='2' align='right'><button  class='ui-state-default' id="submit_button">提交</button></td></tr>
	</table>
</form>