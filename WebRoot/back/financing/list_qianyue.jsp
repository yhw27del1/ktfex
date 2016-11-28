<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<style>
<!--
body{
 overflow:auto !important;
}
td,th{white-space: nowrap;}
-->
</style>
<script type="text/javascript">

function toAddDbht(financingCode,financingId){  
   var url='/back/financingBaseAction!dbhtFileList?id='+financingId;     
   $.dialog({   
         id:"opSelectId",
         title: '编号为：'+financingCode+'融资项目上传担保合同',
         lock:  true,
         min:  false,
         max:  false, 
         width:650,
         height:400,  
         content:'url:'+url});        
}   
function toURL(url){ 
   window.location.href = url; 
}
function del(id){ 
  window.location.href = "/back/financingBaseAction!del?id="+id; 
}
$(document).ready(function(){
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
    $(".table_solid").tableStyleUI(); 
	$("#seachButton").click(function() {
		$("#form1").submit();
	}); 
	$("#excelImg").click(function(){
        $("#excel").val("1");
        $("#form1").submit();
        $("#excel").val("0");
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
function showInfo(id){
	$.dialog({title:'投标信息',width:'1000px',height:'400px',content:'url:/back/financingBaseAction!showInvest?fid='+id,lock:true});
	return false;
}
</script>  
 
<body>
<form id="form1" action="" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
      项目编号<input type="text" name="queryCode" value="${queryCode}" size='8'/>
     &nbsp; &nbsp;项目简称<input type="text" name="keyWord" value="${keyWord}"  size='10'/>
    &nbsp;&nbsp;开始日期<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate" size="8"/>
  &nbsp;&nbsp;结束日期<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate" size="8"/>
 &nbsp;&nbsp;关键字<input type="text" name="qkeyWord" value="${qkeyWord}"  size='10' title="关键字中输入融资方名称,融资方用户名或担保公司名称、担保公司用户名模糊查询"/>
 <input type="hidden" name="excel" id="excel" value="0" />
 &nbsp;&nbsp; <button class="ui-state-default" id="seachButton">查找</button>
  <a style="color:red;" id = "excelImg" name="excelImg" href="javascript:;" title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
       
  </div>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header"> 
				<th align="center">编号</th>
				<th align="center">项目简称</th> 
				<th align="center">担保方</th>
				<th align="center">融资方</th>
				<th align="center">总融资额<br/>可融资额<br/>已融资额</th>
				<th align="center">期限<br/>还款方式</th>
				<th align="center">年利率</th>
				<th align="center">投标<br/>人数</th> 
				<th align="center">签约日期</th> 
				<th align="center">所属地域<br/>行业</th>
				<th align="center">优先<br/>投标</th>
				<th align="center" width="15%">操作</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td>
				<c:choose>
					<c:when test="${fn:length(entry.shortName) >10}">
						<c:out value="${fn:substring(entry.shortName,0,8)}." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortName}" />
					</c:otherwise>
				</c:choose>
				</td>
				<td>${entry.createBy.org.shortName}<br/>${entry.createBy.org.showCoding}</td>
				 <td>
				
				<c:if test="${(entry.financier)!= null}">		
					<a href="/back/member/memberBaseAction!memberDetails?id=${entry.financier.id}" title='${entry.financier.eName}'>		
					   <c:choose>
						<c:when test="${fn:length(entry.financier.eName) > 3}">
							<c:out value="${fn:substring(entry.financier.eName,0,2)}." />
						</c:when>
						<c:otherwise>
							<c:out value="${entry.financier.eName}" />
						</c:otherwise>				
					 </c:choose>
					 </a>
				</c:if>	 
				<c:if test="${(entry.financier)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxAmount}'   type="currency" currencySymbol=""/>
				<br/><fmt:formatNumber value='${entry.curCanInvest}' type="currency" currencySymbol=""/>
				<br/><fmt:formatNumber value='${entry.currenyAmount}'   type="currency" currencySymbol=""/></td>
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
				<td><fmt:formatDate value="${entry.qianyueDate}" pattern="yyyy-MM-dd"/></td>

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
				   <c:if test="${entry.preInvest=='1'}">
				       <span title='${entry.groupStr}'>
				           <c:choose>
							 <c:when test="${fn:length(entry.groupStr) > 5}">
								<c:out value="${fn:substring(entry.groupStr,0,5)}.." />
							 </c:when>
							 <c:otherwise>
								<c:out value="${entry.groupStr}" />
							</c:otherwise>				
						 </c:choose>
				       </span> 
					</c:if>
					<c:if test="${entry.preInvest=='0'}">
						无
					</c:if>
			   </td>
				<td>  
					<button class="ui-state-default" onclick="showInfo('${entry.id}');return false;">投标信息</button> &nbsp;&nbsp;&nbsp;    
					<button class="ui-state-default" onclick="toAddDbht('${entry.code}','${entry.id}');return false;"  style="display:<c:out value="${menuMap['qianyue_dbhtFile']}" />">上传担保合同</button>  
				</td> 
			</tr>
		</c:forEach>
		<tr > 
				<td align="center">小计</td>
				<td align="center"></td> 
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"><fmt:formatNumber value='${totalData[0]}'   type="currency" currencySymbol=""/>
				<br/><fmt:formatNumber value='${totalData[2]}'   type="currency" currencySymbol=""/>
				<br/><fmt:formatNumber value='${totalData[1]}'   type="currency" currencySymbol=""/></td>
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
				<td colspan="12">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 