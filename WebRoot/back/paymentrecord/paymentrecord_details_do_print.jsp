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
			$(function(){
				$("#print").click(function(){
					$(this).hide();
					print();
					$(this).show();
				});
			});
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
	padding-left: 50px;
}

.title {
	font-size: 13.5px;
	font-weight: bold
}

.padding {
	padding: 6px;
}

.tr_on_selected {
	background: #f7f7f7;
	font-weight: bold;
}

.float_div_1{
	border:1px solid #cccccc;float:left;padding:0 10px 0 10px;
}
.next-page{
	page-break-after:always;
}
#print{
	position: fixed;
}
</style>

	</head>
	<body>
		<input type="button" value="打印" id="print"/>
		<c:forEach items="${records}" var="financingBase" varStatus="state_">
		<div style="margin: 50px auto; font-weight: bold;">
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
				<span class="title">融资项目：</span><span class="value">${financingBase.code}</span>
				<span class="space"></span>
				<span class="title">贷款期限：</span>
				<span class="value">
				    <c:if test="${(financingBase.interestDay)!= 0}">${financingBase.interestDay}天</c:if>
			        <c:if test="${(financingBase.interestDay)== 0}">${financingBase.businessType.term}月</c:if> 
				</span>
				<span class="space"></span>
				<span class="title">年利率：</span><span class="value">${financingBase.rate}% </span>
				<span class="space"></span>
				<span class="title">还款方式：</span><span class="value">${financingBase.businessType.returnPattern} </span>
				<span class="space"></span>
				<span class="title">滞纳金计算比例:</span><span class="value">${financingBase.contractTemplate.zhinajinbili}</span>
				<span class="space"></span>
				<span class="title">违约金计算方式:</span><span class="value">${financingBase.contractTemplate.weiyuejinbili}</span>
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
							${financingBase.shortName }
						</td>
						<td>
							<c:if test="${financingBase.financier.eName != null}">
								<script>document.write(name("${financingBase.financier.eName}"));</script>
							</c:if>
							<c:if test="${financingBase.financier.eName == null}">
								<script>document.write(name("${financingBase.financier.pName}"));</script>
							</c:if>
						</td>
						<td>
							${financingBase.financier.user.username}
						</td>
						<td>
							<script>document.write(bankcard("${financingBase.financier.bankAccount}"));</script>
						</td>
						<td>
							${financingBase.currenyAmount}
						</td>
						<td id="td_bj">
						
							<fmt:formatNumber value="${benjin_lixi[financingBase.id][0]}" pattern="#,##0.00"/>
						</td>
						<td id="td_lx">
							<fmt:formatNumber value="${benjin_lixi[financingBase.id][1]}" pattern="#,##0.00"/>
						</td>
						<td id="td_bx">
							<fmt:formatNumber value="${benjin_lixi[financingBase.id][0]+benjin_lixi[financingBase.id][1]}" pattern="#,##0.00"/>
						</td>
					</tr>
				</tbody>
			</table>
			<c:forEach items="${financingBase.paymentrecords}" var="ps">
				<c:set value="0" var="hj_tz"/>
				<c:set value="0" var="hj_lx"/>
				<c:set value="0" var="hj_bj"/>
				<c:set value="0" var="hj_fj"/>
				<c:set value="0" var="fee_1"/>
				<c:set value="0" var="hj_zhglf_fj"/>
				<c:set value="0" var="fee_2"/>
				<c:set value="0" var="hj_dbf_fj"/>
				<div style="padding: 0; margin: 30px auto; " class="context" >
					<div>
						<span class="title">应还时间：</span>
						<span class="value"><fmt:formatDate value="${ps.key.date}" pattern="yyyy-MM-dd" /> </span>
						<span><c:if test="${ps.key.group!=0}">组${ps.key.group}</c:if></span>
					</div>

					<table>
						<thead>
							<tr>
								<th style="width:10px;overflow: hidden">
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
									期次
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
									备注
								</th>
							</tr> 

						</thead>
						<tbody>
						
							
							
							<c:forEach items="${ps.value}" var="p" varStatus="state">
								<tr>
									<td>
										${state.count }
									</td>
									<td>
										<script>document.write(name("${p.beneficiary_name}"));</script>
									</td>
									<td class="shengoue">
										<fmt:formatNumber value="${p.investRecord.investAmount }" pattern="#,##0.00"/>
										<c:set value="${hj_tz + p.investRecord.investAmount}" var="hj_tz"/>
										
									</td>
									<td class="bj" writeable="true">
										<c:set var="hj_bj" value="${hj_bj+p.xybj}"/>
										<fmt:formatNumber value="${p.xybj}" pattern="#,##0.00"/>
									</td>
									<td class="lx" writeable="true">
										<c:set var="hj_lx" value="${hj_lx+p.xylx}"/>
										<fmt:formatNumber value="${p.xylx}" pattern="#,##0.00"/>
									</td>
									<td class="paid_debt">
										<fmt:formatNumber value="${p.xybj+p.xylx}" pattern="#,##0.00"/>
									</td>
									<td class="penal" writeable="true">
										<c:set var="hj_fj" value="${hj_fj+p.penal}"/>
										<fmt:formatNumber value="${p.penal}" pattern="#,##0.00"/>
									</td>
									<td class="rzfwf" writeable="true">
										<c:set var="fee_1" value="${fee_1+p.fee_1}"/>
										<fmt:formatNumber value="${p.fee_1}" pattern="#,##0.00"/>
									</td>
									<td class="zhglf_fj" writeable="true">
										<c:set var="hj_zhglf_fj" value="${hj_zhglf_fj+p.zhglf_fj}"/>
										<fmt:formatNumber value="${p.zhglf_fj}" pattern="#,##0.00"/>
									</td>
									<td class="dbf" writeable="true">
										<c:set var="fee_2" value="${fee_2+p.fee_2}"/>
										<fmt:formatNumber value="${p.fee_2}" pattern="#,##0.00"/>
									</td>
									<td class="dbf_fj" writeable="true">
										<c:set var="hj_dbf_fj" value="${hj_dbf_fj+p.dbf_fj}"/>
										<fmt:formatNumber value="${p.dbf_fj}" pattern="#,##0.00"/>
									</td>
									<td class="fxglf" writeable="true">
										<c:set var="fee_3" value="${fee_3+p.fee_3}"/>
										<fmt:formatNumber value="${p.fee_3}" pattern="#,##0.00"/>
									</td>
									<td class="fxglf_fj" writeable="true">
										<c:set var="hj_fxglf_fj" value="${hj_fxglf_fj+p.fee_3_fj}"/>
										<fmt:formatNumber value="${p.fee_3_fj}" pattern="#,##0.00"/>
									</td>
									<td>
										${financingBase.businessType.returnTimes}-${p.succession}
									</td>
									<td>
										<c:if test="${p.state == 0}">未还款</c:if>
										<c:if test="${p.state == 1}">正常还款</c:if>
										<c:if test="${p.state == 2}">提前还款</c:if>
										<c:if test="${p.state == 3}">
											逾期${p.overdue_days}天
										</c:if>
										<c:if test="${p.state == 4}">担保代偿</c:if>
									</td>
									<td>
										<fmt:formatDate value="${ p.actually_repayment_date}" pattern="yyyy-MM-dd" />
									</td>
									<!--
									<td class="noClassPint">
										<a href="/back/investBaseAction!agreement_ui2?invest_record_id=${p.investRecord.id}" target="_blank">${p.investRecord.contract.contract_numbers}</a>
									</td>
									 -->
									<td>${p.remark}</td>
									
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
									<fmt:formatNumber value="${hj_tz}" pattern="#,##0.00"/>
								</th>
								<th class="hj_bj">
									<fmt:formatNumber value="${hj_bj}" pattern="#,##0.00"/>
									
								</th>
								<th class="lx_all">
								   <fmt:formatNumber value="${hj_lx}" pattern="#,##0.00"/>
								  
								</th>
								<th class="bxhj_all">
									<fmt:formatNumber value="${hj_bj+hj_lx}" pattern="#,##0.00"/>
								</th>
								<th class="penal_all">
									<fmt:formatNumber value="${hj_fj}" pattern="#,##0.00"/>
								</th>
								<th class="fee_1_all"><fmt:formatNumber value="${fee_1}" pattern="#,##0.00"/></th>
								<th class="zhglf_fj_all"><fmt:formatNumber value="${hj_zhglf_fj}" pattern="#,##0.00"/></th>
								<th class="fee_2_all"><fmt:formatNumber value="${fee_2}" pattern="#,##0.00"/></th>
								<th class="dbf_fj_all"><fmt:formatNumber value="${hj_dbf_fj}" pattern="#,##0.00"/></th>
								<th class="fee_2_all"><fmt:formatNumber value="${fee_3}" pattern="#,##0.00"/></th>
								<th class="dbf_fj_all"><fmt:formatNumber value="${hj_fxglf_fj}" pattern="#,##0.00"/></th>
								<th colspan="4">
									
									 <span style="margin-right:10px;">还款合计:</span>
									 <span class="all_all"><fmt:formatNumber value="${hj_lx+hj_bj+hj_fj+fee_1+hj_zhglf_fj+fee_2+hj_dbf_fj+fee_3+hj_fxglf_fj}" pattern="#,##0.00"/></span>
									
								</th>
								
								
								
							</tr>
						</tfoot> 
					</table>
					<c:if test="${financingBase.code=='X12000059'}">
							    <c:if test="${p.succession=='1'}">
							    <tr><td>逾期4天2012-10-10已还三天，2012-11-1补划一天，补还款合计：5.32元，特此说明</td></tr>
							    </c:if>
					</c:if>
				</div>				
			 </c:forEach>
			 <div style="float:right">
			 	<div style="float:left;width:140px">操作员:${session.LOGININFO.realname}</div><div style="margin-left:20px;float:left;width:140px">复核员:</div><div style="float:left;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			 </div>
		</div>
		<c:if test="${!state_.last}"><div class="next-page"></div></c:if>
		</c:forEach>
	</body>
</html>


