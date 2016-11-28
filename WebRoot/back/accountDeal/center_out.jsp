<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>中心账户出账</title>
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
			if($("#chargeAmount").val()){
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
			}else{
				alert("请输入出账金额");
			}
		}
	</script>
  </head>
<body>
<form id="dataForm">
<table border="0">
	<tr>
		<td align="right">
			中心账户当前余额:
		</td>
		<td>
			${account.balance}元
		</td>
		<td>
			
		</td>
		<td>
			
		</td>
	</tr>
	<tr>
		<td align="right">
			出账金额:
		</td>
		<td>
			<input type="text" name="chargeAmount" id="chargeAmount" />
		</td>
		<td>
			
		</td>
	</tr>
	<tr>
		<td align="right">
			备注:
		</td>
		<td>
			<input type="text" name="memo" id="memo" />
		</td>
		<td>
			<button onclick="toURL('/back/accountDealAction!center_out_do');return false;" class="ui-state-default" >出账</button>
		</td>
	</tr>
</table>
</form>
<br />
<form action="">
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				出账日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						出账日期
					</th>
					<th>
						类型
					</th>
					<th style="text-align: right;">
						金额
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
							<fmt:formatDate value="${entry.createtime}" type="date" />
						</td>
						<td>
							<c:choose>
								<c:when test="${entry.action==35}">
									中心账户出账-划出
								</c:when>
							</c:choose>
						</td>
						<td style="text-align: right;">
							<fmt:formatNumber currencySymbol="" value="${entry.value}" type="currency" />
						</td>
						<td>
							${entry.operater.realname}
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