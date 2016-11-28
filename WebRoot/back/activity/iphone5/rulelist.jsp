<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<style>
.a_button {
	cursor: pointer;
}

.a_button:HOVER {
	text-decoration: underline;
}

.ui-helper-hidden-accessible{
	display:none;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".table_solid").tableStyleUI();
		$(".modify_button").css({"font-size":"13px","padding":"3px 5px 3px 5px","cursor":"pointer"}).button().click(function(){
			var id = $(this).attr("target");
			window.location.href="/back/activity/iphone5!rule?rule_id="+id;
		});
		$(".del_button").css({"font-size":"13px","padding":"3px 5px 3px 5px","cursor":"pointer"}).button().click(function(){
			var id = $(this).attr("target");
			window.location.href="/back/activity/iphone5!del?rule_id="+id;
		});
	});
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<div style="float: left;line-height:25px;">
			<form id="form1" action="/back/credit/creditRulesAction!rules_list" method="post">
				<input type="hidden" name="page" value="1" />
			</form>
		</div>
		<div style="float: right;">
			<button class="ui-state-default reflash">
				刷新
			</button>
			<button class="ui-state-default" onclick="javascript:window.location.href = '/back/activity/iphone5!rule';return false;">
				新增
			</button>
		</div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						类型
					</th>
					<th>
						标题
					</th>
					<th>
						充值金额
					</th>
					<th>
						限制期限(月)
					</th>
					<th>操作</th>
				</tr>
				
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${rules}" var="iter">
					<tr>
						<td>
							${iter.code}
						</td>
						<td>
							${iter.title}
						</td>
						<td>
							<fmt:formatNumber value="${iter.value}" pattern="#0.00"/>
						</td>
						<td>
							${iter.qixian}
						</td>
						<td>
							<span class="modify_button" target="${iter.id}">修改</span>
							<span class="del_button" target="${iter.id}">删除</span>
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