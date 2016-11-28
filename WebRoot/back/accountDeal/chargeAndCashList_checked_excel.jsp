<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
  response.setHeader("Content-Disposition", "inline;filename="+ request.getAttribute("msg")+".xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/back/four.jsp"></script>
<style>
   .xlsMoney{mso-number-format:"0\.00";}
   .xlsText{mso-number-format:"\@";}
</style>
<html xmlns="http://www.w3.org/1999/xhtml">
	<body style="font-size:12px;">
			<h1>昆投互联网金融交易-现金明细表</h1>
			<span>
				会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy/MM/dd"/>-<fmt:formatDate value="${endDate}" pattern="yyyy/MM/dd"/>
			</span>
		<table class="table_content" width="100%">
			<thead>
				<tr>
					<th>
						会员类型2
					</th>
					<th>
						会员名称
					</th>
					<th>
						交易账号
					</th>
					<th>
						银行账号
					</th>
					<th>
						交易类型
					</th>
					<th>
						充值额
					</th>
					<th>
						提现额
					</th>
					<th>
						专户
					</th>
					<th>
						充值/提现时间
					</th>
					<th>
						审核日期
					</th>
					<th>
						划款日期
					</th>
					<th>
						备注
					</th>
					<th>
						操作员
					</th>
		            <th>
		            	审核员
		            </th>
		            <th>
		            	方式
		            </th>
		            <th>
		            	状态
		            </th>
				</tr>
			
			</thead>
			<tbody class="table_solid">
				<c:set value="0" var="dai_sum" />
				<c:set value="0" var="dai_count" />
				<c:set value="0" var="jie_sum" />
				<c:set value="0" var="jie_count" />
				<c:forEach items="${resultList}" var="item" varStatus="status">
				<c:set value="${status.count}" var="count"/>
					<tr>
						<td>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='T'}">投资人</c:if>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='R'}">融资方</c:if>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='D'}">担保方</c:if>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='Q'}">其他</c:if>
						</td>
						<td>
							<c:if test="${menuMap['name']=='inline'}">
								${item.TARGET_ACCOUNT_USER_REALNAME}
							</c:if>
							<c:if test="${menuMap['name']!='inline'}">
								<c:choose>
									<c:when test="${fn:length(item.TARGET_ACCOUNT_USER_REALNAME) == 0}">
										无
									</c:when>
									<c:when test="${fn:length(item.TARGET_ACCOUNT_USER_REALNAME) > 1}">
										<c:out value="${fn:substring(item.TARGET_ACCOUNT_USER_REALNAME,0,1)}****" />
									</c:when>
									<c:otherwise>
										****
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<td align="right" style="padding:0 0;" class="xlsText">
							${item.TARGET_ACCOUNT_USER_USERNAME}
						</td>
						<td align="right" style="padding:0 0;" class="xlsText">
							<c:if test="${menuMap['bankcard']=='inline'}">
								${item.TARGET_ACCOUNT_USER_BANK}
							</c:if>
							<c:if test="${menuMap['bankcard']!='inline'}">
								<c:choose>
									<c:when test="${fn:length(item.TARGET_ACCOUNT_USER_BANK) == 0}">
										无
									</c:when>
									<c:when test="${fn:length(item.TARGET_ACCOUNT_USER_BANK) > 4}">
										<c:out value="****${fn:substring(item.TARGET_ACCOUNT_USER_BANK,fn:length(item.TARGET_ACCOUNT_USER_BANK)-4,-1)}" />
									</c:when>
									<c:otherwise>
										****
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<td>
							${item.TYPE}
						</td>
						<td align="right" style="text-align: right;" class="xlsMoney">
							<c:if test="${item.TXDIR==2}"></c:if>
							<c:if test="${item.TXDIR==1}">
								<fmt:formatNumber value="${item.MONEY_ADD}" pattern="#,###,##0.00" />
								<c:set value="${dai_sum+item.MONEY_ADD}" var="dai_sum" />
								<c:set value="${dai_count+1}" var="dai_count" />
							</c:if>
						</td>
						<td align="right" style="text-align: right;" class="xlsMoney">
							<c:if test="${item.TXDIR==1}"></c:if>
							<c:if test="${item.TXDIR==2}">
								<fmt:formatNumber value="${item.MONEY_SUBTRACT}" pattern="#,###,##0.00" />
								<c:if test="${item.CHECKFLAG2=='1'}">
									<c:set value="${jie_sum+item.MONEY_SUBTRACT}" var="jie_sum" />
									<c:set value="${jie_count+1}" var="jie_count" />
								</c:if>
							</c:if>
						</td>
						<td>
							<c:if test="${item.CHANNEL==0}">无</c:if>
							<c:if test="${item.CHANNEL==1}">招商银行</c:if>
							<c:if test="${item.CHANNEL==2}">工商银行</c:if>
						</td>
						<td>
							<fmt:formatDate value="${item.CREATEDATE}" pattern="yyyy/MM/dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${item.CHECKDATE}" pattern="yyyy/MM/dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${item.HKDATE}" pattern="yyyy/MM/dd HH:mm:ss"/>
						</td>
						<td>
							${item.MEMO}
						</td>
						<td>
							${item.OPERAT_USER}
						</td>
						<td>
							${item.CHECK_USER}
						</td>
						<td>
							<c:if test="${item.BATCHFLAG==1}">批量</c:if>
							<c:if test="${item.BATCHFLAG!=1}">单笔</c:if>
						</td>
						<td>
							<c:if test="${item.CHECKFLAG=='1'&&item.CHECKFLAG2=='1'}">
								已审核
							</c:if>
							<c:if test="${item.CHECKFLAG=='37'||item.CHECKFLAG=='41'||item.CHECKFLAG=='43'}">
								已审核
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='0'}">
								待划款
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='1'}">
								已划款
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='2'}">
								转账异常
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='3'}">
								提现错误
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='4'}">
								提现冲正
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th>合计</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th style="text-align: right;">共:${count}笔</th>
					<th align="right" style="text-align: right;" class="xlsMoney">
						${dai_count}笔<br />
						<fmt:formatNumber value="${dai_sum}" pattern="#,###,##0.00" />
					</th>
					<th align="right" style="text-align: right;" class="xlsMoney">
						${jie_count}笔<br />
						<fmt:formatNumber value="${jie_sum}" pattern="#,###,##0.00" />
					</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
				</tr>
			</tfoot>
		</table>
	</body>
</html>
