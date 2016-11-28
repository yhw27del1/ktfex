<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<style>
	.a_button{
		
		cursor:pointer;
	}
	.a_button:HOVER {
		text-decoration: underline;
	}
</style>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<input type="hidden" name="page" value="1" />
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<button class="ui-state-default reflash">刷新</button>
	<button class="ui-state-default" onclick="javascript:window.location.href='/back/chargingstandard/chargingStandardAction!ui';return false;">新增</button>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
				<th>收费类型</th>
				<th>会员类型</th>
				<th>金额</th>
				<th>百分比</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pageView.records}" var="iter">
			<tr>
				<td>${iter.costBase.name}</td>
				<td>${iter.memberTypel.name}</td>
				<td>${iter.money}</td>
				<td>${iter.percent}</td>
				<td><button class="ui-state-default" onclick="javascript:window.location.href='/back/chargingstandard/chargingStandardAction!ui?costitem_id=${iter.id}';return false;" >修改</button></td>
			</tr>
		</c:forEach>
			<tr>
				<td colspan="5">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table>
</div>
</form>
</body>