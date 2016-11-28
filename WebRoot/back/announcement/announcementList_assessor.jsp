<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<body>
<form action="/back/announcement/announcementAction!list_assessor">
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>

<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<button class="ui-state-default reflash">刷新</button>
	<!-- <button class="ui-state-default" disabled="disabled">删除所选</button> 
	关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default">搜索</button>
	-->
</div>

<div class="dataList ui-widget">
<c:forEach items="${pageView.records}" var="iter">
	<table style="margin-top:20px;background-color:">
		<tbody>
			<tr>
				<td width="80">标题</td><td width="200">${iter.title}</td>
				<td>目标用户</td>
				<td>
					<c:if test="${iter.target=='A'}">全部</c:if>
					<c:if test="${iter.target!='A'}">
					<c:forEach items="${membertypes}" var="iter_membertype">
						<c:if test="${iter.target==iter_membertype.code}">
							${iter_membertype.name}
						</c:if>
					</c:forEach>
					</c:if>
				</td>
				<td width="80">操作</td>
				<td>
					<button onclick="javascript:window.location.href='/back/announcementAction!state_assessor?announcement_id=${iter.id}&announcement.audit_state=2';return false;" class="ui-state-default">审核通过</button>
					<button onclick="javascript:window.location.href='/back/announcementAction!state_assessor?announcement_id=${iter.id}&announcement.audit_state=3';return false;" class="ui-state-default">驳回</button>
				</td>
			<tr>
			<tr>
				<td>添加时间</td><td>${iter.addtime}</td>
				<td>过期时间</td><td>${fn:substring(iter.endtime,0,10)}</td>
				<td></td><td></td>
			<tr>
			<tr>
				<td>内容</td>
				<td colspan="5"><div>${iter.content}</div></td>
			</tr>
		</tbody>
	</table>
	</c:forEach>
	<jsp:include page="/common/page.jsp"></jsp:include>
</div>
</form>
</body>