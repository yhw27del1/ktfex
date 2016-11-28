<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set value="<%=new Date()%>" var="now"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>还款记录统计打印</title>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			
			table,td,th{
				border:1px solid #000;   
    			border-collapse:collapse;
    			white-space:nowrap;
			}
			.table_content tbody td{
				padding-left:5px;
				padding-right:5px;
			}
			#print_do{
				position: fixed;
				top:10px;
				right:10px;
			}
			#logo{
				position: absolute;
				left:10px;
				top:10px;
				width:40px;
				height:40px;
			}
</style>
		
	</head>
	<body style="font-size:12px;">
		<input id="print_do" type="button" value="打印页面"/>
		<div style="margin:0 auto 0 auto;">
			<img src="/Static/images/logo.png" id="logo"/>
			<div style="text-align: center"><h1>昆投互联网金融交易-结算部还款明细表</h1></div>
			<span>
				会计期间:<fmt:formatDate value="${startDate }" pattern="yyyy/MM/dd"/>-<fmt:formatDate value="${endDate }" pattern="yyyy/MM/dd"/>
			</span>
			<span style="margin-left:10px;">
				记录状态
				<c:choose>
					<c:when test="${state==0}">未还款</c:when>
					<c:when test="${state==1}">正常还款</c:when>
					<c:when test="${state==2}">提前还款</c:when>
					<c:when test="${state==3}">逾期还款</c:when>
					<c:when test="${state==4}">担保代偿</c:when>
					<c:when test="${state==5}">全部还款</c:when>
				</c:choose>
			</span>
			<c:if test="${fcode!=null}">
				<span style="margin-left:10px;"><c:if test="${fcode!=''}">筛选:${fcode }</c:if></span>
			</c:if>
			<span style="margin-left:10px;">担保机构:<c:if test="${org!=null}">${org.showCoding}/${org.name}</c:if><c:if test="${org==null}">全部</c:if></span>
			
		<table class="table_content" width="100%">
			<thead>
				<tr>
					
					<th rowspan="2">
						还款日
					</th>
					<th rowspan="2">
						签约日
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
				<c:set value="${status.count}" var="count"/>
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
							${item.financbasecode}
						</td>
						<td>
							<script>document.write(name("${item.frealname}"));</script>
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
						<td>
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
					<th>${count}条</th>
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
					<th>
						-
					</th>
					<th>
						-
					</th>
					<th>
						-
					</th>
				</tr>
			</tfoot>
		</table>
		<div style="float:right;line-height:30px;font-weight: bold">
			<div style="float:left;width:120px;">经办:</div>
			<div style="float:left;width:120px;">复核:</div>
			<div style="float:left;">日期:<fmt:formatDate value="${now}" pattern="yyyy/MM/dd HH:mm:ss"/></div>
		</div>
		</div>
	</body>
</html>
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