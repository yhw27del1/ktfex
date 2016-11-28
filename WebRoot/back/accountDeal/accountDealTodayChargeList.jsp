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
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<div style="line-height:26px;">
			<a href="javascript:;" onclick="window.open('/back/accountDeal/accountDealAction!print_recharge_kiting_report_cmb')">打印日结单</a>
			<!--  &nbsp;&nbsp;
			<a href="javascript:;" onclick="window.open('/back/accountDeal/accountDealAction!print_recharge_kiting_report_icbc')">打印工行日结单</a>-->
		</div>
	</div>



	<form action="">
    <input type="hidden" name="page" value="1" />
	<input type="hidden" name="keyWord" value="${keyWord}" />
	<input type="hidden" name="startDate" value="<fmt:formatDate value='${startDate}' type='date'/>" />
	<input type="hidden" name="endDate" value="<fmt:formatDate value='${endDate}' type='date'/>" />
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						交易账号
					</th>
					<th>
						会员名称
					</th>
					<th>
						用户类型
					</th>
					<th>
						联系电话
					</th>
					<th>
						类型
					</th>
					<th>
						专户
					</th>
					<th>
						充值金额
					</th>
					<th>
						操作者
					</th>
					<th>
						操作时间
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="accountDeal">
					<tr>
					    <!--  <td>
					       ${accountDeal.account.accountId}
					    </td>-->
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
						   <script>document.write(phone("${accountDeal.account.user.userContact.mobile}"));</script>
						</td>
						<td>
						   ${accountDeal.type}
						</td>
						<td>
							<c:if test="${accountDeal.channel==1}">银行专户</c:if>
							<c:if test="${accountDeal.channel==2}"></c:if>
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${accountDeal.user.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.checkDate}" type="both" />
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="10">
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