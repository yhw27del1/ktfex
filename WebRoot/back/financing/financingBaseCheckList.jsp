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
function showlogs(id){
	showModalDialog('/back/financingBaseAction!logs?_id='+id,'','dialogWidth:700px;dialogHeight:500px;center:yes;help:no;resizable:no;status:no'); 
}
$(document).ready(function(){
    $(".table_solid").tableStyleUI(); 
	$("#seachButton").click(function() {
		$("#form1").submit();
	}); 
	//setTitle2("融资项目信息"); //重新设置切换tab的标题
	$(".table_solid").tableStyleUI(); 
}); 
</script>  
 
<body>
<form id="form1" action="/back/financingBaseAction!checkList" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
    <input type="text" name="queryCode" value="${queryCode}"  placeholder="项目编号"/>&nbsp;&nbsp;<input type="text" name="keyWord" value="${keyWord}"  placeholder="项目简称"/>
    <button class="ui-state-default" id="seachButton">查找</button>
  </div> 
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>项目编号</th>
				<th>项目简称</th> 
				<th>担保方</th> 
				<th>融资方</th>
				<th>总融资额(￥)</th> 
				<th>期限(还款方式)</th> 
				<th>年利率</th> 
				<th>担保方式</th> 
				<th>申请日期</th>
				<th>状态</th>
				<th>进度</th>
				<th width="8%">操作</th>
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td title="${entry.shortName}"><!-- <a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!checkList');return false;"  class="tooltip" title="${entry.shortName}"> -->
				<c:choose>
					<c:when test="${fn:length(entry.shortName) > 10}">
						<c:out value="${fn:substring(entry.shortName,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				<!-- </a> --></td>
				
                <td>${entry.createBy.org.shortName}<br/>${entry.createBy.org.showCoding}</td>
				 <td>
				 <a href="/back/member/memberBaseAction!memberDetails?id=${entry.financier.id}" title='${entry.financier.eName}'>
				<c:if test="${(entry.financier)!= null}">
					<script>document.write(name("${entry.financier.eName}"));</script>
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if>
				</a>
				</td>
				<td><fmt:formatNumber value='${entry.maxAmount}'   type="currency" currencySymbol=""/></td> 
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
				<td>${entry.fxbzStateName}</td> 
				<td><fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd"/></td> 
				<!--<td><fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd"/></td>-->
				<td> 
			  	    ${entry.stateName} 
				</td> 
				<td> 
					  <span style="color:#4169E1;"><fmt:formatNumber value='${(entry.currenyAmount/entry.maxAmount)*100}'   type="currency" currencySymbol=""/>%</span>
				</td> 
				<td> 
					<c:if test="${entry.state=='0'}">
					      <button onclick="toURL('/back/financingBaseAction!checkUI?id=${entry.id}');return false;" class="ui-state-default"   style="display:<c:out value="${menuMap['financingBase_check']}"/>" >审核</button>
	 				</c:if>  
	 				<a onclick="showlogs('${entry.id}');return false;" href="javascript:void(0)" style="float:right">日志</button>
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