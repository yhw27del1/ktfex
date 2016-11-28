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
	var b = '${state}';
    $("option[value='"+b+"']",$("#state")).attr("selected",true);
	var c = '${trade}';
    $("option[value='"+c+"']",$("#trade")).attr("selected",true);
    //if(b=='success'){
    //	$("#sysqingsuan").show();
    //}else{
    //	$("#sysqingsuan").hide();
    //}
    $("#sysqingsuan").click(function(){
    	$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要执行系统清算操作吗？",
			ok:function(){
    			$("body").showLoading();
				$.post("/back/hxbankAction!sys_liquidation_reconciliation_do",function(data,state){
					alert(data.tip);
					$("body").hideLoading();
				},'json');
			},
			cancelVal:'关闭',cancel:true
		});
    });
    $("#history").click(function(){
    	window.showModalDialog("/back/hxbankAction!sys_liquidation_reconciliation_detail","","dialogWidth=1000px;dialogHeight=600px;");
    });
});
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/Static/js/showloading/jquery.showLoading.min.js"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				关键字&nbsp;<input type="text" id="keyWord" name="keyWord" value="${keyWord}" />&nbsp;
				交易&nbsp;<select name="trade" id="trade"><option value="yes">有交易</option><option value="no">无交易</option><option value="all">全部</option></select>
				状态&nbsp;<select name="state" id="state"><option value="failure">失败</option><option value="success">成功</option><option value="all">全部</option></select>
				<input type="submit" class="ui-state-default" value="查询" />&nbsp;
				<input type="button" id="sysqingsuan" class="ui-state-default" value="系统清算" />&nbsp;
				<input type="button" id="history" class="ui-state-default" value="系统清算历史" />
	</div>
<div>
	<table>
		<tbody>
			<tr id="chargeInput">
				<td style="width: 50%;font-size:14px;">
					<ul>
						<li>原总余额 = 上一日系统总余额 + 当日入金 - 当日出金</li>
						<li>计算总余额 = 原总余额 + 贷总额 - 借总额</li>
						<li>差价 = 计算总余额 - 系统总余额</li>
						<li>状态为失败的记录，应该给此记录的系统可用余额增加或减少差价金额。</li>
						<li>差价为正数，说明应该给系统可用余额增加此差价金额。差价为负数，说明应该给系统可用余额减少此差价金额。</li>
						<li>贷：会员资金增加的业务。借：会员资金减少的业务。</li>
					</ul>
				</td>
			</tr>
		</tbody>
	</table>
</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
						<th>清算日期</th>
						<th>
							用户名
						</th>
						<th>
							名称
						</th>
						<th>
							注册日期
						</th>
						<!-- 
						<th>
							系统冻结金额
						</th>
						<th>
							系统可用余额
						</th>
						 -->
						<th>
							原总余额
						</th>
						<th>
							贷总额
						</th>
						<th>
							贷笔数
						</th> 
						<th>
							借总额
						</th>
						<th>
							借笔数
						</th>
						<th>
							计算总余额
						</th>
						<th>
							系统总余额
						</th>
						<th>
							差价
						</th>
						<th>
							状态
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.result}" var="c" varStatus="sta">
						<tr>
							<td><fmt:formatDate value="${startDate}" type="date" /></td>
							<td>
								${c.username}
							</td>
							<td>
								${c.realname}
							</td>
							<td>
								<fmt:formatDate value="${c.regtime}" type="date" />
							</td>
							<!-- 
							<td>
								${c.frozen_sys}
							</td>
							<td>
								${c.balance_sys}
							</td>
							 -->
							<td class="jiacu red">
								${c.old_balance}
							</td>
							<td class="jiacu red">
								${c.sum_dai}
							</td>
							<td class="jiacu">
								${c.count_dai}
							</td>
							<td class="jiacu green">
								${c.sum_jie}
							</td>
							<td class="jiacu">
								${c.count_jie}
							</td>
							<td>
								${c.balance_jisuan}
							</td>
							<td>
								${c.all_sys}
							</td>
							<td>
								${c.difference}
							</td>
							<td style="${c.difference==0?'color:green;':'color:red;'}">
								${c.state}
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
