<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<html>
<head>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script>
$(function(){
	$("#form1").validate({
        rules: { 
          "zqbuysellrule.term":{ required:true,number:true},
          "zqbuysellrule.days":{ required:true,number:true},
          "zqbuysellrule.overdue":{ required:true,number:true},
          "zqbuysellrule.createDate":{ required:true}
          
        },  
        messages: {      
          "zqbuysellrule.term":{ required:"请输入允许转让包的期限",number: "必须为数字"},
          "zqbuysellrule.days":{ required:"请输入转让间隔天数",number:"必须为数字"},
          "zqbuysellrule.overdue":{ required:"请输入允许的最小逾期天数",number:"必须为数字"},
          "zqbuysellrule.createDate":{ required:"请指定启用日期"}
        }    
	});
	$("#createDate").datepicker({
		   dateFormat: "yy-mm-dd"
     });
})
		
</script>
<style>
		input {
			padding: 3px;
			width: 200px;
			outline: none;
			border: 1px solid #DDD;
		}
		
		input:hover,input:focus {
			border-color: #7DBDE2;
			box-shadow: 0 0 5px #7DBDE2;
		}
		
		#ui-datepicker-div {
			display: none;
			font-size: 13px;
			border-radius: 4px;
		}
		</style>
</head>
<body>
<form action='/back/zhaiquan/buySellRuleAction!save' id="form1">
     <input type="hidden" value="${id}" name='id'/>
	<table>
		<tr>
		    <td>允许转让包的期限:</td>
		   <td><input type='text' name='zqbuysellrule.term' value="${zqbuysellrule.term }"/>个月</td>
		</tr>
		<tr>
		    <td>转让间隔天数:</td>
		   <td><input type='text' name='zqbuysellrule.days' value="${zqbuysellrule.days }"/></td>
		</tr>
		<tr>
		    <td>最小逾期天数:</td>
		   <td><input type='text' name='zqbuysellrule.overdue' value="${zqbuysellrule.overdue}"/></td>
		</tr>
		<tr>
		   <td>启用日期:</td>
		   <td>
			   <input type='text' name='zqbuysellrule.createDate' value="<fmt:formatDate value="${zqbuysellrule.createDate }" pattern="yyyy-MM-dd"/>"
			    id="createDate" readonly="readonly" />
		  </td>
		</tr>
		<tr>
		    <td colspan='2' align='right'>
		      <button  class='ui-state-default' id="submit_button">提交</button>
		    </td>
		</tr>
	</table>
</form>
</body>
</html>