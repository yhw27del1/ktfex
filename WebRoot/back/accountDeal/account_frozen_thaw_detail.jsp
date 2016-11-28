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
    var b = '${memo}';
    $("option[value='"+b+"']",$("#memo")).attr("selected",true);
});
</script>
<script type="text/javascript">
	function toURL(url,flag) {
		$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:flag,
			ok:function(){
				$.getJSON(url,function(data){
					if(data.message=="success"){
						alert("操作成功。");
						window.location.href=window.location.href;
					}else{
						alert(data.message);
						window.location.href=window.location.href;
					}
				});
			},
			cancelVal:'关闭',cancel:true
		});
	}
	function del(id) {
		window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
	function doprint(){
		$("#myToolBar").hide();
		$("#toPrint").hide();
		$(".state").hide();
		print();
		$("#myToolBar").show();
		$("#toPrint").show();
		$(".state").show();
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>

<body>
<form action="">
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				申请日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				类型:<select name="memo" id="memo"><option value="">全部</option><option value="协议保证金">协议保证金</option><option value="还款保证金">还款保证金</option></select>&nbsp;
				机构:<input type="text" name="keyWord" value="${keyWord}"  />&nbsp;
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table>
		<thead>
			<tr>
				<th style="text-align: center;font-size:22px;">昆投互联网金融交易-账户冻结/解冻明细表</th>
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
					<th>
						申请日期
					</th>
					<th>
						会员类型
					</th>
					<th>
						机构
					</th>
					<th>
						会员名称
					</th>
					<th>
						交易账号
					</th>
					<th>
						交易类型
					</th>
					<th style="text-align: right;">
						冻结金额
					</th>
					<th style="text-align: right;">
						解冻金额
					</th>
					<th>
						项目编号
					</th>
					<th>
						操作员
					</th>
					<th>
						审核员
					</th>
					<th>
						审核时间
					</th>
					<th>
						状态
					</th>
					<th>
						备注
					</th>
					<th>
						操作
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${list}" var="entry">
					<tr>
						<td>
							<fmt:formatDate value="${entry.createDate}" type="date" />
						</td>
						<td>
							<c:if test="${entry.account.user.userType=='T'}">投资人</c:if>
							<c:if test="${entry.account.user.userType=='R'}">融资方</c:if>
							<c:if test="${entry.account.user.userType=='D'}">担保方</c:if>
						</td>
						<td>
							${entry.account.user.org.showCoding}
						</td>
						<td>
						   <script>document.write(name("${entry.account.user.realname}"));</script>
						</td>
						<td>
						  ${entry.account.user.username}
						</td>
						<td>
							${entry.type}
						</td>
						<td style="text-align: right;">
							<c:if test="${entry.type=='资金冻结'}">
								<fmt:formatNumber currencySymbol="" value="${entry.money}" type="currency" />
							</c:if>
						</td>
						<td style="text-align: right;">
							<c:if test="${entry.type=='资金解冻'}">
								<fmt:formatNumber currencySymbol="" value="${entry.money}" type="currency" />
							</c:if>
						</td>
						<td>
						<c:if test='${entry.codes !=null}'>
							${entry.codes}(${entry.bzj}%)
						</c:if>
						</td>
						<td>
							${entry.user.realname}
						</td>
						<td>
							${entry.checkUser.realname}
						</td>
						<td>
							<fmt:formatDate value="${entry.checkDate}" type="date" />
						</td>
						<td>
							<c:if test="${entry.checkFlag=='30'||entry.checkFlag=='33'}">
								<span style="color:#4169E1;">待审核</span>
							</c:if>
							<c:if test="${entry.checkFlag=='31'||entry.checkFlag=='34'}">
								<span style="color:green;">已审核</span>
							</c:if>
							<c:if test="${entry.checkFlag=='32'||entry.checkFlag=='35'}">
								<span style="color:red;">已驳回</span>
							</c:if>
						</td>
						<td>
							${entry.memo}
						</td>
						<td>
						<c:if test="${entry.checkFlag=='30'||entry.checkFlag=='33'}">
							<button style="display:<c:out value="${menuMap['frozen_thaw_pass']}" />" onclick="toURL('/back/accountDealAction!account_frozen_thaw_pass?id=${entry.id}','你确定要审核通过吗？');return false;" class="ui-state-default" >审核通过</button>
							<button style="display:<c:out value="${menuMap['frozen_thaw_nopass']}" />" onclick="toURL('/back/accountDealAction!account_frozen_thaw_nopass?id=${entry.id}','你确定要审核驳回吗？');return false;" class="ui-state-default" >审核驳回</button>
						</c:if>
						</td>
					</tr>
			</c:forEach>
			<tr>
					<td>
						<span style="font-size: 16px;font-weight: bold;">合计</span>
					</td>
					<td colspan="4">
					  &nbsp;
					</td>
					<td style="text-align: right;" colspan="2">
						<span style="font-size: 16px;font-weight: bold;"><fmt:formatNumber value='${dj_sum}' type="currency" currencySymbol=""/>元<br>${dj_count}笔</span>
					</td>
					<td style="text-align: right;" colspan="2">
						<span style="font-size: 16px;font-weight: bold;"><fmt:formatNumber value='${jd_sum}' type="currency" currencySymbol=""/>元<br>${jd_count}笔</span>
					</td>
					<td colspan="6">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="15" style="text-align: right;">
						<input type="button" value="打印" id="toPrint" onclick="doprint()">&nbsp;&nbsp;报表打印时间：<fmt:formatDate value="${showToday}" type="date" />&nbsp;&nbsp;经办员：${session.LOGININFO.realname}
					</td>
				</tr>
			</tbody>
		</table>
			</tr>
		</tbody>
	</div>
	</form>
</script>