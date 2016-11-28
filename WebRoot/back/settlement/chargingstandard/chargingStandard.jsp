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
          "costitem.money":{ required:true,number:true},
          "costitem.percent":{ required:true,range:[0,100]}
        },  
        messages: {      
          "costitem.money":{ required:"请输入费用",number: "请输入一个合法的数字"},
          "costitem.percent":{ required:"请输入百分比",range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值")}
        }    
	}); 
	 
})
		
</script>
<form action='/back/chargingStandardAction!save' id="form1">
<input type="hidden" value="${costitem_id}" name='costitem_id'/>
	<table>
		<tr><td>收费项目:</td><td><s:select list="costbases" name="costitem.costBase.id" listKey="id" listValue="name" value="costitem.costBase.id"></s:select></td></tr>
		<tr><td>会员类型:</td><td><s:select list="membertypes" name="costitem.memberTypel.id" listKey="id" listValue="name" value="costitem.memberTypel.id"></s:select></td></tr>
		<tr><td>费用:</td><td><input type='text' name='costitem.money' value="${costitem.money}"/></td></tr>
		<tr><td>百分比:</td><td><input type='text' name='costitem.percent' value="${costitem.percent}"/></td></tr>
		<tr><td colspan='2' align='left'><button  class='ui-state-default' id="submit_button">提交</button></td></tr>
	</table>
</form>