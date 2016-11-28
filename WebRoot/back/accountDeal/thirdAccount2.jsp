<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
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
	$("#backTo").click(function(){
		toURL("/back/accountDeal/accountDealAction!thirdAccount");
		return false;
	});
});
</script>
<body><input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				<input type="hidden" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" />
				<input type="hidden" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" />
				<fmt:formatDate value="${startDate}" type="date" />的清单&nbsp;<button class="ui-state-default" id="backTo">返回</button>
	</div>

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						项目编号
					</th>
					<th>
						项目简称
					</th>
					<th>
						账户类型
					</th>
					<th>
						类型
					</th>
					
					<th>
						存入
					</th>
					<th>
						支出
					</th>
					<th>
						结余
					</th>
					<th>
						操作者
					</th>
					<th>
						日期
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="accountDeal">
					<tr>
						<td>
							${accountDeal.financing.code}
						</td>
						<td> <a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${accountDeal.financing.id}');return false;"  class="tooltip" title="${accountDeal.financing.shortName}">
							<c:choose>
								<c:when test="${fn:length(accountDeal.financing.shortName) > 10}">
									<c:out value="${fn:substring(accountDeal.financing.shortName,0,10)}..." />
								</c:when>
								<c:otherwise>
									<c:out value="${accountDeal.financing.shortName}" />
								</c:otherwise>
							</c:choose>
							</a>
						</td>
						<td>
							${accountDeal.account.accountType}
						</td>
						<td>
							${accountDeal.type}
						</td>
						
						<td>
							<c:if test="${accountDeal.zf!=\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
								<c:if test="${accountDeal.checkFlag=='16'}">  罚金:<fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if>
							</c:if>
						</td>
						<td>
							<c:if test="${accountDeal.zf==\"-\"}">
								${accountDeal.zf}<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
							</c:if>
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.nextMoney}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${accountDeal.user.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="date" />
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="9">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
