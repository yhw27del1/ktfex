<%--
2012-08-21 修改“已取消”为“已撤单”
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
} 
$(document).ready(function(){
    $(".table_solid").tableStyleUI(); 
	$("#seachButton").click(function() {
		$("#form1").submit();
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
	//setTitle2("融资项目查询"); //重新设置切换tab的标题
	$("#print_button").click(function(){
		window.open("/back/financingBaseAction!print_end_list?keyWord=${keyWord}&startDate=<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>&org_code=${org_code}");
	});
	$("#export_button").click(function(){
		window.open("/back/financingBaseAction!export_end_list?keyWord=${keyWord}&startDate=<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>&org_code=${org_code}");
	});
});
 
</script>  
 <style>
<!--
#startDate,#endDate{
	width:80px;
}
-->
</style>
<body>
<form id="form1" action="/back/financingBaseAction!end_list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:35px">
  <table style="color:#E69700;font-weight:bold;font-size:13px;">
				<tr>
					<td>
						关键字<input type="text" name="keyWord" value="${keyWord}" style="width:80px;"/>
					</td>
					<td>
						机构编码<input type="text" name="org_code" value="${org_code}" style="width:80px;"/>
					</td>
					<td>
						结束日期 从
					</td>
					<td>
						<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate" style="width:80px;"/>
					</td>
					<td>
						到
					</td>
					<td>
						<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate" style="width:80px;"/>
					</td>
					<td>
						<button class="ui-state-default" id="seachButton" style="width: 60px; height: 25px;">
							查询
						</button>
					</td>
					<c:if test="${org_code!=null && fn:length(org_code) > 5}">
						<td>
							<a href="javascript:;" id="print_button">打印</a>
						</td>
						<td>
							<a href="javascript:;" id="export_button">导出</a>
						</td>
					</c:if>
				</tr>
			</table>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
			    <th>序号</th>
			    <th>结清日期</th>
			    <th>签约日期</th> 
				<th>项目编号</th>
				<th>担保方</th>
				<!-- <th>项目简称</th>  -->
				<th>融资户名</th>
				<th>融资额(￥)</th>
				<th>投标人数</th> 
				<th>期限</th>				
				<th></th> 
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:if test="${fn:length(pageView.records)==0}"><tr><td colspan="12">没有数据</td></tr></c:if>
		<c:forEach items="${pageView.records}" var="entry" varStatus="list_s">
			<tr>
			    <td> 
			  	    ${list_s.count }
				</td>
			    <td> 
			  	    <fmt:formatDate value="${entry.terminal_date}" pattern="yyyy-MM-dd"/>
				</td>
				<td> 
			  	    <fmt:formatDate value="${entry.qianyueDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>${entry.code}</td>
				<td>${entry.createBy.org.shortName}<br/>${entry.createBy.org.showCoding}</td>
				<!--  <td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!listQuery');return false;"  class="tooltip" title="${entry.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.shortName) > 10}">
						<c:out value="${fn:substring(entry.shortName,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				</a></td>-->
				 <td>
				<c:if test="${(entry.financier)!= null}">
					<script>document.write(name("${entry.financier.eName}"));</script>
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.currenyAmount}'   type="currency" currencySymbol=""/></td>
				<td>${entry.haveInvestNum}</td>
				<td title='${entry.businessType.returnPatternTerm}'>
						<c:choose>
						    <c:when test="${entry.interestDay>0}">
								${entry.interestDay}天<br/>
							</c:when>
							<c:otherwise>
								${entry.businessType.term}个月<br/>
							</c:otherwise>
						</c:choose>
				    	${entry.businessType.returnPattern}
					</td>
				
				<td> 
					  <a href="/back/paymentRecord/paymentRecordAction!list_paymentRecord?id=${entry.id}" target="_blank">还款明细</a>
				</td> 
				 
			</tr>
		</c:forEach>
				</tbody>
		<tbody>
			<tr>
				<td colspan="12">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
<%@ include file="/common/messageTip.jsp" %>
</body> 