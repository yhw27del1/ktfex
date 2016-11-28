<%@ page language="java" import="java.util.*,com.kmfex.zhaiquan.model.*,com.wisdoor.core.page.PageView,com.wisdoor.core.utils.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<c:set value="<%=new Date()%>" var="now"/>
<%@ include file="/common/import.jsp"%> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript"> 
function show(url){
	window.showModalDialog(url, null, "dialogWidth:800px;dialogHeight:auto;status:no;help:yes;resizable:no;");
}
</script>
		<style>
			body{
				font-family: "微软雅黑";
			}
			
			table,td,th{
				border:1px solid #000;   
    			border-collapse:collapse;
			}
			#print_button{
				position: fixed;
				right:10px;
				top:10px;
				z-index:1000;
			}
			
			.nextpage{
				page-break-before: always;
				clear: both;
			}  
			.w80{
				width:80px;
			}
			.w20{
				width:20px;
			}
			
			.al{
				text-align: left;
			}
			.w240{
				width:240px;
			}
			.foot{
				float:right;
				margin-right:30px;
				font-size:12px;
			}
			.foot div{
				float:left;
				width:120px;
			}
			.logo{
				position: absolute;
				width:50px;
				height:50px;
				left:50px;
				top:10px;
			}
			.title{
				font-size:20px;
				text-align: center;
				margin-top:30px;
			}
			td{
			font-size:15px;
			}
			.show_page{
				font-size:12px;
			}
		</style>
