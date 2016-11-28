<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script>
	$(function(){
		$(".table_solid").tableStyleUI();
				//时间控件
		    $("#startDate").datepicker({
		        showOn: 'button',
		        buttonImageOnly: false,
		        //changeMonth: true,
		        //changeYear: true,
		        defaultDate:-3,
		        numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"//,
		        //minDate: +1 
		        //maxDate: "+1M"
		    });
		    
		    $("#endDate").datepicker({
		        showOn: 'button',
		        buttonImageOnly: false,
		        //changeMonth: true,
		        //changeYear: true,
		        numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"//,
		        //minDate: +1 
		    });
	 $("#ui-datepicker-div").css({'display':'none'});
	 
		$('.contract').click(function(){
			window.showModalDialog("/back/investBaseAction!agreement_ui2?invest_record_id="+$(this).attr("id"), null, "dialogWidth:1000px;dialogHeight:auto;status:no;help:yes;resizable:no;");
			//$("input.ui-state-default").trigger('click');
		});
	})
	
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
		关键字&nbsp;<input type="text" value="${keyWord}" name="keyWord">&nbsp;
				开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' pattern="yyyy-MM-dd"/>" id="startDate" readonly="readonly"/>&nbsp;结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' pattern="yyyy-MM-dd"/>" id="endDate" readonly="readonly"/>
				<input class="ui-state-default" type="submit" value="查询">
	</div>
	<input type="hidden" name="page" value="1" />
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						项目简称
					</th>
					<th>
						融资方
					</th>
					<th>
						融资方用户名
					</th>
					<th>
						投标金额(￥)
					</th>
					<th>
						投资服务费(￥)
					</th>
					<th>
						投标日期
					</th>
					<th>
						本金/利息余额
					</th>
					<th>
						下次还款日
					</th>
					<th>
						合同编号及状态
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
							${entry.financingBase.shortName}
						</td>
						<td>
							${entry.financingBase.financier.eName}
						</td>
						<td>
							${entry.financingBase.financier.user.username}
						</td>
						<td>
							<fmt:formatNumber value='${entry.investAmount}' type="currency" currencySymbol=""/>
							<!-- (${entry.investAmount_daxie}) -->
						</td>
						<td>
							<fmt:formatNumber value='${entry.cost.tzfwf}' type="currency" currencySymbol=""/>
							<!--(${entry.cost.tzfwf_daxie}) -->
						</td>
						<td>
							<fmt:formatDate value="${entry.createDate}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							<fmt:formatNumber value="${entry.bjye}" type="currency" currencySymbol=""/>/<fmt:formatNumber value="${entry.lxye}" type="currency" currencySymbol=""/>
						</td>
						<td>
							<fmt:formatDate value="${entry.xyhkr}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							<a class="contract" href="javascript:void(0);" id="${entry.id}" title="${entry.contract.contract_numbers }">${fn:substring(entry.contract.contract_numbers,0,8)}...</a>/<c:if test="${entry.contract.investor_make_sure==null}">未确认</c:if><c:if test="${entry.contract.investor_make_sure!=null}">已确认</c:if>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="9">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
<div id="agreement_div"></div>
