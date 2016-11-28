<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set value="<%=new Date()%>" var="now"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>工行专户业务明细打印</title>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			
			table,td,th{
				border:1px solid #000;   
    			border-collapse:collapse;
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
			<div style="text-align: center"><h1>昆投互联网金融交易-工行专户业务明细</h1></div>
			<span>
				会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy/MM/dd"/>-<fmt:formatDate value="${endDate}" pattern="yyyy/MM/dd"/>
			</span>
		<table class="table_content" width="100%">
			<thead>
				<tr>
					<th>
						会员名称
					</th>
					<th>
						交易账号
					</th>
					<th>
						会员类型
					</th>
					<th>
						专户
					</th>
					<th>
						交易类型
					</th>
		            <th>
		            	项目编号
		            </th>
		            <th>
		            	贷金额
		            </th>
		            <th>
		            	借金额
		            </th>
		            <th>
		            	成功日期
		            </th>
				</tr>
			
			</thead>
			<tbody class="table_solid">
				<c:set value="0" var="dai_sum" />
				<c:set value="0" var="dai_count" />
				<c:set value="0" var="jie_sum" />
				<c:set value="0" var="jie_count" />
				<c:forEach items="${resultList}" var="item" varStatus="status">
				<c:set value="${status.count}" var="count"/>
					<tr>
						<td>
							<script>document.write(name("${item.TARGET_ACCOUNT_USER_REALNAME}"));</script>
						</td>
						<td>
							${item.TARGET_ACCOUNT_USER_USERNAME}
						</td>
						<td>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='T'}">投资人</c:if>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='R'}">融资方</c:if>
						</td>
						<td>
							<c:if test="${item.CHANNEL==2}">工行<br />2502110419024503160</c:if>
						</td>
						<td>
							${item.TYPE}
						</td>
						<td>
							${item.FCODE}
						</td>
						<td style="text-align: right;">
							<c:if test="${item.TXDIR==2}"></c:if>
							<c:if test="${item.TXDIR==1}">
								<c:if test="${item.TYPE=='债权卖出'}"><c:set value="${dai_sum+item.BJ}" var="dai_sum" /><fmt:formatNumber value="${item.BJ}" pattern="#,###,##0.00" /></c:if><c:if test="${item.TYPE!='债权卖出'}"><c:set value="${dai_sum+item.MONEY_DAI}" var="dai_sum" /><fmt:formatNumber value="${item.MONEY_DAI}" pattern="#,###,##0.00" /></c:if>
								<c:set value="${dai_count+1}" var="dai_count" />
							</c:if>
						</td>
						<td style="text-align: right;">
							<c:if test="${item.TXDIR==1}">
								<c:if test="${item.TYPE=='债权卖出'}">${item.LX}</c:if>
								<c:if test="${item.TYPE=='债权卖出'&&item.LX>0}">
									<c:set value="${jie_sum+item.LX}" var="jie_sum" />
									<c:set value="${jie_count+1}" var="jie_count" />
								</c:if>
							</c:if>
							<c:if test="${item.TXDIR==2}">
								<fmt:formatNumber value="${item.MONEY_JIE}" pattern="#,###,##0.00" /><c:if test="${item.TYPE=='债权买入'}"><br />(<fmt:formatNumber value="${item.BJ}" pattern="#,###,##0.00" />+<fmt:formatNumber value="${item.LX}" pattern="#,###,##0.00" />)</c:if>
								<c:set value="${jie_sum+item.MONEY_JIE}" var="jie_sum" />
								<c:set value="${jie_count+1}" var="jie_count" />
								<c:if test="${item.TYPE=='债权买入'&&item.LX>0}">
									<c:set value="${jie_count+1}" var="jie_count" />
								</c:if>
							</c:if>
						</td>
						<td>
							<fmt:formatDate value="${item.SUCCESSDATE}" pattern="yyyy/MM/dd HH:mm:ss"/>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th>合计</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th style="text-align: right;">共:${count}笔</th>
					<th style="text-align: right;">
						${dai_count}笔<br />
						<fmt:formatNumber value="${dai_sum}" pattern="#,###,##0.00" />
					</th>
					<th style="text-align: right;">
						${jie_count}笔<br />
						<fmt:formatNumber value="${jie_sum}" pattern="#,###,##0.00" />
					</th>
					<th>-</th>
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