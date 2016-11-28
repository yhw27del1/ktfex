<%--
2012-08-21 aora 修改此文件的“已取消”为“已撤单”
--%>
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
});



</script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action=""> 
<input type="hidden" name="page" value="1" />
<input type="hidden" name="userName" value="${userName}"/>
<input type="hidden" name="pu" value="${pu}"/>
<input type="hidden" name="userType" value="${userType}"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all"> 
	<div style="position:absolute;left:10px;">
			总资产：<font color="red"><fmt:formatNumber value='${account.totalAmount}' type="currency" currencySymbol=""/></font>元&nbsp;&nbsp;
			<!--  ${account.credit+forzencredit}积分&nbsp;&nbsp;-->
			可用余额：<font color="red"><fmt:formatNumber value='${account.balance}' type="currency" currencySymbol=""/></font>元&nbsp;&nbsp;
			冻结金额：<font color="red"><fmt:formatNumber value='${account.frozenAmount}' type="currency" currencySymbol=""/></font>元&nbsp;&nbsp;
			<c:if test="${user.flag=='2'}">可转金额：<font color="red"><fmt:formatNumber value='${account.old_balance}' type="currency" currencySymbol=""/></font>元&nbsp;&nbsp;</c:if>
			<c:if test="${user.channel==2}">可提金额：<font color="red"><fmt:formatNumber value='${account.old_balance}' type="currency" currencySymbol=""/></font>元&nbsp;&nbsp;</c:if>
			持有债权：<font color="red"><fmt:formatNumber value='${account.cyzq}' type="currency" currencySymbol=""/></font>元
		  <br/>   
	</div>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable">
		<thead>
			<tr class="ui-widget-header " style="padding:0 0;"> 
				<th><!-- 融资编号 -->债权编号 </th> 
				<th>简称</th>  
				<th>年利率</th> 
				<th>期限</th>  
				<th>还款方式</th>  
				<th style="text-align:right;">协议本金</th> 
		        <th style="text-align:right;">协议利息</th> 	
				<th style="text-align:right;">本金余额</th> 
		        <th style="text-align:right;">利息余额</th> 	
				<th style="text-align:right;">剩余本息合计</th> 
			    <th>下一还款日</th>
			    <th>融资方<br />状态</th> 
			   <!-- <th>有无担保</th>
			    <th>资金用途</th>-->
			    <th>说明</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${pageView.result}" var="entity">
				<tr  class="body" >
					   <td  align="left" style="color:#0C108B;">
							${entity.zhaiquancode}
						</td>  
						<td  align="left" style="color:#0C108B;padding:0 0;">
							<a href="javascript:void(0);" class="tooltip" title="${entity.fshortname}">
								<c:choose>
									<c:when test="${fn:length(entity.fshortname) > 3}">
										<c:out value="${fn:substring(entity.fshortname,0,3)}..." />
									</c:when>
									<c:otherwise>
										<c:out value="${entity.fshortname}" />
									</c:otherwise>
								</c:choose>
							</a>
						</td>
						<td  align="left" style="color:#0C108B;">
							${entity.rate}%
						</td>						
						<td  align="left" style="color:#0C108B;">
							<c:choose>
								<c:when test="${entity.interestday>0}">
									${entity.interestday}天
								</c:when>
								<c:otherwise>
									${entity.businesstype}个月
								</c:otherwise>
							</c:choose>
						</td>
						<td  align="left" style="color:#0C108B;">
							${entity.returnpattern}
						</td>
						
						<td align="right" style="color:#0C108B;text-align:right;">
							 <fmt:formatNumber value='${entity.investamount}'  type="currency" currencySymbol=""/>
						</td>
						<td align="right" style="color:#0C108B;text-align:right;">
							 <fmt:formatNumber value='${entity.interest_allah}'  type="currency" currencySymbol=""/>
						</td>
						
						<td align="right" style="color:#0C108B;text-align:right;">
							 <fmt:formatNumber value='${entity.bjye}'  type="currency" currencySymbol=""/>
						</td>
						<td  align="right" style="color:#0C108B;text-align:right;">
							<fmt:formatNumber value='${entity.lxye}'  type="currency" currencySymbol=""/>
						</td>  
						<td  align="right" style="color:#0C108B;text-align:right;">
							<fmt:formatNumber value='${entity.bjye+entity.lxye}'  type="currency" currencySymbol=""/>
						</td>  
						<td  style="color:#0C108B;">
							<c:if test="${entity.state==\"1\"}"><span style="color:green;">-</span></c:if>
							<c:if test="${entity.state==\"2\"}"><fmt:formatDate value="${entity.xyhkr}" pattern="yyyy-MM-dd" /></c:if>
							<c:if test="${entity.state==\"3\"}"><span style="color:green;">项目撤销</span></c:if>
							<!--<c:if test="${(entity.bjye+entity.lxye)==0}"><span style="color:green;">已还清</span></c:if>-->
							<c:if test="${entity.terminal==1}"><span style="color:green;">已还清</span></c:if>
						</td>
						<td  style="color:#0C108B;">
							<c:if test="${entity.state==\"1\"}"><span style="color:green;">待签约</span></c:if>
							<c:if test="${entity.state==\"2\"}"><span style="color:red;">已签约</span></c:if>
							<c:if test="${entity.state==\"3\"}"><span style="color:green;">已撤单</span></c:if>
						</td>
						<td style="color:#red;padding:0 0;">
							<c:if test="${entity.transferflag!=\"2\"}"><span style="color: red;">投标额:<fmt:formatNumber value='${entity.investamount}' pattern="#0" type="currency" currencySymbol=""/>元<c:if test="${entity.fromapp=='autoinvest'}"><br />自动投标</c:if><!--服务费:0元</span>--></c:if>
							<c:if test="${entity.transferflag==\"2\"}"><span style="color: red;">买入成本价:<fmt:formatNumber value='${entity.cbj}' type="currency" currencySymbol=""/>元<br />费用:<fmt:formatNumber value='${entity.fwf+entity.sf}' type="currency" currencySymbol=""/>元</span></c:if>
						</td>
						
					</tr>
				</c:forEach>  
				<tr>
					<td colspan="16">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
		</tbody> 
	</table>
</div>
</form>
</body>


