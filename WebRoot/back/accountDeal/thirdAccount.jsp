<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	function toURL(url) {
		window.location.href = url;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
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
</script>
<body><input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				兴易贷账户余额：${account.balance}元&nbsp;
				开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input class="ui-state-default" type="submit" value="查询">
	</div>

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						日期
					</th>
					<th>
						发生额汇总(￥)
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<s:property value="pageView.records"/>
				<s:iterator value="#request.pageView.records" var="accountDeal">
					<tr>
						<td>
							<s:property value="#accountDeal[0]" />【<a href="javascript:void(0);" onclick="toURL('/back/accountDeal/accountDealAction!thirdAccount2?startDate=<s:property value='#accountDeal[0]' />&endDate=<s:property value='#accountDeal[0]' />');return false;">查看明细</a>】
						</td>
						<td>
							<!-- <s:property value="#accountDeal[1]" /> 无格式化金额 -->
							<fmt:formatNumber value='${accountDeal[1]}' type="currency" currencySymbol=""/>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
	</form>
</body>
