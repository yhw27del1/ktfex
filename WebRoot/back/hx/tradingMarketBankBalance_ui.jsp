<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>银行子帐号余额查询</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
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
		   			alert(data.tip);
		   			location.reload();
		   		},
		   		error:function(data){
		   			alert(data.tip);
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
			<span style="color: red">*</span>用户名：
		</td>
		<td>
			<input name="param.merAccountNo" maxlength="20" type="text"/>
		</td>
		<td align="right">
			<span style="color: red">*</span>子账号：
		</td>
		<td>
			<input name="param.accountNo" maxlength="6" type="text"/>
		</td>
	</tr>
	<tr>
		<td align="right">
			&nbsp;
		</td>
		<td>
			<button onclick="toURL('/back/hxbank/hxbankAction!tradingMarketBankBalance');return false;" class="ui-state-default" >银行子帐号余额查询</button>
		</td>
		<td align="right" colspan="4">
			&nbsp;
		</td>
	</tr>
</table>
</form>
<br />
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				余额查询日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
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
						银行流水
					</th>
					<th>
						交易名称<br/>交易码
					</th>
					<th>
						用户名
					</th>
					<th>
						子账号
					</th>
					<th>
						银行总余额
					</th>
					<th>
						银行可用余额
					</th>
					<th>
						状态
					</th>
					<th>
						操作者
					</th>
					<th>
						备注
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
							<fmt:formatDate value="${entry.createDate}" type="date" />
						</td>
						<td>
							${entry.merchantTrnxNo}
						</td>
						<td>
							${entry.bankTxSerNo}
						</td>
						<td>
							${entry.name}<br/>${entry.trnxCode}
						</td>
						<td>
							${entry.merAccountNo}
						</td>
						<td>
							${entry.accountNo}
						</td>
						<td>
							${entry.bankAmt}
						</td>
						<td>
							${entry.bankAmtUse}
						</td>
						<td>
							<c:if test="${entry.success}"><span class="green">成功</span></c:if>
							<c:if test="${!entry.success}"><span class="red">失败</span></c:if>
						</td>
						<td>
							${entry.operator.realname}
						</td>
						<td>
							${entry.message}
						</td>
					</tr>
			</c:forEach>
			<tr>
				<td colspan="12">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
</html>
