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
    $("#ui-datepicker-div").css({'display':'none'});
	
	$(".autoheight").val($("#mytable").height()+50);
});



</script>
 
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="/back/paymentRecord/paymentRecordAction!list_yqhktx" >
	<input type="hidden" name="page" value="1" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all"> 
		<div style="position:absolute;left:10px;">
		项目编号或简称&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:120px;"/>&nbsp;
		融资方账号或名称&nbsp;<input type="text" id="qkeyWord" name="qkeyWord" value="${qkeyWord}" style="width:120px;"/>&nbsp;
		逾期天数<s:select name="bjStr" list="#{'<':'>','<=':'>=','=':'='}" theme="simple" /><input type="text" name="day" value='${day}' style="width:60px;" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/> 
		<button class="ui-state-default">查询</button>
	     <c:if test="${!empty dataList}">
	       &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="/back/paymentRecord/paymentRecordAction!list_yqhktx?qkeyWord=${qkeyWord}&bjStr=${bjStr}&day=${day}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&excelFlag=1&fbcode=${fbcode}"  title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
	      </c:if>
		</div>
	</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable" style="font-size:12px;">
		<thead>
			<tr class="ui-widget-header "> 
				<th>序号</th>
				<th>签约日期</th>
				<th>项目编号</th>
				<th>项目简称</th>
				<th>融资额(元)</th>
				<th>融资方帐号</th> 
				<th>融资方</th> 
				<th>融资方手机</th> 
				<th>担保方</th>  
				<th>应还日期</th> 
				<th>期次</th>
				<th>当月本金</th>  
			</tr>
		</thead>
		<tbody class="table_solid">
		    <%int i=1; %> 
		    <c:set value="0" var="total_obj"/>
			<c:forEach items="${dataList}" var="data">
				<tr> 
					<td><%=i%><%i++;%></td> 
					<td><fmt:formatDate value="${data.qianyuedate}" pattern="yyyy-MM-dd"/></td> 
					<td>${data.financbasecode}</td> 
					<td>${data.fshortname}</td> 
					<td><fmt:formatNumber value="${data.currenyamount}" pattern="#0.00"/><c:set value="${data.currenyamount+total_obj}" var="total_obj"/>
					</td> 
					<td>${data.financiername}</td> 
					<td><script>document.write(name("${data.frealname}"));</script></td>
					<td><script>document.write(phone("${data.emobile}"));</script></td>
					<td><script>document.write(name("${data.dbhsname}"));</script></td>
					<td><fmt:formatDate value='${data.yhdate}' pattern="yyyy-MM-dd"/></td>  
					<td  class='xlsText'>${data.qs}/${data.returntimes}</td>
					<td>
                       <fmt:formatNumber value="${data.totalbj}" pattern="#0.00"/> 
					</td>
		    	</tr> 
			</c:forEach>
			 <tr> 
				<td>合计</td>
				<td></td>
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
 

