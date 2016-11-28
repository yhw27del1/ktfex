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
	$("#seachButton").click(function() {
		$("#form1").submit();
	}); 
	//setTitle2("我的融资项目"); //重新设置切换tab的标题
	$(".table_solid").tableStyleUI(); 
	
	$(".sh").css({'cursor':'pointer'}).click(function(){
		var id = $(this).attr("id");
		if($("#h"+id).is(":hidden")){
			$(this).attr("src","/Static/js/tree/tabletree/images/minus.gif");
			$("#h"+id).show();
		}else{
			$(this).attr("src","/Static/js/tree/tabletree/images/plus.gif");
			$("#h"+id).hide();
		}
		
		iframe.height($("#mytable").height()+200);
	});
	
});
function contract(id){
	window.showModalDialog("/back/financingBaseAction!financingContract?id="+id, null, "dialogWidth:800px;dialogHeight:auto;status:no;help:yes;resizable:no;");
	$("input.ui-state-default").trigger('click');
}

</script>  
 
<body>
<form id="form1" action="/back/financingBaseAction!listForPerson" method="post"> 
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
				<th>编号</th>
				<th>项目简称</th> 
				<th>总融资额(￥)</th>
				<th>已融资额(￥)</th>
				<th>可融资额(￥)</th> 
				<th>投标人数</th>
				<!-- <th>开始日期</th> -->
				<th>截止日期</th>
				<th>进度</th> 
			</tr>  
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!listForPerson');return false;"  class="tooltip" title="${entry.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.shortName) > 10}">
						<c:out value="${fn:substring(entry.shortName,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				<td><fmt:formatNumber value='${entry.maxAmount}'  type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.currenyAmount}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.curCanInvest}' type="currency" currencySymbol=""/></td>
				<td>${entry.haveInvestNum}</td>
				<!--<td><fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd"/></td> -->
				<td><fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd"/></td>
				<td> 
				<span style="color:#4169E1;"><fmt:formatNumber value='${(entry.currenyAmount/entry.maxAmount)*100}'   type="currency" currencySymbol=""/>%</span>
				<c:if test="${entry.state=='7'}"><a onclick="contract('${entry.id}')" href="javascript:void(0);" id="">查看合同 </a></c:if>
				<!--  
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中 </span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已完成 </span></c:if>	 -->
				</td> 
 
			</tr>
			
		</c:forEach>
				</tbody>
		<tbody>
			<tr>
				<td colspan="9">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 