<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
$(function(){
    $(".table_solid").tableStyleUI(); 
   });
function showInfo(id){
	$.dialog({title:'公告',width:'1000px',height:'400px',content:'url:/back/announcement/announcementAction!detail?announcement_id='+id,lock:true});
	return false;
}
</script>
<body>
<form action="/back/announcement/announcementAction!list_normal">
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<button class="ui-state-default reflash">刷新</button>
	<button class="ui-state-default" onclick="javascript:window.location.href='/back/announcement/announcementAction!ui';return false;">新增</button>
	<!-- <button class="ui-state-default" disabled="disabled">删除所选</button> 
	关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default">搜索</button>
	-->
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
				<th>标题</th>
				<th>添加时间</th>
				<th>过期时间</th>
				<th>目标用户</th>
				<th>审核状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="iter">
			<tr>
				<td><a onclick="showInfo('${iter.id}');" href="javascript:void(0);">${iter.title}</a></td>
				<td>${iter.addtime}</td>
				<td>${fn:substring(iter.endtime,0,10)}</td>
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
				<td>
					<c:choose>
						<c:when test="${iter.audit_state==0}">预备审核</c:when>
						<c:when test="${iter.audit_state==1}">待审核</c:when>
						<c:when test="${iter.audit_state==2}">审核通过</c:when>
						<c:when test="${iter.audit_state==3}">驳回</c:when>
						<c:otherwise>未处理</c:otherwise>
					</c:choose>
				</td>
				<td>
					
					<c:choose>
						<c:when test="${iter.audit_state==0}">
							<button onclick="javascript:window.location.href='/back/announcementAction!ui?announcement_id=${iter.id}';return false;" class="ui-state-default">修改</button>
							<button onclick="javascript:window.location.href='/back/announcementAction!state_normal?announcement_id=${iter.id}&announcement.audit_state=1';return false;" class="ui-state-default">申请审核</button>
						</c:when>
						<c:when test="${iter.audit_state==1}">等待审核结果</c:when>
						<c:when test="${iter.audit_state==2}">无可用操作</c:when>
						<c:when test="${iter.audit_state==3}">
							<button onclick="javascript:window.location.href='/back/announcementAction!ui?announcement_id=${iter.id}';return false;" class="ui-state-default">修改</button>
							<button onclick="javascript:window.location.href='/back/announcementAction!state_normal?announcement_id=${iter.id}&announcement.audit_state=1';return false;" class="ui-state-default">重新申请审核</button>
						</c:when>
						<c:otherwise>未处理</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
			<tr>
				<td colspan="4">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table>
</div>
</form>
</body>