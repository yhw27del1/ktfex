<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
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
});
</script>
<script type="text/javascript">
	function toURL(url) {
		window.location.href = url;
	}
	function del(id) {
		window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				关键字&nbsp;
				<input type="text" name="keyWord" value="${keyWord}" />
			开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate"/>&nbsp;结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
		  <c:if test="${!empty pageView.records}">
	        &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="/back/accountDeal/accountDealAction!list?keyWord=${keyWord}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />&excelFlag=1" title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
	    </c:if>	
	</div>
	

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						交易账户
					</th>
					<th>
						会员名称
					</th>
					<th>
					     银行账号
					</th>
					<th>
						交易类型
					</th>
					<th>
						存入
					</th>
					<th>
						支出
					</th>
					<th>
						结余
					</th>
					<th>
						操作者
					</th>
					<th>
						审核者
					</th>
					<th>
						日期
					</th>
					<th>
						状态
					</th>
					<th>
						备注
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="accountDeal">
					<tr>
						<td>
							${accountDeal.account.user.username}
						</td>
						<td>
						    <script>document.write(name("${accountDeal.account.user.realname}"));</script>
						</td>
						<td>
						     <script>document.write(bankcard("${accountDeal.bankAccount}"));</script>
						</td>
						<td>
							<c:choose>
								<c:when test="${accountDeal.businessFlag=='20'}">融资提现</c:when>
								<c:when test="${accountDeal.businessFlag=='21'}">还款充值</c:when>
								<c:when test="${accountDeal.businessFlag=='22'}">履约充值</c:when>
								<c:otherwise>${accountDeal.type}</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:if test="${accountDeal.zf!=\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/><br />
								<c:if test="${accountDeal.checkFlag=='9'}">本:<fmt:formatNumber value='${accountDeal.bj}' type="currency" currencySymbol=""/> 息:<fmt:formatNumber value='${accountDeal.lx}' type="currency" currencySymbol=""/><c:if test="${accountDeal.fj>0}"> 罚金:<fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if></c:if>
							</c:if>
						</td>
						<td>
							<c:if test="${accountDeal.zf==\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/><br />
								<c:if test="${accountDeal.checkFlag=='6'}">本:<fmt:formatNumber value='${accountDeal.bj}' type="currency" currencySymbol=""/> 息:<fmt:formatNumber value='${accountDeal.lx}' type="currency" currencySymbol=""/><c:if test="${accountDeal.fj>0}"> 罚金:<fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if></c:if>
								<c:if test="${accountDeal.checkFlag=='15'&&accountDeal.fj>0}">其中罚金:<fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if>
							</c:if>
						</td>
						<td>
						 	<fmt:formatNumber value='${accountDeal.nextMoney}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${accountDeal.user.realname}
						</td>
						<td>
							${accountDeal.checkUser.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
							<c:if test="${accountDeal.checkFlag=='0'}"><span style="color:#4169E1;">充值待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='1'}"><span style="color:green;">充值已审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='2'}"><span style="color:red;">充值已驳回 </span></c:if>
							<c:if test="${accountDeal.checkFlag=='3'}"><span style="color:#4169E1;">提现待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'}"><span style="color:green;">提现已审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='5'}"><span style="color:red;">提现已驳回 </span></c:if>
							<c:if test="${accountDeal.checkFlag=='22'||accountDeal.checkFlag=='23'}">
								<c:if test="${accountDeal.checkFlag2=='0'}"><span style="color:#4169E1;">转账待审核</span></c:if>
								<c:if test="${accountDeal.checkFlag2=='3'}"><span style="color:green;">转账已审核</span></c:if>
								<c:if test="${accountDeal.checkFlag2=='4'}"><span style="color:red;">转账已驳回</span></c:if>
							</c:if>
							<c:if test="${accountDeal.checkFlag=='30'||accountDeal.checkFlag=='33'||accountDeal.checkFlag=='36'||accountDeal.checkFlag=='40'}">
								<span style="color:#4169E1;">待审核</span>
							</c:if>
							<c:if test="${accountDeal.checkFlag=='31'||accountDeal.checkFlag=='34'||accountDeal.checkFlag=='37'||accountDeal.checkFlag=='41'||accountDeal.checkFlag=='43'}">
								<span style="color:green;">已审核</span>
							</c:if>
							<c:if test="${accountDeal.checkFlag=='32'||accountDeal.checkFlag=='35'||accountDeal.checkFlag=='38'||accountDeal.checkFlag=='42'}">
								<span style="color:red;">已驳回</span>
							</c:if>
						</td>
						<td>
							${accountDeal.memo} 
							<c:if test="${accountDeal.checkFlag=='21'}">专用账户:${accountDeal.accountDeal.account.user.realname}/${accountDeal.accountDeal.account.user.username}</c:if>
							<c:if test="${accountDeal.checkFlag=='20'}">会员账户:${accountDeal.accountDeal.account.user.realname}/${accountDeal.accountDeal.account.user.username}</c:if>
						</td > 
					</tr>
				</c:forEach>
				<tr>
					<td colspan="12">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(100);
</script>