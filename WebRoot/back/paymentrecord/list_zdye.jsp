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
<form action="/back/paymentRecord/paymentRecordAction!list_zdye" >
	<input type="hidden" name="page" value="1" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all"> 
		<div style="position:absolute;left:10px;">
		融资项目&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:90px;" title='输入项目编号或简称'/>&nbsp;
		融资方&nbsp;<input type="text" id="qkeyWord" name="qkeyWord" value="${qkeyWord}" style="width:90px;" title='输入融资方账号或名称'/>&nbsp;
		担保方&nbsp;<input type="text" id="createrOrgStr" name="createrOrgStr" value="${createrOrgStr}" style="width:90px;" title='可以输入担保方编码或担保方名称'/>&nbsp;
		日期节点：<input type="text" name="startDate" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />" id="startDate" style="width:90px;"/> 
		<button class="ui-state-default">查询</button>
	     <c:if test="${!empty dataList}">
	       &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="/back/paymentRecord/paymentRecordAction!list_zdye?createrOrgStr=${createrOrgStr}&qkeyWord=${qkeyWord}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&excelFlag=1&fbcode=${fbcode}"  title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
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
				<th nowrap="nowrap">融资方帐号</th> 
				<th>融资方</th> 
				<th>担保方</th> 
				<th nowrap="nowrap">担保方</th>
				<th nowrap="nowrap">融资额(元)</th> 
				<th nowrap="nowrap">已还本金(元)</th> 
				<th nowrap="nowrap">未还本金(元)</th> 
				<th nowrap="nowrap">到期未还</th> 
			</tr>
		</thead>
		<tbody class="table_solid">
		    <%int i=1; %>
			<c:set value="0" var="total_obj"/>
			<c:set value="0" var="total_bj"/> 
			<c:forEach items="${dataList}" var="data">
				<tr> 
					<td><%=i%><%i++;%></td> 
					<td>${data.financbasecode}</td> 
					<td>${fn:substring(data.fshortname,0,6)}</td> 
					<td>${data.financiername}</td> 
					<td><script>document.write(name("${data.frealname}"));</script>/<script>document.write(phone("${data.emobile}"));</script></td> 
					<td>${data.showcoding}<br/>${data.createuseruame}</td> 
					<td><script>document.write(name("${data.dbhsname}"));</script></td>
				    <td>
				      <fmt:formatNumber value="${data.currenyamount}" pattern="#0.00"/> 
				      <c:set value="${data.currenyamount+currenyamount_all}" var="currenyamount_all"/>
				    </td> 
					<td>
						<fmt:formatNumber value="${data.totalbj}" pattern="#0.00"/>
						<c:set value="${data.totalbj+ totalbj_all}" var="totalbj_all"/>
					</td>  
					<td>
						<fmt:formatNumber value="${data.weihuan}" pattern="#0.00"/>
						<c:set value="${ weihuan_all + data.weihuan}" var="weihuan_all"/>
					</td>  
					<td>
						<fmt:formatNumber value="${data.yuqiweihuan}" pattern="#0.00"/>
						<c:set value="${ yuqi_all + data.yuqiweihuan}" var="yuqi_all"/>
					</td>
		    	
		    	</tr> 
			</c:forEach>
 			<tr> 
				<td>合计</td>
				<td></td>
				<td></td> 
				<td></td> 
				<td></td> 
				<td></td>   
				<td></td>   
				<td><fmt:formatNumber  value="${currenyamount_all}" pattern="#0.00"/></td>  
				<td><fmt:formatNumber  value="${totalbj_all}" pattern="#0.00"/></td>  
				<td><fmt:formatNumber  value="${weihuan_all}" pattern="#0.00"/></td>   
				<td><fmt:formatNumber  value="${yuqi_all}" pattern="#0.00"/></td>
 			</tr> 
		</tbody> 
	</table>
</div>
</form>
</body>
 

