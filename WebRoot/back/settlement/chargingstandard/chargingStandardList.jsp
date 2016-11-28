<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
<style>
.a_button {
	cursor: pointer;
}

.a_button:HOVER {
	text-decoration: underline;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".table_solid").tableStyleUI();
	});
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<div style="float: left;">
			<form id="form1" action="/back/member/chargingStandardAction!list"
				method="post">
				收费项目名称或编码&nbsp;&nbsp;<input type="text" name="keyword" value="${keyword}" />
				&nbsp;&nbsp;会员类型&nbsp;&nbsp;<s:select list="membertypes" name="memberTypeId" headerKey="" headerValue="请选择" listKey="id" listValue="name"></s:select>
				<input type="submit" value="查询" class="ui-state-default" />
				<input type="hidden" name="page" value="1" />
			</form>
		</div>
		<div style="float: right;">
			<button class="ui-state-default reflash">
				刷新
			</button>
			<button class="ui-state-default"
				onclick="javascript:window.location.href = '/back/chargingstandard/chargingStandardAction!ui';return false;">
				新增
			</button>
		</div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						收费项目名称
					</th>
					<th>
						收费项目编码
					</th>
					<th>
						收费方式
					</th>
					<th>
						会员类型
					</th>
					<th>
						金额
					</th>
					<th>
						百分比
					</th>
					<th>
						操作
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="iter">
					<tr>
						<td>
							${iter.costBase.name}
						</td>
						<td>
							${iter.costBase.code}
						</td>
						<td>
							<c:if test="${iter.costBase.tariff==0}">一次收费</c:if>
							<c:if test="${iter.costBase.tariff==1}">按月收费</c:if>
						</td>
						<td>
							${iter.memberTypel.name}
						</td>
						<td>
							${iter.money}
						</td>
						<td>
							${iter.percent}
						</td>
						<td>
							<button class="ui-state-default"
								onclick="javascript:window.location.href='/back/chargingstandard/chargingStandardAction!ui?costitem_id=${iter.id}';return false;">
								修改
							</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tbody>
				<tr>
					<td colspan="5">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>