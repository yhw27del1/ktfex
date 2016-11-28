<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<button class="ui-state-default reflash">刷新</button>
	<button class="ui-state-default">新增</button>
	<button class="ui-state-default">删除所选</button>
	关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default">搜索</button>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
				<th width="3%"><input type="checkbox" class="selectAll" /></th>
				<th>用户名</th>
				<th>真实名</th>
				<th>地址</th>
				<th>邮编</th>
				<th>手机</th>
				<th>座机</th>
				<th width="8%">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pageView.records}" var="user">
			<tr>
				<td><input type="checkbox" name="ids" class='selectOne' value="${user.id}" /></td>
				<td>${user.username}</td>
				<td>${user.realname}</td>
				<td>${user.userContact.address}</td>
				<td>${user.userContact.postalcode}</td>
				<td>${user.userContact.mobile}</td>
				<td>${user.userContact.phone}</td>
				<td><button aurl="/user/userManager!mofifyUI?id=${user.id}" class="ui-state-default">修改</button></td>
			</tr>
		</c:forEach>
			<tr>
				<td colspan="8">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table>
	<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			fjdksljfldsjfdsjfl
</div>