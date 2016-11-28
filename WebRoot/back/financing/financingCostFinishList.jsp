<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
function doprint(){
	$("#myToolBar").hide();
	print();
	$("#myToolBar").show();
}
$(document).ready(function(){ 
	$(".table_solid").tableStyleUI(); 
	$("#seachButton").click(function() {
		$("#form1").submit();
	});  
	$("input[name='startDate'],input[name='endDate']").datepicker({
        dateFormat: "yy-mm-dd"
    });
});
</script>
<style>
	.w1{
		width:80px;
	}
	#ui-datepicker-div{
		display:none;
	}
</style>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>

<body>
<form id="form1" action="/back/financingCostAction!finishList" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float:left;position:relative;width:100%">
  		关键字&nbsp;<input type="text" class="w1" name="keyWord" value="${keyWord}"/>&nbsp;&nbsp;&nbsp;融资方<input type="text" class="w1" name="queryEname" value="${queryEname}"/>
		&nbsp;签约时间:<input name="startDate" class="w1" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>"/> 至
		<input name="endDate" class="w1" value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>"/>
		<button class="ui-state-default" id="seachButton">查找</button>
		<input type="button" value="打印" onclick="doprint()" style="position:absolute;right:10px;"/>
  </div> 	
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th rowspan="2">编号</th>
				<th rowspan="2">项目简称</th>
				<th rowspan="2">融资额</th>
				<th rowspan="2">银行清分</th>
				<th colspan="4" style="text-align:center;">融资成功一次性扣费</th> 
				<th rowspan="2">融资结余</th>  
				<th rowspan="2">签约时间</th>
				<th rowspan="2">划款时间</th>
			</tr>
			<tr class="ui-widget-header ">
				<th>风险管理费</th>
				<th>担保费</th>
				<th>融资服务费</th>
				<th>保证金</th>
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${list}" var="entry">
			<tr> 
				<td>${entry.financingBase.code}</td>
				<td>
					<a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.financingBase.id}&directUrl=/back/financingCostAction!finishList');return false;"  class="tooltip" title="${entry.financingBase.shortName}">
						<c:choose>
							<c:when test="${fn:length(entry.financingBase.shortName) > 10}">
								<c:out value="${fn:substring(entry.financingBase.shortName,0,10)}..." />
							</c:when>
							<c:otherwise>
								<c:out value="${entry.financingBase.shortName}" />
							</c:otherwise>
						</c:choose>
					</a>
				</td> 
				<td>
					<fmt:formatNumber value='${entry.financingBase.currenyAmount}' type="currency" currencySymbol=""/>
					<c:set value="${entry.financingBase.currenyAmount + amount}" var="amount"/>
				</td>
				<td>
					<c:forEach items="${entry.bankmap}" var="bankmap">
					
						${bankmap.key[0]}<c:if test="${bankmap.key[1]=='2'}">(已签)</c:if><c:if test="${bankmap.key[1]=='0'||bankmap.key[1]=='1'||bankmap.key[1]==null||bankmap.key[1]==''}">(未签)</c:if>:<fmt:formatNumber value='${bankmap.value}' type="currency" currencySymbol=""/><br />
					</c:forEach>
				</td>
				<td>
					
						<c:choose>
							<c:when test="${fn:startsWith(entry.financingBase.code, 'X')}">
								<c:if test="${entry.fee1_tariff == 0 }">
									<fmt:formatNumber value='${entry.fee1}' type="currency" currencySymbol=""/>
									<c:set value="${entry.fee1 + fxglf}" var="fxglf"/>
								</c:if>
								<c:if test="${entry.fee1_tariff != 0 }">
									0.00
									<c:set value="${0 + fxglf}" var="fxglf"/>
								</c:if>
							</c:when> 
							<c:otherwise>
								<c:if test="${entry.fxglf_tariff == 0 }">
									<fmt:formatNumber value='${entry.fxglf}' type="currency" currencySymbol=""/>
									<c:set value="${entry.fxglf + fxglf}" var="fxglf"/>
								</c:if>
								<c:if test="${entry.fxglf_tariff != 0 }">
									0.00
									<c:set value="${0 + fxglf}" var="fxglf"/>
								</c:if>
							</c:otherwise>
						</c:choose>   
					
				</td>
				<td>
					<fmt:formatNumber value='${entry.dbf}' type="currency" currencySymbol=""/>
					<c:set value="${entry.dbf + dbf}" var="dbf"/>
				</td>
				<td>
					<c:choose>
						<c:when test="${fn:startsWith(entry.financingBase.code, 'X')}">
							<c:if test="${entry.fee2_tariff == 0 }">
								<fmt:formatNumber value='${entry.fee2}' type="currency" currencySymbol=""/>
								<c:set value="${entry.fee2 + rzfwf}" var="rzfwf"/>
							</c:if>
							<c:if test="${entry.fee2_tariff != 0 }">
								0.00
								<c:set value="${0 + rzfwf}" var="rzfwf"/>
							</c:if>
						</c:when> 
						<c:otherwise>
							<c:if test="${entry.rzfwf_tariff == 0 }">
								<fmt:formatNumber value='${entry.rzfwf}' type="currency" currencySymbol=""/>
								<c:set value="${entry.rzfwf + rzfwf}" var="rzfwf"/>
							</c:if>
							<c:if test="${entry.rzfwf_tariff != 0 }">
								0.00
								<c:set value="${0 + rzfwf}" var="rzfwf"/>
							</c:if>
						</c:otherwise>
					</c:choose>  
					
				</td>
				<td>
					<fmt:formatNumber value='${entry.bzj}' type="currency" currencySymbol=""/>
					<c:set value="${entry.bzj + bzj}" var="bzj"/>
				</td>
                <td>
                	<fmt:formatNumber value='${entry.realAmount}' type="currency" currencySymbol=""/>
                	<c:set value="${entry.realAmount + realAmount}" var="realAmount"/>
                </td> 
				<td><fmt:formatDate value="${entry.financingBase.qianyueDate}" pattern="yyyy-MM-dd"/></td>   
				<td><fmt:formatDate value="${entry.hkDate}" pattern="yyyy-MM-dd"/></td>   
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="2" rowspan="2">合计</th>
				<th><fmt:formatNumber value="${amount}" pattern="#,##0.00"/></th>
				<th></th>
				<th><fmt:formatNumber value="${fxglf}" pattern="#,##0.00"/></th>
				<th><fmt:formatNumber value="${dbf}" pattern="#,##0.00"/></th>
				<th><fmt:formatNumber value="${rzfwf}" pattern="#,##0.00"/></th>
				<th><fmt:formatNumber value="${bzj}" pattern="#,##0.00"/></th>
				<th><fmt:formatNumber value="${realAmount}" pattern="#,##0.00"/></th>
				<th></th>
				<th></th>
			</tr>
			<tr>
				<th></th>
				<th></th>
				<th colspan="4"><fmt:formatNumber value="${fxglf+dbf+rzfwf+bzj}" pattern="#,##0.00"/></th>
				<th></th>
				<th></th>
				<th></th>
			</tr>
		</tfoot>
	</table> 
</div>
</form>
</body> 