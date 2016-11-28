<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/open.js"></script>
<script type="text/javascript">
	function toURL(url){
		   window.location.href = url;
	}
	$(function() {
		$("#chargeAmount").focus();
		$("#charge").click(function() {
			var chargeAmount = $("#chargeAmount").val();
			if (chargeAmount == "") {
				alert("请输入提现金额，再进行申请操作");
				$("#chargeAmount").focus();
				return false;
			} else {
				if(chargeAmount>parseInt($("#balance").val())){
					alert("您的可用余额不足，请检查您的交易账号可用余额，再进行申请操作。");
					$("#chargeAmount").focus();
					return false;
				}else{
					$.getJSON("/back/accountDeal/accountDealAction!toCash?time="+ new Date().getTime()+ "&chargeAmount="+ chargeAmount,function(data){
						alert(data);
						toURL(window.location.href);
					});
				}
			}
		});
	});
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
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
				<td><fmt:formatNumber value='${account.totalAmount}' type="currency" currencySymbol=""/>&nbsp;元</td>
			</tr>
		</tbody>
	</table>
	</div>
	<table>
		<tbody>
			<tr id="chargeInput">
				<td style="width: 50%;font-size:14px;">
					<ul>
						<li>提现金额不能大于您交易帐号当前的可用余额。</li>					
						<li>我们会在第一时间内处理您的提现请求，并将在一个工作日内将您的提现金额划转至您的银行卡号中。</li>
                        </br>
						<li style="list-style-type: none;">
							提现金额：
							<input type="text" name="chargeAmount" size="20" onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" id="chargeAmount" />
							<button id="charge" class="ui-state-default mybutton">余额提现</button>
						</li>
					</ul>
				</td>
			</tr>
		</tbody>
	</table>
	<form action="">
	<input type="hidden" name="page" value="1" />
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
						<td>
							${accountDeal.user.username}
						</td>
						<td>
							${accountDeal.checkUser.username}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="date" />
						</td>
						<td>
							<c:if test="${accountDeal.checkFlag=='1'}"><span style="color:green;">充值成功</span></c:if>
							<c:if test="${accountDeal.checkFlag=='3'}"><span style="color:#4169E1;">待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='2.5'}"><span style="color:#4169E1;">待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='2.4'}"><span style="color:red;">提现驳回</span></c:if>
							<c:if test="${accountDeal.checkFlag=='5'}"><span style="color:red;">提现驳回 </span></c:if>
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
	<%@ include file="/common/messageTip.jsp" %>
</body>