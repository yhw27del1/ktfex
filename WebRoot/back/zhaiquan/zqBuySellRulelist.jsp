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
	function setenable(id,value){
		$.post("/back/zhaiquan/buySellRuleAction!setEnable",{"id":id,"value":value},function(data,status,xml){
			$("#form1").submit();
		});
	}
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<div style="float: left;">
			<form id="form1" action="/back/zhaiquan/buySellRuleAction!list"
				method="post">
				<input type="hidden" name="page" value="1" />
			</form>
		</div>
		<div style="float: left;">
			<button class="ui-state-default reflash">
				刷新
			</button>
			<button class="ui-state-default"
				onclick="javascript:window.location.href = '/back/zhaiquan/buySellRuleAction!ui';return false;">
				新增
			</button>
		</div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
					       允许转让包的期限
					</th>
					<th>转让间隔天数
					</th>
					<th>最小逾期天数
					</th>				
					<th>
					         收费方向
					</th>
					<th>
						启用日期
					</th>
					<th>
					         停用日期
					</th>
					<th>
						状态
					</th>
					<th></th>
					
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="iter">
					<tr>
						<td>
							${iter.term}个月
						</td>
						<td>${iter.days}
						</td>
						<td>${iter.overdue}</td>
						<td>双向</td>
						<td>
							<fmt:formatDate value="${iter.createDate}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.endDate}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							<c:if test="${iter.enable}">启用</c:if>
							<c:if test="${!iter.enable}"><span style="color: red">停用</span></c:if>
						</td>
						<td>
							<button class="ui-state-default"
								onclick="javascript:window.location.href='/back/zhaiquan/buySellRuleAction!ui?id=${iter.id}';return false;">
								修改
							</button>
							<button class="ui-state-default"
							   <c:if test="${iter.enable}">
								    onclick="setenable('${iter.id}',false)">
								停用</c:if>
								<c:if test="${!iter.enable}">
								    onclick="setenable('${iter.id}',true)">
								启用</c:if>
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