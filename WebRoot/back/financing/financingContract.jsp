<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
$(document).ready(function(){
	$(".table_solid").tableStyleUI(); 
	$('.contract').click(function(){
		window.showModalDialog("/back/investBaseAction!agreement_ui2?invest_record_id="+$(this).attr("id"), null, "dialogWidth:1000px;dialogHeight:800px;status:no;help:yes;resizable:no;");
	});
});
 
</script>  
 
<body>
<div class="dataList ui-widget">
	<br />
			<table class="ui-widget ui-widget-content" width="100%" style="padding:10 10;">
				<thead>
					<tr class="ui-widget-header ">
						
						<th>
							投标人
						</th>
						<th>
							投标金额(￥)
						</th>
						<th>
							投标日期
						</th>
						<th>
							投标合同编号及状态
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${investRecords}" var="entry">
						<tr>
							<td>
								${entry.investor.pName}
							</td>
							<td>
								<fmt:formatNumber value='${entry.investAmount}' type="currency" currencySymbol="" />
							</td>
							<td>
								<fmt:formatDate value="${entry.createDate}" pattern="yyyy-MM-dd" />
							</td>
							<td>
								<a class="contract" href="javascript:void(0);" id="${entry.id}">${entry.contract.contract_numbers}</a>/<c:if test="${entry.contract.investor_make_sure==null}">未确认</c:if><c:if test="${entry.contract.investor_make_sure!=null}">已确认</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<span style="padding:10 10;">
			本金总额：<fmt:formatNumber value='${principal_allah}' type="currency" currencySymbol=""/>元，利息总额：<fmt:formatNumber value='${interest_allah}' type="currency" currencySymbol=""/></span>
			<c:if test="${financingBase.businessType.returnTimes>1}">
			<br /><span style="padding:10 10;">按月等额本息还款(本金)：<fmt:formatNumber value='${principal_allah_monthly}' type="currency" currencySymbol=""/>元，
				     按月等额本息还款(利息)：<fmt:formatNumber value='${interest_allah_monthly}' type="currency" currencySymbol=""/>元，
				     每月应还金额：<fmt:formatNumber value='${repayment_amount_monthly_allah}' type="currency" currencySymbol=""/>元</span>
			</c:if>
			</span>
			<br /><br />
</div>
</body> 