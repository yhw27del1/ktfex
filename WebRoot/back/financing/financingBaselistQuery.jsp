
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<style>
<!--
body{
 overflow:auto !important;
}
td,th{white-space: nowrap;}
-->
</style>
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
});
 
</script>  
 
<body>
<form id="form1" action="/back/financingBaseAction!listQuery" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:35px">
  <table style="color:#E69700;font-weight:bold;font-size:13px;">
  		<tr>
  			<td>项目编号</td><td><s:select list="#{'1':'包含','2':'不包含'}" listKey="key" listValue="value"  cssStyle="padding:3px;" name="containstr" ></s:select><input type="text" name="queryCode" value="${queryCode}" style="padding:3px;width:100px" title='如果多个值,中间用半角逗号隔开,如：A,C,D'/></td>
            <td><s:select list="#{'2':'投标中','4':'已满标','5':'已确认','7':'已签约','8':'已撤单'}" listKey="key" listValue="value" headerKey="" headerValue="状态" cssStyle="padding:3px;" name="states"  value="#request.states"></s:select></td></td>
  			<td>开始日期 从</td><td><input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate" style="padding:3px;width:100px"/></td>
  			<td>到</td><td><input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate" style="padding:3px;width:100px"/></td>
  			<td>担保机构</td><td><input type="text" value="${queryByOrgCode}" style="width:90px" name="queryByOrgCode" placeholder="担保机构"/></td>
  			<td>关键字&nbsp;<input type="text" name="qkeyWord" value="${qkeyWord}" title="关键字中可以输入融资方名称,融资方用户名或担保公司名称、担保公司用户名模糊查询"/></td>
  			<td><button class="ui-state-default" id="seachButton" style="width:60px;height:25px;">查找</button></td></td>
  		</tr>
  	</table>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th width="80px;">编号</th>
				<th>项目简称</th> 
                <th align="center">担保方</th>
				<th>融资方</th>
				<th>总融资额<br>已融资额<br>可融资额</th>
				<th align="center">期限<br/>还款方式</th>
				<th>年利率</th>
				<th>投标<br/>人数</th> 
				<th>开始日期<br/>投标截止</th>
				<th>状态<br/>进度</th>
				<th>所属地域</th> 
				<th>行业</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!listQuery');return false;"  class="tooltip" title="${entry.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.shortName) > 8}">
						<c:out value="${fn:substring(entry.shortName,0,8)}." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				<td>${entry.createBy.org.shortName}<br/>${entry.createBy.org.showCoding}</td>
				 <td>
				 <a href="/back/member/memberBaseAction!memberDetails?id=${entry.financier.id}">
				<c:if test="${(entry.financier)!= null}">
					${entry.financier.eName}
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if></a>
				</td>
				<td><fmt:formatNumber value='${entry.maxAmount}'   type="currency" currencySymbol=""/>
				<br><fmt:formatNumber value='${entry.currenyAmount}'   type="currency" currencySymbol=""/><br>
				<c:if test="${(entry.curCanInvest)== 0}">
				    <fmt:formatNumber value='${entry.curCanInvest}' type="currency" currencySymbol=""/>
				</c:if>
				<c:if test="${(entry.curCanInvest)>0}">
                    <span style="color: red"><fmt:formatNumber value='${entry.curCanInvest}' type="currency" currencySymbol=""/>
                </c:if>
				</td>
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
				<td>${entry.rate}%</td>
				<td>${entry.haveInvestNum}</td>
				<td><fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd"/><br/><fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd"/></td>
				<td> 
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='1.5'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中 </span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已确认 </span></c:if>	
					<c:if test="${entry.state=='6'}"><span style="color:green;">已确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约 </span></c:if>
					<c:if test="${entry.state=='8'}"><span style="color:red;">已撤单 </span></c:if>	
					<br/>
		           <span style="color:#4169E1;"><fmt:formatNumber value='${(entry.currenyAmount/entry.maxAmount)*100}'   type="currency" currencySymbol=""/>%</span>
					
				</td> 
				<td> 
				 ${entry.financier.provinceName}${entry.financier.cityName}
				</td> 
				 <td>
				   <c:if test="${(entry.hyType)!= null}">
				      ${entry.hyTypeShow}
                   </c:if> 
				 </td>
			</tr>
		</c:forEach>
				<tr > 
				<td align="center">小计</td>
				<td align="center"></td> 
				<td align="center"></td>
				<td align="center"></td>
				<td align="center">
				<fmt:formatNumber value='${totalData[0]}'   type="currency" currencySymbol=""/><br>
				<fmt:formatNumber value='${totalData[1]}'   type="currency" currencySymbol=""/><br>
				<fmt:formatNumber value='${totalData[2]}'   type="currency" currencySymbol=""/>
				</td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td> 
				<td align="center"></td>
				<td align="center"></td>  
				<td align="center"></td>   
				<td align="center"></td> 
			</tr>
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