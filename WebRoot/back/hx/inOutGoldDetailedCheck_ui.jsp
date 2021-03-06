<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>出入金明细核对</title>
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
				minDate:'2013-03-13',
		        dateFormat: "yy-mm-dd"
		    });
			$("#endDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
			$("#checkDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
		    $("#ui-datepicker-div").css({'display':'none'});
		    var b = '${state}';
    		$("option[value='"+b+"']",$("#state")).attr("selected",true);
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
			<span style="color: red">*</span>发生日期：
		</td>
		<td>
			<input type="text" name="checkDate" value="<fmt:formatDate value='${checkDate}' type='date' />" id="checkDate"/>
		</td>
		<td align="right">
			<button onclick="toURL('/back/hxbank/hxbankAction!inOutGoldDetailedCheck');return false;" class="ui-state-default" >出入金明细核对</button>
		</td>
	</tr>
</table>
</form>
<br />
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				处理日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				状态&nbsp;<select name="state" id="state"><option value="success">成功</option><option value="failure">失败</option><option value="all">全部</option></select>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						发生日期
					</th>
					<th style="text-align: right;">
						入金总额
					</th>
					<th style="text-align: right;">
						入金总笔数
					</th>
					<th style="text-align: right;">
						出金总额
					</th>
					<th style="text-align: right;">
						出金总笔数
					</th>
					<th>
						处理日期
					</th>
					<th>
						处理者
					</th>
					<th>
						状态
					</th>
					<th>
						备注
					</th>
					<th>
						
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry">
					<tr id="s${entry.id}">
						<td>
							${entry.workDay}
						</td>
						<td style="text-align: right;">
							<fmt:formatNumber value='${entry.amtIn}' type="currency" currencySymbol=""/>
						</td>
						<td style="text-align: right;">
							${entry.countIn}
						</td>
						<td style="text-align: right;">
							<fmt:formatNumber value='${entry.amtOut}' type="currency" currencySymbol=""/>
						</td>
						<td style="text-align: right;">
							${entry.countOut}
						</td>
						<td>
							<fmt:formatDate value="${entry.createDate}" type="date" />
						</td>
						<td>
							${entry.operator.realname}
						</td>
						<td>
							<c:if test="${entry.success}"><span class="green">成功</span></c:if>
							<c:if test="${!entry.success}"><span class="red">失败</span></c:if>
						</td>
						<td>
							${entry.message}
						</td>
						<td>
							<a href='/back/hxbank/hxbankAction!inOutGoldDetail?id=${entry.id}' target="_blank">华夏出入金明细</a>
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
