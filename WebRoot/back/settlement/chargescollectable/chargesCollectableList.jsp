<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>

<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
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
			<form action="/back/costCategoryAction!list">
				名称或编码&nbsp;
				<input type="text" name="keyword" value="${keyword}" />
				<input type="submit" class="ui-state-default" value="查询">
				<input type="hidden" name="page" value="1" />
			</form>
		</div>
		<div style="float: right;">
			<button class="ui-state-default reflash">
				刷新
			</button>
			<button class="ui-state-default"
				onclick="javascript: location.href = '/back/costcategory/costCategoryAction!ui';return false;">
				新增
			</button>
		</div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						名称
					</th>
					<th>
						编码
					</th>
					<th>
						收费方式
					</th>
					<th>
						备注
					</th>
					<th>
						添加时间
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
							${iter.name}
						</td>
						<td>
							${iter.code}
						</td>
						<td>
							<c:if test="${iter.tariff==0}">一次收费</c:if>
							<c:if test="${iter.tariff==1}">按月收费</c:if>
						</td>
						<td>
							${iter.memo}
						</td>
						<td>							
							<fmt:formatDate value="${iter.addtime}"/>
						</td>
						<td>
							<button class="ui-state-default"
								onclick="javascript:window.location.href='/back/costCategoryAction!ui?costbase_id=${iter.id}';return false;">
								修改
							</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tbody>
				<tr>
					<td colspan="4">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>