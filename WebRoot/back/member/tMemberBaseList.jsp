<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
    $(function(){
    $(".table_solid").tableStyleUI(); 
    });
	function toURL(url) {
		window.location.href = url;
	}
	function saveMemberLevel(memberId) {
		var url = "/back/member/memberBaseAction!saveLevel?id=" + memberId
				+ "&levelId=" + $('#levelId' + memberId).val();
		toURL(url);
	}
</script>
<body>
	<form id="form1" action="/back/member/memberBaseAction!tList"
		method="post">
		<input type='hidden' class='autoheight' value="auto" />
		<div>
			<div id="myToolBar"
				class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<div style="float: left;">
					<input type="hidden" name="page" value="1" />
					会员名/会员名&nbsp;
					<input type="text" class="input_box" style="width: 120px;" name="keyword" value="${keyword}" />
					<input type="submit" class="ui-state-default" value="查找" />
				</div>
			</div>
			<div class="dataList ui-widget">
				<table class="ui-widget ui-widget-content">
					<thead>
						<tr class="ui-widget-header">
							<th>
								会员名称
							</th>
							<th>
								用户名
							</th>
							<th>
								余额
							</th>
							<th>
								所在区域
							</th>
							<th>
								状态
							</th>
							<th>
								类型
							</th>
							<th>
								级别
							</th>
							<th>
								开户机构
							</th>
							<th>
								开户时间
							</th>
							<th>
								操作
							</th>
						</tr>
					</thead>
					<tbody class="table_solid">
						<c:forEach items="${pageView.records}" var="memberbase">
							<tr>
								<td>
									<c:if test="${memberbase.category==\"1\"}">
							<script>document.write(name("${memberbase.pName}"));</script>
							</c:if>
									<c:if test="${memberbase.category==\"0\"}">
							<script>document.write(name("${memberbase.eName}"));</script>
							</c:if>
								</td>
								<td>
									${memberbase.user.username}
								</td>
								<td>
									<fmt:formatNumber value="${memberbase.user.userAccount.balance}" type="currency" currencySymbol=""></fmt:formatNumber>&nbsp;</td>
								<td>
									${memberbase.provinceName}&nbsp;${memberbase.cityName}
								</td>
								<%--<td>
								${memberbase.cityName}
							</td>--%>
								<td>
									<c:if test="${memberbase.state==\"1\"}">
							待审核
							</c:if>
									<c:if test="${memberbase.state==\"2\"}">
							正常
							</c:if>
									<c:if test="${memberbase.state==\"3\"}">
							未通过审核
							</c:if>
									<c:if test="${memberbase.state==\"4\"}">
										<span style="color: red">已停用</span>
									</c:if>
								</td>
								<td>		<c:if test="${memberbase.category==\"1\"}">
							个人
							</c:if>
									<c:if test="${memberbase.category==\"0\"}">
							企业
							</c:if>&nbsp;${memberbase.memberType.name}									
								</td>
								<td>
									<select id="levelId${memberbase.id}" name="levelId">
										<c:forEach var="subject" items="${memberLevels}">
											<c:choose>
												<c:when test="${memberbase.memberLevel.id==subject.id}">
													<option value='${subject.id}' selected="selected">
														<c:out value="${subject.levelname}" />
													</option>
												</c:when>
												<c:otherwise>
													<option value='${subject.id}'>
														<c:out value="${subject.levelname}" />
													</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</td>
								<td>
									${memberbase.orgNo}
								</td>
								<td>
									<fmt:formatDate value="${memberbase.createDate}" type="date" />
								</td>
								<td>
									<button class="ui-state-default"
										onclick="toURL('/back/member/memberBaseAction!memberDetails?id=${memberbase.id}');return false;"
										style="display:<c:out value="${menuMap['memberBase_details']}" />">
										详细
									</button>
									<span style="clear: both"></span>
									<input
										onclick="saveMemberLevel('${memberbase.id}');return false;"
										class="ui-state-default" type="button"
										style="display:<c:out value="${menuMap['memberBase_not_audit']}" />"
										value="保存" />
								</td>
							</tr>
						</c:forEach>
						</tbody>
						<tbody>
						<tr>
							<td colspan="15">
								<jsp:include page="/common/page.jsp"></jsp:include></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form>
</body>