<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<style>
<!--
	.jiacu{font-size: 16px;font-weight: bold;}
-->
</style>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
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
<script type="text/javascript">
	function toURL(url) {
		window.location.href = url;
	}
	function del(id) {
		window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	  <div style="float: left;">
		  日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>
		 <input type="submit" class="ui-state-default" value="查询" />
	  </div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th rowspan="2">编号</th>
					<th rowspan="2">机构编码</th>
					<th rowspan="2">机构名称</th>
					<th colspan="2" style="text-align:center">
						<fmt:formatDate value="${today}" type="date" pattern="yyyy年MM月dd日" />投资人实时余额
					</th>
					<c:if test="${!show}">
						<th colspan="2" style="text-align:center">
							<fmt:formatDate value="${startDate}" type="date" pattern="yyyy年MM月dd日" />投资人日切余额
						</th>
					</c:if>
					<th colspan="4" style="text-align:center">
						<fmt:formatDate value="${startDate}" type="date" pattern="yyyy年MM月dd日" />投资人出入金
					</th>
				</tr>
				<tr class="ui-widget-header ">
					<th style="text-align: right;">可用余额</th>
					<th style="text-align: right;">冻结余额</th>
					<c:if test="${!show}">
						<th style="text-align: right;">可用余额</th>
						<th style="text-align: right;">冻结余额</th>
					</c:if>
					<th style="text-align: right;">入金总额</th>
					<th style="text-align: right;">入金总笔数</th>
					<th style="text-align: right;">出金总额</th>
					<th style="text-align: right;">出金总笔数</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${jyrb}" var="tm" varStatus="ss">
					<tr>
						<td>${ss.index+1}</td>
						<td>${tm.orgCode}</td>
						<td>${tm.orgName}</td>
						<td style="text-align: right;"><fmt:formatNumber value='${tm.sum_balance}' type="currency" currencySymbol=""/></td>
						<td style="text-align: right;"><fmt:formatNumber value='${tm.sum_frozen}' type="currency" currencySymbol=""/></td>
						<c:if test="${!show}">
							<td style="text-align: right;"><fmt:formatNumber value='${tm.sum_balance_s}' type="currency" currencySymbol=""/></td>
							<td style="text-align: right;"><fmt:formatNumber value='${tm.sum_frozen_s}' type="currency" currencySymbol=""/></td>
						</c:if>
						<td style="text-align: right;"><fmt:formatNumber value='${tm.sum_in}' type="currency" currencySymbol=""/></td>
						<td style="text-align: right;">${tm.count_in}</td>
						<td style="text-align: right;"><fmt:formatNumber value='${tm.sum_out}' type="currency" currencySymbol=""/></td>
						<td style="text-align: right;">${tm.count_out}</td>
					</tr>
				</c:forEach>
					<tr class="jiacu">
						<td colspan="3" style="text-align: right;">汇总</td>
						<td style="text-align: right;"><fmt:formatNumber value='${hz.sum_balance}' type="currency" currencySymbol=""/></td>
						<td style="text-align: right;"><fmt:formatNumber value='${hz.sum_frozen}' type="currency" currencySymbol=""/></td>
						<c:if test="${!show}">
							<td style="text-align: right;"><fmt:formatNumber value='${hz.sum_balance_s}' type="currency" currencySymbol=""/></td>
							<td style="text-align: right;"><fmt:formatNumber value='${hz.sum_frozen_s}' type="currency" currencySymbol=""/></td>
						</c:if>
						<td style="text-align: right;"><fmt:formatNumber value='${hz.sum_in}' type="currency" currencySymbol=""/></td>
						<td style="text-align: right;">${hz.count_in}</td>
						<td style="text-align: right;"><fmt:formatNumber value='${hz.sum_out}' type="currency" currencySymbol=""/></td>
						<td style="text-align: right;">${hz.count_out}</td>
					</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
