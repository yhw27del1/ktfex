<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
  response.setHeader("Content-Disposition", "inline;filename=Investors.xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style>
   .xlsMoney{mso-number-format:"0\.0";}
   .xlsDate{mso-number-format:"yyyy\/mm\/dd hh\:mm\:ss";}
   .xlsDate1{mso-number-format:"yyyy-mm-dd";}
   .xlsDate2{mso-number-format:"yyyy年mm月";}
   .xlsText{mso-number-format:"\@";} 
</style>
    <body> 
        <div class="dataList ui-widget">
            <table class="ui-widget ui-widget-content">
                 <tr>
                    <td align="center" colspan="17" style="font-size: 25px;font-weight: bold;">
                        <div>
                           投/融资方信息导出
                        </div>
                    </td>
                </tr> 
             <thead>
                    <tr class="ui-widget-header" style="font-size: 15px;">
                        <th align="center">用户编号</th>
                        <th align="center">姓名</th> 
                        <th align="center">性别</th> 
                        <th align="center">开户时间</th> 
                        <th align="center">初次审核时间</th> 
                        <th align="center">电话号码</th>
                        <th align="center">企业联系人号码</th>
                        <th align="center">身份证号</th>
                        <th align="center">账户余额</th>
                        <th align="center">冻结金额</th>
                        <th align="center">会员等级</th>
                        <th align="center">介绍人</th>
                        <th align="center">所在区域</th>
                        <th align="center">会员类型</th>
                        <th align="center">开户机构</th>
                        <th align="center">状态</th>
                        <th align="center">qq</th>
                        <th align="center">email</th>
                    </tr>
                </thead>
                <tbody class="table_solid">
                    <c:forEach items="${list}" var="entry">
	                   <tr> 
                           
	                       <td align="right">${entry.username}</td>
		                   <td align="right">
			                   <c:if test="${entry.category==\"1\"}">
	                            	<c:if test="${menuMap['name']=='inline'}">
										${entry.pName}
									</c:if>
									<c:if test="${menuMap['name']!='inline'}">
										<c:choose>
											<c:when test="${fn:length(entry.pName) == 0}">
												无
											</c:when>
											<c:when test="${fn:length(entry.pName) > 1}">
												<c:out value="${fn:substring(entry.pName,0,1)}****" />
											</c:when>
											<c:otherwise>
												****
											</c:otherwise>
										</c:choose>
									</c:if>
	                            </c:if>
	                            <c:if test="${entry.category==\"0\"}">
	                            	<c:if test="${menuMap['name']=='inline'}">
										${entry.eName}
									</c:if>
									<c:if test="${menuMap['name']!='inline'}">
										<c:choose>
											<c:when test="${fn:length(entry.eName) == 0}">
												无
											</c:when>
											<c:when test="${fn:length(entry.eName) > 1}">
												<c:out value="${fn:substring(entry.eName,0,1)}****" />
											</c:when>
											<c:otherwise>
												****
											</c:otherwise>
										</c:choose>
									</c:if>
	                            </c:if>
                            </td>
                            <td align="right"><c:if test="${entry.pSex==\"0\"}">
                            男
                            </c:if>
                                <c:if test="${entry.pSex==\"1\"}">
                            女
                            </c:if></td>
                            <td align="right" class="xlsDate">${entry.createdate}</td>
                            <td align="right" class="xlsDate">${entry.firstauditdate}</td>
                            <td align="right">
                            <c:if test="${entry.category==\"1\"}">
                            	<c:if test="${menuMap['phone']=='inline'}">
									${entry.pMobile}
								</c:if>
								<c:if test="${menuMap['phone']!='inline'}">
									<c:choose>
										<c:when test="${fn:length(entry.pMobile) == 0}">
											无
										</c:when>
										<c:when test="${fn:length(entry.pMobile) > 4}">
											<c:out value="****${fn:substring(entry.pMobile,fn:length(entry.pMobile)-4,-1)}" />
										</c:when>
										<c:otherwise>
											****
										</c:otherwise>
									</c:choose>
								</c:if>
                            </c:if>
                            <c:if test="${entry.category==\"0\"}">
                            	<c:if test="${menuMap['phone']=='inline'}">
									${entry.eMobile}
								</c:if>
								<c:if test="${menuMap['phone']!='inline'}">
									<c:choose>
										<c:when test="${fn:length(entry.eMobile) == 0}">
											无
										</c:when>
										<c:when test="${fn:length(entry.eMobile) > 4}">
											<c:out value="****${fn:substring(entry.eMobile,fn:length(entry.eMobile)-4,-1)}" />
										</c:when>
										<c:otherwise>
											****
										</c:otherwise>
									</c:choose>
								</c:if>
                            </c:if>
                           </td>
                           <td align="right">
                           		<c:if test="${menuMap['phone']=='inline'}">
									${entry.eContactMobile}
								</c:if>
								<c:if test="${menuMap['phone']!='inline'}">
									<c:choose>
										<c:when test="${fn:length(entry.eContactMobile) == 0}">
											无
										</c:when>
										<c:when test="${fn:length(entry.eContactMobile) > 4}">
											<c:out value="****${fn:substring(entry.eContactMobile,fn:length(entry.eContactMobile)-4,-1)}" />
										</c:when>
										<c:otherwise>
											****
										</c:otherwise>
									</c:choose>
								</c:if>
                           </td>
                           <td align="right" class="xlsText">
                           		<c:if test="${menuMap['idcard']=='inline'}">
									${entry.idCardNo}
								</c:if>
								<c:if test="${menuMap['idcard']!='inline'}">
									<c:choose>
										<c:when test="${fn:length(entry.idCardNo) == 0}">
											无
										</c:when>
										<c:when test="${fn:length(entry.idCardNo) > 4}">
											<c:out value="****${fn:substring(entry.idCardNo,fn:length(entry.idCardNo)-4,-1)}" />
										</c:when>
										<c:otherwise>
											****
										</c:otherwise>
									</c:choose>
								</c:if>
                           </td>
		                   <td align="right"><fmt:formatNumber value='${entry.balance_}' pattern="#,##0.00"/></td>
		                   <td align="right"><fmt:formatNumber value='${entry.frozenAmount}' pattern="#,##0.00"/></td>
		                   <td align="right">${entry.levelname}</td>
		                   <td align="right">${entry.jingbanren}</td>
		                   <td align="right">
                                ${entry.provinceName}&nbsp;${entry.cityName}
                            </td>
                            <td align="right">
                                <c:if test="${entry.category==\"1\"}">
                            个人&nbsp;${entry.tyname}
                            </c:if>
                                <c:if test="${entry.category==\"0\"}">
                            机构&nbsp;${entry.tyname}
                            </c:if>
                            </td>
                            <td align="right">
                                ${entry.orgname}
                            </td>
                            <td align="right">
                                <c:if test="${entry.state==\"1\"}">
                                    待审核
                                </c:if>
                                <c:if test="${entry.state==\"2\"}">
                                    正常
                                </c:if>
                                <c:if test="${entry.state==\"3\"}">
                                    未通过审核
                                </c:if>
                            </td>
                            <td align="right">
                                ${entry.qq}
                            </td>
                            <td align="right">
                                ${entry.email}
                            </td>
	                   </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
  </body>
</html>
