<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script>
$(function(){
	$("#form_").validate({
        rules: { 
          "investcondition.lowestMoney":{ required:true,number:true},
          "investcondition.highPercent":{ required:true,range:[0,100]}
        },  
        messages: {      
          "investcondition.lowestMoney":{ required:"单笔最低投标额",number: "请输入一个合法的数字"},
          "investcondition.highPercent":{ required:"最高投标额百分比",range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值")}
        }    
	});   
})
		
</script> 
<body>
  <form action="/back/financing/investConditionAction!edit" id="form_">
  <input type='hidden' class='autoheight' value="auto" />
  <table>
    <tr><td>会员级别：</td><td><s:select name="memberLeveId" list="memberLevels" listKey="id" listValue="levelname"></s:select></td></tr>
  	<tr><td>单笔最低投标额：</td><td><input  name="investcondition.lowestMoney" type="text"  value="${investcondition.lowestMoney}"/></td></tr>
  	<tr><td>最高投标额百分比：</td><td><input  name="investcondition.highPercent"  type="text"  value="${investcondition.highPercent}"/></td></tr>
  </table>
   	<input type="hidden" name="investcondition_id" value="${investcondition_id}"/>  
   	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="ui-state-default" id="submitBtn">保存</button>
  </form>   
</body>
