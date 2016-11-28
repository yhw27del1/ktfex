<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="now" value="<%=new java.util.Date()%>" />
<html>
	<head>
		<title>融资项目待还款记录</title>
		<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script src="/Static/js/jQuery_thickbox3.1/js/thickbox-compressed.js" type="text/javascript"></script>
		<link href="/Static/js/jQuery_thickbox3.1/style/thickbox.css" media="screen" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script>
		
		function doprint(){
			$("#toolbar").hide();
			$(".noClassPint").hide();
			var now = new Date();
			var now_time = now.getFullYear()+"-"
				+((now.getMonth()+1)>9?(now.getMonth()+1):"0"+(now.getMonth()+1))+"-"
				+(now.getDate()>9?now.getDate():"0"+now.getDate())+" "
				+(now.getHours()>9?now.getHours():"0"+now.getHours())+":"
				+(now.getMinutes()>9?now.getMinutes():"0"+now.getMinutes())+":"
				+(now.getSeconds()>9?now.getSeconds():"0"+now.getSeconds());
			$("#create_time").html(now_time);
			now_time = now = null;
			print();  
			$("#toolbar").show();
			$(".noClassPint").show();
			
		}
		
		function close_children_window(){
			tb_remove();
		}
		</script>

		<style>
body {
	font-size: 13px;
	padding: 5px;
	margin: 0;
}

.l1 {
	text-align: center;
}

.space {
	padding-left: 20px;
}

.title,.value {
	font-size: 13px;
	
}

.padding {
	padding: 6px;
}

.tr_on_selected {
	background: #f7f7f7;
	font-weight: bold;
}

#toolbar{
	position: fixed;
	top: 0px;
	left: 0px;
	width: 100%;
	background-color: #F2F2F2;
	border-bottom: 1px solid #DCDCDC;
	padding-bottom:2px;
	height:40px;
	line-height:40px;
	z-index:90;
}

.float_div_1{
	border:1px solid #cccccc;float:left;padding:0 10px 0 10px;
}

