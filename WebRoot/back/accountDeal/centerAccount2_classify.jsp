<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Insert title here</title>
		<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
		<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	
});
</script>
<style>
	table,th,td{
		border:1px solid #eee;
		border-collapse: collapse;
		border-spacing: 0;
		padding: 5px 10px;
	}
</style>
	</head>
	<body>
		<form action="" id="table_solid">
		<input type="hidden" name="page" value="1" />
		<input type="hidden" name="date" value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>"/>
		<input type="hidden" name="startDate" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>"/>
		<input type="hidden" name="endDate" value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>"/>
			<table class="ui-widget ui-widget-content table_solid" style="margin: 0;font-size:13px !important;" width="100%">
				<thead>
					<tr class="ui-widget-header">
						<th>
							类型
						</th>
						<th>
							项目简称
						</th>
						<th>
							(收)发生额
						</th>
						<th>
							(付)发生额
						</th>
						<th>
							发生时间
						</th>
						<th>
							发生对象
						</th>
						<th>
							操作者
						</th>
						<th>
							核算状态
						</th>
					</tr>
				</thead>
				<tbody class="table_solid" >
					<c:if test="${fn:length(pageView.records)==0}"><tr><td colspan="7">没有数据</td></tr></c:if>
					<c:forEach items="${pageView.records}" var="calr">
						<tr>
							<td>
								<c:choose>
									<c:when test="${calr.action==1}">
									
									融资款交割-划出
								</c:when>
									<c:when test="${calr.action==2}">
									投资款划入中心账户-划入
								</c:when>
									<c:when test="${calr.action==3}">
									三方结算-划入
								</c:when>
									<c:when test="${calr.action==4}">
									债权买入-划入
								</c:when>
									<c:when test="${calr.action==5}">
									债权卖出-划入
								</c:when>
									<c:when test="${calr.action==6}">
									融资服务费+罚金
								</c:when>
									<c:when test="${calr.action==7}">
									担保费+罚金
								</c:when>
									<c:when test="${calr.action==8}">
									风险管理费+罚金
								</c:when>
								<c:when test="${calr.action==9}">
									交易手续费
								</c:when>
									<c:when test="${calr.action==21}">
									风险管理费
								</c:when>
									<c:when test="${calr.action==31}">
									担保费
								</c:when>
									<c:when test="${calr.action==32}">
									风险管理费（按月计算）
								</c:when>
									<c:when test="${calr.action==33}">
									融资服务费
								</c:when>
									<c:when test="${calr.action==34}">
									保证金
								</c:when>
								<c:when test="${calr.action==35}">
                                    中心账户出账-划出
                                </c:when>
								<c:when test="${calr.action==36}">
									席位费
								</c:when>
								<c:when test="${calr.action==37}">
									信用管理费
								</c:when>
								<c:when test="${calr.action==41}">
									内部转帐
								</c:when>
									<c:otherwise>${calr.action}</c:otherwise>
								</c:choose>
							</td>
							<td>
								<a href="/back/financingBaseAction!detail?id=${calr.fbase.id}" class="tooltip" title="${calr.fbase.shortName}"> <c:choose>
										<c:when test="${fn:length(calr.fbase.shortName) > 10}">
											<c:out value="${fn:substring(calr.fbase.shortName,0,10)}..." />
										</c:when>
										<c:otherwise>
											<c:out value="${calr.fbase.shortName}" />
										</c:otherwise>
									</c:choose> </a>
							</td>

						<td>
							<!-- 此处本来取的是绝对值 --> <c:if test="${calr.value>=0}">
								<fmt:formatNumber value='${calr.abs_value}' pattern="#,##0.00" />
								<c:set value="${allmoney + calr.abs_value}" var="allmoney" />
							</c:if> <c:if test="${calr.value<0}">
										0
										</c:if>

						</td>
						<td><c:if test="${calr.value<0}">
								<!-- 此处取的是绝对值 -->
								<fmt:formatNumber value='${calr.abs_value}' pattern="#,##0.00" />
								<c:set value="${allmoney2 + calr.abs_value}" var="allmoney2" />
							</c:if> <c:if test="${calr.value>=0}">
										0
										</c:if></td>
						<td>
								<fmt:formatDate value="${calr.createtime}" pattern="yyyy-MM-dd hh:mm:ss" />
							</td>
							<td>
								${calr.object_.user.username }
							</td>
							<td>
								${calr.operater.username}
							</td>
							<td>
								${calr.calculated}
							</td>
						</tr>
					</c:forEach>
					
				</tbody>
				<tfoot>
					<tr>
						<th colspan="7" align="left">
							<jsp:include page="/common/page.jsp"></jsp:include>
						</th>
					</tr>
				</tfoot>
			</table>
			</form>
	</body>
</html>