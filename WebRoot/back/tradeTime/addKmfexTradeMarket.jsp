<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<%@ include file="/common/import.jsp"%>
		<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script>
		<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css" />
		<style>
			input {
				padding: 3px;
				width: 200px;
				outline: none;
				border: 1px solid #DDD;
			}
			
			input.short {
				width: 80px;
				maxlength : 5;
			}
			
			input:hover,input:focus {
				border-color: #7DBDE2;
				box-shadow: 0 0 5px #7DBDE2;
			}
		</style>
		<script>
		$(function(){
			$("#form1").validate({
		        rules: { 
		          "market.title":{ required:true}
		        },  
		        messages: {
		          "market.title":{ required:"请输入标题"}
		        }
			}); 
		});
		
		</script>
	</head>
	<body>
		<form action='/back/tradeTime/kmfexTradeMarketAction!save' id="form1">
			<table>
				<tr>
					<td align="right">标题:</td>
					<td>
						<input type="text" name="market.title" value="交易市场开市与休市规则" />
					</td>
				</tr>
				<tr>
					<td colspan='2' align='left'>
						<button class='ui-state-default' id="submit_button">提交</button>&nbsp;&nbsp;&nbsp;&nbsp;
						<s:actionerror theme="simple"/>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>