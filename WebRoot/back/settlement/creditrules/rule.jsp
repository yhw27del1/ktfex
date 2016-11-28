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
		          "creditrule.title":{ required:true},
		          "creditrule.effecttime":{ required:true},
		          "creditrule.expiretime":{ required:true},
		          "creditrule.value":{ required:true,number:true},
		          "creditrule.creditexpiretime":{ required:true}
		        },  
		        messages: {
		          "creditrule.title":{ required:"请输入标题"},
		          "creditrule.effecttime":{ required:"请指定生效日期"},
		          "creditrule.expiretime":{ required:"请指定失效日期"},
		          "creditrule.value":{ required:"请输入奖励积分",number: "请输入一个合法的数字"},
		          "creditrule.creditexpiretime":{ required:"请指定积分失效日期"}
		        }    
			}); 
			$("#expiretime,#effecttime,#creditexpiretime").datepicker({
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
		<form action='/back/credit/creditRulesAction!save' id="form1">
			<input type="hidden" value="0" name='creditrule.activity' />
			<table>
				<tr>
					<td>标题:</td>
					<td>
						<input type="text" name="creditrule.title" />
					</td>
				</tr>
				<tr>
					<td>生效时间:</td>
					<td>
						<input type='text' name='creditrule.effecttime' id="effecttime" readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td>失效时间:</td>
					<td>
						<input type='text' name='creditrule.expiretime' id="expiretime" readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td>开户奖励:</td>
					<td><input type='text' name='creditrule.khjf' /></td>
				</tr>
				<tr>
					<td>投标抵扣阀值:</td>
					<td><input type='text' name='creditrule.relation_value' /></td>
				</tr>
				<tr>
					<td>投标抵扣比例(小数):</td>
					<td><input type='text' name='creditrule.value' /></td>
				</tr>
				
				<tr>
					<td colspan='2' align='left'>
						<button class='ui-state-default' id="submit_button">提交</button>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>