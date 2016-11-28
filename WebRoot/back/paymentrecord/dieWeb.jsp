<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<style>
	.ui-autocomplete{
		width:120px;
		overflow:hidden;
		padding:0;
		margin:0;
	}
	.ui-autocomplete li {
		width:120px;
		list-style-type: none;
		padding:0;
		margin:0;
	}
	.ui-autocomplete li a:HOVER{
		background-image: none;
	}
	.error{float:left;}
</style>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<script>
$(function(){
	var iframe = $("iframe",parent.document);
	$( "#fbcode" ).autocomplete({
		source: "/back/paymentRecord/paymentRecordAction!autocomplete_fbcode",
		minLength: 2,
		scrollHeight: 300
	});
	$(".sh").css({'cursor':'pointer'}).click(function(){
		var id = $(this).attr("id");
		if($("#h"+id).is(":hidden")){
			$(this).attr("src","/Static/js/tree/tabletree/images/minus.gif");
			$("#h"+id).show();
		}else{
			$(this).attr("src","/Static/js/tree/tabletree/images/plus.gif");
			$("#h"+id).hide();
		}
		
		iframe.height($("#mytable").height()+50);
	}); 
	$(".table_solid").tableStyleUI();  
	$(".autoheight").val($("#mytable").height()+50);
	
	 $("#okButton").click(function() {
		$("#form1").submit();
	}); 
});



</script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="/back/paymentRecord/paymentRecordAction!dieWeb" id="form1">
	<input type="hidden" name="page" value="1" /> 
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:35px"> 
	<div style="position:absolute;left:10px;">
	项目编号&nbsp;<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width:120px;"/>
           状态：<s:radio list="#{'9':'全部','0':'未还款','1':'已还款','3':'逾期'}" name="state" />
	<button class="ui-state-default" id="okButton">查询</button>
	<br/><br/>
	</div>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable">
		<thead>
			<tr class="ui-widget-header "> 
				<th>项目编号</th>
				<th>项目简称</th> 
				<th>融资方</th>
				<th>状态</th>
				<th>投标金额</th>
				<th>到期时间</th>
				<th>还款时间</th>
				<th>还款额</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.records}" var="iter">
			<tr> 
				<td>${iter.investRecord.financingBase.code}</td>
				<td>${iter.investRecord.financingBase.shortName}</td> 
				<td>
				<c:if test="${iter.investRecord.financingBase.financier.category=='1'}">
					${iter.investRecord.financingBase.financier.pName}
				</c:if>
				<c:if test="${iter.investRecord.financingBase.financier.category!='1'}">
					${iter.investRecord.financingBase.financier.eName}
				</c:if>
				</td> 
				<td> 
				<c:if test="${iter.state==0}"><span style="color:#4169E1;">未还款</span></c:if>
				<c:if test="${iter.state==1}"><span style="color:green;">已还款 </span></c:if>
				<c:if test="${iter.state==2}"><span style="color:red;">延期 </span></c:if>
				<c:if test="${iter.state==3}"><span style="color:#4169E1;">逾期</span></c:if> 
				</td>
				<td>${iter.investRecord.investAmount}</td>
				<td><fmt:formatDate value="${iter.predict_repayment_date}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${iter.actually_repayment_date}" pattern="yyyy-MM-dd"/></td>
				<td> 
				 <fmt:formatNumber value="${iter.shbj+iter.shlx}" pattern="0.00"/>元(其中本金:<fmt:formatNumber value="${iter.shbj}" pattern="0.00"/>元，利息:<fmt:formatNumber value="${iter.shlx}" pattern="0.00"/>元，罚金:<fmt:formatNumber value="${iter.penal}" pattern="0.00"/>元)
				 <br/>
				 还款次数:${iter.investRecord.financingBase.businessType.returnTimes}次，当前为第${iter.succession}次
				</td>
			</tr>
			  
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8"><jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tfoot>
	</table>
</div>
</form>
</body>


