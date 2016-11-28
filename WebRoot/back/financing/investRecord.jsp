<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
	function toURL(url) {
		window.location.href = url;
	}
</script>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	var by = $(".groupBy").val();
	$("option[value='"+by+"']").attr("selected",true);
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
});
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3
}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
	<input type='hidden' class='autoheight' value="auto" />
	<input type='hidden' class='groupBy' value="${groupBy}" />
	<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		关键字&nbsp;<input type="text" value="${keyWord}" name="keyWord">&nbsp;
				开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date'/>" id="startDate"/>&nbsp;结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<select name="groupBy">
					<option value="CreateDate">按日期统计</option>
					<option value="T">按投资人统计</option>
					<option value="R">按融资方统计</option>
				</select>
				<input class="ui-state-default" type="submit" value="查询">
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
				<c:if test="${groupBy=='CreateDate'}">
					<th>
						日期
					</th>
				</c:if>
				<c:if test="${groupBy=='T'}">
					<th>
						投标人
					</th>
				</c:if>
				<c:if test="${groupBy=='R'}">
					<th>
						融资方
					</th>
				</c:if>
					<th>
						投标额汇总(￥)
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			
				<c:forEach items="${pageView.records}" var="accountDeal">
							<tr>
								<td>
									<c:choose>
										<c:when test="${groupBy=='CreateDate'}"><fmt:formatDate value="${accountDeal[0]}" pattern="yyyy-MM-dd"/></c:when>
										<c:otherwise>${accountDeal[0]}</c:otherwise>
									</c:choose>
									<c:if test="${groupBy=='CreateDate'}">
									【<a href="javascript:void(0);" onclick="toURL('/back/investBaseAction!recordListDetail?keyWord=${keyWord }&groupBy=CreateDate&startDate=${accountDeal[0]}&endDate=${accountDeal[0]}');return false;">查看明细</a>】
									</c:if>
									<c:if test="${groupBy=='T'}">
									【<a href="javascript:void(0);" onclick="toURL('/back/investBaseAction!recordListDetail?keyWord=${keyWord }&memberId=${accountDeal[2]}&groupBy=T&startDate=<fmt:formatDate value='${startDate}' type='date'/>&endDate=<fmt:formatDate value='${endDate}' type='date'/>');return false;">查看明细</a>】
									</c:if>
									<c:if test="${groupBy=='R'}">
									【<a href="javascript:void(0);" onclick="toURL('/back/investBaseAction!recordListDetail?keyWord=${keyWord }&memberId=${accountDeal[2]}&groupBy=R&startDate=<fmt:formatDate value='${startDate}' type='date'/>&endDate=<fmt:formatDate value='${endDate}' type='date'/>');return false;">查看明细</a>】
									</c:if>
								</td>
								<td>
									<fmt:formatNumber value="${accountDeal[1] }" pattern="#,###,###,##0.00"/>
								</td>
							</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(50);
</script>