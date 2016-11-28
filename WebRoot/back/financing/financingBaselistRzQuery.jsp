<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
} 
$(document).ready(function(){
    $(".table_solid").tableStyleUI(); 
    		//时间控件
		    $("#startDate").datepicker({
		       // showOn: 'button',
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
		        //showOn: 'button',
		        buttonImageOnly: false,
		        //changeMonth: true,
		        //changeYear: true,
		        numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"//,
		        //minDate: +1 
		    });
		    
		     $("#ui-datepicker-div").css({'display':'none'});
	$("#seachButton").click(function() {
		$("#form1").submit();
	}); 
	//setTitle2("融资项目查询"); //重新设置切换tab的标题
});
 
</script>  
 
<body>
<form id="form1" action="/back/financingBaseAction!listRzQuery" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
       借款项目编号&nbsp;&nbsp;<input type="text" name="queryCode" value="${queryCode}"/>&nbsp;&nbsp;&nbsp;项目简称&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
   日期段&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' pattern="yyyy-MM-dd"/>" id="startDate" readonly="readonly"/>&nbsp;:&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' pattern="yyyy-MM-dd"/>" id="endDate" readonly="readonly"/>
				 <button class="ui-state-default" id="seachButton">查找</button>
  </div>   
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>编号</th>
				<th>项目简称</th> 
				<th>融资方</th>
				<th>总融资额(￥)</th>
				<th>已融资额(￥)</th>
				<th>可融资额(￥)</th>
				<th>投标人数</th> 
				<th>开始日期</th>
				<th>投标截止</th>
				<th>状态</th>
				<th>进度</th> 
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!listRzQuery');return false;"  class="tooltip" title="${entry.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.shortName) > 10}">
						<c:out value="${fn:substring(entry.shortName,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				 <td>
				<c:if test="${(entry.financier)!= null}">
					${entry.financier.eName}
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxAmount}'   type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.currenyAmount}'   type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.curCanInvest}' type="currency" currencySymbol=""/></td>
				<td>${entry.haveInvestNum}</td>
				<td><fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd"/></td>
				<td> 
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中 </span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已确认 </span></c:if>	
					<c:if test="${entry.state=='6'}"><span style="color:green;">已确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约 </span></c:if>	
				</td> 
				<td> 
					  <span style="color:#4169E1;"><fmt:formatNumber value='${(entry.currenyAmount/entry.maxAmount)*100}'   type="currency" currencySymbol=""/>%</span>
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
<%@ include file="/common/messageTip.jsp" %>
</body> 