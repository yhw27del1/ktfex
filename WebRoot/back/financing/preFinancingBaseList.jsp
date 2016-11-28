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
function del(id){ 
  window.location.href = "/back/financingBaseAction!del?id="+id; 
}


$(document).ready(function(){
	$("#seachButton").click(function() {
		$("#form1").submit();
	}); 
	//setTitle2("融资项目信息"); //重新设置切换tab的标题
	$(".table_solid").tableStyleUI(); 
}); 
function stop(url){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("确认要删除现有废弃此融资项目吗？");
        dlgHelper.set_Msg("执行这个操作，此融资项目被废弃？");
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

</script>  
 
<body>
<form id="form1" action="/back/preFinancingBaseAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
      项目状态：<s:select list="#{99:'全部',2:'风控已确认',3:'风控驳回',4:'审核驳回',5:'废弃'}"  listKey="key" listValue="value" name="queryState"  headerKey="1" headerValue="待确认"/>
      项目编号:<input type="text" name="queryCode" value="${queryCode}"  style='width:90px;'/>项目简称:<input type="text" name="keyWord" value="${keyWord}"  style='width:90px;'/>
      担保方:<input type="text" name="queryOrg" value="${queryOrg}"  style='width:90px;' title='可以输入担保方编码或担保方名称'/>
      融资方:<input type="text" name="queryFinancier" value="${queryFinancier}"  style='width:90px;' title='可以输入融资方编码或融资方名称'/>  
    <button class="ui-state-default" id="seachButton">查找</button>
  </div>
  <div style="float: right; margin-right: 20px;">
    <button class="ui-state-default" onclick="toURL('/back/preFinancingBaseAction!ui');return false;"    style="display:<c:out value="${menuMap['preFinancingBase_ui']}" />">融资项目申请</button>
  </div>		
</div>

 <div class="dataList ui-widget">

	<table class="datagrid-htable">
		<thead>
			<tr class="ui-widget-header "> 
				<th>项目编号</th>
				<th>项目简称</th> 
				<th>担保方</th> 
				<th>融资方</th>
				<th>项目金额(￥)</th>  
				<th>申请日期</th>
				<th>期限(还款方式)</th> 
				<th>年利率</th> 
				<th>担保方式</th>
				<th>状态</th>  
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/preFinancingBaseAction!detail?id=${entry.id}&directUrl=/back/preFinancingBaseAction!list');return false;"  class="tooltip" title="${entry.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.shortName) > 20}">
						<c:out value="${fn:substring(entry.shortName,0,16)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				<td>${entry.createBy.org.shortName}<br/>${entry.createBy.org.showCoding}</td>
				 <td>
				<c:if test="${(entry.financier)!= null}">
					<script>document.write(name("${entry.financier.eName}"));</script>
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxAmount}'   type="currency" currencySymbol=""/></td> 
				<td><fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd"/></td>
				<!-- <td><fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd"/></td> -->
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
				<td> 
			  	    <c:if test="${entry.state==1}"><span style="color:red;">待确认</span></c:if>  
					<c:if test="${entry.state==2}"><span style="color:#4169E1;">风控已确认 </span></c:if>
					<c:if test="${entry.state==3}"><span style="color:green;">风控驳回</span></c:if>
					<c:if test="${entry.state==4}"><span style="color:green;">审核驳回</span></c:if>
					<c:if test="${entry.state==5}"><span style="color:red;">废弃</span></c:if>
				</td>  
				<td>${entry.opeNote}</td>
				<td> 
				<c:if test="${(entry.state!='5')}">
					<c:if test="${(entry.state=='0')||(entry.state=='1')||(entry.state=='3')||(entry.state=='4')}">
						 <button onclick="toURL('/back/preFinancingBaseAction!xyUi?id=${entry.id}');return false;" class="ui-state-default"     style="display:<c:out value="${menuMap['preFinancingBase_xyFinish']}" />">风控确认</button>
		 			</c:if> 
		 			 <c:if test="${(entry.state=='0')||(entry.state=='1')||(entry.state=='3')||(entry.state=='4')}">
						 <button onclick="toURL('/back/preFinancingBaseAction!ui?id=${entry.id}');return false;" class="ui-state-default"    style="display:<c:out value="${menuMap['preFinancingBase_edit']}" />">修改</button>
		 			</c:if>     
	 			</c:if>
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