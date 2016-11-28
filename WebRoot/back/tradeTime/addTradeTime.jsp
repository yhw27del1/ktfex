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
		          "trade.title":{ required:true},
		          "trade.am_start":{ required:true},
		          "trade.am_end":{ required:true},
		          "trade.pm_start":{ required:true},
		          "trade.pm_end":{ required:true}
		        },  
		        messages: {
		          "trade.title":{ required:"请输入标题"},
		          "trade.am_start":{ required:"请输入上午交易起始时间"},
		          "trade.am_end":{ required:"请输入上午交易截止时间"},
		          "trade.pm_start":{ required:"请输入下午交易截止时间"},
		          "trade.pm_end":{ required:"请输入下午交易截止时间"}
		        }
			}); 
		});
		
		//只能输入 00:00 - 23:59 范围内的字符
		function checkTime(ts){
			var str = ts.value.match(/^(?:0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/);
			ts.value = str;
			if(str){
				$(ts).css("border","1px solid #DDD");
				$(ts).attr("placeholder","");
			}else{
				$(ts).css("border","1px solid red");
				$(ts).attr("placeholder","格式错误");
			}
			//alert(str);
		}
		
		//只能输入数字和英文的冒号:
		function checkNumber(ts){
			ts.value=ts.value.replace(/[^(\d|\:)]/g,'');
		}
		</script>
	</head>
	<body>
		<form action='/back/tradeTime/tradeTimeAction!save' id="form1">
			<table>
				<tr>
					<td align="right">标题:</td>
					<td>
						<input type="text" name="trade.title" />
					</td>
				</tr>
				<tr>
					<td align="right">上午交易时间:</td>
					<td>
						<input class='short' maxlength="5" onkeyup="checkNumber(this)" onblur="checkTime(this)" type='text' name='trade.am_start' id="am_start" />至<input class='short' maxlength="5" onkeyup="checkNumber(this)" onblur="checkTime(this)" type='text' name='trade.am_end' id="am_end" />
					</td>
				</tr>
				<tr>
					<td align="right">下午交易时间:</td>
					<td>
						<input class='short' maxlength="5" onkeyup="checkNumber(this)" onblur="checkTime(this)" type='text' name='trade.pm_start' id="pm_start" />至<input class='short' maxlength="5" onkeyup="checkNumber(this)" onblur="checkTime(this)" type='text' name='trade.pm_end' id="pm_end" />
					</td>
				</tr>
				<tr>
					<td colspan='2' align='left'>
						<button class='ui-state-default' id="submit_button">提交</button>&nbsp;&nbsp;&nbsp;&nbsp;交易时间格式为:"09:00",长度必须为5位。
						<s:actionerror theme="simple"/>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>