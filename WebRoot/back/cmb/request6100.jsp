<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>指定银商转账银行(预签约)</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
	<script type="text/javascript" src="/back/four.jsp"></script>
	<style type="text/css">
		.red{color:red;}
		.green{color:green;}
	</style>
	<script type="text/javascript">
		$(function(){
			$(".table_solid").tableStyleUI();
			$("#startDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
			$("#endDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
		    $("#ui-datepicker-div").css({'display':'none'});
		});
		function toURL(url){
			var d = $("#dataForm").serialize()+"&time="+new Date().getTime();
		   	$.ajax({
		   		type:"post",
		   		url:url,
		   		data:d,
		   		dataType:"json",
		   		success:function(data){
		   		console.log(data);
		   			alert(data.msg);
		   			location.reload();
		   		},
		   		error:function(data){
		   		console.log(data);
		   			alert(data.msg);
		   			location.reload();
		   		}
		   	});
		}
	</script>
  </head>
<body>
<form id="dataForm">
<table border="0">
	<tr>
		<td align="right">
			<span style="color: red">*</span>银行帐号：
		</td>
		<td>
			<input name="request6100.bankAcc" maxlength="20" type="text"/>
		</td>
		<td align="right">
			<span style="color: red">*</span>交易帐号：
		</td>
		<td>
			<input name="request6100.fundAcc" maxlength="20" type="text"/>
		</td>
		<td align="right">
			<span style="color: red">*</span>业务类型：
		</td>
		<td>
			<select name="request6100.busiType">
				<option value="01">银商转账模式</option>
				<option value="02">中介支付模式</option>
			</select>
		</td>
		<td align="right">
			<span style="color: red">*</span>证件类型：
		</td>
		<td>
			<select name="request6100.iDType">
				<option value="P01">身份证</option>
				<option value="C09">组织机构代码证</option>
			</select>
		</td>
	</tr>
	<tr>
		<td align="right">
			<span style="color: red">*</span>证件号：
		</td>
		<td>
			<input name="request6100.iDNo" maxlength="20" type="text"/>
		</td>
		<td align="right">
			<span style="color: red">*</span>客户名称：
		</td>
		<td>
			<input name="request6100.custName" maxlength="20" type="text"/>
		</td>
		<td align="right">
			<span style="color: red">*</span>帐号类别：
		</td>
		<td>
			<select name="request6100.accType">
				<option value="10">一卡通</option>
				<option value="20">对公账户</option>
				<option value="30">财富账户</option>
			</select>
		</td>
		<td>
			<button onclick="toURL('/back/cmb/cmbAction!request6100_do');return false;" class="ui-state-default" >预签</button>
		</td>
	</tr>
</table>
</form>
<br />
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				查询日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						日期
					</th>
					<th>
						市场流水
					</th>
					<th>
						交易名称<br/>交易码
					</th>
					<th>
						交易帐号
					</th>
					<th>
						银行帐号
					</th>
					<th>
						业务类型
					</th>
					<th>
						身份证号
					</th>
					<th>
						客户名称
					</th>
					<th>
						账户类别
					</th>
					<th>
						状态
					</th>
					<th>
						操作者
					</th>
					<th>
						备注&nbsp;
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${data}" var="entry">
					<tr>
						<td>
							${entry.createdate}
						</td>
						<td>
							${entry.coserial}
						</td>
						<td>
							${entry.name}<br/>${entry.txcode}
						</td>
						<td>
							${entry.fundacc}
						</td>
						<td>
							<script>document.write(bankcard("${entry.bankacc}"));</script>
						</td>
						<td>
							<c:if test="${entry.busitype=='01'}">银商转账模式</c:if>
							<c:if test="${entry.busitype=='02'}">中介支付模式</c:if>
						</td>
						<td>
							<script>document.write(idcard("${entry.idno}"));</script>
						</td>
						<td>
							<script>document.write(name("${entry.custname}"));</script>
						</td>
						<td>
							<c:if test="${entry.acctype=='10'}">一卡通</c:if>
							<c:if test="${entry.acctype=='20'}">对公账户</c:if>
							<c:if test="${entry.acctype=='30'}">财富账户</c:if>
						</td>
						<td>
							<c:if test="${entry.success==1}"><span class="green">成功</span></c:if>
							<c:if test="${entry.success==0}"><span class="red">失败</span></c:if>
						</td>
						<td>
							${entry.username}/${entry.realname}
						</td>
						<td>
							${entry.memo}
						</td>
					</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	</form>
</body>
</html>