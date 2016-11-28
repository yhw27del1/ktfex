<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<%
  response.setHeader("Content-Disposition", "inline;filename="+ request.getAttribute("msg")+".xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style>
   .xlsMoney{mso-number-format:"0\.0";}
   .xlsDate{mso-number-format:"yyyy\/mm\/dd hh\:mm\:ss";}
   .xlsText{mso-number-format:"\@";} 
</style>
<body>
<form action="">
 

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
					 <tr style="width: 100%;">
				<td align="center" colspan="11">
					<div>
					      会员账户查询(${msg})单位:元   
					</div>
				</td>
				</tr> 
			<thead>
				<tr class="ui-widget-header ">
					<th>
						交易账户
					</th>
					<th>
						会员名称
					</th>
					<th>
						银行账号
					</th>
					<th>
						交易类型
					</th>
					<th>
						存入
					</th>
					<th>
						支出
					</th>
					<th>
						结余
					</th>
					<th>
						操作者
					</th>
					<th>
						审核者
					</th>
					<th>
						日期
					</th>
					<th>
						状态
					</th>
					<th>
						备注
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="accountDeal">
					<tr>
						<td>
							${accountDeal.account.user.username}
						</td>
						<td>
						    <c:if test="${menuMap['name']=='inline'}">
								${accountDeal.account.user.realname}
							</c:if>
							<c:if test="${menuMap['name']!='inline'}">
								<c:choose>
									<c:when test="${fn:length(accountDeal.account.user.realname) == 0}">
										无
									</c:when>
									<c:when test="${fn:length(accountDeal.account.user.realname) > 1}">
										<c:out value="${fn:substring(accountDeal.account.user.realname,0,1)}****" />
									</c:when>
									<c:otherwise>
										****
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<td>
						     ${accountDeal.bankAccount}
						     <c:if test="${menuMap['bankcard']=='inline'}">
								${accountDeal.bankAccount}
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
						</td>
						<td>
							${accountDeal.type}
						</td>
						<td>
							<c:if test="${accountDeal.zf!=\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/><br />
								<c:if test="${accountDeal.checkFlag=='9'}">本:<fmt:formatNumber value='${accountDeal.bj}' type="currency" currencySymbol=""/> 息:<fmt:formatNumber value='${accountDeal.lx}' type="currency" currencySymbol=""/><c:if test="${accountDeal.fj>0}"> 罚金:<fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if></c:if>
							</c:if>
						</td>
						<td>
							<c:if test="${accountDeal.zf==\"-\"}">
								<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/><br />
								<c:if test="${accountDeal.checkFlag=='6'}">本:<fmt:formatNumber value='${accountDeal.bj}' type="currency" currencySymbol=""/> 息:<fmt:formatNumber value='${accountDeal.lx}' type="currency" currencySymbol=""/><c:if test="${accountDeal.fj>0}"> 罚金:<fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if></c:if>
								<c:if test="${accountDeal.checkFlag=='15'&&accountDeal.fj>0}">其中罚金:<fmt:formatNumber value='${accountDeal.fj}' type="currency" currencySymbol=""/></c:if>
							</c:if>
						</td>
						<td>
						 	<fmt:formatNumber value='${accountDeal.nextMoney}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${accountDeal.user.realname}
						</td>
						<td>
							${accountDeal.checkUser.realname}
						</td>
						<td>
							<fmt:formatDate value="${accountDeal.createDate}" type="date" />
						</td>
						<td>
							<c:if test="${accountDeal.checkFlag=='0'}"><span style="color:#4169E1;">充值待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='1'}"><span style="color:green;">充值已审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='2'}"><span style="color:red;">充值已驳回 </span></c:if>
							<c:if test="${accountDeal.checkFlag=='3'}"><span style="color:#4169E1;">提现待审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='4'}"><span style="color:green;">提现已审核</span></c:if>
							<c:if test="${accountDeal.checkFlag=='5'}"><span style="color:red;">提现已驳回 </span></c:if>
							<c:if test="${accountDeal.checkFlag=='22'||accountDeal.checkFlag=='23'}">
								<c:if test="${accountDeal.checkFlag2=='0'}"><span style="color:#4169E1;">转账待审核</span></c:if>
								<c:if test="${accountDeal.checkFlag2=='3'}"><span style="color:green;">转账已审核</span></c:if>
								<c:if test="${accountDeal.checkFlag2=='4'}"><span style="color:red;">转账已驳回</span></c:if>
							</c:if>
							<c:if test="${accountDeal.checkFlag=='30'||accountDeal.checkFlag=='33'||accountDeal.checkFlag=='36'||accountDeal.checkFlag=='40'}">
								<span style="color:#4169E1;">待审核</span>
							</c:if>
							<c:if test="${accountDeal.checkFlag=='31'||accountDeal.checkFlag=='34'||accountDeal.checkFlag=='37'||accountDeal.checkFlag=='41'||accountDeal.checkFlag=='43'}">
								<span style="color:green;">已审核</span>
							</c:if>
							<c:if test="${accountDeal.checkFlag=='32'||accountDeal.checkFlag=='35'||accountDeal.checkFlag=='38'||accountDeal.checkFlag=='42'}">
								<span style="color:red;">已驳回</span>
							</c:if>
						</td>
						<td>
							${accountDeal.memo} 
							<c:if test="${accountDeal.checkFlag=='21'}">专用账户:${accountDeal.accountDeal.account.user.realname}/${accountDeal.accountDeal.account.user.username}</c:if>
							<c:if test="${accountDeal.checkFlag=='20'}">会员账户:${accountDeal.accountDeal.account.user.realname}/${accountDeal.accountDeal.account.user.username}</c:if>
						</td > 
					</tr>
				</c:forEach>
 
			</tbody>
		</table>
	</div>
	</form>
</body> 