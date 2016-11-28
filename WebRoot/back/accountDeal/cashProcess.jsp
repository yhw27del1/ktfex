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
				开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;截止日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
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
						会员类型
					</th>
					<th>
						类型
					</th>
					<th>
						提现金额
					</th>
					<th>
						申请日期
					</th>
					<th>
						审核日期
					</th>
					<th>
						审核人
					</th>
					<th>
						划款日期
					</th>
					<th>
						划款人
					</th>
					<th>
						状态
					</th>
					<th>备注</th>
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
						    <c:if test="${accountDeal.account.user.userType=='T'}">投资人</c:if>
						    <c:if test="${accountDeal.account.user.userType=='R'}">融资方</c:if>
						    <c:if test="${accountDeal.account.user.userType=='D'}">担保方</c:if>
						</td>
						<td>
							${accountDeal.type}
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.checkDate}" type="both" />
						</td>
						<td>
							${accountDeal.checkUser.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.hkDate}" type="both" />
						</td>
						<td>
							${accountDeal.hkUser.realname}
						</td>
						<td>
							<c:if test="${accountDeal.checkFlag=='1'}"><span style="color:green;">充值成功</span></c:if>
							<c:if test="${accountDeal.checkFlag=='3'}"><span style="color:#4169E1;">待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='5'}"><span style="color:red;">提现驳回 </span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='0'}"><span style="color:#4169E1;">待划款</span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='1'}"><span style="color:green;">已划款</span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='2'}"><span style="color:red;">转账异常</span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='3'}"><span style="color:red;">提现错误</span></c:if>
						</td>
						<td>
							${accountDeal.memo}
						</td > 
					</tr>
				</c:forEach>
				<tr>
					<td colspan="13">
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