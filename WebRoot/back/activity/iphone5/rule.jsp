<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<%@ include file="/common/import.jsp"%>
		<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script>
		<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css" />
		<script>
		$(function(){
			$("#form1").validate({
		        rules: { 
		          "rule.code":{ required:true},
		          "rule.title":{ required:true},
		          "rule.value":{ required:true,number:true},
		          "rule.qixian":{ required:true,number:true}
		        },  
		        messages: {
		          "rule.code":{ required:"请输入编码"},
		          "rule.title":{ required:"请输入标题"},
		          "rule.value":{ required:"请输入阀值",number: "请输入一个合法的数字"},
		          "rule.qixian":{ required:"请输入奖励积分",number: "请输入一个合法的数字"}
		        }    
			}); 
		})
				
		</script>
		<style>
		input,select {
			padding: 3px;
			width: 200px;
			outline: none;
			border: 1px solid #DDD;
		}
		
		input:hover,input:focus,select:hover,select:focus {
			border-color: #7DBDE2;
			box-shadow: 0 0 5px #7DBDE2;
		}
		
		#ui-datepicker-div {
			display: none;
			font-size: 13px;
			border-radius: 4px;
		}
		#submit_button{padding:5px 10px 5px 10px !important;font-size:20px !important;}
		</style>

	</head>
	<body>
		<form action='/back/activity/iphone5!save' id="form1">
			<input type="hidden" name='rule_id' value="${rule.id}" />
			<table>
				<tr>
					<td>类型:</td>
					<td>
						<input type="text" name="rule.code" value="${rule.code}"/>
					</td>
				</tr>
				<tr>
					<td>标题:</td>
					<td>
						<input type="text" name="rule.title" value="${rule.title}"/>
					</td>
				</tr>
				<tr>
					<td>限制金额:</td>
					<td><input type='text' name='rule.value' value="${rule.value}"/></td>
				</tr>
				<tr>
					<td>提款期限(单位:月):</td>
					<td><input type='text' name='rule.qixian' value="${rule.qixian}"/></td>
				</tr>
				<tr>
					<td colspan="2" align='left'>
						<button class='ui-state-default' id="submit_button">提交</button>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>