<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="member_state_not_audited" value="<%=com.kmfex.model.MemberBase.STATE_NOT_AUDIT %>" />
<%
  response.setHeader("Content-Disposition", "inline;filename="+ request.getAttribute("msg")+".xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style>
   .xlsMoney{mso-number-format:"0\.0";}
   .xlsDate{mso-number-format:"yyyy\/mm\/dd hh\:mm\:ss";}
   .xlsDate1{mso-number-format:"yyyy-mm-dd";}
   .xlsText{mso-number-format:"\@";} 
</style>
<body> 
<table class="ui-widget ui-widget-content">
  			 <tr style="width: 100%;">
				<td align="center" colspan="10">
					<div>
					      (<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />:<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"  />)投标及合同明细(单位:元)  
					</div>
				</td>
				</tr> 
</table>
		<div class="dataList ui-widget" style="width:100%">
		统计期间，投标总额为${amount } 共${recordcount}条
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
						<th>
							项目编号
						</th>
						<th>
							签约日期
						</th>
						<th>
							项目简称
						</th>
						<th>期限</th>  
						<th>
							融资方
						</th>
						<th>
							融资方户名
						</th>
						<th>
							投标方
						</th>
						<th>
							投标方户名
						</th>
						<th>
							投标金额
						</th>
						<th>
							投标日期
						</th>
						<th>
							合同编号
						</th>
						<th>
							状态
						</th>
						<th>
							月收本金
						</th>
						<th>
							月收利息
						</th>
						<th>
							月收本息
						</th>
						<th>
							本息余额
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${result}" var="c" varStatus="sta">
						<tr>
							<td>
								${c.financbasecode}
							</td>
							<td>
								<fmt:formatDate value="${c.financier_make_sure_date}" pattern="yy-MM-dd" />
							</td>
							<td>
								<a title="${c.fshortname}">${fn:substring(c.fshortname,0,6)}</a>
							</td>
							<td >${c.businesstype}期(${c.returnpattern})</td>
							<td>
								<c:if test="${menuMap['name']=='inline'}">
									${c.frealname}
								</c:if>
								<c:if test="${menuMap['name']!='inline'}">
									<c:choose>
										<c:when test="${fn:length(c.frealname) == 0}">
											无
										</c:when>
										<c:when test="${fn:length(c.frealname) > 1}">
											<c:out value="${fn:substring(c.frealname,0,1)}****" />
										</c:when>
										<c:otherwise>
											****
										</c:otherwise>
									</c:choose>
								</c:if>
							</td>
							<td>
								${c.financiername}
							</td>
							<td>
							    <c:if test="${menuMap['name']=='inline'}">
									${c.investorrealname}
								</c:if>
								<c:if test="${menuMap['name']!='inline'}">
									<c:choose>
										<c:when test="${fn:length(c.investorrealname) == 0}">
											无
										</c:when>
										<c:when test="${fn:length(c.investorrealname) > 1}">
											<c:out value="${fn:substring(c.investorrealname,0,1)}****" />
										</c:when>
										<c:otherwise>
											****
										</c:otherwise>
									</c:choose>
								</c:if>
							</td>
							<td>
								${c.investorname}
							</td>
							<td>
								${c.investamount}
								<c:set value="${investamount+c.investamount}" var="investamount" />								
							</td>
							<td>
								<fmt:formatDate value="${c.investdate}" pattern="yy-MM-dd" />
							</td>
							
							<td>
								${c.contract_number}
							</td>
							<td>
								<c:choose>
									<c:when test="${c.state==1}">未签</c:when>
									<c:when test="${c.state==2}">已签</c:when>
									<c:when test="${c.state==3}">取消</c:when>
								</c:choose>
							</td>
							<td>
								<c:if test="${c.payment_method==1}">
									<fmt:formatNumber value="${c.investamount}" pattern="0.00" />
								</c:if>
								<c:if test="${c.payment_method==2}">
									<fmt:formatNumber value="${c.principal_allah_monthly}" pattern="0.00" />
								</c:if>
							</td>
							<td>
								<c:if test="${c.payment_method==1}">
									<fmt:formatNumber value="${c.interest_allah}" pattern="0.00" />
								</c:if>
								<c:if test="${c.payment_method==2}">
									<fmt:formatNumber value="${c.interest_allah_monthly}" pattern="0.00" />
								</c:if>
							</td>
							<td>
								<c:if test="${c.payment_method==1}">
									<fmt:formatNumber value="${c.investamount}" pattern="0.00" var="investamount" />
									<fmt:formatNumber value="${c.interest_allah}" pattern="0.00" var="interest_allah" />
							${investamount+interest_allah}
						</c:if>
								<c:if test="${c.payment_method==2}">
									<fmt:formatNumber value="${c.repayment_amount_monthly_allah}" pattern="0.00" />
								</c:if>
							</td>
							<td>
								<fmt:formatNumber value="${c.bjye}" pattern="0.00" var="bjye" />
								<fmt:formatNumber value="${c.lxye}" pattern="0.00" var="lxye" />
								<fmt:formatNumber value="${bjye+lxye}" pattern="0.00" />
							</td>
						</tr>
					</c:forEach>
				</tbody>
				
			</table>
		</div>
	</body>
</html>