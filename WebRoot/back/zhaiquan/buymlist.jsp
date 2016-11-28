<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
	<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
	$(function() {
		$("#startDate").datepicker( {
			numberOfMonths : 2,
			dateFormat : "yy-mm-dd"
		});
		$("#endDate").datepicker( {
			numberOfMonths : 2,
			dateFormat : "yy-mm-dd"
		});
		$("#ui-datepicker-div").css( {
			'display' : 'none'
		});
		$(".table_solid").tableStyleUI(); 
	});
	function show(url){
	    window.showModalDialog(url, null, "dialogWidth:800px;dialogHeight:auto;status:no;help:yes;resizable:no;");
    }
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3
}

.agreement {
	cursor: pointer;
}

.agreement:HOVER {
	text-decoration: underline;
}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
<form action="/back/zhaiquan/buyingAction!mlist"> 
	<input type='hidden' class='autoheight' value="auto" />
		<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		查询条件:&nbsp;关键字&nbsp;<input type="text" value="${querykeyWord}" name="querykeyWord">&nbsp;
				日期段：&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate"/>到<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" id="endDate"/>
            状态:<s:select name="queryState" list="stateList" listKey="string1" listValue="string2" headerKey="" headerValue="全部"/>
				<input class="ui-state-default" type="submit" value="查询">
	</div>
	<input type="hidden" name="page" value="1" /> 
	<div class="dataList ui-widget" style="float:left; width:1150px;margin-top:15px;">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						债权代码
					</th>
					<!--  <th>
						借款合同编号
					</th>-->
					<th>
						受让人编号
					</th>
					<th>
						受让人姓名
					</th>
					<th>
						受让时间
					</th>
					<th>
						受让价格
					</th>
					<th>
						状态
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
							 <a href="javascript:void(0)" onclick="show('/back/zhaiquan/zhaiQuanInvestAction!detail?zhaiQuanId=${entry.investRecord.id}')">${entry.zhaiQuanCode}</a>
						</td>  
						<!--  <td>
							${entry.contract_numbers}
						</td>-->
						<td> 
						    ${entry.buyer.username}
						</td>
						<td> 
						    <script>document.write(name("${entry.buyer.realname}"));</script>
						</td>
						<td>
							${entry.createDate}
						</td>
						<td>
							<fmt:formatNumber value='${entry.buyingPrice}' type="currency"
								currencySymbol="" />
						</td>
						<td>
							<c:if test="${entry.state=='0'}">
								<span style="color: #4169E1;">受让</span>
							</c:if>
							<c:if test="${entry.state=='1'}">
								<span style="color: green;">成功 </span>
							</c:if>
							<c:if test="${entry.state=='2'}">
								<span style="color: red;">失败 </span>
							</c:if> 
							 <c:if test="${entry.state=='3'}">
								<span style="color: red;">撤单 </span>
							</c:if> 
							<c:if test="${entry.state=='4'}">
								<span style="color: red;">已过期 </span>
							</c:if> 
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="6">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div> 
	</form>
</body>