</style>

	</head>
	<body>
		<div style="" id="toolbar">
			<div class="float_div_1">
			<a href="javascript:;" onclick="doprint()">打印列表</a>
			<c:if test="${menuMap['payhandler']=='inline'}">
				<a href="/back/paymentRecord/paymentRecordAction!template?id=${id}&keepThis=true&TB_iframe=true&height=600&width=1200&modal=true" title="还款记录模板" class="thickbox">还款模板</a>
			</c:if>
			</div>
			<div class="float_div_1" style="margin-left:10px;">
				<form action="/back/paymentRecord/paymentRecordAction!list_paymentRecord" id="succession_form" method="post">
				<input type="hidden" name="id" value="${id}"/>
				筛选第
				<input type="text" value="${succession}" name="succession" style="width:40px;text-align: center"/>
				期
				<a href="javascript:;" onclick="succession_form.submit();">查询</a>
				</form>
			</div>
		</div>
		<div style="margin: 50px auto;">
			<p style="text-align: center; margin: 20px auto;">
			<div style="float: right">
				<img width="80" height="80" style="position: relative; right: 30px;" src="/Static/images/logo.png">
			</div>
			<span style="text-align: center;"><h2 align="center">
					昆投互联网金融交易
				</h2>
				<h1 align="center">
					还款明细
				</h1>
			</span>
			</p>
			<p style="line-height: 60px;">
			<div style="float: left; margin: 0px;">
				会计日期：
				<span id="create_time"><fmt:formatDate value="${now}" pattern="yyyy/MM/dd"/></span>
			</div>
			<br />
			</p>
			<p style="line-height: 60px;">
			<div>
				<span class="title">担保机构：</span><span class="value">${financingbase.create_org_name}</span>
				<span class="space"></span>
				<span class="title">融资项目：</span><span class="value">${financingbase.fb_code}</span>
				<span class="space"></span>
				<span class="title">贷款期限：</span>
				<span class="value">
				    <c:if test="${(financingbase.fb_interestday)!= 0}">${financingbase.fb_interestday}天</c:if>
			        <c:if test="${(financingbase.fb_interestday)== 0}">${financingbase.bt_term}月</c:if> 
				</span>
				<span class="space"></span>
				<span class="title">年利率：</span><span class="value">${financingbase.fb_rate}% </span>
				<span class="space"></span>
				<span class="title">还款方式：</span><span class="value">${financingbase.bt_returnpattern} </span>
				<span class="space"></span>
				<span class="title">滞纳金计算比例:</span><span class="value">${financingbase.ct_zhinajinbili}</span>
				<span class="space"></span>
				<span class="title">违约金计算方式:</span><span class="value">${financingbase.ct_weiyuejinbili}</span>
			</div>
			<table style="font-size: 13px; font-weight: bold">
				<thead>
					<tr>
						<th>
							项目简称
						</th>
						<th>
							融资方户名
						</th>
						<th>
							帐号（交易）
						</th>
						<th>
							帐号（银行）
						</th>
						<th>
							融资总额
						</th>
						<th>
							月还本金
						</th>
						<th>
							月还利息
						</th>
						<th>
							月还本息
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							${financingbase.fb_shortname }
						</td>
						<td>
							<c:if test="${financingbase.fm_ename != null}">
								<script>document.write(name("${financingbase.fm_ename}"));</script>
							</c:if>
							<c:if test="${financingbase.fm_ename == null}">
								<script>document.write(name("${financingbase.fm_pname}"));</script>
							</c:if>
						</td>
						<td>
							${financingbase.fu_username}
						</td>
						<td>
							<script>document.write(bankcard("${financingbase.fm_bankaccount}"));</script>
						</td>
						<td>
							<fmt:formatNumber value="${financingbase.fb_currentamount}" pattern="#,###,##0.00"/>
						</td>
						<td id="td_bj">
							<fmt:formatNumber value="${financingbase.xybj}" pattern="#,##0.00"/>
						</td>
						<td id="td_lx">
							<fmt:formatNumber value="${financingbase.xylx}" pattern="#,##0.00"/>
						</td>
						<td id="td_bx">
						    <fmt:formatNumber value="${financingbase.xybj+financingbase.xylx}" pattern="#,##0.00"/>
						</td>
					</tr>
				</tbody>
			</table>
			
			<c:forEach items="${list}" var="group">
				<div style="margin-top:20px;">
					<span class="title">应还时间：</span>
					<span class="value"><fmt:formatDate value="${yhsj}" pattern="yyyy-MM-dd" /> </span>
					<span><c:if test="${group.key!=0}">组${group.key}</c:if></span>
					<span>期次${financingbase.bt_returntimes}-${succession}</span>
				</div>

				<table>
					<thead>
						<tr>
							<th style="width: 10px; overflow: hidden">
							</th>
							<th>
								投标方
							</th>
							<th>
								投标额
							</th>
							<th>
								本金
							</th>
							<th>
								利息
							</th>
							<th>
								本息合计
							</th>
							<th>
								罚金

							</th>
							<th>
								融资服务费
							</th>
							<th>
								罚金
							</th>
							<th>
								担保费
							</th>
							<th>
								罚金
							</th>
							<th>
								风险管理费
							</th>
							<th>
								罚金
							</th>
							<th>
								状态
							</th>
							<th>
								实还时间
							</th>
							<!-- 
								<th class="noClassPint">
									投标合同
								</th>
								 -->
							<th>
								公共备注
							</th>
							<th>
								私有备注
							</th>
						</tr>

					</thead>
					<tbody>
						<c:set value="0" var="hj_tz" />
						<c:set value="0" var="hj_lx" />
						<c:set value="0" var="hj_bj" />
						<c:set value="0" var="hj_fj" />
						<c:set value="0" var="fee_1" />
						<c:set value="0" var="hj_zhglf_fj" />
						<c:set value="0" var="fee_2" />
						<c:set value="0" var="hj_dbf_fj" />
						<c:set value="0" var="fee_3" />
						<c:set value="0" var="hj_fee_3_fj" />
						<c:forEach items="${group.value}" var="item" varStatus="state">
							<tr>
								<td>
									${state.count }
								</td>
								<td>
									<script>document.write(name("${item.realname}"));</script>
								</td>
								<td class="shengoue">
									<fmt:formatNumber value="${item.investamount }" pattern="#,##0.00" />
									<c:set value="${hj_tz + item.investamount}" var="hj_tz" />

								</td>
								<td class="bj" writeable="true">
									<c:set var="hj_bj" value="${hj_bj+item.shbj}" />
									<fmt:formatNumber value="${item.shbj}" pattern="#,##0.00" />

								</td>
								<td class="lx" writeable="true">
									<c:set var="hj_lx" value="${hj_lx+item.shlx}" />
									<fmt:formatNumber value="${item.shlx}" pattern="#,##0.00" />
								</td>
								<td class="paid_debt">
									<fmt:formatNumber value="${item.shbj+item.shlx}" pattern="#,##0.00" />
								</td>
								<td class="penal" writeable="true">
									<c:set var="hj_fj" value="${hj_fj+item.penal}" />
									<fmt:formatNumber value="${item.penal}" pattern="#,##0.00" />
								</td>
								<td class="rzfwf" writeable="true">
									<c:if test="${item.state != 0}">
										<c:set var="fee_1" value="${fee_1+item.fee_1}" />
										<fmt:formatNumber value="${item.fee_1}" pattern="#,##0.00" />
									</c:if>
									<c:if test="${item.state == 0}">
											0.00
										</c:if>
								</td>
								<td class="zhglf_fj" writeable="true">
									<c:set var="hj_zhglf_fj" value="${hj_zhglf_fj+item.zhglf_fj}" />
									<fmt:formatNumber value="${item.zhglf_fj}" pattern="#,##0.00" />
								</td>
								<td class="dbf" writeable="true">
									<c:if test="${item.state != 0}">
										<c:set var="fee_2" value="${fee_2+item.fee_2}" />
										<fmt:formatNumber value="${item.fee_2}" pattern="#,##0.00" />
									</c:if>
									<c:if test="${item.state == 0}">
											0.00
										</c:if>
								</td>
								<td class="dbf_fj" writeable="true">
									<c:set var="hj_dbf_fj" value="${hj_dbf_fj+item.dbf_fj}" />
									<fmt:formatNumber value="${item.dbf_fj}" pattern="#,##0.00" />
								</td>
								<td class="fxglf" writeable="true">
									<c:if test="${item.state != 0}">
										<c:set var="fee_3" value="${fee_3+item.fee_3}" />
										<fmt:formatNumber value="${item.fee_3}" pattern="#,##0.00" />
									</c:if>
									<c:if test="${item.state == 0}">
											0.00
										</c:if>
								</td>
								<td class="fxglf_fj" writeable="true">
									<c:set var="hj_fee_3_fj" value="${hj_fee_3_fj+item.fee_3_fj}" />
									<fmt:formatNumber value="${item.fee_3_fj}" pattern="#,##0.00" />
								</td>
								<td>
									<c:if test="${item.state == 0}">未还款</c:if>
									<c:if test="${item.state == 1}">正常还款</c:if>
									<c:if test="${item.state == 2}">提前还款</c:if>
									<c:if test="${item.state == 3}">
											逾期${item.overdue_days}天
										</c:if>
									<c:if test="${item.state == 4}">担保代偿</c:if>
								</td>
								<td>
									<fmt:formatDate value="${ item.actually_repayment_date}" pattern="yyyy-MM-dd" />
								</td>
								<!--
									<td class="noClassPint">
										<a href="/back/investBaseAction!agreement_ui2?invest_record_id=${p.investRecord.id}" target="_blank">${p.investRecord.contract.contract_numbers}</a>
									</td>
									 -->
								<td>
									${item.remark_}
								</td>
								<td>
									${item.remark2}
								</td>

							</tr>
						</c:forEach>

					</tbody>
					<tfoot>
						<tr>
							<th></th>
							<th>
								合计
							</th>
							<th class="shengoue_hj">
								<fmt:formatNumber value="${hj_tz}" pattern="#,##0.00" />
							</th>
							<th class="hj_bj">
								<fmt:formatNumber value="${hj_bj}" pattern="#,##0.00" />

							</th>
							<th class="lx_all">
								<fmt:formatNumber value="${hj_lx}" pattern="#,##0.00" />

							</th>
							<th class="bxhj_all">
								<fmt:formatNumber value="${hj_bj+hj_lx}" pattern="#,##0.00" />
							</th>
							<th class="penal_all">
								<fmt:formatNumber value="${hj_fj}" pattern="#,##0.00" />
							</th>
							<th class="fee_1_all">
								<fmt:formatNumber value="${fee_1}" pattern="#,##0.00" />
							</th>
							<th class="zhglf_fj_all">
								<fmt:formatNumber value="${hj_zhglf_fj}" pattern="#,##0.00" />
							</th>
							<th class="fee_2_all">
								<fmt:formatNumber value="${fee_2}" pattern="#,##0.00" />
							</th>
							<th class="dbf_fj_all">
								<fmt:formatNumber value="${hj_dbf_fj}" pattern="#,##0.00" />
							</th>
							<th class="fee_3_all">
								<fmt:formatNumber value="${fee_3}" pattern="#,##0.00" />
							</th>
							<th class="fee_3_fj_all">
								<fmt:formatNumber value="${hj_fee_3_fj}" pattern="#,##0.00" />
							</th>
							<th colspan="4">
								<span style="margin-right: 10px;">还款合计:</span>
								<span class="all_all"><fmt:formatNumber value="${hj_lx+hj_bj+hj_fj+fee_1+hj_zhglf_fj+fee_2+hj_dbf_fj+fee_3+hj_fee_3_fj}" pattern="#,##0.00" /> </span>
							</th>
						</tr>
					</tfoot>
				</table>
				<c:if test="${financingbase.code=='X12000059'}">
					<c:if test="${item.succession=='1'}">
						<tr>
							<td>
								逾期4天2012-10-10已还三天，2012-11-1补划一天，补还款合计：5.32元，特此说明
							</td>
						</tr>
					</c:if>
				</c:if>
			 </div>				
			 </c:forEach>
			 <div style="float:right">
			 	<div style="float:left;width:140px">操作员:${session.LOGININFO.realname}</div><div style="margin-left:20px;float:left;width:140px">复核员:</div><div style="float:left;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			 </div>
		</div>
	</body>
</html>

