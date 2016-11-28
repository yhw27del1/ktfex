<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script tyle="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI(); 
});
</script>
<body>
<div class="dataList ui-widget">
	<br />
			<table class="ui-widget ui-widget-content" width="100%" style="padding:10 10;">
				<thead>
					<tr class="ui-widget-header ">
						<th>
							编号
						</th>
						<th>
							项目简称
						</th>
						<th>
							本金总额
						</th>
						<th>
							利息总额
						</th>
						<th>
							贷款期限
						</th>
						<th>
							还款次数
						</th>
						<th>
							还款方式
						</th>
						<th>
							<!-- 按月等额本息还款(本金) -->
							每次应还本金
						</th>
						<th>
							<!-- 按月等额本息还款(利息) -->
							每次应还利息
						</th>
						<th>
							生效日期
						</th>
						<th>
							终止日期
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${contracts}" var="entry">
						<tr>
							<td>${entry.financingBase.code}</td>
							<td>
							<a href="javascript:void(0);"  class="tooltip" title="${entry.financingBase.shortName}">
								<c:choose>
									<c:when test="${fn:length(entry.financingBase.shortName) > 10}">
										<c:out value="${fn:substring(entry.financingBase.shortName,0,10)}..." />
									</c:when>
									<c:otherwise>
										<c:out value="${entry.financingBase.shortName}" />
									</c:otherwise>
								</c:choose>
							</a>
							</td>
							<td>
								<fmt:formatNumber value='${entry.bj}'  type="currency" currencySymbol="" />
							</td>
							<td>
								<fmt:formatNumber value='${entry.lx}'  type="currency" currencySymbol="" />
							</td>
							<td>
								 <c:if test="${(entry.financingBase.interestDay)!= 0}">
								     ${entry.financingBase.interestDay}天</c:if>
			                    <c:if test="${(entry.financingBase.interestDay)== 0}">
								     <c:if test="${entry.loan_term<10}">&nbsp;${entry.loan_term}</c:if><c:if test="${entry.loan_term>=10}">${entry.loan_term}</c:if>个月
                                 </c:if>  
							</td>
							<td>
								<c:if test="${entry.financingBase.businessType.returnTimes<10}">&nbsp;${entry.financingBase.businessType.returnTimes}</c:if><c:if test="${entry.financingBase.businessType.returnTimes>=10}">${entry.financingBase.businessType.returnTimes}</c:if>次
							</td>
							<td>${entry.financingBase.businessType.returnPattern}</td>
							<td>
								<fmt:formatNumber value='${entry.bj_monthly}'  type="currency" currencySymbol="" />
							</td>
							<td>
								<fmt:formatNumber value='${entry.lx_monthly}'  type="currency" currencySymbol="" />
							</td>
							<td>
								<fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd" />
							</td>
							<td>
								<fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd" />
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
</div>
</body> 