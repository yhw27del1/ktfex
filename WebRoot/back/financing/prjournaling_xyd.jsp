<%-- 
2012-08-29 修改此文件：修正月还款合计、月换本金合计与实际累加不相等的问题；
增加月收本息与融资服务费和担保方的合计
--%>
<%@ page language="java" import="java.util.*,com.kmfex.model.*,java.text.SimpleDateFormat,com.wisdoor.core.utils.CurrencyOperator" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglib.jsp"%>
<%

	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	String date=dateFormat.format(new Date());


	FinancingBase fb = (FinancingBase)request.getAttribute("financingBase");
	List<InvestRecord> investrecords = (List<InvestRecord>)request.getAttribute("investRecords");
	
	double lx = 0, bj = 0,hj_tz=0,hj_bj=0,hj_lx=0,hj_fj,hj_fee_1=0,hj_fee_2=0;
	
	/**月还本金*/
	for(InvestRecord ir : investrecords){
		ContractKeyData con = ir.getContract();
		if(con.getPayment_method()==2 || con.getPayment_method()==3 || con.getPayment_method()==4 ){//按月待额本息还款
			hj_bj += (double)Math.round(con.getPrincipal_allah_monthly()*100)/100;
			hj_lx += (double)Math.round(con.getInterest_allah_monthly()*100)/100;
		}else {
			hj_bj += (double)Math.round(con.getLoan_allah()*100)/100;
			hj_lx += (double)Math.round(con.getInterest_allah()*100)/100;
		}
	}
%>


<html>
	<head> 
		<title>借款合同汇总</title>
		<link skin="skin" href="/Static/css/themes/flick/jquery-ui-1.8.21.custom.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery.multiselect-1.12/jquery.multiselect.css" />
		<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-ui-1.8.17.custom.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery.chromatable.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.multiselect-1.12/jquery.multiselect.mini.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script>
		
		function doprint(){
			$("#toolbar").hide();
			print();
			$("#toolbar").show();
			
		}
		$(function(){
			/***
			//$("table").chromatable({
			//	width: "100%",
			//	height: "auto",
			//	scrolling: "yes"
			//});
			*/
			$("select").multiselect({
				header: false,
				classes:"padding",
				selectedText:"已选择 # 项",
				click: function(event, ui){
	      			var target = ui.value;
	      			if(ui.checked){
	      				$("#"+target).show();
	      			}else{
	      				$("#"+target).hide()
	      			}
   				}
   			}); 
   			<c:if test="${financingBase.businessType.branch == 4 }">
   				$(".td_head_bxhj").html($(".td_list_bxhj").html());
   				$(".span_lxhj").html( ($(".td_list_bxhj").html() * ${financingBase.businessType.term} -${financingBase.currenyAmount}).toFixed(2) );
   			</c:if>
		});
		</script>
		<style>
body {
	font-size: 13px;
	padding:5px;
	margin:0;
}

.l1 {
	text-align: center;
}

.space {
	padding-left: 50px;
}
 
