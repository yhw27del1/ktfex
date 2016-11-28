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
   .xlsDate1{mso-number-format:"yyyy-mm-dd";}
   .xlsText{mso-number-format:"\@";} 
   table,td,th{
				border:1px solid #000;   
    			border-collapse:collapse;
			}
</style>
<body> 
 
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		  <thead>
		  <tr style="width: 100%;">
				<td align="center" colspan="12">
					<div>
					      (<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />:<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"  />)还款综合查询(单位:元)  
					</div>
				</td>
		   </tr> 
		  <tr>
					
					<th rowspan="2">
						还款日
					</th>
					<th rowspan="2">
						签约日
					</th>
					<th rowspan="2">
						应还日
					</th>
					<th rowspan="2">
						担保机构
					</th>
					<th rowspan="2">
						项目编号
					</th>
					
					<th rowspan="2">
						名称
					</th>
					<th colspan="5">应还款项</th>
					<th colspan="9">实还款项</th>
					<th rowspan="2">交易手续费</th>
					<th rowspan="2">
						期数
					</th>
					<th rowspan="2">
						状态
					</th>
					<th rowspan="2">
						备注
					</th>
				</tr>
				
				<tr>
					<th align="right">应还本金</th>
					<th align="right">应还利息</th>
					<th align="right">服务费</th>
					<th align="right">担保费</th>
					<th align="right">风险管理费</th>
					<th align="right">实还本金</th>
					<th align="right">实还利息</th>
					<th align="right">罚金</th>
					<th align="right">服务费</th>
					<th align="right">罚金</th>
					<th align="right">担保费</th>
					<th align="right">罚金</th>
					<th align="right">风险管理费</th>
					<th align="right">罚金</th>
				</tr>
		  </thead>
		  <tbody class="table_solid">
	  		<c:forEach items="${resultList}" var="item" varStatus="status">
		  		<tr>
						<td>
							<c:if test="${item.shdate != null}">
							${fn:substring(item.shdate,0,4)}/${fn:substring(item.shdate,4,6)}/${fn:substring(item.shdate,6,8)}
							</c:if>
						</td>
						<td>
							<fmt:formatDate value="${item.qianyuedate}" pattern="yyyy/MM/dd"/>
						</td>
						<td>
							<fmt:formatDate value="${item.yhdate}" pattern="yyyy/MM/dd"/>
						</td>
						<td>
							${item.forgname }
						</td>
						<td>
							${item.financbasecode}
						</td>
						<td>
							<c:if test="${menuMap['name']=='inline'}">
								${item.frealname}
							</c:if>
							<c:if test="${menuMap['name']!='inline'}">
								<c:choose>
									<c:when test="${fn:length(item.frealname) == 0}">
										无
									</c:when>
									<c:when test="${fn:length(item.frealname) > 1}">
										<c:out value="${fn:substring(item.frealname,0,1)}****" />
									</c:when>
									<c:otherwise>
										****
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<td align="right">
								<fmt:formatNumber value="${item.yhbj}" pattern="#,###,##0.00" />
								<c:set value="${z_yhbj +item.yhbj}" var="z_yhbj"/>
						</td>
						<td align="right">
								<fmt:formatNumber value="${item.yhlx}" pattern="#,###,##0.00" />
								<c:set value="${z_yhlx + item.yhlx}" var="z_yhlx"/>
						</td>
						<td align="right">
								<fmt:formatNumber value="${item.yhfee1}" pattern="#0.00" />
								<fmt:formatNumber value="${z_yhfee1 + item.yhfee1}" pattern="#0.00" var="z_yhfee1" />
						</td>
						<td align="right">
								<fmt:formatNumber value="${item.yhfee2}" pattern="#0.00" />
								<fmt:formatNumber value="${z_yhfee2+item.yhfee2}" pattern="#0.00" var="z_yhfee2" />
						</td>
						<td align="right">
								<fmt:formatNumber value="${item.yhfee3}" pattern="#0.00" />
								<fmt:formatNumber value="${z_yhfee3+item.yhfee3}" pattern="#0.00" var="z_yhfee3" />
						</td>
						
						
						<td align="right">
								<fmt:formatNumber value="${item.shbj}" pattern="#,###,##0.00" />
								<c:set value="${z_shbj +item.shbj}" var="z_shbj"/>
						</td>
						<td align="right">
								<fmt:formatNumber value="${item.shlx}" pattern="#,###,##0.00" />
								<c:set value="${z_shlx + item.shlx}" var="z_shlx"/>
						</td>
						
						<td align="right">
							<fmt:formatNumber value="${item.shfj}" pattern="#,###,##0.00" />
							<c:set value="${z_fj + item.shfj}" var="z_fj"/>
						</td>
						<td align="right">
								<fmt:formatNumber value="${item.shfee1}" pattern="#,##0.00" />
								<fmt:formatNumber value="${z_shfee1+item.shfee1}" pattern="#0.00" var="z_shfee1" />
						</td>
						<td align="right">
							<fmt:formatNumber value="${item.fj1}" pattern="#,##0.00" />
							<fmt:formatNumber value="${z_shfj1+item.fj1}" pattern="#0.00" var="z_shfj1" />
						</td>
						<td align="right">
						
								<fmt:formatNumber value="${item.shfee2}" pattern="#,##0.00" />
								<fmt:formatNumber value="${z_shfee2+item.shfee2}" pattern="#0.00" var="z_shfee2" />
						</td>
						<td align="right">
							<fmt:formatNumber value="${item.fj2}" pattern="#,##0.00" />
							<fmt:formatNumber value="${z_shfj2+item.fj2}" pattern="#0.00" var="z_shfj2" />
						</td>
						<td align="right">
							<fmt:formatNumber value="${item.shfee3}" pattern="#,##0.00" />
							<fmt:formatNumber value="${z_shfee3+item.shfee3}" pattern="#0.00" var="z_shfee3" />
						</td>
						<td align="right">
							<fmt:formatNumber value="${item.fj3}" pattern="#,##0.00" />
							<fmt:formatNumber value="${z_shfj3+item.fj3}" pattern="#0.00" var="z_shfj3" />
						</td>
						<td align="right">
							<fmt:formatNumber value="${item.ssfee4}" pattern="#,##0.00" />
							<fmt:formatNumber value="${z_ssfee4+item.ssfee4}" pattern="#0.00" var="z_ssfee4" />
						</td>
						<td class="xlsText">
							${item.returntimes}-${item.qs}
						</td>
						<td>
							<c:if test="${item.state==0}">未还</c:if>
							<c:if test="${item.state==1}">正常</c:if>
							<c:if test="${item.state==2}">提前</c:if>
							<c:if test="${item.state==3}">逾期[${item.overdue_days}]</c:if>
							<c:if test="${item.state==4}">代偿</c:if>
						</td>
						<td>
							${item.remark2}
						</td>
						
					</tr>
	  		</c:forEach>
	  	</tbody>
	  	<tfoot>
	  		<tr>
					<th>
						合计
					</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>${fn:length(resultList)}条</th>
					<th align="right"><fmt:formatNumber value="${z_yhbj}" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_yhlx}" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_yhfee1}" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_yhfee2}" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_yhfee3}" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_shbj}" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_shlx}" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_fj}" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_shfee1 }" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_shfj1 }" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_shfee2 }" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_shfj2 }" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_shfee3 }" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_shfj3 }" pattern="#,###,##0.00"/></th>
					<th align="right"><fmt:formatNumber value="${z_ssfee4 }" pattern="#,###,##0.00"/></th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
				</tr>
	  		
	  	</tfoot>
	  </table>
  </body>
</html>
