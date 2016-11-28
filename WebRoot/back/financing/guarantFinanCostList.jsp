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
});
$(document).ready(function(){
    $(".table_solid").tableStyleUI(); 
 
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css("display","none"); 
}); 
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<body>
<form id="form1" action="/back/guaranteeAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<input type="hidden" name="id" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
            关键字<input type="text" name="keyWord" value="${keyWord}"/>签约日期:<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate" style="padding:3px;width:100px"/>到<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate" style="padding:3px;width:100px"/>
 	<button class="ui-state-default" id="seachButton">查找</button>(关键字中可以是项目简称、编号、担保公司查询)
  </div> 	
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>编号</th>
				<th>项目简称</th>
				<th>签约时间</th>
				<th>担保公司</th>
				<th>融资方</th>
				<th>担保费(￥)</th>   
				<th>核算日期</th> 
				<th>费用明细</th> 
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.financingBase.code}</td>
				<td> <a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.financingBase.id}&directUrl=/back/guaranteeAction!list');return false;"  class="tooltip" title="${entry.financingBase.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.financingBase.shortName) > 10}">
						<c:out value="${fn:substring(entry.financingBase.shortName,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.financingBase.shortName}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				<td><fmt:formatDate value="${entry.financingBase.qianyueDate}" pattern="yyyy-MM-dd"/></td>
				<td>${entry.financingBase.guarantee.eName}</td> 
				<td>
				<c:if test="${(entry.financier)!= null}">
					 <script>document.write(name("${entry.financier.eName}"));</script>
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if>
				</td>
				<td>
					<c:if test="${(entry.dbf)>0}">
						<fmt:formatNumber value='${entry.dbf}' type="currency" currencySymbol=""/>
					</c:if>
					 <c:if test="${(entry.fee3)>0}">
						<fmt:formatNumber value='${entry.fee3}' type="currency" currencySymbol=""/>
					</c:if>	 
				</td> 
				<td><fmt:formatDate value="${entry.auditDate}" pattern="yyyy-MM-dd"/></td>  
			     <td><a href="/back/guaranteeDetailAction!list?id=1&&keyWord=${entry.financingBase.code}&&startDate=2012-01-01" style='color:red;'>查看明细</a></td> 
			</tr>
		</c:forEach>
		<tr> 
				<td colspan="8" align="center"> 
					总合计：<FONT color="red"><c:if test="${(dbfSum)>0}">
						<fmt:formatNumber value='${dbfSum}' type="currency" currencySymbol=""/>
					</c:if>
					<c:if test="${(dbfSumXyd)>0}">
						<fmt:formatNumber value='${dbfSumXyd}' type="currency" currencySymbol=""/>
					</c:if>	  </FONT>
					<!-- &nbsp;&nbsp;&nbsp;&nbsp;当前页合计：<FONT color="red"><c:if test="${(dbfPageSum)>0}">
						<fmt:formatNumber value='${dbfPageSum}' type="currency" currencySymbol=""/>
					</c:if>
					<c:if test="${(dbfPageSumXyd)>0}">
						<fmt:formatNumber value='${dbfPageSumXyd}' type="currency" currencySymbol=""/>
					</c:if>	 </FONT> -->    
			    </td>  
			</tr>
		</tbody>
		<tbody>
			<tr>
				<td colspan="8">
					<jsp:include page="/common/page.jsp"></jsp:include>
				</td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 