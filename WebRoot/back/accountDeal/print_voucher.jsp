<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="now" value="<%=new Date() %>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
	<head>
		<title>凭证打印</title>
		<style>
			body{
				font-family: "微软雅黑";
				padding:0;
				margin:0;
			}
			.center{
				margin:0 auto;
				width:760px;
				padding-top:10px;
				text-align: center;
				position: relative;
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
			.lineheight{
				line-height:28px;
			}
			.nextpage{
				page-break-before: always;
				clear: both;
				margin-top:40px;
			}  
			.print_t,.print_d{
				margin:10px 0 10px 0;
				font-size:13px;
			} 
			
			.print_t{
				font-size:22px;
				font-weight: bold;
			}
			#logo{
				position: absolute;
				right:10px;
				top:10px;
				width:60px;
				height:60px;
			}
			.left{
				float:left;
				outline: none;
				
			}
			.height45{
				height:45px;
			}
		</style>
		
	</head>
	<body>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<input type="button" value="打印" id="print_button"/>
		<c:forEach items="${deals}" var="accountDeal" varStatus="status">
		<div class="center <c:if test="${status.count!=1}">nextpage</c:if>">
			<img src="/Static/images/logo.png" id="logo"/>
			<div class="print_t">
			
			<c:choose>
				<c:when test="${accountDeal.type=='提现' && usertype=='T'}">
					昆投互联网金融交易投资提现凭证
				</c:when>
				<c:when test="${accountDeal.type=='提现' && usertype=='R'}">
					昆投互联网金融交易融资提现凭证
				</c:when>
				<c:when test="${accountDeal.type=='现金充值' && usertype=='T'}">
					昆投互联网金融交易充值凭证
				</c:when>
				<c:when test="${accountDeal.type=='现金充值' && usertype=='R'}">
					昆投互联网金融交易融资充值凭证
				</c:when>
				<c:when test="${accountDeal.type=='现金充值' && usertype=='D'}">
					昆投互联网金融交易融资充值凭证
				</c:when>
			</c:choose>
			</div>
			<div class="print_d">日期:&nbsp;&nbsp;<fmt:formatDate value="${date}" pattern="yyyy年  MM月  dd日" /></div>
			<h1 style="position:absolute;left:0;top:0;margin:0;font-size:60px">
				<c:choose>
					<c:when test="${usertype=='R'}">
						融
					</c:when>
					<c:when test="${usertype=='T'}">
						投
					</c:when>
					<c:when test="${usertype=='D'}">
						担
					</c:when>
				</c:choose>
			</h1>
			
			<table width="100%" style="font-size:13px;">
				<tr>
					<td rowspan="3" width="100" style="text-align: center" valign="middle">
						<div class="lineheight">交易</div>
						<div class="lineheight">帐户</div>
						<div class="lineheight">信息</div>
					</td>
					<td width="100" height="28" style="text-align: center" valign="middle">户名</td>
					<td align="left" style="font-size:16px">
						<script>document.write(name("${accountDeal.account.user.realname}"));</script>
						<c:if test="${accountDeal.account.user.username == '44030000001'}">(夏琳)</c:if>
					</td>
				</tr>
				<tr>
					<td height="28" style="text-align: center" valign="middle">交易帐号</td>
					<td align="left">
						
						<div class="left">(${accountDeal.account.user.username})</div>
						<div class="left" contentEditable="true">
						<c:if test="${menuMap['name']=='inline'}">
							${fn:substring(accountDeal.bankAccount,0,4)}
							${fn:substring(accountDeal.bankAccount,4,8)}
							${fn:substring(accountDeal.bankAccount,8,12)}
							${fn:substring(accountDeal.bankAccount,12,-1)}
						</c:if>
						<c:if test="${menuMap['bankcard']!='inline'}">
							<c:choose>
								<c:when test="${fn:length(accountDeal.bankAccount) == 0}">
									无
								</c:when>
								<c:when test="${fn:length(accountDeal.bankAccount) > 4}">
									<c:out value="****${fn:substring(accountDeal.bankAccount,fn:length(accountDeal.bankAccount)-4,-1)}" />
								</c:when>
								<c:otherwise>
									****
								</c:otherwise>
							</c:choose>
						</c:if>
						</div>
					</td>
				</tr>
				<tr>
					<td height="28" style="text-align: center" valign="middle">开户银行</td>
					<td align="left">${accountDeal.bank}</td>
				</tr>
				<tr>
					<td colspan="2" height="36" style="text-align: center" valign="middle">金额</td>
					<td align="left">人民币（大小写）
					${accountDeal.money_upcase }
					<span style="margin-left:30px;font-size:16px;">¥<fmt:formatNumber value='${accountDeal.money}' pattern="#,###,###,##0.00"/></span>
					</td>
				</tr>
				<tr>
					<td colspan="3" height="50" valign="top" align="left">
						
						<div contentEditable="true" class="left height45">
						<c:choose>
							<c:when test="${accountDeal.type=='提现' && usertype=='T'}">
								摘要:投资提现
							</c:when>
							<c:when test="${accountDeal.type=='现金充值' && usertype=='T'}">
								摘要:投资充值
							</c:when>
							<c:when test="${accountDeal.type=='现金充值' && usertype=='R'}">
								
								融资项目号：${fcode }<span style="margin-left:20px;"></span>
								摘要:融资还款充值
								
							</c:when>
							<c:when test="${accountDeal.type=='现金充值' && usertype=='D'}">
								
								融资项目号：${fcode }<span style="margin-left:20px;"></span>
								摘要:融资充值
								
							</c:when>
							<c:when test="${accountDeal.type=='提现' && usertype=='R'}">
								摘要:${accountDeal.memo}
								<c:if test="${accountDeal.financing!=null}">
									<span>${accountDeal.financing.code}</span>
								</c:if>
								<c:if test="${cost!=null}">
									<span style="margin-left:30px;">风险管理费:
									<c:choose>
										<c:when test="${fn:startsWith(cost.financingBase.code,'X')}">
 											<fmt:formatNumber value="${cost.fee1 }" type="currency" currencySymbol="¥" />
										</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${cost.fxglf }" type="currency" currencySymbol="¥" />
										</c:otherwise>
									</c:choose>   
									</span>
								</c:if>
							</c:when>
						
						</c:choose>
						</div>
					</td>
				</tr>
			</table>
			<div style="float:left;width:140px;margin-left:160px;margin-top:5px;font-size:12px;">经办:
			${accountDeal.checkUser.realname}</div>
			<div style="float:left;margin-left:30px;width:140px;margin-top:5px;font-size:12px;">复核:
			${accountDeal.hkUser.realname}</div>
			<div style="float:left;margin-left:30px;margin-top:5px;font-size:12px;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
		</div>
	</c:forEach>
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
