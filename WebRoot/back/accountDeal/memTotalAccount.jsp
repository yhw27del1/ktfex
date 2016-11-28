<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI(); 
});
</script>
<script type="text/javascript">
	 
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="/back/accountDealAction!totalAccount" >
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				<input type="hidden" name="id" value="1"/>
				关键字<input type="text" name="keyWord" value="${keyWord}" style="width:85px;"/>
				交易账户<input type="text" name="queryCode" value="${queryCode}" style="width:85px;"/>
				&nbsp;用户类型：<s:select list="#{'4':'全部','T':'投资人','R':'融资方','D':'担保公司'}"   listKey="key" listValue="value" name="memberType"/>
				&nbsp;可用余额&nbsp;>=<input type="text" name="queryBalance" value="${queryBalance}" style="width:70px;" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>元&nbsp;&nbsp;冻结金额&nbsp;>=<input type="text" name="queryFrozenAmount" value="${queryFrozenAmount}" style="width:70px;" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>元&nbsp;&nbsp;<input type="submit" class="ui-state-default" value="查询" />(关键字中可以是用户名、真实姓名模糊查询)     
	</div>    

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
		<tbody>
			<tr>
				<td colspan="6"> 
					      总资产合计:<fmt:formatNumber value='${totalAmountSum}' type="currency" currencySymbol=""/>元&nbsp;&nbsp;
					      可用余额合计:<fmt:formatNumber value='${balanceSum}' type="currency" currencySymbol=""/>元<br/>
					      冻结金额合计:<fmt:formatNumber value='${frozenAmountSum}' type="currency" currencySymbol=""/>元&nbsp;&nbsp;
					      持有债权合计:<fmt:formatNumber value='${cyzqSum}' type="currency" currencySymbol=""/>元 
					     <c:if test="${!empty acs}">
					        &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="/back/accountDealAction!totalAccountEx?memberType=${memberType}&queryBalance=${queryBalance}&queryFrozenAmount=${queryFrozenAmount}&keyWord=${keyWord}&queryCode=${queryCode}"  title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
 				         </c:if>
 				</td>
			</tr>
		</tbody>
	</table>	
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						序号
					</th>
					<th>
						交易账户
					</th>
					<th>
						用户名
					</th>
					<th>
					    总资产
					</th>
					<th>
						可用余额
					</th>
					<th>
						冻结金额
					</th>
					<th>
						持有债权
					</th>
					<th>
					    借出笔数 
					</th>
					<th>
						借出总额
					</th>
					<th>
						已回收总笔数
					</th>
					<th>
						已回收总额
					</th>
				</tr>
			</thead>
			<tbody class="table_solid"> 
				<c:forEach items="${acs}" var="account" varStatus="xh">  
					<tr>
						<td>
							${xh.count}
						</td>
						<td>
							${account.string5}
						</td>
						<td>
						    <script>document.write(name("${account.string6}"));</script>
						</td>  
	                   <td><fmt:formatNumber value='${account.totalAmount}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${account.balance}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${account.frozenAmount}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${account.cyzq}' type="currency" currencySymbol=""/></td> 
	                   	<td> ${account.string1} </td>  
	                   	<td><fmt:formatNumber value='${account.string2}' type="currency" currencySymbol=""/></td>  
	                   	<td> ${account.string3} </td>  
	                   	<td><fmt:formatNumber value='${account.string4}' type="currency" currencySymbol=""/></td>  
					</tr>
				</c:forEach>
 
			</tbody>
		</table>
	</div>
	</form>
</body>
<script>
setIframeHeight(100);
</script>