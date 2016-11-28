<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<style>
<!--
td.jiacu{font-size: 16px;font-weight: bold;}
td.red{color: red;}
td.green{color: green;}
-->
</style>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	$("#selectDate").datepicker({
		numberOfMonths: 1,
		maxDate: '+0d',//最大可选日期，0d表示只能选择到今天
        dateFormat: "yy-mm-dd"
    });
	$("#ui-datepicker-div").css({'display':'none'});
});
</script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				日期&nbsp;<input type="text" name="selectDate" value="<fmt:formatDate value='${selectDate}' type='date' />" id="selectDate"/>
				<input type="text" id="keyWord" name="keyWord" placeholder="会员名称/会员编号" value="${keyWord}" />&nbsp;
				<input type="submit" class="ui-state-default" value="查询" />&nbsp;
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
						<th rowspan="2"></th>
						<th rowspan="2">
							会员编号
						</th>
						<th rowspan="2">
							会员名称
						</th>
						<th colspan="3" style="text-align:center">
							<fmt:formatDate value="${selectDate_pre}" type="date" pattern="yyyy/MM/dd" />日切余额
						</th>
						<th rowspan="2">
							入金总额
						</th>
						<th rowspan="2">
							出金总额
						</th>
						<th colspan="1" style="text-align:center">
							贷总额(加钱)
						</th>
						<th colspan="1" style="text-align:center">
							借总额(减钱)
						</th>
						<th rowspan="2">
							计算余额
						</th>
						<th colspan="3" style="text-align:center">
							<fmt:formatDate value="${selectDate}" type="date" pattern="yyyy/MM/dd" />${show?'实时':'日切'}余额
						</th>
						<th rowspan="2">
							状态
						</th>
					</tr>
					<tr class="ui-widget-header ">
						<th style="text-align:center">
							可用
						</th>
						<th style="text-align:center">
							冻结
						</th>
						<th style="text-align:center">
							总额
						</th>
						<th style="text-align:center">
							还款等
						</th>
						<th style="text-align:center">
							投标等
						</th>
						<th style="text-align:center">
							可用
						</th>
						<th style="text-align:center">
							冻结
						</th>
						<th style="text-align:center">
							总额
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${result}" var="c" varStatus="sta">
						<tr>
							<td>${sta.index+1}</td>
							<td>
								${c.username}
							</td>
							<td>
								${c.realname}
							</td>
							<td>
								${c.balance_pre}
							</td>
							<td>
								${c.frozen_pre}
							</td>
							<td class="jiacu">
								${c.balance_pre+c.frozen_pre}<!-- 上日总额，日切 -->
							</td>
							<td class="jiacu red">
								${c.in_money}<!-- 当日入金 -->
							</td>
							<td class="jiacu green">
								${c.out_money}<!-- 当日出金 -->
							</td>
							<td class="jiacu red">
								${c.add_money}<!-- 当日贷 -->
							</td>
							<td class="jiacu green">
								${c.subtract_money}<!-- 当日借 -->
							</td>
							<td class="jiacu" style="color:blue;">
								<c:if test="${show}">
									<c:set var="jisuan" value="${((c.balance_pre+c.frozen_pre) + c.in_money - c.out_money + c.add_money - c.subtract_money)}"></c:set>
									<c:set var="current" value="${c.account_balance+c.account_frozen}"></c:set>
								</c:if>
								<c:if test="${!show}">
									<c:set var="jisuan" value="${((c.balance_pre+c.frozen_pre) + c.in_money - c.out_money + c.add_money - c.subtract_money)}"></c:set>
									<c:set var="current" value="${c.balance_select+c.frozen_select}"></c:set>
								</c:if>
								${jisuan}
							</td>
							<c:if test="${show}">
								<td>
									${c.account_balance}
								</td>
								<td>
									${c.account_frozen}
								</td>
								<td class="jiacu" style="color:blue;">
									${c.account_balance+c.account_frozen}<!-- 当日总额，实时-->
								</td>
							</c:if>
							<c:if test="${!show}">
								<td>
									${c.balance_select}
								</td>
								<td>
									${c.frozen_select}
								</td>
								<td class="jiacu" style="color:blue;">
									${c.balance_select+c.frozen_select}<!-- 当日总额，日切 -->
								</td>
							</c:if>
							<td>
								<c:choose>
									<c:when test="${(jisuan-current)>0}">
										<span style="color:red;">失败</span>
										
									</c:when>
									<c:otherwise>
										<span style="color:green;">成功</span>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="15">
							<jsp:include page="/common/page.jsp"></jsp:include></td>
					</tr>
				</tbody>
			</table>
	</div>
	</form>
</body>
