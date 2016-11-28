<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script>
$(function(){
	
	$("#form2").validate({
        rules:{ 
          "transaction.name":{required:true},
          "transaction.time":{required:true},
          "transaction.entity":{required:true}
        },  
        messages: {      
          "transaction.name":{ required:"请输入事务名称"},
          "transaction.time":{ required:"请输入正确的日期"},
          "transaction.entity":{ required:"请输入要执行的对象完整路径"}
        }
	});   
	
});
function form_submit(){
	$("#form2").submit();
	return false;
}
		
</script>
<body><input type='hidden' class='autoheight' value="auto" /> 
<form action='/sys_/transaction/transactionAction!edit' id="form2">
<input type="hidden" value="${transaction_id}" name='transaction_id'/>
	<table style="font-size:14px;" width="300">
		<tr><td>事务名称:</td><td><input type='text' name="transaction.name" value="${transaction.name }"/></td></tr>
		<tr><td>事务描述:</td><td><textarea name="transaction.description">${transaction.description}</textarea></td></tr>
		<tr><td>执行时间:</td><td><input type='text' name='transaction.time' value="${transaction.time}"/></td></tr>
		<tr><td colspan="2" style="font-size:12px;">5组数字组成(组成字符有[数字|*])，以半角空格间隔，分别表示 年 月 日 小时 分，不指定应该该位设为*，如"* * * 12 0"表示在每天的12点整执行，"2012 * 1 0 0"表示在2012年每个月的1号0时0分执行</td></tr>
		<tr><td>执行对象:</td><td><input type='text' name='transaction.entity' value="${transaction.entity}"/></td></tr>
		<tr><td colspan="2" style="font-size:12px;">执行对象，填写完整包及类名，该类须实现com.wisdoor.core.trigger.TransactionBase接口，覆写excute方法，引入service等操作请放到无参构造函数中。</td></tr>
		<tr><td colspan='2' align='left'><button  class='ui-state-default' onclick="return form_submit()">提交</button></td></tr>
	</table>
</form>
</body>