.title {
	font-size:13.5px;
	font-weight: bold
}
.padding{padding:6px;}
</style>

	</head>
	<body>
		<div style="position: fixed; top: 0px; left: 0px; width: 100%; background-color: #F2F2F2; border-bottom: 1px solid #DCDCDC;" id="toolbar">
			<input type="button" value="打印列表" onclick="doprint()" style="padding: 6px;" />
			<%--<select multiple="multiple" size="5">
				<s:iterator value="#request.financingBase.paymentrecords" var="temp_ps" status="sta">
					<s:if test="#sta.first"><s:set var="first" value="#temp_ps.key"/></s:if>
					<s:if test="#sta.last"><s:set var="last" value="#temp_ps.key"/></s:if>
					<option selected="selected" value="<fmt:formatDate value="${temp_ps.key }" pattern="yyyyMMdd"/>"><fmt:formatDate value="${temp_ps.key }" pattern="yyyy-MM-dd"/></option>
				</s:iterator>
			</select>--%>
		</div>
		<div style="margin: 40px auto;font-weight:bold">
        <p style="text-align: center;margin: 20px auto;">
        <div style="float: left;margin-left: -20px;">
        	<img width="80" height="80" style="margin:2px;margin-left:20px;" src="/Static/images/logo.png">
        </div><span style="text-align: center;">
        	<h2 align="center">昆投互联网金融交易</h2>
        	<h1 align="center">电子合同汇总(${financingBase.createBy.org.shortName}&nbsp;${financingBase.createBy.org.showCoding})</h1>
        </span>
        </p>
        <p style="line-height: 60px;"><div style="float: right;margin: 5px;">制表日期：<%=date%></div><br/></p>						<div >
				<span class="title">融资项目：</span><span class="value">${financingBase.code}</span>
				<span class="space"></span>
				<span class="title">贷款期限：</span>
				<span class="value">
				<c:if test="${(financingBase.interestDay)!= 0}">按日计息(${financingBase.interestDay}天)</c:if>
			    <c:if test="${(financingBase.interestDay)== 0}">${financingBase.businessType.term}个月 </c:if>   
				[<fmt:formatDate value="${financingBase.qianyueDate}" pattern="yyyy-MM-dd"/>至<%--<fmt:formatDate value="${last}" pattern="yyyy-MM-dd"/>--%><fmt:formatDate value="${financingBase.daoqiDate}" pattern="yyyy-MM-dd"/>]</span>
				<span class="space"></span><br/>
				<span class="title">年利率：</span><span class="value">${financingBase.rate}% </span>
				<span class="space"></span>
				<span class="title">还款方式：</span><span class="value">${financingBase.businessType.returnPattern} </span>
				<span class="space"></span>
				<span class="title">滞纳金比例：</span><span class="value">${financingBase.contractTemplate.zhinajinbili} </span>
				<span class="space"></span>
				<span class="title">违约金比例：</span><span class="value">${financingBase.contractTemplate.weiyuejinbili} </span>
			</div>
			<table style="font-size: 13px;font-weight:bold;" border="1px" bordercolor="black">
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
						<c:if test="${financingBase.businessType.branch != 4 }">
						<th>
							月还本金
						</th>
						<th>
							月还利息
						</th>
						</c:if>
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
							<s:if test="#request.financingBase.financier.eName == null">
								<script>document.write(name("${financingBase.financier.pName}"));</script>
							</s:if>
							<s:else>
								<script>document.write(name("${financingBase.financier.eName}"));</script>
							</s:else>
						</td>
						<td>
							${financingBase.financier.user.username}
						</td>
						<td>
							<script>document.write(bankcard("${financingBase.financier.bankAccount}"));</script>
						</td>
						<td>
							<fmt:formatNumber value="${financingBase.currenyAmount}" pattern="#0.00"/>
						</td>
						<c:if test="${financingBase.businessType.branch != 4 }">
						<td>
							<%=String.format("%.2f",hj_bj)%>
						</td>
						<td>
							<%=String.format("%.2f",hj_lx)%>
							
						</td>
						</c:if>
						<c:set value="<%=hj_lx %>" var="hj_lx"/>
						<td class="td_head_bxhj">
							<%=String.format("%.2f",hj_bj + hj_lx)%>
						</td>
					</tr>
				</tbody>
			</table>
			<div >
				<span class="title">融资总额（大写）：<span class="value">${financingBase.currenyAmount_daxie}</span><span class="space"></span><span class="title">￥</span><span class="value"><fmt:formatNumber value="${financingBase.currenyAmount}" pattern="#0.00"/></span>
				</span><br/>
				<span class="title">风险管理费合计：<span class="value"><fmt:formatNumber value="${financingBase.fc.fee1}" pattern="#0.00"/></span><c:if test="${financingBase.fc.fee1 > 0}">[${financingBase.fc.fee1_bl}%]</c:if>${financingBase.fc.fee1_tariff == 1?"[按月]":""}</span>
				<span class="space"></span>
				<span class="title">还款保证金：<span class="value"><fmt:formatNumber value="${financingBase.fc.bzj}" pattern="#0.00"/></span><c:if test="${financingBase.fc.bzj > 0}">[${financingBase.fc.bzj_bl}%]</c:if>${financingBase.fc.bzj_tariff == 1?"[按月]":""}</span>
				<span class="space"></span>
				<span class="title">席位费：</span><span class="value"><fmt:formatNumber value="${financingBase.fc.fee7}" pattern="#0.00" />
				</span>
				<span class="space"></span>
				<span class="title">信用管理费：</span><span class="value"><fmt:formatNumber value="${financingBase.fc.fee10}" pattern="#0.00" />
				</span>
				<span class="space"></span>
				<span class="title">融资服务费：<span class="value"><fmt:formatNumber value="${financingBase.fc.fee2}" pattern="#0.00"/></span><c:if test="${financingBase.fc.fee2 > 0}">[${financingBase.fc.fee2_bl}%]</c:if>${financingBase.fc.fee2_tariff == 1?"[按月]":""}</span>
				<span class="space"></span>
				<span class="title">担保费：<span class="value"><fmt:formatNumber value="${financingBase.fc.fee3}" pattern="#0.00"/></span><c:if test="${financingBase.fc.fee3 > 0}">[${financingBase.fc.fee3_bl}%]</c:if>${financingBase.fc.fee3_tariff == 1?"[按月]":""}</span>
				<span class="space"></span>
				<span class="title">应还利息总额：</span><span class="value span_lxhj"><fmt:formatNumber value="${financingBase.businessType.returnTimes*hj_lx}" pattern="#0.00"/></span>				
				<span class="space"></span>
				<span class="title">融资结余：</span><span class="value"><fmt:formatNumber value="${financingBase.fc.realAmount}" pattern="#0.00"/></span>				
				<c:if test="${financingBase.ii_fee_bl > 0 }">
					<span class="space"></span>
					<span class="title">交易手续费收费比例：</span>
					<span class="value"><fmt:formatNumber value="${financingBase.ii_fee_bl * 100}" pattern="0.##" />%</span>
				</c:if>
			</div>		
			
			<div style="padding: 0; margin: 25px auto;" class="context" >
					<table border="1px" bordercolor="black">
						<thead>
							<tr>
								<th rowspan="2" style="width:30px;">序号</th>
								<th rowspan="2">
									投资方户名
								</th>
								<th rowspan="2">
									帐号（交易）
								</th>
								<th rowspan="2">
									机构编码
								</th>
								<th rowspan="2">
									帐号（银行）
								</th>
								<th rowspan="2">
									投资总额
								</th>
								<th <c:if test="${financingBase.businessType.branch != 4 }">colspan="3"</c:if> style="text-align: center">投资人收益</th>
								<th colspan="3" style="text-align: center">(${financingBase.createBy.org.shortName}+商)共同收益</th>
							</tr>
							<tr>
								<c:if test="${financingBase.businessType.branch != 4 }">
								<th>
									月收本金
								</th>
								<th>
									月收利息
								</th>
								</c:if>
								<th>
									月收本息
								</th>
								<%--<th>
									逾期利息
								</th>
								<th>
									还款总额
								</th>--%>
								<th>融资服务费</th>
								<th>担保费</th>
								<th>风险管理费</th>
							</tr>
						</thead>
						<tbody>
							<!-- 投资人部分   -->
							<c:forEach items="${investRecords}" varStatus="ss" var="ir">
								<%
									hj_tz = 0;
									hj_bj = 0;
									hj_lx = 0;
									hj_fj = 0;
									
								%>
								<tr>
									<td style="text-align: center">
										${ss.count}
									</td>
									<td>
										<script>document.write(name("${ir.contract.first_party}"));</script>
									</td>
									<td>
										${ir.contract.first_party_code}
									</td>
									<td>
										${ir.orgcode}
									</td>
									<td>
										<script>document.write(bankcard("${ir.investor.bankAccount}"));</script>
									</td>
									<td>
										<fmt:formatNumber value="${ir.contract.loan_allah }" pattern="#0.00"/>
										<c:set var="tzze" value="${tzze+ir.contract.loan_allah}"/>
									</td>
									<c:if test="${financingBase.businessType.branch != 4 }">
									<td>
										<c:if test="${ir.contract.payment_method==2}">
											<fmt:formatNumber value="${ir.contract.principal_allah_monthly }" pattern="#0.00" var="bj_temp"/>
										</c:if>
										<c:if test="${ir.contract.payment_method==1}">
											<fmt:formatNumber value="${ir.contract.loan_allah }" pattern="#0.00" var="bj_temp"/>
										</c:if>
										${bj_temp}
										<c:set value="${bjhj+ bj_temp }" var="bjhj"/>
									</td>
									<td>
										<c:if test="${ir.contract.payment_method==2}">
											<fmt:formatNumber value="${ir.contract.interest_allah_monthly}" pattern="#0.00" var="lx_temp"/>
											
										</c:if>
										<c:if test="${ir.contract.payment_method==1}">
											<fmt:formatNumber value="${ir.contract.interest_allah}" pattern="#0.00" var="lx_temp"/>
										</c:if>
										<c:if test="${ir.contract.payment_method==3}">
											<fmt:formatNumber value="${ir.contract.interest_allah_monthly}" pattern="#0.00" var="lx_temp"/>
											
										</c:if>
										 ${lx_temp }
										<c:set value="${lxhj+lx_temp}" var="lxhj"/>
									</td>
									</c:if>
									<td>
										<c:if test="${financingBase.businessType.branch != 4 }">
										<fmt:formatNumber value="${bj_temp+lx_temp}" pattern="#0.00"/>
										<c:set value="${bxhj+bj_temp+lx_temp}" var="bxhj"/>
										</c:if>
										<c:if test="${financingBase.businessType.branch == 4 }">
											<fmt:formatNumber value="${ir.contract.repayment_amount_monthly_allah}" pattern="#0.00"/>
											<c:set value="${bxhj+ir.contract.repayment_amount_monthly_allah}" var="bxhj"/>
										</c:if>
									</td>
									
									<td>
										<fmt:formatNumber value="${ir.contract.service_cost_allah}" pattern="#0.00"/>
										<c:set value="${ir.contract.service_cost_allah + rzfwf}" var="rzfwf"/>
									</td>
									<td>
										<fmt:formatNumber value="${ir.contract.dbf}" pattern="#0.00"/>
										<c:set value="${ir.contract.dbf + dbf}" var="dbf"/>
									</td>
									<td>
										<fmt:formatNumber value="${ir.contract.riskmanagement_cost_allah}" pattern="#0.00"/>
										<c:set value="${ir.contract.riskmanagement_cost_allah + fxglf}" var="fxglf"/>
									</td>
								</tr>
							</c:forEach>
							<!-- 投资人部分   -->
							
							
							<tr>
								<%--<td></td>
								<td></td>
								<td></td>--%>
								<td align="center" colspan="5" style="font-weight:bolder">
									小计
								</td>
								<td>
									<fmt:formatNumber value="${tzze}" pattern="#0.00"/>
								</td>
								<c:if test="${financingBase.businessType.branch != 4 }">
									<td>
										<fmt:formatNumber value="${bjhj}" pattern="#0.00"/>
									</td>
									<td>
										<fmt:formatNumber value="${lxhj}" pattern="#0.00"/>
									</td>
								</c:if>
								<td class="td_list_bxhj">
									<fmt:formatNumber value="${bxhj}" pattern="#0.00"/>
								</td>
								<td>
									<fmt:formatNumber value="${rzfwf }" pattern="#0.00"/>
								</td>
								<td>
									<fmt:formatNumber value="${dbf }" pattern="#0.00"/>
								</td>
								<td>
									<fmt:formatNumber value="${fxglf }" pattern="#0.00"/>
								</td>
							</tr>
							<tr>
								<td align="center" <c:if test="${financingBase.businessType.branch != 4 }">colspan="8"</c:if><c:if test="${financingBase.businessType.branch == 4 }">colspan="6"</c:if> style="font-weight:bolder">每月还款合计</td>
								<td colspan="4" >
									<!--<fmt:formatNumber value="${bjhj+lxhj+rzfwf+dbf+fxglf+bxhj }" pattern="#0.00"/>-->
									<fmt:formatNumber value="${rzfwf+dbf+fxglf+bxhj }" pattern="#0.00"/>
									
								</td>
							</tr>
							
						</tbody>
					</table>
				</div>
			
			
			
			
			
			
		</div>
	</body>
</html>
