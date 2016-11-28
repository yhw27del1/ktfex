<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,com.wisdoor.core.utils.DateUtils" pageEncoding="UTF-8"%>
<%@page import="com.wisdoor.core.utils.DateUtils"%>
<%@ include file="/common/taglib.jsp" %>
<%
	Date yhsj = null;
%>
<html>
	<head>
	
<%@ include file="/common/import.jsp" %>

<style>
	.ui-autocomplete{
		width:120px;
		overflow:hidden;
		padding:0;
		margin:0;
	}
	.ui-autocomplete li {
		width:120px;
		list-style-type: none;
		padding:0;
		margin:0;
	}
	.ui-autocomplete li a:HOVER{
		background-image: none;
	}
	.error{float:left;}
</style>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<script>
$(function(){
	$("#okButton").click(function() {
		$("#form1").submit();
	});
	var $span = $(".showabout").parent();
	var $td = $span.parent();
	$span.css({"color":"red"});
	$td.siblings(".showwhat").css({"color":"red"});
	$(".table_solid").tableStyleUI();
});



</script>
	</head>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="/back/paymentRecord/paymentRecordAction!dieWs" id="form1">
	<input type="hidden" name="page" value="1" />
		<input type="hidden" name="userName" value="${userName}" />
		<input type="hidden" name="pu" value="${pu}" />
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:35px"> 
	<div style="position:absolute;left:10px;">
	项目编号&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:120px;"/>
           还款状态:<s:radio list="#{'9':'全部','0':'未还款','1':'正常还款','3':'逾期还款','2':'提前还款','4':'担保代偿'}" name="state" />
           还清状态:<s:radio list="#{'9':'全部','0':'未还清','1':'已还清'}" name="payOver"></s:radio>
	<button class="ui-state-default" id="okButton">查询</button>
	</div>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable">
		<thead>
			<tr class="ui-widget-header "> 
				<th>项目编号</th>
				<th style="padding:0 0">项目简称</th> 
				<th style="padding:0 0">还款<br />期次</th> 
				<th style="padding:0 0">融资方</th>
				<th style="padding:0 0">状态</th>
				<th style="text-align:right;">投标额</th>
				<th>应还日期</th>
				<th>还款日期</th>
				<th style="text-align:right;">应收<br />总额</th>
				<th style="text-align:right;">应收<br />本金</th>
				<th style="text-align:right;">应收<br />利息</th>
				<th style="text-align:right;">实收<br />总额</th>
				<th style="text-align:right;">实收<br />本金</th>
				<th style="text-align:right;">实收<br />利息</th>
				<th style="text-align:right;">实收<br />罚金</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.result}" var="iter">
			<tr> 
				<td align="left"  style="color:#0C108B;">${iter.code}</td>
				<td align="left" style="color:#0C108B;padding:0 0;">
					<a href="javascript:void(0);" class="tooltip" title="${iter.shortname}">
						<c:choose>
							<c:when test="${fn:length(iter.shortname) > 3}">
								<c:out value="${fn:substring(iter.shortname,0,3)}..." />
							</c:when>
							<c:otherwise>
								<c:out value="${iter.shortname}" />
							</c:otherwise>
						</c:choose>
					</a>
				</td>
				<td align="left" style="color:#0C108B;padding:0 0"> 
					<c:if test="${(iter.interestday)!= 0}">按日计息(${iter.interestday}天)</c:if><c:if test="${(iter.interestday)== 0}">${iter.term}个月</c:if>-${iter.succession}
				</td>
				<td align="left" style="color:#0C108B;padding:0 0;">
					<a href="javascript:void(0);" class="tooltip" title="${iter.ename}">
						<c:choose>
							<c:when test="${fn:length(iter.ename) > 3}">
								<c:out value="${fn:substring(iter.ename,0,3)}..." />
							</c:when>
							<c:otherwise>
								<c:out value="${iter.ename}" />
							</c:otherwise>
						</c:choose>
					</a>
				</td> 
				<td align="left" style="color:#0C108B;padding:0 0">
				<c:set var="yhsj" value="${iter.predict_repayment_date}" scope="request"></c:set>
				<c:if test="${iter.state==0}"><span style="color:#4169E1;">未还款<%yhsj = (Date)request.getAttribute("yhsj");if(DateUtils.getBetween(new Date(),yhsj)<0){out.print("<input type='hidden' class='showabout' value='showred' />");}%></span></c:if>
				<c:if test="${iter.state==1}"><span style="color:green;">正常还款 </span></c:if>
				<c:if test="${iter.state==2}"><span style="color:red;">提前还款 </span></c:if>
				<c:if test="${iter.state==3}"><span style="color:#4169E1;">逾期还款</span></c:if>
				<c:if test="${iter.state==4}"><span style="color:#4169E1;">担保代偿</span></c:if>
				</td>
				<td align="right" style="color:#0C108B;text-align:right;">
					<fmt:formatNumber value="${iter.investamount}" pattern="0.00"/>
				</td>
				<td style="color:#0C108B;"><fmt:formatDate value="${iter.predict_repayment_date}" pattern="yyyy-MM-dd"/></td>
				<td style="color:#0C108B;"><fmt:formatDate value="${iter.actually_repayment_date}" pattern="yyyy-MM-dd"/></td>
				
				<td class="showwhat" align="left" style="color:green;text-align:right;">
					<fmt:formatNumber value="${iter.xybj+iter.xylx}" pattern="0.00"/>
				</td>
				<td class="showwhat" align="left" style="color:green;text-align:right;">
					<fmt:formatNumber value="${iter.xybj}" pattern="0.00"/>
				</td>
				<td class="showwhat" style="color:green;text-align:right;">
					<fmt:formatNumber value="${iter.xylx}" pattern="0.00"/>
				</td>
				<td style="color:red;text-align:right;"">
					<c:if test="${iter.state==0}">-</c:if>
					<c:if test="${iter.state!=0}"><fmt:formatNumber value="${iter.shbj+iter.shlx+iter.penal}" pattern="0.00"/></c:if>
				</td>
				<td style="color:red;text-align:right;">
					<c:if test="${iter.state==0}">-</c:if>
					<c:if test="${iter.state!=0}"><fmt:formatNumber value="${iter.shbj}" pattern="0.00"/></c:if>
				</td>
				<td style="color:red;text-align:right;">
					<c:if test="${iter.state==0}">-</c:if>
					<c:if test="${iter.state!=0}"><fmt:formatNumber value="${iter.shlx}" pattern="0.00"/></c:if>
				</td>
				<td style="color:red;text-align:right;">
					<c:if test="${iter.state==0}">-</c:if>
					<c:if test="${iter.state!=0}"><fmt:formatNumber value="${iter.penal}" pattern="0.00"/></c:if>
				</td>
				<td>${iter.remark_}</td>
			</tr>
			  
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="18"><jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tfoot>
	</table>
</div>
</form>
</body>
</html>

