<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
function doprint(){
	$("#myToolBar").hide();
	$("#toPrint").hide();
	print();
	$("#myToolBar").show();
	$("#toPrint").show();
}
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
    var userTypeVal = $("#userTypeHidden").val();
    if(userTypeVal){
    	//alert(userTypeVal);
    	$("option").attr("selected",false);
    	$("option[value='"+userTypeVal+"']").attr("selected",true);
    }
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
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				会员类型&nbsp;
				<input type="hidden" id="userTypeHidden" value="${userType}" />
				<select id="userTypeSelect" name="userType">
					<option value="all">全部</option>
					<option value="T">投资人</option>
					<option value="R">融资方</option>
				</select>
				开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
	<table>
		<thead>
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
		<tr>
			<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						日期
					</th>
					<th>
						会员类型
					</th>
					<th>
						会员名
					</th>
					<th>
						交易账号
					</th>
					<th>
						银行账户
					</th>
					<th>
						交易类型
					</th>
					<th style="text-align: right;">
						充值额
					</th>
					<th>
						充值时间
					</th>
					<th>
						审核时间
					</th>
					<th>
						操作员姓名
					</th>
					<th>
						审核员姓名
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${deals}" var="accountDeal">
					<tr>
					    <td>
							<fmt:formatDate value="${accountDeal.checkDate}" type="date" />
						</td>
						<td>
							<c:if test="${accountDeal.account.user.userType=='T'}">投资人</c:if>
							<c:if test="${accountDeal.account.user.userType=='R'}">融资方</c:if>
						   
						</td>
						<td>
						   ${accountDeal.account.user.realname}
						</td>
						<td>
						  ${accountDeal.account.user.username}
						</td>
						<td style="padding:0 0;">
						   ${accountDeal.bankAccount}
						</td>
						<td>
						   ${accountDeal.type}
						</td>
						<td style="text-align: right;">
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="both" />
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.checkDate}" type="both" />
						</td>
						<td>
							${accountDeal.user.realname}
						</td>
						<td>
							${accountDeal.checkUser.realname}
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
					<td>
					   &nbsp;
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
					<td>
						&nbsp;
					</td>
					<td style="text-align: right;">
						<input type="button" value="打印" id="toPrint" onclick="doprint()">
					</td>
					<td style="text-align: right;">
						报表打印时间：
					</td>
					<td>
						<fmt:formatDate value="${showToday}" type="date" />
					</td>
					<td style="text-align: right;">
						经办员：
					</td>
					<td>
						${user.realname}
					</td>
				</tr>
			</tbody>
		</table>
		</tr>
		</tbody>
	</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(100);
</script>