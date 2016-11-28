<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
 <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script> 
<script>
$(function(){ 
	$(".table_solid").tableStyleUI();  
	$(".autoheight").val($("#mytable").height()+50);
});



</script>
<body> 
<input type='hidden' class='autoheight' value="auto" /> 
   		<table cellpadding="0" cellspacing="1" bgcolor="#339933"> 
  			   <!--  <tr style="background-color:#F0F0F0;font-weight:bold;" >
					<td colspan="5" align="left"  >   
					          总资产：<fmt:formatNumber value='${account.balance+sumAmount}' type="currency" currencySymbol=""/>元&nbsp;&nbsp; &nbsp;&nbsp; 可用余额：<fmt:formatNumber value='${account.balance}' type="currency" currencySymbol=""/>元&nbsp;&nbsp;&nbsp;&nbsp;冻结金额：<fmt:formatNumber value='${account.frozenAmount}' type="currency" currencySymbol=""/>元&nbsp;&nbsp;&nbsp;&nbsp;持有债权：<fmt:formatNumber value='${sumAmount}' type="currency" currencySymbol=""/>元
					    <br/>
					</td>    
				</tr>   -->
				<tr style="background-color:#F0F0F0;font-weight:bold;">  
					<td  align="center" >
						日期
					</td> 
					<!-- <td align="center">
						交易前金额(￥)
					</td> -->
					<td align="center" >
						金额(元)
					</td>
					<!-- <td align="center">
						交易后金额(￥)
					</td >  -->
		            <td align="center"  >
						操作类型
					</td>	
					<td  align="center" > 
						备注
					</td> 
				</tr> 
				<c:forEach items="${dealVos}" var="accountDeal" >
				<tr  class="body" >
						<td align="center"  style="color:#0C108B;">
							&nbsp;&nbsp;<fmt:formatDate value="${accountDeal.createDate}" pattern="yyyy-MM-dd HH:mm" />&nbsp;&nbsp;
						</td > 
						<!-- <td align="center" style="color:#0C108B;">
							<fmt:formatNumber value='${accountDeal.preMoney}' type="currency" currencySymbol=""/>
						</td>  -->
						<td  align="center" style="color:#0C108B;">
							&nbsp;&nbsp;<fmt:formatNumber value='${accountDeal.money}' type="currency" currencySymbol=""/>&nbsp;&nbsp;
						</td>
						<!-- <td  align="center" style="color:#0C108B;">
							<fmt:formatNumber value='${accountDeal.nextMoney}' type="currency" currencySymbol=""/>
						</td>  -->    
                        <td  align="center" style="color:#0C108B;">
							&nbsp;&nbsp;${accountDeal.type}&nbsp;&nbsp;
						</td>
						<td  style="color:#0C108B;">
							&nbsp;&nbsp;${accountDeal.note}&nbsp;&nbsp;
						</td>
					</tr>
				</c:forEach>  
		</table>  
</body> 