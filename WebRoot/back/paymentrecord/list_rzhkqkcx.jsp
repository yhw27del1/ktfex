<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script> 
<script type="text/javascript" src="/back/four.jsp"></script>
<script>
$(function(){ 
	
	$(".table_solid").tableStyleUI(); 

	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    }); 
    
    $("#endDate").datepicker({
		numberOfMonths: 2,
		dateFormat: "yy-mm-dd"  
	});
			    
			    
    $("#ui-datepicker-div").css({'display':'none'});
	
	$(".autoheight").val($("#mytable").height()+50);
});



</script>
 
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="/back/paymentRecord/paymentRecordAction!list_rzhkqkcx" >
	<input type="hidden" name="page" value="1" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all"> 
		<div style="position:absolute;left:10px;">
  		担保方式: <s:select list="#{'':'全部','12':'本金担保','15':'本息担保','10':'无担保'}" name="fxbzstate" id="fxbzstate"  cssStyle="padding:3px"></s:select>
		还款状态:<s:select list="#{99:'全部',0:'未还款',1:'正常还款',2:'提前还款',3:'逾期还款',4:'担保代偿',5:'全部已还款'}" name="state" id="state" value="state" cssStyle="padding:3px"></s:select>
		按<s:select list="#{'shdate':'实还日期','yhdate':'应还日期'}" name="selectby" id="selectby" value="#request.selectby" ></s:select>
  		<input type="text" id="startDate" name="startDate" style="width:80px;padding:3px;" id="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>"/>
  		到 
  		<input type="text" name="endDate" style="width:80px;padding:3px;" id="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>"/>
  		项目编号 <input type="text" name="fbcode" value="${fbcode}" style="padding:3px;"/> 
  		
		<button class="ui-state-default">查询</button> 
	     <c:if test="${!empty dataList}">  
	       &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="/back/paymentRecord/paymentRecordAction!list_rzhkqkcx?fxbzstate=${fxbzstate}&selectby=${selectby}&state=${state}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />&excelFlag=1&fbcode=${fbcode}"  title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
	      </c:if>
		</div>
	</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable" style="font-size:12px;">
		<thead>
			<tr class="ui-widget-header "> 
				<th>序号</th>
				<th>项目编号</th>
				<th>项目简称</th>
				<th>融资额(元)</th>
				<th>融资方帐号</th> 
				<th>融资方</th>  
				<th>融资方手机</th>  
				<th>担保方</th>  
				<th>应还日期</th>  
				<th>实还日期</th>  
				<th>期次</th>  
			</tr>
		</thead>
		<tbody class="table_solid">
		    <%int i=1; %> 
		    <c:set value="0" var="total_obj"/>
			<c:forEach items="${dataList}" var="data">
				<tr> 
					<td><%=i%><%i++;%></td> 
					<td>${data.financbasecode}</td> 
					<td>${data.fshortname}</td> 
					<td><fmt:formatNumber value="${data.currenyamount}" pattern="#0.00"/><c:set value="${data.currenyamount+total_obj}" var="total_obj"/></td> 
					<td>${data.financiername}</td> 
					<td><script>document.write(name("${data.frealname}"));</script></td>  
					<td><script>document.write(phone("${data.emobile}"));</script></td>  
					<td><script>document.write(name("${data.dbhsname}"));</script></td>  
					<td><fmt:formatDate value='${data.yhdate}' pattern="yyyy-MM-dd"/></td>  
					<td><fmt:formatDate value='${data.shdatestr}' pattern="yyyy-MM-dd"/></td> 
					<td>${data.qs}/${data.returntimes}</td>
		    	</tr> 
			</c:forEach>
			<tr> 
				<td>合计</td>
				<td></td>
				<td></td> 
				<td><fmt:formatNumber  value="${total_obj}" pattern="#0.00"/></td> 
				<td></td> 
				<td></td>  
				<td></td>   
				<td></td>    
				<td></td>    
				<td></td>    
				<td></td>    
 			</tr>   
		</tbody> 
	</table>
</div>
</form>
</body>
 

