<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
$(document).ready(function(){ 
    $(".table_solid").tableStyleUI(); 
	$("#seachButton").click(function() {
		$("#form1").submit();
	});  
	//setTitle2("我的融资费用核算"); //重新设置切换tab的标题
});
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>

<body>
<form id="form1" action="/back/financingCostAction!listFeeForPerson" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
            关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default" id="seachButton">查找</button>(关键字中可以输入项目简称和编号查询)
  </div> 	
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>项目编号</th>
				<th>项目简称</th>
				<th>融资额(￥)</th>
                <th>扣除费用</th> 
				<th>融资结余(￥)</th>  
				<th>创建日期</th>
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.financingBase.code}</td>
				<td> <a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.financingBase.id}&directUrl=/back/financingCostAction!listFeeForPerson');return false;"  class="tooltip" title="${entry.financingBase.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.financingBase.shortName) > 10}">
						<c:out value="${fn:substring(entry.financingBase.shortName,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.financingBase.shortName}" />
					</c:otherwise>
				</c:choose>
				</a></td> 
				<td><fmt:formatNumber value='${entry.financingBase.currenyAmount}'  type="currency" currencySymbol=""/></td>
				<td nowrap>
					<c:choose>
						<c:when test="${fn:startsWith(entry.financingBase.code, 'X')}">
					           风险管理费${entry.fee1_tariff==0?"":"[按月]"}[${entry.fee1_bl}%]:<fmt:formatNumber value='${entry.fee1}' pattern="#,###,##0.00" /><br />
					            融资服务费${entry.fee2_tariff==0?"":"[按月]"}[${entry.fee2_bl}%]:<fmt:formatNumber value='${entry.fee2}' pattern="#,##0.00" /><br />
					           还款保证金:<fmt:formatNumber value='${entry.bzj}' pattern="#,##0.00"/><br />  
					          
					           担保费${entry.fee3_tariff==0?"":"[按月]"}[${entry.fee3_bl}%]:<fmt:formatNumber value='${entry.fee3}' pattern="#,##0.00" /><br />
						</c:when>
						<c:otherwise>
                     		风险管理费${entry.fxglf_tariff==0?"":"[按月]"}[${entry.fxglf_bl}%]:<fmt:formatNumber value='${entry.fxglf}' pattern="#,##0.00" /><br />
                     		融资服务费${entry.rzfwf_tariff==0?"":"[按月]"}[${entry.rzfwf_bl}%]:<fmt:formatNumber value='${entry.rzfwf}' pattern="#,##0.00" /><br /> 
                     		还款保证金:<fmt:formatNumber value='${entry.bzj}' pattern="#,##0.00"/><br />  	
	            			担保费${entry.dbf_tariff==0?"":"[按月]"}[${entry.dbf_bl}%]:<fmt:formatNumber value='${entry.dbf}' pattern="#,##0.00" /><br />
						</c:otherwise>
					</c:choose> 
					席位费:<fmt:formatNumber value='${entry.fee7}' pattern="#,##0.00"/><br />
				   <!--  信用管理费:<fmt:formatNumber value='${entry.fee10}' pattern="#,##0.00"/><br />
				    其他费用:<fmt:formatNumber value='${entry.other}' pattern="#,##0.00"/>  --> 
				</td> 	 
                <td><fmt:formatNumber value='${entry.realAmount}'  type="currency" currencySymbol=""/></td> 
				<td><fmt:formatDate value="${entry.createDate}" pattern="yyyy-MM-dd"/></td>   
			</tr>
		</c:forEach>
		</tbody>
		<tbody>
			<tr>
				<td colspan="6">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 