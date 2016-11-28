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
	$("#okButton").click(function() {
		$("#form1").submit();
	}); 
	showOrHide($("input[type='radio']:checked").val());
	//实时点击时触发显示与隐藏。
	//$("input[name='type']:radio").click(function(){
	//	showOrHide($(this).val());
	//});
});

function showOrHide(m){
	$("[showandhide='showandhide']").hide();
	$("."+m).show();
}

</script>
<body>
<form action="/back/accountDealAction!acWs" id="form1">
<input type="hidden" name="page" value="1" />
<input type="hidden" name="userName" value="${userName}"/>
<input type="hidden" name="pu" value="${pu}"/>
<input type="hidden" name="userType" value="${userType}"/>
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:35px"> 
	<div style="position:absolute;left:10px;">
	日期&nbsp;从<input type="text" style="width: 80px;" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" style="width: 80px;" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
	<!-- 
	这些为预留信息，一下业务需要查询时开放。
	'zqzr':'债权'
	'daidian':'代垫操作'
	'neibu':'内部转账'
	'bank_zhengquan':'银证转账'
	 --> 
           <c:if test="${member.user.flag=='2'}">类型：<s:radio list="#{'all':'全部','charge':'充值','cash':'提现','invest_out':'投标','payment_in':'还款','jyfwf':'交易手续费','bank_zhengquan':'银商转账','zqzr':'债权'}" name="type" /></c:if>
           <c:if test="${member.user.flag!='2'}">类型：<s:radio list="#{'all':'全部','charge':'充值','cash':'提现','invest_out':'投标','payment_in':'还款','jyfwf':'交易手续费','zqzr':'债权'}" name="type" /></c:if>
	<button class="ui-state-default" id="okButton">查询</button> 
	<br/><br/>
	</div>
