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
		<script type="text/javascript" src="/back/four.jsp"></script>
	</head>
	<body>
		
		<input type="button" value="打印" id="print_button"/>
		<c:forEach items="${deals}" var="accountDeal" varStatus="status">
		<div class="center <c:if test="${status.count!=1}">nextpage</c:if>">
			<img src="/Static/images/logo.png" id="logo"/>
			<div class="print_t">
				昆投互联网金融交易收、付凭证
			</div>
			<div class="print_d">日期:&nbsp;&nbsp;<fmt:formatDate value="${date}" pattern="yyyy年  MM月  dd日" /></div>
			<table width="100%" style="font-size:13px;">
				<tr>
					<td align="center">付</td>
					<td align="center">户名</td>
					<td align="left"><script>document.write(name("${accountDeal.account.user.realname}"));</script></td>
					<td align="center">收</td>
					<td align="center">户名</td>
					<td align="left"><script>document.write(name("${accountDeal.accountDeal.account.user.realname}"));</script></td>
				</tr>
				<tr>
					<td align="center">款</td>
					<td align="center">会员交易号</td>
					<td align="left">${accountDeal.account.user.username}</td>
					<td align="center">款</td>
					<td align="center">会员交易号</td>
					<td align="left">${accountDeal.accountDeal.account.user.username}</td>
				</tr>
				<tr>
					<td align="center">人</td>
					<td align="center">银行账号</td>
					<td align="left">
						<div class="left" contentEditable="true">
							<c:if test="${menuMap['bankcard']=='inline'}">
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
					<td align="center">人</td>
					<td align="center">银行账号</td>
					<td align="left">
						<div class="left" contentEditable="true">
							<c:if test="${menuMap['bankcard']=='inline'}">
								${fn:substring(accountDeal.accountDeal.bankAccount,0,4)}
								${fn:substring(accountDeal.accountDeal.bankAccount,4,8)}
								${fn:substring(accountDeal.accountDeal.bankAccount,8,12)}
								${fn:substring(accountDeal.accountDeal.bankAccount,12,-1)}
							</c:if>
							<c:if test="${menuMap['bankcard']!='inline'}">
								<c:choose>
									<c:when test="${fn:length(accountDeal.accountDeal.bankAccount) == 0}">
										无
									</c:when>
									<c:when test="${fn:length(accountDeal.accountDeal.bankAccount) > 4}">
										<c:out value="****${fn:substring(accountDeal.accountDeal.bankAccount,fn:length(accountDeal.accountDeal.bankAccount)-4,-1)}" />
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
					<td colspan="2" height="36" style="text-align: center" valign="middle">金额</td>
					<td align="left" colspan="4">人民币（大小写）
					${accountDeal.money_upcase }
					<span style="margin-left:30px;font-size:16px;"><fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol="¥" /></span>
					</td>
				</tr>
				<tr>
					<td colspan="6" height="50" valign="top" align="left">
						摘要：${accountDeal.memo}
					</td>
				</tr>
			</table>
			<div style="float:left;width:140px;margin-left:260px;margin-top:5px;font-size:12px;">经办:</div>
			<div style="float:left;width:140px;margin-left:30px;margin-top:5px;font-size:12px;">复核:</div>
			<div style="float:left;margin-left:30px;margin-top:5px;font-size:12px;">主管:</div>
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
