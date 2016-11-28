<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url;
}

</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 

<body>
<form id="form1" action="/back/financingBaseAction!investList" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
            关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default" id="seachButton">查找</button>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>项目简称</th>
				<th>总融资额(￥)</th>
				<th>已融资额(￥)</th>
				<th>可融资额(￥)</th>
				<th>投标人数</th>
				<th>开始日期</th>
				<th>截止日期</th>
				<th>进度</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.shortName}</td>
				<td><fmt:formatNumber value='${entry.maxAmount}' type="currency" currencySymbol=""/><!-- (${entry.maxAmount_daxie}) --></td>
				<td><fmt:formatNumber value='${entry.currenyAmount}' type="currency" currencySymbol=""/><!-- (${entry.currenyAmount_daxie}) --></td>
				<td><fmt:formatNumber value='${entry.curCanInvest}' type="currency" currencySymbol=""/><!-- (${entry.curCanInvest_daxie}) --></td>
				<td>${entry.haveInvestNum}</td>
				<td><fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd"/></td>
				<td> 
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中 </span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:#4169E1;">已满标</span></c:if>
				</td> 
				<td>
					<c:if test="${entry.state=='2'||entry.state=='3'}">
						<button onclick="toURL('/back/investBaseAction!investUI?id=${entry.id}');return false;" class="ui-state-default" >查看详情</button>
					</c:if>
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