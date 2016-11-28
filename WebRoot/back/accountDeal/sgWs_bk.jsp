<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<style>
<!--
.body td{background-color:#fff}
-->
</style>
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
  			    <tr style="background-color:#F0F0F0;font-weight:bold;" >
					<td colspan="6" align="left"  >   
					          总资产：<fmt:formatNumber value='${account.totalAmount}' type="currency" currencySymbol=""/>元&nbsp;&nbsp; &nbsp;&nbsp; 可用余额：<fmt:formatNumber value='${account.balance}' type="currency" currencySymbol=""/>元&nbsp;&nbsp;&nbsp;&nbsp;冻结金额：<fmt:formatNumber value='${account.frozenAmount}' type="currency" currencySymbol=""/>元&nbsp;&nbsp;&nbsp;&nbsp;持有债权：<fmt:formatNumber value='${account.cyzq}' type="currency" currencySymbol=""/>元
					    <br/>
					</td>    
				</tr>  
				<tr style="background-color:#F0F0F0;font-weight:bold;">  
					<td  align="center" >
						融资编号
					</td> 
					 <td align="center">
						简称
					</td> 
					<td align="center" >
						本金余额(元)
					</td> 
		            <td align="center"  >
						利息余额(元)
					</td>	
					 <td align="center"  >
						本息合计(元)
					</td>	
					<td  align="center" > 
						下一还款日
					</td> 
				</tr> 
				<c:forEach items="${listInVos}" var="entity">
				<tr  class="body" >
					   <td  align="center" style="color:#0C108B;">
							&nbsp;&nbsp;${entity.financingBase.code}&nbsp;&nbsp;
						</td>
						<td  align="center" style="color:#0C108B;">
							&nbsp;&nbsp;${entity.financingBase.shortName}&nbsp;&nbsp;
						</td>
						 <td align="center" style="color:#0C108B;">
							 &nbsp;&nbsp;<fmt:formatNumber value='${entity.bjye}' type="currency" currencySymbol=""/>&nbsp;&nbsp;
						</td>
						<td  align="center" style="color:#0C108B;">
							&nbsp;&nbsp;<fmt:formatNumber value='${entity.lxye}' type="currency" currencySymbol=""/>&nbsp;&nbsp;
						</td>  
						<td  align="center" style="color:#0C108B;">
							&nbsp;&nbsp;<fmt:formatNumber value='${entity.bxhj}' type="currency" currencySymbol=""/>&nbsp;&nbsp;
						</td>  
						<td  style="color:#0C108B;">
							&nbsp;&nbsp;<fmt:formatDate value="${entity.xyhkr}" pattern="yyyy-MM-dd HH:mm" />&nbsp;&nbsp;
						</td>
					</tr>
				</c:forEach>  
		</table>  
</body> 