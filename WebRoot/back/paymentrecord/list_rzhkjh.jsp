<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script> 
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
<script>
$(function(){ 
	
	$(".table_solid").tableStyleUI(); 

	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    }); 
    $("#ui-datepicker-div").css({'display':'none'});
    
    $("#dataListDiv").css({'width':'1800px'});
	
	$(".autoheight").val($("#mytable").height()+50);
});



</script>
 
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="/back/paymentRecord/paymentRecordAction!list_rzhkjh" >
	<input type="hidden" name="page" value="1" />
	<div id="myToolBar" > 
		<div style="position:absolute;left:10px;">
		项目编号或简称&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:120px;"/>&nbsp;
		状态：<s:select  list="#{'9':'全部','0':'未还款','1':'正常还款','2':'提前还款','3':'逾期已还款','4':'担保代偿','5':'逾期未还款'}" name="fstate"  theme="simple" />&nbsp; 
		还款日期<s:select name="bjStr" list="#{'<':'<','<=':'<=','=':'=','>':'>','>=':'>='}" theme="simple" /><input type="text" name="startDate" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />" id="startDate"/> 
		<button class="ui-state-default">查询</button>  
	     <c:if test="${!empty dataList}">
	       &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="/back/paymentRecord/paymentRecordAction!list_rzhkjh?fstate=${fstate}&bjStr=${bjStr}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&excelFlag=1&fbcode=${fbcode}"  title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
	      </c:if>
		</div>
	</div>

<div class="dataList ui-widget" id='dataListDiv'>
	<table id="mytable" style="font-size:12px;">
		<thead>
			<tr class="ui-widget-header "> 
				<th>序号</th>
				<th>项目编号</th>
				<th>项目简称</th>				
				<th>融资方</th> 
				<th>交易账号</th>  
				<th>年利率</th>  
				<th>融资额</th>  
				<th>签约额</th> 
				<th>担保方</th> 
				<th>签约日期</th> 
				<th>到期日期</th> 
				<th>实还时间</th> 
				<th>期数</th> 
				<th>逾期天数</th> 
				<th>应还本金</th> 
				<th>应还利息</th> 
				<th>风险管理费</th> 
				<th>融资服务费</th> 
                <th>担保费</th>  
				<th>保证金</th> 
				<th>应还合计</th> 
			</tr>
		</thead> 
		
		<tbody class="table_solid">
		    <%int i=1; %>  
		    <c:set value="0" var="total_1"/>
		    <c:set value="0" var="total_2"/>
		    <c:set value="0" var="total_3"/>
		    <c:set value="0" var="total_4"/>
		    <c:set value="0" var="total_5"/>
		    <c:set value="0" var="total_6"/>
		    <c:set value="0" var="total_7"/>
			<c:forEach items="${dataList}" var="data">  
				<tr> 
					<td><%=i%><%i++;%></td> 
					<td>${data.financbasecode}</td> 
					<td>${data.fshortname}</td> 					
					<td>${data.frealname}</td> 					
					<td>${data.financiername}</td>  
					<td>${data.rate}%</td>  
					<td>${data.maxamount}</td>  
					<td>${data.currenyamount}</td>  
					<td>${data.dbhsname}</td>  
					<td><fmt:formatDate value='${data.qianyuedate}' pattern="yyyy-MM-dd"/></td>  
					<td><fmt:formatDate value='${data.yhdate}' pattern="yyyy-MM-dd"/></td>  
					<td><fmt:formatDate value='${data.shdate}' pattern="yyyy-MM-dd"/></td>  
					<td>${data.qs}/${data.returntimes}</td>
					<td>${data.overdue_days}</td>
					<td><fmt:formatNumber value="${data.totalbj}" pattern="#0.00"/>
					   <c:set value="${total_1+data.totalbj}" var="total_1"/>  
					</td>  
					<td><fmt:formatNumber value="${data.totallx}" pattern="#0.00"/>
					   <c:set value="${total_2+data.totallx}" var="total_2"/>  
					</td>  
					<td><fmt:formatNumber value="${data.totalfxglf}" pattern="#0.00"/>
					   <c:set value="${total_3+data.totalfxglf}" var="total_3"/>
					</td>  
					<td><fmt:formatNumber value="${data.totalrzfwf}" pattern="#0.00"/>
					   <c:set value="${total_4+data.totalrzfwf}" var="total_4"/>
					</td>  
					<td><fmt:formatNumber value="${data.totaldbf}" pattern="#0.00"/>
					   <c:set value="${total_5+data.totaldbf}" var="total_5"/>
					</td>  
					<td><fmt:formatNumber value="${data.totalbzj}" pattern="#0.00"/>
					   <c:set value="${total_6+data.totalbzj}" var="total_6"/>
					</td>   
                    <td><fmt:formatNumber value="${data.total}" pattern="#0.00"/>
                      <c:set value="${total_7+data.total}" var="total_7"/>
                    </td> 
		    	</tr> 
			</c:forEach> 
			<tr> 
				<td>总计</td>
				<td></td>
				<td></td>
				<td></td>    
				<td></td>     
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td><fmt:formatNumber  value="${total_1}" pattern="#0.00"/></td>   
				<td><fmt:formatNumber  value="${total_2}" pattern="#0.00"/></td>   
				<td><fmt:formatNumber  value="${total_3}" pattern="#0.00"/></td>    
                <td><fmt:formatNumber  value="${total_4}" pattern="#0.00"/></td>
                <td><fmt:formatNumber  value="${total_5}" pattern="#0.00"/></td>
                <td><fmt:formatNumber  value="${total_6}" pattern="#0.00"/></td>
                <td><fmt:formatNumber  value="${total_7}" pattern="#0.00"/></td>     
 			</tr>  
		</tbody> 
	</table>
</div>
</form>
</body>
 

