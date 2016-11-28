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
	function doprint(){
		$("#myToolBar").hide();
		$("#toPrint").hide();
		print();
		$("#myToolBar").show();
		$("#toPrint").show();
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
				关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}" />
				开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />" id="startDate"/>&nbsp;截止日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate"/>
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
						银行账号
					</th>
					<th>
						类型
					</th>
					<th>
						提现金额
					</th>
					<th>
						专户
					</th>
					<th>
						申请时间
					</th>
					<th>
						审核时间
					</th>
					<th>
						审核人
					</th>
					<th>
						划款时间
					</th>
					<th>
						划款人
					</th>
					<th>
						状态
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="accountDeal">
					<tr>
						<!-- <td>
							${accountDeal.account.accountId}
						</td> -->
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
						    <script>document.write(bankcard("${accountDeal.bankAccount}"));</script>
						</td>
						<td>
							${accountDeal.type}
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
						    <c:if test="${accountDeal.channel==0}">无</c:if>
							<c:if test="${accountDeal.channel==1}">招商银行</c:if>
							<c:if test="${accountDeal.channel==2}">工商银行</c:if>
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.checkDate}" pattern="yyyy-MM-dd HH:mm:ss" />
						</td>
						<td>
							${accountDeal.checkUser.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.hkDate}" pattern="yyyy-MM-dd HH:mm:ss" />
						</td>
						<td>
							${accountDeal.hkUser.realname}
						</td>
						<td>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='1'}"><span style="color:green;">已划款</span></c:if>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="12">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
						<td><input type="button" value="打印" id="toPrint" onclick="doprint()"></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(100);
</script>