<body  style="font-size: 8pt;margin-left:50px; color: black"> 
		
		
			    <%
					int _page = 1;
					int _count = 0;
					PageView<Contract> pvs = (PageView<Contract>)request.getAttribute("pageView");  
					List<Contract> list=pvs.getRecords();
					for(int lc = 0; lc < list.size(); lc++ ){
						Contract entry = list.get(lc); 					
						if(_page == 1 && _count == 0){
			     %>	
			   <img src="/Static/images/logo.png" class="logo"/>
			   <div class="title">昆投互联网金融交易-债权转让费用明细表&nbsp;&nbsp;&nbsp;&nbsp;<input id="print_do" type="button" value="打印页面"/></div>
				<div style="float:right; font-size:13px;">
					会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日"/>-<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日"/>
				</div>
				<table width="100%" style="font-size:13px;">
			<thead>
				<tr class="ui-widget-header ">
					    <th rowspan="3">
							序号
						</th>
 						 <th rowspan="3">
							成交日期
						 </th> 
						<th rowspan="3">
							成交价
						</th> 
						<th rowspan="3">
							费率(%)
						</th>  
						<th style="text-align:center" colspan="2">手续费</th>
					
					 <th colspan="4" style="text-align:center">会员信息</th>
					 
						<th rowspan="3">
							协议编号
						</th>  
				</tr>
				<tr class="ui-widget-header ">
						<th style="text-align:center" rowspan="2">出让方</th>
						<th style="text-align:center" rowspan="2">受让方</th>
						
						 <th style="text-align:center" colspan="2">出让方</th> 
					     <th style="text-align:center" colspan="2">受让方</th> 
						
					</tr>
				 <tr class="ui-widget-header ">
				    <th style="text-align:center">交易账户</th>
			  		<th style="text-align:center">户名</th> 
				    <th style="text-align:center">交易账户</th>
			  		<th style="text-align:center">户名</th> 
			  	</tr>
			</thead>
			<tbody class="table_solid">
			<%
						} else if(_page != 1 && _count == 0){
			%>  
			   <div style='width:50px;height:50px;left:10px;top:10px;'></div>
			   <div class="title">昆投互联网金融交易-债权转让费用明细表</div>
				<div style="float:right; font-size:13px;">
					会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日"/>-<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日"/>
				</div>
				<table width="100%" style="font-size:13px;">
			<thead>
				<tr class="ui-widget-header ">
					    <th rowspan="3">
							序号
						</th>
 						 <th rowspan="3">
							成交日期
						 </th> 
						<th rowspan="3">
							成交价
						</th> 
						<th rowspan="3">
							费率(%)
						</th>  
						<th style="text-align:center" colspan="2">手续费</th>
					
					 <th colspan="4" style="text-align:center">会员信息</th>
					 
						<th rowspan="3">
							协议编号
						</th>   
				</tr>
				<tr class="ui-widget-header ">
						<th style="text-align:center" rowspan="2">出让方</th>
						<th style="text-align:center" rowspan="2">受让方</th>
						
						 <th style="text-align:center" colspan="2">出让方</th> 
					     <th style="text-align:center" colspan="2">受让方</th> 
						
					</tr>
				 <tr class="ui-widget-header ">
				    <th style="text-align:center">交易账户</th>
			  		<th style="text-align:center">户名</th> 
				    <th style="text-align:center">交易账户</th>
			  		<th style="text-align:center">户名</th> 
			  	</tr>
			</thead>
			<tbody class="table_solid">
			<%
			   }
			%> 
						<tr>
							<td align='center'>
								 <%=lc+1%> 
							</td> 
							<td align='center'>
								<fmt:formatDate value='<%=entry.getBuyerDate()%>' pattern="yyyy-MM-dd" />
							</td> 
							<td align='center'>
								<fmt:formatNumber value='<%=DoubleUtils.doubleCheck2(entry.getPrice(),2)%>' type="currency" currencySymbol="" />
							</td> 
					        <td align='center'><%=DoubleUtils.doubleCheck2(entry.getPercentSell(),2)%></td> 
					        <td align='center'><%=DoubleUtils.doubleCheck2(entry.getSelling().getZqfwf(),2)%></td>
					        <td align='center'><%=DoubleUtils.doubleCheck2(entry.getBuying().getZqfwf(),2)%></td>
					        <td align='center'><%=entry.getSeller().getUsername()%></td> 
					        <td align='center'><%=entry.getSeller().getRealname()%></td>  
					        <td align='center'><%=entry.getBuyer().getUsername()%></td> 
					        <td align='center'><%=entry.getBuyer().getRealname()%></td>   					        
					        <td align='center'><%=entry.getXieyiCode()%></td> 
						</tr>
						
					<%
						_count++;
						if(_count == 20 || lc + 1 == list.size()){
							_count = 0;
							_page++;
						}
						if(_count ==0){
					%>
							</tbody>
							</table>
							<center><div class="show_page">第<%=_page-1 %>页/<span class='pageTotal'><span></div></center>
					<%
							
						}
					
						if(lc + 1 == list.size()){
					%>		
	<script>
							$(function(){
								var tfoot = $("<tfoot></tfoot>");
								var tr = $("<tr></tr>");
								tr.append("<th align='center'>合计</th>");  
								tr.append("<th>-</th>");
								tr.append("<th align='center'><fmt:formatNumber value='${price}' type='currency' currencySymbol='' /></th>");   
								tr.append("<th></th>");
								tr.append("<th align='center'><fmt:formatNumber value='${zqfwf_s}' type='currency' currencySymbol='' /></th>");
								tr.append("<th align='center'><fmt:formatNumber value='${zqfwf_b}' type='currency' currencySymbol='' /></th>"); 
								tr.append("<th>-</th>");
								tr.append("<th>-</th>");
								tr.append("<th>-</th>");
								tr.append("<th>-</th>");								
								tr.append("<th>-</th>"); 
								tfoot.append(tr);
								$("table:last").append(tfoot);
								$(".pageTotal").html('共<%=_page-1 %>页');     
							});
						</script>					
				 
	    </div>
			<div style="float:right;line-height:30px;font-weight: bold">
			<div style="float:left;width:120px;">经办:</div>
			<div style="float:left;width:120px;">复核:</div>
			<div style="float:left;">打印:<fmt:formatDate value="${now}" pattern="yyyy/MM/dd HH:mm:ss"/></div>
		</div>
						<%
					}
				}
				%> 
<script>
	var obj = document.getElementById("print_do");
	if (window.addEventListener) {
		obj.addEventListener('click', print_action, false);
	} else if (window.attachEvent) {
		obj.attachEvent('onclick', print_action);
	}
	function print_action(){
		obj.style.display="none";
		print();
		obj.style.display="";
	}
</script>
</body>
