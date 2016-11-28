<%@ page language="java" import="java.util.*"  contentType="application/msexcel"  pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglib.jsp"%>
<%
  response.setHeader("Content-Disposition", "inline;filename=cashFlow.xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<style>
   .xlsMoney{mso-number-format:"0\.00";}
   .xlsDate{mso-number-format:"yyyy\/mm\/dd";}
   .xlsText{mso-number-format:"\@";} 
</style>

<body style="font-size: 8pt; color: black">
	<input type='hidden' class='autoheight' value="auto" />
	<form action="" id='form1'> 
	
	<input type="hidden" name="page" value="1" /> 
	<div >
		<table >
			<thead>
				<tr >
					    <th colspan="7" align="center" valign="middle" style="text-align: center;">
							昆投互联网金融交易-招商银行（投、融资充值、提现）日报表
						</th>
				</tr>
				<tr >
						 <th style="text-align:left" colspan="4">
						 <c:choose>
							 <c:when test="${!empty startDate} && ${!empty endDate} ">
							     <c:out value="会计期间:${startDate}到${endDate}"></c:out>
							 </c:when>
							 <c:when test="${!empty startDate } && ${empty endDate } ">
							     <c:out value="会计期间:${startDate }至今"></c:out>
							 </c:when>
							 <c:when test="${empty startDate } && ${!empty endDate } ">
	                               <c:out value="会计期间:截至到${endDate }"></c:out>
	                         </c:when>
	                         <c:otherwise>
	                               <c:out value="会计期间:"></c:out>
	                         </c:otherwise>
                         </c:choose>
						 </th> 
					     <th  colspan="3">开户行;招商银行（一般帐户）   帐号：871903469010608</th> 
				</tr>
				 <tr >
				    <th style="text-align:center">日期</th>
			  		<th style="text-align:center">投资充值</th> 
				    <th style="text-align:center">融资充值</th>
			  		<th style="text-align:center">投资提现</th> 
			  		<th style="text-align:center">融资提现</th> 
			  		<th style="text-align:center">财务结转</th> 
			  		<th style="text-align:center">交易帐户总余额</th> 
			  	</tr>
			</thead>
			<tbody class="table_solid">
			     <tr>
                            <td>
                                上月累计
                            </td>
                            <td class="xlsMoney">${lmsumtzcz} </td> 
                            <td class="xlsMoney">${lmsumrzcz} </td> 
                            <td class="xlsMoney">${lmsumtztx} </td> 
                            <td class="xlsMoney">${lmsumrztx} </td> 
                            <td class="xlsMoney">${lmcwzj}</td> 
                            <td class="xlsMoney">${lmjyzhye}
                            <c:set value="${sumyu_e + lmjyzhye}" var="sumyu_e" /></td> 
                   </tr>
				<c:forEach items="${pageView.result}" var="entry" varStatus="index">
						<tr>
					        <td class="xlsDate">${entry.date_}  </td> 
					        <td  class="xlsMoney">${entry.tzcz} <c:set value="${sumtzcz + entry.tzcz}"
                                        var="sumtzcz" /></td>
					        <td class="xlsMoney">${entry.rzcz} <c:set value="${sumrzcz + entry.rzcz}"
                                        var="sumrzcz" /></td>
					        <td class="xlsMoney">${entry.tztx} <c:set value="${sumtztx + entry.tztx}"
                                        var="sumtztx" /></td> 
					        <td class="xlsMoney">${entry.rztx} <c:set value="${sumrztx + entry.rztx}"
                                        var="sumrztx" /></td>  
					        <td class="xlsMoney">${entry.cwzj} <c:set value="${sumcwzj + entry.cwzj}"
                                        var="sumcwzj" /></td> 
					        <td class="xlsMoney"> <c:set value="${sumyu_e + entry.yu_e}"
                                        var="sumyu_e" />${sumyu_e }</td>  
					     
						</tr>
					</c:forEach>
					     <tr>
							<td>
								合计
							</td>
							<td class="xlsMoney"> ${sumtzcz}</td> 
							<td class="xlsMoney"> ${sumrzcz}</td> 
							<td class="xlsMoney">	${sumtztx}</td> 
					        <td class="xlsMoney"> ${sumrztx}</td> 
					        <td class="xlsMoney"> ${sumcwzj }</td> 
					        <td> </td> 
				        </tr>
				        <tr>
                            <td>
                                累计
                            </td>
                            <td class="xlsMoney"> ${sumtzcz+lmsumtzcz}</td> 
                            <td class="xlsMoney"> ${sumrzcz+lmsumrzcz}</td> 
                            <td class="xlsMoney"> ${sumtztx+lmsumtztx}</td> 
                            <td class="xlsMoney"> ${sumrztx+lmsumrztx}</td> 
                            <td class="xlsMoney"> ${sumcwzj+lmcwzj}</td> 
                            <td class="xlsMoney"> </td> 
                        </tr>
				<tr>
				<% SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); %>
					<td colspan="7" align="right" style="text-align: center;">制表：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 复核： &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;    日期：<%=sdf.format(new Date()) %>
						</td>
				</tr>
				
			</tbody>
		</table>
	</div>
	</form>
</body>
