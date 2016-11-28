<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<style>
<!--
body{overflow-x:scroll;}
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
	var f = $('#f').val();
	var msg = $("#msg");
	if(f>0){
		msg.text(f+'条失败');
	}else{
		msg.text('全部成功');
	}
	var b = '${type}';
    $("option[value='"+b+"']",$("#type")).attr("selected",true);
    $("#clearsuccess").click(function(){
    	$(".success").parent().parent().remove();
    });
});
</script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				日期&nbsp;<input type="text" name="selectDate" value="<fmt:formatDate value='${selectDate}' type='date' />" id="selectDate"/>
				<input type="text" id="keyWord" name="keyWord" placeholder="会员名称/会员编号" value="${keyWord}" />&nbsp;
				会员类型&nbsp;<select id="type" name="type">
					<option value="T">投资人</option>
					<option value="R">融资方</option>
				</select>
				<input type="hidden" name="load" value="true" />
				<input type="submit" class="ui-state-default" value="查询" />&nbsp;<span id="msg"></span><input id="clearsuccess" type="button" class="ui-state-default" value="清除成功" />
	</div>
	<div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header">
						<th rowspan="2">
							会员编号
						</th>
						<th rowspan="2">
							会员名称
						</th>
						<th colspan="3">
							<fmt:formatDate value="${selectDate_pre}" type="date" pattern="yyyy/MM/dd" />日切余额
						</th>
						<th rowspan="2">
							入金总额
						</th>
						<th rowspan="2">
							出金总额
						</th>
						<th colspan="1">
							贷总额(加钱)
						</th>
						<th colspan="1">
							借总额(减钱)
						</th>
						<th rowspan="2">
							计算余额
						</th>
						<th colspan="3">
							<fmt:formatDate value="${selectDate}" type="date" pattern="yyyy/MM/dd" />${show?'实时':'日切'}余额
						</th>
						<th rowspan="2" class="sort-alpha">
							状态
						</th>
					</tr>
					<tr class="ui-widget-header">
						<th>
							可用
						</th>
						<th>
							冻结
						</th>
						<th>
							总额
						</th>
						<th>
							<c:if test="${type=='T'}">还款等</c:if><c:if test="${type=='R'}">交割等</c:if>
						</th>
						<th>
							<c:if test="${type=='T'}">投标等</c:if><c:if test="${type=='R'}">还款等</c:if>
						</th>
						<th>
							可用
						</th>
						<th>
							冻结
						</th>
						<th>
							总额
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:set var="f" value="0"></c:set>
					<c:forEach items="${result}" var="c" varStatus="sta">
						<tr>
							<td>
								${c.username}
							</td>
							<td>
								<script>document.write(name("${c.realname}"));</script>
							</td>
							<td>
								<fmt:formatNumber value="${c.balance_pre}" pattern="#.##"/>
							</td>
							<td>
								<fmt:formatNumber value="${c.frozen_pre}" pattern="#.##"/>
							</td>
							<td class="jiacu">
								<fmt:formatNumber value="${c.balance_pre+c.frozen_pre}" pattern="#.##"/><!-- 上日总额，日切 -->
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
								<fmt:formatNumber value="${jisuan}" pattern="#.##"/>
								<fmt:formatNumber value="${jisuan}" pattern="#.##" var="jisuan_f"/>
								<fmt:formatNumber value="${current}" pattern="#.##" var="current_f"/>
							</td>
							<c:if test="${show}">
								<td>
									<fmt:formatNumber value="${c.account_balance}" pattern="#.##"/>
								</td>
								<td>
									<fmt:formatNumber value="${c.account_frozen}" pattern="#.##"/>
								</td>
								<td class="jiacu" style="color:blue;">
									<fmt:formatNumber value="${c.account_balance+c.account_frozen}" pattern="#.##"/><!-- 当日总额，实时-->
								</td>
							</c:if>
							<c:if test="${!show}">
								<td>
									<fmt:formatNumber value="${c.balance_select}" pattern="#.##"/>
								</td>
								<td>
									<fmt:formatNumber value="${c.frozen_select}" pattern="#.##"/>
								</td>
								<td class="jiacu" style="color:blue;">
									<fmt:formatNumber value="${c.balance_select+c.frozen_select}" pattern="#.##"/><!-- 当日总额，日切 -->
								</td>
							</c:if>
							<td>
								<c:choose>
									<c:when test="${(jisuan_f-current_f)>0||(jisuan_f-current_f)<0}">
										<span style="color:red;">失败</span>
										<c:set var="f" value="${f+1}"></c:set>
									</c:when>
									<c:otherwise>
										<span style="color:green;" class="success">成功</span>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="text" id="f" value="${f}" style="display: none;" />
	</div>
	</form>
</body>
