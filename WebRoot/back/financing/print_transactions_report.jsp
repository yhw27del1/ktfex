<%@ page language="java" import="java.util.*,com.wisdoor.core.model.*" pageEncoding="UTF-8"%>
<%@page import="org.apache.taglibs.standard.tag.el.fmt.FormatNumberTag"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="now" value="<%=new Date() %>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
	<head>
		<title>昆明商企业金融服务有限公司-融资成交明细月报</title>
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
				left:10px;
				top:10px;
			}
			.title{
				font-size:20px;
				text-align: center;
				margin-top:30px;
			}
			td{
			font-size:12px;
			}
			.show_page{
				font-size:12px;
				text-align: center;
			}
		</style>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
	</head>
	<body>
		<!-- <input type="button" value="打印" id="print_button"/> -->
			<%
				FormatNumberTag fmt = new FormatNumberTag();
				fmt.setPattern("#,###,##0.00");
				int _page = 1;
				int _count = 0;
				List<Org> orgs = (List<Org>)request.getAttribute("orglist");
				List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("list");
				String org_code2 = request.getParameter("org_code2");
				Map<String,Double> sum = new HashMap<String,Double>();
				double lx_sum = 0d;//总利息
				double tze_sum = 0d;//总投标额
				double otheramount_all = 0d;//其它授权中心总投标额
				int all_pages = 0;
				while(++all_pages * 25 < list.size());
				
				for(int lc = 0; lc < list.size(); lc++ ){
					Map<String,Object> item = list.get(lc);
					double rowamount = 0d;
					
					if(_page == 1 && _count == 0){
			%> 
				
							<img src="/Static/images/logo.png" class="logo"/>
							<div class="title">昆投互联网金融交易-融资成交明细月报</div>
							<div style="text-align:center; font-size:13px;">
								会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日"/>-<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日"/>
								&nbsp;&nbsp;&nbsp;<c:if test="${org_code!=''}">[${org.showCoding}-${org.name}]</c:if>
							</div>
							<table width="100%" style="font-size:13px;">
								<thead>
									<tr>
										<th nowrap="nowrap">序号</th>
										<th nowrap="nowrap">签约日期</th>
										<th nowrap="nowrap">项目编号</th>
										<th nowrap="nowrap">融资户名</th>
										<th nowrap="nowrap">期限</th>
										<th nowrap="nowrap">还款方式</th>
										<th nowrap="nowrap">总额</th>
										<%
											for(Org org : orgs){
												out.println("<th nowrap=\"nowrap\">"+org.getShowCoding()+"</th>");
											}
											if(null != org_code2 && !"".equals("org_code2")){
												out.println("<th nowrap=\"nowrap\">其它</th>");
											}
										%>
										<th nowrap="nowrap">利息</th>
									</tr>
								</thead>	
								<tbody>
						<%
						} else if(_page != 1 && _count == 0){
						%>
							<div class="nextpage center">
								<img src="/Static/images/logo.png" class="logo"/>
								<div class="title">昆投互联网金融交易-融资成交明细月报</div>
								<div style="text-align:center;font-size:13px;">
									会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日"/>-<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日"/>
								&nbsp;&nbsp;&nbsp;<c:if test="${org_code!=''}">[${org.showCoding}-${org.name}]</c:if>
								</div>
							<table width="100%" style="font-size:13px;">
								<thead>
									<tr>
										<th nowrap="nowrap">序号</th>
										<th nowrap="nowrap">签约日期</th>
										<th nowrap="nowrap">项目编号</th>
										<th nowrap="nowrap">融资户名</th>
										<th nowrap="nowrap">期限</th>
										<th nowrap="nowrap">还款方式</th>
										<th nowrap="nowrap">总额</th>
										<%
											for(Org org : orgs){
												out.println("<th nowrap=\"nowrap\">"+org.getShowCoding()+"</th>");
											}
											if(null != org_code2 && !"".equals("org_code2")){
												out.println("<th nowrap=\"nowrap\">其它</th>");
											}
										%>
										<th nowrap="nowrap">利息</th>
									</tr>
								</thead>	
								<tbody>
						<%
						}
						%>
						
					
							
							<tr>
								<td nowrap="nowrap" align="center"><%=lc+1%></td>
								<td nowrap="nowrap"><%=item.get("qianyuedate")%></td>
								<td nowrap="nowrap"><%=item.get("financecode")%></td>
								<td nowrap="nowrap"><script>document.write(name('<%=item.get("financerrealname")%>'));</script></td>
								<td nowrap="nowrap">
									<%
										if(Integer.parseInt(item.get("interestday").toString()) == 0){
											out.print(item.get("term")+"[月]");	
										}else{
											out.print(item.get("interestday")+"[天]");	
										}
									%>
								</td>
								<td nowrap="nowrap"><%=item.get("returnpattern")%></td>
								<td nowrap="nowrap">
									<fmt:formatNumber value='<%=item.get("currenyamount")%>' pattern="#,###,##0.00"/>
									<%
										tze_sum += Double.parseDouble(item.get("currenyamount").toString());
									%>
									
								</td>
								<%
									for(Org org : orgs){
										Object _amount = item.get("f"+org.getShowCoding());
										double amount = _amount == null ? 0d:Double.parseDouble(_amount.toString());
										rowamount += amount;
								%>
									<td nowrap="nowrap">
										<%
											if(amount==0){
												out.print("-");
											}else{
										%>
												<fmt:formatNumber value='<%=amount %>' pattern="#,###,##0.00"/>
										<% 
											}
										%>
										
									</td>
								<%
										if(sum.get(org.getShowCoding()) == null){
											sum.put(org.getShowCoding(),amount);
										}else{
											sum.put(org.getShowCoding(),sum.get(org.getShowCoding())+amount);
										}
									}
									if(null != org_code2 && !"".equals("org_code2")){
								%>
										<td nowrap="nowrap">
											
											<%
												double otheramount =  Double.parseDouble(item.get("currenyamount").toString())- rowamount;
												otheramount_all += otheramount;
											%>
											<fmt:formatNumber value='<%=otheramount%>' pattern="#,###,##0.00"/>
										</td>
								<%
									}
								%>
								<td nowrap="nowrap">
									<%
										double lx = Double.parseDouble(item.get("currenyamount").toString())*Double.parseDouble(item.get("rate").toString())/100/12*Double.parseDouble(item.get("term").toString());
										lx_sum += lx;
									%>
									<fmt:formatNumber pattern="#,###,##0.00" value="<%=lx%>"/>
									
								</td>
							</tr>
							
					<%
						_count++;
						if(_count == 25 || lc + 1 == list.size()){
							_count = 0;
							_page++;
						}
						if(_count ==0){
					%>
							</tbody>
							</table>
							<div class="show_page">第<%=_page-1 %>页 &nbsp;&nbsp;&nbsp;&nbsp; 共<%=all_pages %>页</div>
					<%
							
						}
					
						if(lc + 1 == list.size()){
					%>
					
						<script>
							$(function(){
								var tfoot = $("<tfoot></tfoot>");
								var tr = $("<tr></tr>");
								tr.append("<th colspan='6' align='right' style='padding-right:10px;'>合计</th>");
								tr.append("<th><fmt:formatNumber pattern="#,##0.00" value="<%=tze_sum%>"/></th>");
								<%
									for(Org org : orgs){
								%>
									tr.append("<th><fmt:formatNumber pattern="#,##0.00" value="<%=sum.get(org.getShowCoding())%>"/></th>");
								<%
									}
									if(null != org_code2 && !"".equals("org_code2")){
								%>
									tr.append("<th><fmt:formatNumber pattern="#,##0.00" value="<%=otheramount_all%>"/></th>");
								<%
									}
								%>
								tr.append("<th><fmt:formatNumber pattern="#,##0.00" value="<%=lx_sum%>"/></th>");
								tfoot.append(tr);
								$("table:last").append(tfoot);
							});
						</script>
						<div class="foot">
							<div>经办:</div>
							<div>复核:</div>
							<div style="width:260px;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
						</div>
				<%
					}
				}
				%>
			
		
	</body>
</html>
<script type="text/javascript">
<!--
	var obj = document.getElementById("print_button");
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
	
//-->
</script>
