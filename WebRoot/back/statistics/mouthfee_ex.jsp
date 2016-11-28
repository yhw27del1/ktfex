<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
  response.setHeader("Content-Disposition", "inline;filename=dailifee.xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
						<td align="center" colspan="8">
							<div>
							     ${year}-${month}汇总表  
							</div>
						</td>
						</tr> 
				  <thead>
				  	<tr class="ui-widget-header ">
		                <th>介绍人</th>
		                <th colspan="2">期限</th>
		                <th colspan="3">投标金额</th>
		                <th colspan="2">当月服务费</th>
		            </tr>
				  </thead>
				  <tbody class="table_solid">
			  		<c:forEach items="${resultList}" var="item">
		                <tr>
		                    <td>${item.jingbanren}</td>
		                    <td  colspan="2">
	                            <c:choose>
			                        <c:when test="${item.interestday=='0'}">${item.businesstype}个月</c:when>
			                        <c:otherwise>${item.interestday}天</c:otherwise>
			                    </c:choose>
                            </td>
		                    <td  colspan="3" class="xlsMoney">${item.totalamount}
		                      <c:set value="${allamount + item.totalamount}" var="allamount"/>
		                    </td>
		                    <td  colspan="2" class="xlsMoney">${item.DAILIFEE}
		                          <c:set value="${alldailifei + item.DAILIFEE}" var="alldailifei"/>
		                          
		                    </td>
		                </tr>
		            </c:forEach>
			  		<tr>
		                <td>合计</td>
		                <td colspan="2" ></td>
		                <td colspan="3" class="xlsMoney">${allamount }</td>
		                <td colspan="2" class="xlsMoney"><fmt:formatNumber value='${alldailifei}' type="currency" currencySymbol=""/></td>
		            </tr>
		            <tr>
		            </tr>
		            <tr>
                    </tr>
			  	</tbody>
			  	
		  	</table>
		  	<table class="ui-widget ui-widget-content">
		  	           
                        <td align="center" colspan="8">
                            <div>
                                 ${year}-${month} 明细表  
                            </div>
                        </td>
                        </tr> 
                  <thead>
                    <tr class="ui-widget-header ">
                        <th>投标方户名</th>
                        <th>项目编号</th>
                        <th>介绍人</th>
                        <th>签约日期</th>
                        <th>期限</th>
                        <th>投标金额</th>
                        <th>当期信息服务费</th>
                        <th>签约日期</th>
                        <th>是否分期</th>
                        <th>当前期次</th>
                    </tr>
                  </thead>
                  <tbody class="table_solid">
                    <c:forEach items="${resultList2}" var="item2">
                        <tr>
                            <td  class="xlsText">${item2.investorname}</td>
                            <td  class="xlsText">${item2.financbasecode}</td>
                            <td>${item2.jingbanren}</td>
                            <td   class="xlsDate1">${item2.qianyuedate}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${item2.interestday=='0'}">${item2.businesstype}个月</c:when>
                                    <c:otherwise>${item2.interestday}天</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="xlsMoney">${item2.investamount}
                              <c:set value="${allinvestamount + item2.investamount}" var="allinvestamount"/>
                            </td>
                            <td class="xlsMoney">${item2.DAILIFEE}
                                  <c:set value="${alldailifei2 + item2.DAILIFEE}" var="alldailifei2"/>
                            </td>
                            <td class="xlsDate2">${item2.date2}</td>
                            <td class="xlsText">
                               <c:choose>
                                    <c:when test="${item2.RZFWF_TARIFF=='0'}">一次性支付</c:when>
                                    <c:when test="${item2.RZFWF_TARIFF=='1'}">分期支付</c:when>
                                    <c:when test="${item2.RZFWF_TARIFF==''}"></c:when>
                                </c:choose>
                            </td>
                            <td class="xlsText">
                               <c:choose>
                                    <c:when test="${item2.RZFWF_TARIFF=='1'}">${item2.MON}/${item2.BUSINESSTYPE}</c:when>
                                    
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td>总计</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td class="xlsMoney">${allinvestamount }</td>
                        <td>${alldailifei2}</td>
                        <td></td>
                    </tr>
                </tbody>
            </table>
	  	</div>
  </body>
</html>
