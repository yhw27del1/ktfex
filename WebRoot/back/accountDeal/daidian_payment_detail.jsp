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
    $(".print_a").click(function(){
		var id = $(this).attr("id");
		var va = window.showModalDialog("/back/accountDeal/accountDealAction!print_voucher_dn?ids="+id,"handlerrequestforcash_print","dialogWidth=900px;dialogHeight=600px;");
		return false;
	});
});
</script>
<script type="text/javascript">
	function doprint(){
		$("#myToolBar").hide();
		$("#toPrint").hide();
		$(".print_a").hide();
		print();
		$("#myToolBar").show();
		$("#toPrint").show();
		$(".print_a").show();
	}
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
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<select name="keyWord" id="account_type">
				<option value="请选择专用账户">请选择专用账户</option>
				<option selected value="53010100269">兴易贷-孔祥超</option>
			</select>&nbsp;
			日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
			<input type="submit" class="ui-state-default" value="查询" />
	</div>

	<div class="dataList ui-widget">
	<table>
		<thead>
			<tr>
				<th style="text-align: center;font-size:22px;">昆投互联网金融交易-专用账户代垫明细表</th>
			</tr>
			<th>
			会计日期：
			<c:if test="${startDate==endDate}">
				<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日" type="date" />
			</c:if>
			<c:if test="${startDate!=endDate}">
				<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日" type="date" />&nbsp;至&nbsp;<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日" type="date" />
			</c:if>
			</th>
		</thead>
		<tbody>
			<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						专用账户
					</th>
					<th>
						资金账户
					</th>
					<th>
						类型
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
<!--					<th>-->
<!--						审核者-->
<!--					</th>-->
					<th>
						日期
					</th>
					<th>
						融资方账户
					</th>
					<th>
						备注
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${deals}" var="accountDeal">
					<tr>
						<td>
							<script>document.write(name("${accountDeal.account.user.realname}"));</script>/${accountDeal.account.user.username}
						</td>
						<td>
						     <script>document.write(bankcard("${accountDeal.bankAccount}"));</script>
						</td>
						<td>
							${accountDeal.type}
						</td>
						<td>
							<c:if test="${accountDeal.zf!=\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/><br />
							</c:if>
						</td>
						<td>
							<c:if test="${accountDeal.zf==\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/><br />
							</c:if>
						</td>
						<td>
						 	<fmt:formatNumber value='${accountDeal.nextMoney}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${accountDeal.user.realname}
						</td>
<!--						<td>-->
<!--							${accountDeal.checkUser.username}-->
<!--						</td>-->
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
							<c:if test="${accountDeal.checkFlag=='20'}"><script>document.write(name("${accountDeal.accountDeal.account.user.realname}"));</script>/${accountDeal.accountDeal.account.user.username}</c:if>
						</td > 
						<td>
							${accountDeal.memo}
							<c:if test="${menuMap['print_recharge_voucher']=='inline'}"><button id="${accountDeal.id}" class="ui-state-default print_a" >打印凭证</button></c:if>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td>
						<span style="font-size: 16px;font-weight: bold;">合计</span>
					</td>
					<td>
					  &nbsp;
					</td>
					<td>
					  &nbsp;
					</td>
					<td style="text-align: right;">
						<span style="font-size: 16px;font-weight: bold;"><fmt:formatNumber value='${sum_charge}' type="currency" currencySymbol=""/></span>
					</td>
					<td style="text-align: right;">
						<span style="font-size: 16px;font-weight: bold;"><fmt:formatNumber value='${sum_cash}' type="currency" currencySymbol=""/></span>
					</td>
					<td>
					   &nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="10" style="text-align: right;">
						<input type="button" value="打印" id="toPrint" onclick="doprint()">&nbsp;&nbsp;报表打印时间：<fmt:formatDate value="${showToday}" type="date" />&nbsp;&nbsp;经办员：${user.realname}
					</td>
				</tr>
			</tbody>
		</table>
		</tbody>
	</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(100);
</script>