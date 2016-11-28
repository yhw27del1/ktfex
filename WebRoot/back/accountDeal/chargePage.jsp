<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<body>
    <input type='hidden' class='autoheight' value="auto" />
	<div>		
		<div id="myToolBar"
			class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<div>
				<input type="hidden" name="page" value="1" />
				会员名&nbsp;
				<input type="text" name="keyword" value="${keyword}" />
				<input type="submit" class="ui-state-default" value="查找" />
			</div>
		</div>

		<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
						<th>
							会员名称
						</th>
						<th>
							账户余额
						</th>
						<th>
							开户时间
						</th>
						<th>
							操作
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageView.records}" var="memberbase">
						<tr>
							<td>
								<c:if test="${memberbase.category==\"1\"}">
							${memberbase.pName}
							</c:if>
								<c:if test="${memberbase.category==\"0\"}">
							${memberbase.eName}
							</c:if>
							</td>
							<td>
								${memberbase.user.userAccount.balance}
							</td>
							<td>
								<fmt:formatDate value="${memberbase.createDate}" type="date" />
							</td>
							<td>
								<button class="ui-state-default"
									onclick="toURL('/back/member/memberBaseAction!edit?id=${memberbase.id}');"
									style="display:<c:out value="${menuMap['role_Edit']}" />">
									充值
								</button>
								<span style="clear: both"></span>&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="12">
							<jsp:include page="/common/page.jsp"></jsp:include></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>