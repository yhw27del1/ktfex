<%@ page language="java" import="java.util.*,com.kmfex.model.*,com.kmfex.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="now" value="<%=new Date() %>"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
	<head>
		<title>凭证打印</title>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			body{
				font-family: "微软雅黑";
			}
			.center{
				margin:0 auto;
				width:760px;
				padding-top:10px;
				text-align: center;
				position: relative;
				margin-top:40px;
				clear: both;
			}
			table,td{
				border:1px solid #000;   
    			border-collapse:collapse;
			}
			#print_button{
				position: fixed;
				right:10px;
				top:10px;
			}
			
			.nextpage{
				page-break-before: always;
				clear: both;
				margin-top:1200px;
			}  
			.w80{
				width:80px;
			}
			.w20{
				width:20px;
			}
			
			.al{
				text-align: left;
			}
			.w240{
				width:240px;
			}
			.float{
				position: absolute;
				right:0;
				top:0;
			}
			.foot{
				float:right;
				margin-right:30px;
			}
			.foot div{
				float:left;
				width:120px;
			}
			.logo{
				position: absolute;
				width:50px;
				height:50px;
				top:0px;
				left:0px;
			}
		</style>
		
	</head>
	<body>
		<input type="button" value="打印" id="print_button"/>
		<div class="center">
			<div class="float">
				<div>第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</div>
				<div>借据编号:${financingBase.code}</div>
			</div>
			<img src="/Static/images/logo.png" class="logo"/>
			<div>昆投互联网金融交易-${financingBase.financier.user.org.shortName}融资借据</div>
			<div>签发日期:<fmt:formatDate value="${financingBase.qianyueDate}" pattern="yyyy年MM月dd日"/></div>
			<table width="100%" style="font-size:13px;">
				<tr>
					<td class="w80">户名</td>
					<td class="al w240">${financingBase.financier.user.org.name}-<script>document.write(name("${financingBase.financier.user.realname}"));</script></td>
					<td rowspan="3" class="w20">投<br/>资<br/>人</td>
					<td class="w80">户名</td>
					<td class="al">&nbsp;
						投资人（${financingBase.haveInvestNum}人）&nbsp;
						公司（${investor_with_530101}人）&nbsp;
						<!--  530105（${investor_with_530105}人）-->
					</td>
				</tr>
				<tr>
					<td>帐号</td>
					<td class="al">
						<c:if test="${menuMap['bankcard']=='inline'}">
								${fn:substring(financingBase.financier.bankAccount,0,4)}
								${fn:substring(financingBase.financier.bankAccount,4,8)}
								${fn:substring(financingBase.financier.bankAccount,8,12)}
								${fn:substring(financingBase.financier.bankAccount,12,-1)}
							</c:if>
							<c:if test="${menuMap['bankcard']!='inline'}">
								<c:choose>
									<c:when test="${fn:length(financingBase.financier.bankAccount) == 0}">
										无
									</c:when>
									<c:when test="${fn:length(financingBase.financier.bankAccount) > 4}">
										<c:out value="****${fn:substring(financingBase.financier.bankAccount,fn:length(financingBase.financier.bankAccount)-4,-1)}" />
									</c:when>
									<c:otherwise>
										****
									</c:otherwise>
								</c:choose>
							</c:if>
					</td>
					<td>帐号</td>
					<td></td>
				</tr>
				<tr>
					<td>开户银行</td>
					<td class="al">${financingBase.financier.banklib.caption}</td>
					<td>开户银行</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="5" class="al" height="50px;" style="font-size:16px;">&nbsp;
					人民币(大小写):
					${financingBase.currenyAmount_daxie }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<fmt:formatNumber value="${financingBase.currenyAmount}" type="currency" pattern="￥#,###,###.00"/>
					</td>
				</tr>
				<tr>
					<td colspan="5" class="al">&nbsp;
						利率:<fmt:formatNumber value="${financingBase.rate}"/>%&nbsp;
						贷款到期日<fmt:formatDate value="${financingBase.daoqiDate }" pattern="yyyy年MM月dd日"/>&nbsp;
						${financingBase.businessType.returnPattern}&nbsp;
						期限:
					         <c:if test="${(financingBase.interestDay)!= 0}">按日计息(${financingBase.interestDay}天)</c:if>
			                 <c:if test="${(financingBase.interestDay)== 0}">${financingBase.businessType.term}个月 </c:if>   
					</td>
				</tr>
				<tr>
					<td colspan="2" class="al">&nbsp;
						备注:风险管理费<fmt:formatNumber value="${financingBase.fc.fee1}" type="currency" pattern="￥#,###,###.00"/>
					</td>
					<td colspan="2">担保合同号</td>
					<td></td>
				</tr>
			</table>
			<div class="foot">
				<div>经办:</div>
				<div>复核:</div>
				<div style="width:260px;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			</div>
		</div>
		<div class="center" style="margin-top:120px;">
			<img src="/Static/images/logo.png" class="logo"/>
			<div>昆投互联网金融交易-${financingBase.financier.user.org.shortName}凭证</div>
			<div>签发日期:<fmt:formatDate value="${financingBase.qianyueDate}" pattern="yyyy年MM月dd日"/></div>
			<table width="100%" style="font-size:13px;">
				<tr>
					<td class="w80">户名</td>
					<td class="al w240">${financingBase.financier.user.org.name}-${financingBase.financier.user.realname}</td>
					<td rowspan="3" class="w20">投<br/>资<br/>人</td>
					<td class="w80">户名</td>
					<td class="al">&nbsp;
						投资人（${financingBase.haveInvestNum}人）&nbsp;
						公司（${investor_with_530101}人）&nbsp;
						<!--  530105（${investor_with_530105}人）-->
					</td>
				</tr>
				<tr>
					<td>帐号</td>
					<td class="al">
						${fn:substring(financingBase.financier.bankAccount,0,4)}
						${fn:substring(financingBase.financier.bankAccount,4,8)}
						${fn:substring(financingBase.financier.bankAccount,8,12)}
						${fn:substring(financingBase.financier.bankAccount,12,-1)}
					</td>
					<td>帐号</td>
					<td></td>
				</tr>
				<tr>
					<td>开户银行</td>
					<td class="al">${financingBase.financier.banklib.caption}</td>
					<td>开户银行</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="5" class="al" height="50px;" style="font-size:16px;">&nbsp;
					金额&nbsp;&nbsp;人民币(大小写):
					${lx_all_chinese }
					<fmt:formatNumber value="${lx_all }" pattern="￥#,###,##0.00"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="5" class="al">&nbsp;
					摘要:融资${financingBase.code}&nbsp;&nbsp;应付利息(<c:if test="${(financingBase.interestDay)!= 0}">按日计息(${financingBase.interestDay}天)</c:if>
			                 <c:if test="${(financingBase.interestDay)== 0}">${financingBase.businessType.term}个月 </c:if>   ${financingBase.businessType.returnPattern})
					</td>
				</tr>
			</table>
			<div class="foot">
				<div>经办:</div>
				<div>复核:</div>
				<div style="width:260px;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
<!--
	var obj = document.getElementById("print_button");
	if (window.addEventListener) {
		obj.addEventListener('click', print_action, false);
	} else if (window.attachEvent) {
		obj.attachEvent('onclick', print_action);
	}
	function print_action(){
		obj.style.display="none";
		print();
		obj.style.display="";
	}
	
//-->
</script>
