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
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
<div class="dataList ui-widget" style="border:0;">
	<table width="100%" class="ui-widget ui-widget-content">
		<thead class="ui-widget-header ">
			<tr >				
				<td>银行卡号</td>
				<td>交易帐号</td>
				<td>可用余额</td>
				<td>冻结金额</td>
				<td>持有债权</td>
				<td>总资产</td>
			</tr>
		</thead>
		<tbody id="chager_list">
			<tr >
			    <td>${member.bankAccount}</td>
				<td>${userName}</td>				
				<td><fmt:formatNumber value='${account.balance}' type="currency" currencySymbol=""/>&nbsp;元</td>
				<td><fmt:formatNumber value='${account.frozenAmount}' type="currency" currencySymbol=""/>&nbsp;元</td>
				<td><fmt:formatNumber value='${account.cyzq}' type="currency" currencySymbol=""/>&nbsp;元</td>
				<td><fmt:formatNumber value='${account.totalAmount}' type="currency" currencySymbol="" />&nbsp;元</td>
			</tr>
		</tbody>
	</table>
</div>
	<br />
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value="${startDate}"  pattern="yyyy-MM-dd"/>" id="startDate"/>&nbsp;结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						类型
					</th>
					<th>
						存入
					</th>
					<th>
						划出
					</th>
					<th>
						结余
					</th>
					<!--  <th>
						操作者
					</th>
					<th>
						审核者
					</th>-->
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
							${accountDeal.type}
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
							<!--<td>
							${accountDeal.user.username}
						</td>
					  <td>
							${accountDeal.checkUser.username}
						</td>-->
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="date" />
						</td>
						<td>
							<c:if test="${accountDeal.checkFlag=='1'}"><span style="color:green;">充值成功</span></c:if>
							<c:if test="${accountDeal.checkFlag=='3'}"><span style="color:#4169E1;">待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='2.9'}"><span style="color:#4169E1;">待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='2.5'}"><span style="color:#4169E1;">待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='5'}"><span style="color:red;">提现驳回 </span></c:if>
							<c:if test="${accountDeal.checkFlag=='2.4'}"><span style="color:red;">提现驳回 </span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='0'}"><span style="color:#4169E1;">待划款</span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='1'}"><span style="color:green;">已划款</span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='2'}"><span style="color:red;">转账异常</span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='3'}"><span style="color:red;">提现错误</span></c:if>
						</td>
						<td>
							${accountDeal.memo} 
							<c:if test="${accountDeal.checkFlag=='21'}">专用账户:${accountDeal.accountDeal.account.user.realname}/${accountDeal.accountDeal.account.user.username}</c:if>
							<c:if test="${accountDeal.checkFlag=='20'}">会员账户:${accountDeal.accountDeal.account.user.realname}/${accountDeal.accountDeal.account.user.username}</c:if>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="9">
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