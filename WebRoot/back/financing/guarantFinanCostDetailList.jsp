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
<form id="form1" action="/back/guaranteeDetailAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<input type="hidden" name="id" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
            关键字<input type="text" name="keyWord" value="${keyWord}"/>签约日期:<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate" style="padding:3px;width:100px"/>到<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate" style="padding:3px;width:100px"/>
 	状态：<s:select list="#{0:'未还款',1:'正常还款',2:'提前还款',3:'逾期还款',4:'全部'}"  label="状态" listKey="key" listValue="value" name="state"/><button class="ui-state-default" id="seachButton">查找</button>(关键字中可以是项目简称、编号、担保公司查询)
  </div> 	
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<tbody>
			<tr>
				<td colspan="7">
					合计：<fmt:formatNumber value='${sum}' type="currency" currencySymbol=""/>(担保费：<fmt:formatNumber value='${dbfSum}' type="currency" currencySymbol=""/>,罚金：<fmt:formatNumber value='${dbfSumFj}' type="currency" currencySymbol=""/>)<br/> 
					<c:if test="${state=='4'}"> 
					      正常还款:<fmt:formatNumber value='${dbfSum1+dbfSumFj1}' type="currency" currencySymbol=""/>(担保费：<fmt:formatNumber value='${dbfSum1}' type="currency" currencySymbol=""/>,罚金：<fmt:formatNumber value='${dbfSumFj1}' type="currency" currencySymbol=""/>)<br/>
					      未还款:<fmt:formatNumber value='${dbfSum0+dbfSumFj0}' type="currency" currencySymbol=""/>(担保费：<fmt:formatNumber value='${dbfSum0}' type="currency" currencySymbol=""/>,罚金：<fmt:formatNumber value='${dbfSumFj0}' type="currency" currencySymbol=""/>)<br/>
					      提前还款:<fmt:formatNumber value='${dbfSum2+dbfSumFj2}' type="currency" currencySymbol=""/>(担保费：<fmt:formatNumber value='${dbfSum2}' type="currency" currencySymbol=""/>,罚金：<fmt:formatNumber value='${dbfSumFj2}' type="currency" currencySymbol=""/>)<br/>
					      逾期还款:<fmt:formatNumber value='${dbfSum3+dbfSumFj3}' type="currency" currencySymbol=""/>(担保费：<fmt:formatNumber value='${dbfSum3}' type="currency" currencySymbol=""/>,罚金：<fmt:formatNumber value='${dbfSumFj3}' type="currency" currencySymbol=""/>)<br/>
					</c:if> 
				</td>
			</tr>
		</tbody>
	</table>	
    <table class="ui-widget ui-widget-content">	 
		<thead>
			<tr class="ui-widget-header "> 
				<th>编号</th>
				<th>项目简称</th>
				<th>还款次数</th>
				<th>还款状态</th>
				<th>签约时间</th>
				<th>担保费(￥)</th>  
				<th>罚金(￥)</th>   
				<th>担保公司</th> 
				<th>应还日期</th>   
			</tr>
		</thead>
		<tbody  class="table_solid"> 
		<c:forEach items="${vrs}" var="entry">
					<tr>
						<td>
							${entry.financbasecode}
						</td>
						<td>
							<a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.financbaseid}&directUrl=/back/guaranteeDetailAction!list');return false;"  >${entry.fshortname}</a>
						</td>     
						<td>
							第<font color='red'>${entry.succession}</font>次还款
						</td>
						<td>
						<c:if test="${entry.state=='0'}"> 
							<span style="color: green;">未还款</span>
						 </c:if>
						<c:if test="${entry.state=='1'}"> 
							<span style="color: #4169E1;">正常还款</span>
						 </c:if>
						<c:if test="${entry.state=='2'}">  
							<span style="color: red">提前还款</span>
						 </c:if> 
						 <c:if test="${entry.state=='3'}">   
							<span style="color: red;">逾期</span>
						 </c:if>  
						</td>
						<td>
							<fmt:formatDate value='${entry.qianyuedate}' pattern="yyyy-MM-dd"/>
						</td> 
						<td>
							${entry.fee2}
						</td> 
						<td>
							${entry.fj2}
						</td>  
						<td>
							<script>document.write(name("${entry.dbhsname}"));</script>
						</td> 
						<td>
							<fmt:formatDate value='${entry.yhdate}' pattern="yyyy-MM-dd"/>
						</td>  
					</tr>
				</c:forEach>
		</tbody>

	</table> 
</div>
</form>
</body> 