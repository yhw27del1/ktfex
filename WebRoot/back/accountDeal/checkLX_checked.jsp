<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
function doprint(){
	$("#myToolBar").hide();
	$("#toPrint").hide();
	$(".state").hide();
	print();
	$("#myToolBar").show();
	$("#toPrint").show();
	$(".state").show();
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
    	$("option",$("#userTypeSelect")).attr("selected",false);
    	$("option[value='"+userTypeVal+"']",$("#userTypeSelect")).attr("selected",true);
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
<script type="text/javascript" src="/back/four.jsp"></script>
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
				审核日期&nbsp;从<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />" id="startDate" style="width:80px;padding-left:3px;"/>&nbsp;至&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}'  pattern="yyyy-MM-dd" />" id="endDate" style="width:80px;padding-left:3px;"/>
				关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}" style="width:120px;padding-left:3px;"/>
				<input type="submit" class="ui-state-default" value="查询"/>
	</div>
	<div class="dataList ui-widget">
	<table>
		<thead>
			<tr>
			<th style="text-align: center;font-size:22px;">昆投互联网金融交易-利息明细表</th>
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
		<tr>
			<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th style="padding:0 0;">
						会员类型
					</th>
					<th style="padding:0 0;width:10%">
						会员名称
					</th>
					<th style="padding:0 0;width:10%">
						交易账号
					</th>
					<th style="padding:0 0;width:10%">
						银行账号
					</th>
					<th style="padding:0 0;">
						交易类型
					</th>
					<th style="padding:0 0;">
						利息额
					</th>
					<th style="padding:0 0;">
						审核时间
					</th>
					<th style="padding:0 0;">
						操作员
					</th>
					<th style="padding:0 0;">
						审核员
					</th>
					<th style="padding:0 0;">
						备注
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${deals}" var="accountDeal">
					<tr>
						<td class="print_a" id="${accountDeal.id}" style="padding:0 0;">
							<c:if test="${accountDeal.account.user.userType=='T'}">投资人</c:if>
							<c:if test="${accountDeal.account.user.userType=='R'}">融资方</c:if>
						   
						</td>
						<td style="padding:0 0;">
						   <script>document.write(name("${accountDeal.account.user.realname}"));</script>
						</td>
						<td style="padding:0 0;">
						  ${accountDeal.account.user.username}
						</td>
						<td style="padding:0 0;">
						   <script>document.write(bankcard("${accountDeal.bankAccount}"));</script>
						</td>
						<td style="padding:0 0;">
						   ${accountDeal.type}
						</td>
						<td style="padding:0 0;">
							${accountDeal.money}
						</td>
						<td style="padding:0 0;">
							<fmt:formatDate value="${accountDeal.checkDate}" type="both" />
						</td>
						<td style="padding:0 0;">
							${accountDeal.user.realname}
						</td>
						<td style="padding:0 0;">
							${accountDeal.checkUser.realname}
						</td>
						<td style="padding:0 0;">
							${accountDeal.memo}
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td>
						<span style="font-size: 16px;font-weight: bold;">合计</span>
					</td>
					<td colspan="5">
					  &nbsp;
					</td>
					<td>
						<span style="font-size: 16px;font-weight: bold;padding:0 0;">
							<fmt:formatNumber value='${sum_charge}' type="currency" currencySymbol=""/>元<br />
							${sum_charge_count}笔
						</span>
					</td>
					<td colspan="4">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="11" style="text-align: right;">
						<input type="button" value="打印" id="toPrint" onclick="doprint()">&nbsp;&nbsp;报表打印时间：<fmt:formatDate value="${showToday}" type="date" />&nbsp;&nbsp;经办员：${user.realname}
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