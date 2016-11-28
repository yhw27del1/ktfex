<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	$("#backTo").click(function(){
		window.location.href = "/back/investBaseAction!recordListTotal";
		return false;
	});
	$('.agreement').click(function(){
		window.showModalDialog("/back/investBaseAction!agreement_ui2?invest_record_id="+$(this).attr("id"), null, "dialogWidth:1000px;dialogHeight:auto;status:no;help:yes;resizable:no;");
		$("input.ui-state-default").trigger('click');
	});
});
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3
}
.agreement{cursor:pointer;}
.agreement:HOVER{text-decoration: underline;}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
	<input type='hidden' class='autoheight' value="auto" />
	<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<c:if test="${groupBy=='CreateDate'}">
			<fmt:formatDate value="${startDate}" type="date" />&nbsp;&nbsp;的投标清单&nbsp;
		</c:if>
		<c:if test="${groupBy=='T'}">
			投标人:${member.pName}&nbsp;&nbsp;<fmt:formatDate value="${startDate}" type="date" />至<fmt:formatDate value="${endDate}" type="date" />的投标清单&nbsp;
		</c:if>
		<c:if test="${groupBy=='R'}">
			融资方:${member.eName}&nbsp;&nbsp;<fmt:formatDate value="${startDate}" type="date" />至<fmt:formatDate value="${endDate}" type="date" />的投标清单&nbsp;
		</c:if>
		<button class="ui-state-default" id="backTo">返回</button>
	</div>
	<input type="hidden" name="page" value="1" />
	<input type="hidden" name="keyWord" value="${keyWord}" />
	<input type="hidden" name="startDate" value="<fmt:formatDate value='${startDate}' type='date'/>" />
	<input type="hidden" name="endDate" value="<fmt:formatDate value='${endDate}' type='date'/>" />
	<input type="hidden" name="groupBy" value="${groupBy}" />
	<input type="hidden" name="memberId" value="${memberId}" />
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
				    <th>
						项目编号
					</th>
					<th>
						项目简称
					</th>
					<th>
						投标日期
					</th>
					<th>
						投标方
					</th>
					<th>
						投标方编号
					</th>
					<th>
						融资方
					</th>
					<th>
						融资方编号
					</th>
					<th>
						投标金额(￥)
					</th>
					<th>
						服务费(￥)
					</th>
					
					<!--  <th>
						利息余额
					</th>-->
					<th>投标终端
					</th>
					<th>
						合同
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry">
					<tr>
					    <td>
							${entry.financingBase.code}
						</td>
						<td>
							${entry.financingBase.shortName}
						</td>
						<td>
							<fmt:formatDate value="${entry.createDate}" type="both" />							
						</td>
						<td>
							<script>document.write(name("${entry.investor.pName}"));</script>
						</td>
						<td>
							${entry.investor.user.username}
						</td>
						<td>
							<script>document.write(name("${entry.financingBase.financier.eName}"));</script>
						</td>
						<td>
							${entry.financingBase.financier.user.username}
						</td>
						<td>
							<fmt:formatNumber value='${entry.investAmount}' type="currency" currencySymbol=""/>
							<!-- (${entry.investAmount_daxie}) -->
						</td>
						<td>
							<fmt:formatNumber value='${entry.cost.tzfwf}' type="currency" currencySymbol="" />
							<!--(${entry.cost.tzfwf_daxie}) -->
						</td>
						
						<!--<td>
							<fmt:formatNumber value="${entry.bjye}" type="currency" currencySymbol=""/>/<fmt:formatNumber value="${entry.lxye}" type="currency" currencySymbol=""/>
						</td>-->
						<td>
						    <c:if test="${entry.fromApp==null}">电脑</c:if><c:if test="${entry.fromApp=='m'}">手机web</c:if>
						    <c:if test="${entry.fromApp=='android'}">android</c:if><c:if test="${entry.fromApp=='ios'}">ios</c:if>
						    <c:if test="${entry.fromApp=='autoinvest'}">自动投标</c:if>
						</td>
						<td>
							<a class="agreement" id="${entry.id}">${entry.contract.contract_numbers}</a>
							<!--  /  /<c:if test="${entry.contract.investor_make_sure==null}">未确认</c:if><c:if test="${entry.contract.investor_make_sure!=null}">已确认</c:if>-->
						</td>
					</tr>
				</c:forEach>
				</tbody>
		       <tbody>
				<tr>
					<td colspan="11">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
