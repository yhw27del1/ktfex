
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
    var stateVal = $("#stateHidden").val();
    if(stateVal){
    	$("option",$("#stateSelect")).attr("selected",false);
    	$("option[value='"+stateVal+"']",$("#stateSelect")).attr("selected",true);
    }
    
    var channel = $("#channelHidden").val();
    if(channel){
    	$("option",$("#channelSelect")).attr("selected",false);
    	$("option[value='"+channel+"']",$("#channelSelect")).attr("selected",true);
    }
});
</script>
<script type="text/javascript">
function doprint(){
	$("#myToolBar").hide();
	$("#toPrint").hide();
	print();
	$("#myToolBar").show();
	$("#toPrint").show();
}
	function toURL(url,flag) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:flag,
			ok:function(){
				window.location.href = url;
			},
			cancelVal:'关闭',cancel:true
		});
	}
	function del(id) {
		window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}" />
				专户&nbsp;
                <select name="channel" id="channelSelect">
                    <option value="0">全部</option>
                    <option value="1">招行</option>
                    <option value="2">工行</option>
                </select>
				&nbsp;状态&nbsp;
				<input type="hidden" id="stateHidden" value="${state}" />
				<input type="hidden" id="channelHidden" value="${channel}" />
				<select id="stateSelect" name="state">
					<option value="all">全部</option>
					<option value="exception">异常</option>
					<option value="reject">驳回</option>
				</select>	
				审核日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;至&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
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
						专户
					</th>
					<th>
						交易类型
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
						审核人姓名
					</th>
					<th>
						划款日期
					</th>
					<th>
						划款人姓名
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
						    <c:if test="${accountDeal.channel==0}">无</c:if>
							<c:if test="${accountDeal.channel==1}">招商银行</c:if>
							<c:if test="${accountDeal.channel==2}">工商银行</c:if>
						</td>
						<td>
							${accountDeal.type}
						</td>
						<td>
							<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="date" />
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.checkDate}" type="date" />
						</td>
						<td>
							${accountDeal.checkUser.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.hkDate}" type="date" />
						</td>
						<td>
							${accountDeal.hkUser.realname}
						</td>
						<td>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='2'}">
								<span style="color:red;">转账异常</span>
								<button onclick="toURL('/back/accountDeal/accountDealAction!cashError?id=${accountDeal.id}','你确定此笔提现标注为提现错误吗？');return false;" class="ui-state-default" style="display:<c:out value="${menuMap['casherror']}" />">提现错误</button>
							</c:if>
							<c:if test="${accountDeal.checkFlag=='4'&&accountDeal.checkFlag2=='3'}">
								<span style="color:red;">转账异常，提现错误</span>
							</c:if>
							<c:if test="${accountDeal.checkFlag=='5'}">
								<span style="color:red;">审核驳回</span>
							</c:if>
						</td>
						<td>
							${accountDeal.memo}
						</td > 
					</tr>
				</c:forEach>
				<tr>
					<td colspan="13"><jsp:include page="/common/page.jsp"></jsp:include></td>
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