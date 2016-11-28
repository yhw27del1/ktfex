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
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
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
    $("#centerAccount_out").click(function(){
    	$.dialog({
    		title:'中心账户出账',
    		width:'940px',
    		height:'480px',
    		content:'url:/back/accountDealAction!center_out',
    		lock:true
    	});
    });
});
</script>
<body><input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				中心账户余额：<span id="center">${account.balance}</span>元&nbsp;
				开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />" id="startDate"/>&nbsp;结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" id="endDate"/>
				<input class="ui-state-default" type="submit" value="查询">
				<input type="button" style="display:<c:out value="${menuMap['centerAccount_out']}" />" id="centerAccount_out" class="ui-state-default" value="中心账户出账" />
	</div>

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						日期
					</th>
					<th>
						操作
					</th>
					<th>
						收入汇总(￥)
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<s:property value="pageView.records"/>
				<s:iterator value="#request.pageView.records" var="accountDeal">
					<tr>
						<td>
							<s:property value="#accountDeal[0]" />
						</td>
						
						<td>
							【<a href="javascript:void(0);" onclick="toURL('/back/accountDeal/accountDealAction!centerAccount2?date=<s:property value='#accountDeal[0]' />');return false;">查看明细</a>】
							【<a href='/back/accountDeal/accountDealAction!centerAccount2_toExcel?date=<s:property value='#accountDeal[0]' />'>导出明细</a>】
							<%-- 
							【<a href='/back/accountDeal/accountDealAction!centerAccount2_groupByFbase?date=<s:property value='#accountDeal[0]' />'>按费用类型分包查看</a>】
							【<a href='/back/accountDeal/accountDealAction!centerAccount2_groupByWholeFbase?startDate=<s:property value='#accountDeal[0]' />&endDate=<s:property value='#accountDeal[0]' />'>按融资项目查看</a>】
						--%></td>
						
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
<script>
setIframeHeight(50);
</script>