</div>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable" style="padding:0 0;">
		<thead>
			<tr class="ui-widget-header ">  
			    <th>日期</th> 
			    <th>时间</th> 
			    <th>类型</th>
				<th showandhide="showandhide" class="invest_out" style="text-align:right;">投资服务费</th>
				<th showandhide="showandhide" class="zqzr" style="text-align:right;">成交价</th>
				<th showandhide="showandhide" class="zqzr" style="text-align:right;">手续费</th>
				<th showandhide="showandhide" class="zqzr" style="text-align:right;">税费</th>
				<th style="text-align:right;">存入</th>
				<th style="text-align:right;">支出</th>
				<th style="text-align:right;">结余</th>
				<th>项目编号</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody class="table_solid">
			 <c:forEach items="${pageView.records}" var="accountDeal">
				   <tr class="body" >
						<td align="left"  style="color:#0C108B;">
							<fmt:formatDate value="${accountDeal.createDate}" pattern="yyyy-MM-dd" />
						</td > 
						<td align="left"  style="color:#0C108B;">
							<fmt:formatDate value="${accountDeal.createDate}" pattern="HH:mm:ss" />
						</td > 
						
						 <td  align="left" style="color:#0C108B;">
							<c:choose>
								<c:when test="${accountDeal.businessFlag=='20'}">融资提现</c:when>
								<c:when test="${accountDeal.businessFlag=='21'}">还款充值</c:when>
								<c:when test="${accountDeal.businessFlag=='22'}">履约充值</c:when>
								<c:otherwise>${accountDeal.type}</c:otherwise>
							</c:choose>
							<c:if test="${accountDeal.type=='提现'}">
							(<c:if test="${accountDeal.checkFlag=='3'}"><span style="color:#4169E1;">待审核</span></c:if><c:if test="${accountDeal.checkFlag=='5'}"><span style="color:red;">提现驳回</span></c:if><c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='0'}"><span style="color:#4169E1;">待划款</span></c:if><c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='1'}"><span style="color:green;">已划款</span></c:if><c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='2'}"><span style="color:red;">转账异常</span></c:if><c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='3'}"><span style="color:red;">提现错误</span></c:if><c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='4'}"><span style="color:green;">提现冲正</span></c:if>)
							</c:if>
							<c:if test="${accountDeal.checkFlag=='22'||accountDeal.checkFlag=='23'}">
							(<c:if test="${accountDeal.checkFlag2=='0'}"><span style="color:#4169E1;">待审核</span></c:if><c:if test="${accountDeal.checkFlag2=='3'}"><span style="color:green;">已审核</span></c:if><c:if test="${accountDeal.checkFlag2=='4'}"><span style="color:red;">已驳回</span></c:if>)
							</c:if>
							<c:if test="${accountDeal.checkFlag=='25'}">
							(<span style="color:#4169E1;">待审核</span>)
							</c:if>
							<c:if test="${accountDeal.checkFlag=='26'}">
							(<span style="color:green;">已审核</span>)
							</c:if>
							<c:if test="${accountDeal.checkFlag=='27'}">
							(<span style="color:red;">已驳回</span>)
							</c:if>
						</td>
						<td showandhide="showandhide" class="invest_out" align="right" style="color:#0C108B;text-align:right;">
							<c:if test="${accountDeal.checkFlag=='11'}"><fmt:formatNumber value='${accountDeal.lx}' type="currency" currencySymbol=""/></c:if>
						</td>
						<td showandhide="showandhide" class="zqzr" align="right" style="color:#0C108B;text-align:right;">
							<c:if test="${accountDeal.checkFlag=='12'||accountDeal.checkFlag=='13'}"><fmt:formatNumber value='${accountDeal.bj}' type="currency" currencySymbol=""/></c:if>
						</td>
						<td showandhide="showandhide" class="zqzr" align="right" style="color:#0C108B;text-align:right;">
							<c:if test="${accountDeal.checkFlag=='12'||accountDeal.checkFlag=='13'}"><fmt:formatNumber value='${accountDeal.lx}' type="currency" currencySymbol=""/></c:if>
						</td>
						<td showandhide="showandhide" class="zqzr" align="right" style="color:#0C108B;text-align:right;">
							<c:if test="${accountDeal.checkFlag=='12'||accountDeal.checkFlag=='13'}"><fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if>
						</td>
 
						<td  align="right" style="color:red;text-align:right;">
							<c:if test="${accountDeal.zf!=\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/><c:if test="${accountDeal.businessFlag==9}"><br />本:<fmt:formatNumber value='${accountDeal.bj}' type="currency" currencySymbol=""/>,息:<fmt:formatNumber value='${accountDeal.lx}' type="currency" currencySymbol=""/>,罚金:<fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if>
							</c:if>
						</td>
						
						<td  align="right" style="color:green;text-align:right;">
							<c:if test="${accountDeal.zf==\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
							</c:if>
						</td>
						
						<td  align="right" style="color:#0C108B;text-align:right;">
							<fmt:formatNumber value='${accountDeal.nextMoney}' type="currency" currencySymbol=""/>
						</td>
						<td>
						<c:if test="${accountDeal.checkFlag=='11'||accountDeal.checkFlag=='9'}">
							<a href="/back/paymentRecord/paymentRecordAction!dieWs?page=1&userName=${userName}&fbcode=${accountDeal.financing.code}&state=9&showRecord=${showRecord}">${accountDeal.financing.code}</a>
						</c:if>
						<c:if test="${accountDeal.checkFlag=='12'||accountDeal.checkFlag=='13'}">
							${accountDeal.zhaiQuanCode}
						</c:if>
						</td>
						<td align="left"  style="color:#0C108B;">
							${accountDeal.memo}
						</td > 
					</tr>
				</c:forEach>  
				<tr>
					<td colspan="16">
						<jsp:include page="/common/page.jsp"></jsp:include>
					</td>
				</tr>
		</tbody> 
	</table>
</div>
</form>
</body>


