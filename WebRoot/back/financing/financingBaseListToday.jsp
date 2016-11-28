<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
function del(id){ 
  window.location.href = "/back/financingBaseAction!del?id="+id; 
}
$(document).ready(function(){
    $(".table_solid").tableStyleUI(); 
	$("#seachButton").click(function() {
		$("#form1").submit();
	}); 
	//setTitle2("融资项目信息"); //重新设置切换tab的标题
});
function finish(url){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("您要确认此融资项目吗？");
        dlgHelper.set_Msg("执行这个操作，融资过程结束了，你确认要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                    window.location.href = url;
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 

}


function showlogs(id){
	showModalDialog('/back/financingBaseAction!logs?_id='+id,'','dialogWidth:700px;dialogHeight:500px;center:yes;help:no;resizable:no;status:no'); 
}
</script>  
 
<body>
<form id="form1" action="/back/financingBaseAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>编号</th>
				<th>项目简称</th> 
                <th align="center">担保方</th>
				<th align="center">融资方</th>
				<th>融资额</th>
                <th>期限(还款方式)</th> 
                <th>年利率</th>
                <th>担保方式</th> 
				<!-- <th>开始日期</th> -->
				<th>投标截止</th>
				<th>状态</th>
				<th>进度</th>
				<th width="15%">操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!today_for_order');return false;"  class="tooltip" title="${entry.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.shortName) > 10}">
						<c:out value="${fn:substring(entry.shortName,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				</a></td>
                <td>${entry.createBy.org.shortName}<br/>${entry.createBy.org.showCoding}</td>
				 <td>
				<c:if test="${(entry.financier)!= null}">
					${entry.financier.eName}
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if>
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
				<!--<td><fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd"/></td> -->
				<td><fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd"/></td>
				<td> 
				     ${entry.stateName} 
			  	  <!--  <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='1.5'}"><span style="color:green;">已经发布信息</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中 </span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已确认 </span></c:if>	
					<c:if test="${entry.state=='6'}"><span style="color:green;">已确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约 </span></c:if>-->
			  	</td> 
				<td> 
					  <span style="color:#4169E1;"><fmt:formatNumber value='${(entry.currenyAmount/entry.maxAmount)*100}'   type="currency" currencySymbol=""/>%</span>
				</td> 
				<td> 
					<c:if test="${(entry.state=='0')||(entry.state=='1')||(entry.state=='1.5')}">
					      <button onclick="toURL('/back/financingBaseAction!guadanUI?id=${entry.id}&actionUrl=today_for_order');return false;" class="ui-state-default" >挂单</button>
					      <button onclick="toURL('/back/financingBaseAction!fabuUI?id=${entry.id}&actionUrl=today_for_order');return false;" class="ui-state-default" >发布信息</button>
	 				</c:if>  
	 				<!-- 
	 				<c:if test="${entry.currenyAmount==0}">
					      <button onclick="toURL('/back/financingBaseAction!ui?id=${entry.id}');return false;" class="ui-state-default" title="重发融资项目">重发</button>
	 				</c:if>  
	 				 -->
	 				<c:if test="${entry.showOk}"> 
					      <button onclick="finish('/back/financingBaseAction!finish?id=${entry.id}');return false;" class="ui-state-default" title="融资确认">确认</button>
					</c:if>
					<a onclick="showlogs('${entry.id}');return false;" href="javascript:void(0)" style="float:right">查看日志</button>  
				</td> 
			</tr>
		</c:forEach>
				</tbody>
		<tbody>
			<tr>
				<td colspan="10">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
<%@ include file="/common/messageTip.jsp" %>
</body> 