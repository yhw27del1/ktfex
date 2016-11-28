<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<body>
<form action="/sys_/transaction/transactionAction!list">
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>

<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<button class="ui-state-default reflash">刷新</button>
	<button class="ui-state-default" onclick="window.location.href='/sys_/transaction/transactionAction!ui';return false;">新增</button>
	<!-- <button class="ui-state-default" disabled="disabled">删除所选</button> 
	关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default">搜索</button>
	-->
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
				<th>事务名称</th>
				<th>事务描述</th>
				<th>执行时间</th>
				<th>执行对象</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pageView.records}" var="iter">
			<tr>
				<td>${iter.name}</td>
				<td>${iter.description}</td>
				<td>${iter.time}</td>
				<td>${iter.entity}</td>
				<td>
					<button onclick="window.location.href='/sys_/transaction/transactionAction!ui?transaction_id=${iter.id}';return false;" class="ui-state-default">修改</button>
					<button onclick="window.location.href='/sys_/transaction/transactionAction!del?transaction_id=${iter.id}';return false;" class="ui-state-default">删除</button>
				</td>
			</tr>
		</c:forEach>
			<tr>
				<td colspan="4">
					<jsp:include page="/common/page.jsp"/></td>
			</tr>
		</tbody>
	</table>
</div>
</form>
</body>