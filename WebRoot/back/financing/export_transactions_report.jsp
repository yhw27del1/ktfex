<%@ page language="java" import="java.util.*,com.wisdoor.core.model.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="now" value="<%=new Date()%>" />
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	response.setHeader("Content-Disposition", "inline;filename=" + sdf.format(new Date()) + ".xls");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
	<head>
		<title>昆明商企业金融服务有限公司-融资成交明细月报</title>
		<style>
body {
	font-family: "微软雅黑";
}

.center {
	margin: 0 auto;
	width: 80%;
	padding-top: 10px;
	text-align: center;
	position: relative;
	clear: both;
}

table,td,th {
	border: 1px solid #000;
	border-collapse: collapse;
}

#print_button {
	position: fixed;
	right: 10px;
	top: 10px;
	z-index: 1000;
}

.nextpage {
	page-break-before: always;
	clear: both;
}

.w80 {
	width: 80px;
}

.w20 {
	width: 20px;
}

.al {
	text-align: left;
}

.w240 {
	width: 240px;
}

.foot {
	float: right;
	margin-right: 30px;
	font-size: 12px;
}

.foot div {
	float: left;
	width: 120px;
}


.title {
	font-size: 20px;
	text-align: center;
	margin-top: 30px;
}

td {
	font-size: 12px;
}

.show_page {
	font-size: 12px;
}
</style>
	</head>
	<body>
		<%
				int _page = 1;
				int _count = 0;
				List<Org> orgs = (List<Org>)request.getAttribute("orglist");
				List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("list");
				String org_code2 = request.getParameter("org_code2");
				Map<String,Double> sum = new HashMap<String,Double>();
				
					
			%>
				
							<div class="title">昆投互联网金融交易-融资成交明细月报</div>
							<div style="float:left; font-size:13px;">
								会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy年MM月dd日"/>-<fmt:formatDate value="${endDate}" pattern="yyyy年MM月dd日"/>
							</div>
							<table width="100%" style="font-size:13px;">
								<thead>
									<tr>
										<th nowrap="nowrap">序号</th>
										<th nowrap="nowrap">签约日期</th>
										<th nowrap="nowrap">项目编号</th>
										<th nowrap="nowrap">融资户名</th>
										<th nowrap="nowrap">期限</th>
										<th nowrap="nowrap">总额</th>
										<%
											for(Org org : orgs){
												out.println("<th>"+org.getShowCoding()+"</th>");
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
							double lx_sum = 0d;//总利息
							double tze_sum = 0d;//总投标额
							double otheramount_all = 0d;//其它授权中心总投标额
							for(int lc = 0; lc < list.size(); lc++ ){
								double rowamount = 0d;
								Map<String,Object> item = list.get(lc);
								
						%>
							<tr>
								<td nowrap="nowrap"><%=lc+1%></td>
								<td nowrap="nowrap"><%=item.get("qianyuedate")%></td>
								<td nowrap="nowrap"><%=item.get("financecode")%></td>
								<td nowrap="nowrap">
								<c:if test="${menuMap['name']=='inline'}">
									<%=item.get("financerrealname")%>
								</c:if>
								<c:if test="${menuMap['name']!='inline'}">
									****
								</c:if>
								</td>
								<td nowrap="nowrap">
									<%
										if(item.get("interestday") != null && Integer.parseInt(item.get("interestday").toString()) != 0){
											out.println(item.get("interestday")+"天");
										}else{
											out.println(item.get("term")+"个月");
										}
									%>
								</td>
								<td nowrap="nowrap">
									<%
										out.println(item.get("currenyamount"));
										tze_sum += Double.parseDouble(item.get("currenyamount").toString());
									%>
									
								</td>
								<%
									for(Org org : orgs){
										Object _amount = item.get("f"+org.getShowCoding());
										double amount = _amount == null ? 0d:Double.parseDouble(_amount.toString());
										rowamount += amount;
								%>
									<td nowrap="nowrap"><%=amount==0?"-":amount%></td>
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
												out.println(otheramount);
											%>
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
							} 
							%>
					
							</tbody>
								<tfoot>
									<tr>
										<th>合计</th>
										<th></th>
										<th></th>
										<th></th>
										<th></th>
										<th><fmt:formatNumber pattern="#,##0.00" value="<%=tze_sum%>"/></th>
										<%
											for(Org org : orgs){
										%>
											<th><fmt:formatNumber pattern="#,##0.00" value="<%=sum.get(org.getShowCoding())%>"/></th>
										<%
											}
											if(null != org_code2 && !"".equals("org_code2")){
										%>
											<th><fmt:formatNumber pattern="#,##0.00" value="<%=otheramount_all%>"/>
										<%
											}
										%>
										<th><fmt:formatNumber pattern="#,##0.00" value="<%=lx_sum%>"/></th>
										
									</tr>
								</tfoot>
							
							</table>
					
					
						<div class="foot">
							<div>经办:</div>
							<div>复核:</div>
							<div style="width:260px;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
						</div>
				
	</body>
</html>
