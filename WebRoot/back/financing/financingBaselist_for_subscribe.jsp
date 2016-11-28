
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<link rel="stylesheet" href="/Static/css/metro-bootstrap.css" type="text/css" />
<style>
<!--
body{
 overflow:auto !important;
}
td,th{white-space: nowrap;}
.table{font-size:13px;}
.toolbar_table select,.toolbar_table input{height:30px;}
#ui-datepicker-div{font-size:13px;}
.table-striped{background-color: #fff;margin-top: 5px;}
.pagination a{width:24px;height:32px !important;padding:0 !important; line-height: 32px !important;}
#showRecord{width:60px}
-->
</style>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url;   
} 
$(document).ready(function(){
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
    
	//setTitle2("融资项目信息"); //重新设置切换tab的标题
});
function finish(url){  
        window.location.href = url;

}
/**
*取消融资项目
*/
function cancel(url,finacingName){
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("您是否真要撤销“"+finacingName+"”？");
        dlgHelper.set_Msg("这个操作将撤销“"+finacingName+"”，您确定要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                    $(this).dialog('close'); 
                    $.post(url,null,function(data,state){
                    	alert(data.message);
                    },'json');
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 
} 



	function showdialog(url,width){
		showModalDialog(url,'','dialogWidth:'+width+'px;dialogHeight:500px;center:yes;help:no;status:no'); 
	}
</script>  
 
<body>
<form id="form1" action="/back/financingBaseAction!for_subscribe" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
    <table class="toolbar_table">
  		<tr>  
  			<td>项目编号</td><td><s:select list="#{'1':'包含','2':'不包含'}" listKey="key" listValue="value"  cssClass="span1" cssStyle="width:80px;" id="containstr" name="containstr" ></s:select><input type="text" name="queryCode" value="${queryCode}" style="padding:3px;width:100px" title='如果多个值,中间用半角逗号隔开,如：A,C,D' placeholder="如果多个值,中间用半角逗号隔开,如：A,C,D"/></td>
            <td style="border-right: 1px solid black;"></td>
            <td style="padding-left:4px"><s:select list="#{'2':'投标中','4':'已满标','5':'已确认','7':'已签约','8':'已撤单'}" listKey="key" listValue="value" headerKey="" headerValue="融资项目状态" cssStyle="width:110px;" name="states"  value="#request.states"></s:select></td>
  			
  			<td style="border-right: 1px solid black;"></td>
  			<td style="padding-left:4px"><input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate" style="padding:3px;width:90px" placeholder="开始日期"/></td>
  			<td>到</td><td><input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate" style="padding:3px;width:90px" placeholder="截至日期"/></td>
			<td style="border-right: 1px solid black;"></td>
  			<td style="padding-left:4px"><input type="text" value="${queryByOrgCode}" style="width:90px" name="queryByOrgCode" placeholder="担保机构"/></td>
			<td style="border-right: 1px solid black;"></td>  			
  			<td style="padding-left:4px"><input id="qkeyWord" type="text" name="qkeyWord" value="${qkeyWord}" title="关键字中可以输入融资方名称,融资方用户名或担保公司名称、担保公司用户名模糊查询" style="width:100px" placeholder="关键字"/></td>
  			<td><span class="btn btn-primary" id="seachButton" style="height:20px;">查找</span></td>
  		</tr>
  	</table>


	<table class="table table-striped">
		<thead>
			<tr> 
				<th align="center">项目编号</th>
				<th align="center">项目简称</th> 
                <th align="center">担保方</th>
				<th align="center">融资方</th> 
		        <th align="center">总融资额<br/>可融资额<br/>已融资额</th>
			    <th>期限(还款方式)</th> 
			    <th>年利率</th>
		        <th align="center">投标<br/>人数</th>  
				<th align="center">投标截止</th>
				<th align="center">状态<br/>
				<th align="center">所属地域<br/>行业</th>  
				<th align="center" width="15%">操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!for_subscribe');return false;"  title="${entry.shortName}">
				<c:choose>
					<c:when test="${fn:length(entry.shortName) > 8}">
						<c:out value="${fn:substring(entry.shortName,0,8)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				</a>
				</td>
                <td>${entry.createBy.org.shortName}<br/>${entry.createBy.org.showCoding}</td>
				 <td>
				 <a href="/back/member/memberBaseAction!memberDetails?id=${entry.financier.id}" title='${entry.financier.eName}'>
				 <c:if test="${(entry.financier)!= null}">
					
				   <c:choose>
					<c:when test="${fn:length(entry.financier.eName) > 3}">
						<c:out value="${fn:substring(entry.financier.eName,0,2)}." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.financier.eName}" />
					</c:otherwise>				
				 </c:choose>
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if></a>
				</td> 
			  <td><fmt:formatNumber value='${entry.maxAmount}'   type="currency" currencySymbol=""/><br/>
			  <c:if test="${(entry.curCanInvest)>0}">
			      <span style="color: red"><fmt:formatNumber value='${entry.curCanInvest}' type="currency" currencySymbol=""/></span>
                       </c:if>
                       <c:if test="${(entry.curCanInvest)==0}">
                           <span style="color:#4169E1;"><fmt:formatNumber value='${entry.curCanInvest}' type="currency" currencySymbol=""/></span>
                       </c:if><br/><fmt:formatNumber value='${entry.currenyAmount}'   type="currency" currencySymbol=""/></td>
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
				<!--<td><fmt:formatDate value="${entry.startDate}" pattern="yyyy-MM-dd"/></td> -->
				<td><fmt:formatDate value="${entry.endDate}" pattern="yyyy-MM-dd"/></td>
				<td> 
			  	    ${entry.stateName} 
			  	    <span style="color:#4169E1;"><fmt:formatNumber value='${(entry.currenyAmount/entry.maxAmount)*100}'   type="currency" currencySymbol=""/>%</span>
				</td> 
			   <td >
			       ${entry.financier.provinceName}${entry.financier.cityName}<br/>
				   <c:if test="${(entry.hyType)!= null}">
				    <c:out value="${fn:substring(entry.hyTypeShow,0,2)}" />-<c:choose><c:when test="${fn:length(entry.qyTypeShow) > 3}"><c:out value="${fn:substring(entry.qyTypeShow,0,2)}" />
					</c:when><c:otherwise><c:out value="${entry.qyTypeShow}" />
					</c:otherwise>
				    </c:choose>
                   </c:if> 
                     		     			
				</td>  
				<td> 
				    <c:if test="${(entry.state=='2' || entry.state=='3') && menuMap['financingBase_delayUI'] == 'inline' }">
					      <span onclick="showdialog('/back/financingBaseAction!delayUI?id=${entry.id}',1000);return false;"   class="btn btn-info btn-mini"   title="投标延期、开启投标优先"  >变更</span>  
					</c:if>
					<c:if test="${(entry.state != '7' && entry.state != '8')&& menuMap['financingBase_cancel'] == 'inline' }">  
						<span onclick="cancel('/back/financingBaseAction!cancel?id=${entry.id}','${entry.shortName}');return false;"   class="btn btn-danger btn-mini"   title="撤销此融资项目"  >撤单</span>
					</c:if>
					<span onclick="showdialog('/back/financingBaseAction!logs?_id=${entry.id}',700);return false;" class="btn btn-info btn-mini">日志</span>
					
				</td> 
			</tr>
		</c:forEach>
		    <c:if test="${actionUrl!= 'for_subscribe'}">
				<tr > 
				<td align="center">小计</td>  
				<td align="center"></td> 
				<td align="center"></td>
				<td align="center"></td> 
				       <td align="center"><fmt:formatNumber value='${totalData[0]}'   type="currency" currencySymbol=""/>
				       <br/><fmt:formatNumber value='${totalData[2]}'   type="currency" currencySymbol=""/><br/>
				       <fmt:formatNumber value='${totalData[1]}'   type="currency" currencySymbol=""/></td>
				       <td align="center"></td>
				       <td align="center"></td>
				       <td align="center"></td> 

 
				<td align="center"></td>
				<td align="center"></td> 
				<td align="center"></td> 
				<td align="center"></td> 
				<td align="center"></td> 
			</tr>
			</c:if>
				</tbody>
		<tfoot>
			<tr>
				<td colspan="13">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tfoot>
	</table> 

</form>
<%@ include file="/common/messageTip.jsp" %>
</body> 