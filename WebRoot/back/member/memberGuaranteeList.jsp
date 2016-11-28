<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<link rel="stylesheet" href="/Static/css/member.css"
	type="text/css" />
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
	<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
	$(function() {
		$(".table_solid").tableStyleUI();
		$('#provinceCode')
				.change(
						function() {
							$.post('/back/region/regionAction!getchildren?region_id=' + $(
													this).val(), null,
											function(data, status) {
												$('#cityCode').children()
														.remove();
												$('#cityCode').html(data);
											}, 'text');
						});
	});
	function toURL(url) {
		window.location.href = url;
	}
</script>
<body>
	<div id="myToolBar" style="height: 50px;"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<div style="float: left;">
			<div style="margin: 10px;">
				<form action="/back/member/memberGuaranteeAction!list" method="post">
					<input type="hidden" name="page" value="1" />
					<input type='hidden' class='autoheight' value="auto" />
					会员名/用户名&nbsp;
					<input class="input_box" style="width: 120px;" type="text"
						name="keyword" value="${keyword}" />
					<input type="submit" class="ui-state-default" value="查找" />
				</form>
			</div>
		</div>
	</div>
	<form id="form1" action="/back/member/memberGuaranteeAction!list"
		method="post">		
		<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header">
						<th>
							会员名称
						</th>
						<th>
							推荐指数(<img src="/Static/js/star/rater/img/star-on.png">)
						</th> 
						<th>
							操作
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.records}" var="mg">
						<tr>
							<td>
								<c:if test="${mg.memberBase.category==\"1\"}">
							<script>document.write(name("${mg.memberBase.pName}"));</script>
							</c:if>
								<c:if test="${mg.memberBase.category==\"0\"}">
							<script>document.write(name("${mg.memberBase.eName}"));</script>
							</c:if>
							</td>
							<td>
								${mg.tjzs}星
							</td> 
							<td>
								<button class="ui-state-default"
									onclick="toURL('/back/member/memberGuaranteeAction!edit?id=${mg.id}'); return false;"
									style="display:<c:out value="${menuMap['memberGuarantee_edit']}" />">
									修改
								</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
				<tbody>
					<tr>
						<td colspan="3">
							<jsp:include page="/common/page.jsp"></jsp:include></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</body>