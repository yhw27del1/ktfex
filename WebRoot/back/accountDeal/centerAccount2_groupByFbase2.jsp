<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
	$(function() {
		$(".table_solid").tableStyleUI();
		$("#backTo").click(
						function() {
							var startDate = $("#startDate").val();
							var endDate = $("#endDate").val();
							window.location.href = "/back/accountDeal/accountDealAction!centerAccount?startDate="
									+ startDate + "&endDate=" + endDate;
							return false;
						});
		$("#startDate").datepicker({
			numberOfMonths: 2,
	        dateFormat: "yy-mm-dd"
	    });
		$("#endDate").datepicker({
			numberOfMonths: 2,
	        dateFormat: "yy-mm-dd"
	    });
	    $("#ui-datepicker-div").css("display","none");
	    $("#centerAccount_out").click(function(){
	    	$.dialog({
	    		title:'中心账户出账',
	    		width:'940px',
	    		height:'480px',
	    		content:'url:/back/accountDealAction!center_out',
	    		lock:true
	    	});
	    });

	});
</script>
		<style>
			.ui-helper-hidden-accessible{
			display:none;
		}
		</style>
		<style>
		<%--颜色设置为红色带下划线，便于识别  --%>
		.yangshi{color:red;text-decoration: underline;}
		</style>
<script type="text/javascript">
	function openCenterOutDialog(action,showcoding,code){
		$.dialog({
    		title:'中心账户出账',
    		width:'940px',
    		height:'480px',
    		content:'url:/back/accountDealAction!center_out?action='+action+'&showcoding='+showcoding+'&fcode='+code,
    		lock:true
    	});
	}
</script>

<body>

	<input type='hidden' class='autoheight' value="auto" />




	<div class="dataList ui-widget" style="border: none">
		<form action="">
		<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<input type="hidden" name="page" value="1" /> 
			<input type="hidden" name="date" value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>" />
			<a href="javascript:history.go(-1);"><input type="button" class="ui-state-default" value="返回" /></a>
			日期:从<input type="text" id="startDate" name="startDate" style="width:120px;" id="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>"/>&nbsp;
			到&nbsp;<input type="text" name="endDate" style="width:120px;" id="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>"/>
			
			<input type="hidden" name="endDate" value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" />
			
			&nbsp;&nbsp;关键字:<input type="text" name="keyWord" value="${keyWord }" /> 
			&nbsp;&nbsp;融资项目号:<input type="text" name="fcode" value="${fcode }"/> 
			<input type="submit" name="submit" class="ui-state-default" value="搜索" />
			<input type="button" style="display:<c:out value="${menuMap['centerAccount_out']}" />" id="centerAccount_out" class="ui-state-default" value="中心账户出账" />
		</div>
			<table class="ui-widget ui-widget-content" style="margin: 0;">
				<thead>
					<tr class="ui-widget-header ">

						<th>融资方</th>
						<th>发生对象</th>
						<th>项目简称</th>
						<th>融资项目ID</th>
						<th>一次性收入</th>
						<th>还款收入</th>
						<th>收入小计</th>
						<th>核算状态</th>
						<th>担保机构</th>
						<th>查看明细</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.result}" var="calr">
						<tr>

							
							<td>
							<script>document.write(name("${calr.realname}"));</script>
							<c:set value="${allcount + 1}" var="allcount" />
							</td>
							<td>${calr.username }</td>
							<td><a
								href="/back/financingBaseAction!detail?id=${calr.fbase_id}"
								class="tooltip" title="${calr.shortname}"> <c:choose>
										<c:when test="${fn:length(calr.shortname) > 10}">
											<c:out value="${fn:substring(calr.shortname,0,10)}..." />
										</c:when>
										<c:otherwise>
											<c:out value="${calr.shortname}" />
										</c:otherwise>
									</c:choose>
							</a></td>
							<td>${calr.fcode}</td>
							<td>
								<c:if test ="${calr.one_off_income>0}"><span class="yangshi" ><fmt:formatNumber value='${calr.one_off_income}'
										pattern="#,##0.00" />
										</span></c:if>
								<c:if test ="${calr.one_off_income<=0}"><fmt:formatNumber value='${calr.one_off_income}'
										pattern="#,##0.00" />
										</c:if>
								<c:set value="${allmoney + calr.one_off_income}"
										var="allmoney" />
							</td>
							<td>
								<c:if test ="${calr.sustainable_income>0}"><span class="yangshi" ><fmt:formatNumber value='${calr.sustainable_income}'
										pattern="#,##0.00" />
										</span></c:if>
								<c:if test ="${calr.sustainable_income<=0}"><fmt:formatNumber value='${calr.sustainable_income}'
										pattern="#,##0.00" />
										</c:if>
								<c:set value="${allmoney2 + calr.sustainable_income}"
										var="allmoney2" />
							</td>
							<td>
							<c:if test ="${calr.total_income>0}"><span class="yangshi" ">
								<fmt:formatNumber value='${calr.total_income}' pattern="#,##0.00" />
							</span></c:if>
								<c:if test ="${calr.total_income<=0}">
									<fmt:formatNumber value='${calr.total_income}' pattern="#,##0.00" />
										</c:if>
								<c:set value="${allmoney3 + calr.total_income}"
										var="allmoney3" />
							</td>

							
							<td>${calr.calculated}</td>
							<td>
							<c:choose>
										<c:when test="${fn:length(calr.orgname) > 10}">
											<c:out value="${fn:substring(calr.orgname,0,10)}..." />
										</c:when>
										<c:otherwise>
											<c:out value="${calr.orgname}" />
										</c:otherwise>
									</c:choose>
							
							</td>
							<td>
							<a href="javascript:void(0)" 
							     onclick="window.location.href='/back/accountDeal/accountDealAction!centerAccount2_groupByFbase?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&fcode=${calr.fcode}&userName=${calr.username}'">查看明细</a>
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td>本页总计</td>
						<td></td>
						<td></td>
						<td></td>
					

						<td>
						<fmt:formatNumber value='${allmoney}'
										pattern="#,##0.00" /></td>
						<td>
						<fmt:formatNumber value='${allmoney2}'
										pattern="#,##0.00" />
						</td>
						<td>
						<fmt:formatNumber value='${allmoney3}'
										pattern="#,##0.00" />
						</td>
						<td>${allcount}笔</td>
						<td></td>
						<td></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						
					</tr>
				</tfoot>
			</table>
		</form>
	</div>





</body>
<script>
	setIframeHeight(100);
</script>