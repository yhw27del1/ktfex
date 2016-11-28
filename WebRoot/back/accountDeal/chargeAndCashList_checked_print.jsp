<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set value="<%=new Date()%>" var="now"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>现金明细打印</title>
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
			<div style="text-align: center"><h1>昆投互联网金融交易-现金明细表</h1></div>
			<span>
				会计期间:<fmt:formatDate value="${startDate}" pattern="yyyy/MM/dd"/>-<fmt:formatDate value="${endDate}" pattern="yyyy/MM/dd"/>
			</span>
		<table class="table_content" width="100%">
			<thead>
				<tr>
					<th>
						会员类型
					</th>
					<th>
						会员名称
					</th>
					<th>
						交易账号
					</th>
					<th>
						银行账号
					</th>
					<th>
						交易类型
					</th>
					<th>
						充值额
					</th>
					<th>
						提现额
					</th>
					<th>
						专户
					</th>
					<th>
						充值/提现时间
					</th>
					<th>
						审核日期
					</th>
					<th>
						划款日期
					</th>
					<th>
						备注
					</th>
					<th>
						操作员
					</th>
		            <th>
		            	审核员
		            </th>
		            <th>
		            	方式
		            </th>
		            <th>
		            	状态
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
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='T'}">投资人</c:if>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='R'}">融资方</c:if>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='D'}">担保方</c:if>
							<c:if test="${item.TARGET_ACCOUNT_USER_USERTYPE=='Q'}">其他</c:if>
						</td>
						<td>
							<script>document.write(name("${item.TARGET_ACCOUNT_USER_REALNAME}"));</script>
						</td>
						<td>
							${item.TARGET_ACCOUNT_USER_USERNAME}
						</td>
						<td>
							<script>document.write(bankcard("${item.TARGET_ACCOUNT_USER_BANK}"));</script>
						</td>
						<td>
							${item.TYPE}
						</td>
						<td style="text-align: right;">
							<c:if test="${item.TXDIR==2}"></c:if>
							<c:if test="${item.TXDIR==1}">
								<fmt:formatNumber value="${item.MONEY_ADD}" pattern="#,###,##0.00" />
								<c:set value="${dai_sum+item.MONEY_ADD}" var="dai_sum" />
								<c:set value="${dai_count+1}" var="dai_count" />
							</c:if>
						</td>
						<td style="text-align: right;">
							<c:if test="${item.TXDIR==1}"></c:if>
							<c:if test="${item.TXDIR==2}">
								<fmt:formatNumber value="${item.MONEY_SUBTRACT}" pattern="#,###,##0.00" />
								<c:if test="${item.CHECKFLAG2=='1'}">
									<c:set value="${jie_sum+item.MONEY_SUBTRACT}" var="jie_sum" />
									<c:set value="${jie_count+1}" var="jie_count" />
								</c:if>
							</c:if>
						</td>
						<td>
							<c:if test="${item.CHANNEL==0}">无</c:if>
							<c:if test="${item.CHANNEL==1}">招商银行</c:if>
							<c:if test="${item.CHANNEL==2}">工商银行</c:if>
						</td>
						<td>
							<fmt:formatDate value="${item.CREATEDATE}" pattern="yyyy/MM/dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${item.CHECKDATE}" pattern="yyyy/MM/dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${item.HKDATE}" pattern="yyyy/MM/dd HH:mm:ss"/>
						</td>
						<td>
							${item.MEMO}
						</td>
						<td>
							${item.OPERAT_USER}
						</td>
						<td>
							${item.CHECK_USER}
						</td>
						<td>
							<c:if test="${item.BATCHFLAG==1}">批量</c:if>
							<c:if test="${item.BATCHFLAG!=1}">单笔</c:if>
						</td>
						<td>
							<c:if test="${item.CHECKFLAG=='1'&&item.CHECKFLAG2=='1'}">
								已审核
							</c:if>
							<c:if test="${item.CHECKFLAG=='37'||item.CHECKFLAG=='41'||item.CHECKFLAG=='43'}">
								已审核
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='0'}">
								待划款
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='1'}">
								已划款
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='2'}">
								转账异常
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='3'}">
								提现错误
							</c:if>
							<c:if test="${item.CHECKFLAG=='4'&&item.CHECKFLAG2=='4'}">
								提现冲正
							</c:if>
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
					<th style="text-align: right;">共:${count}笔</th>
					<th  style="text-align: right;">
						${dai_count}笔<br />
						<fmt:formatNumber value="${dai_sum}" pattern="#,###,##0.00" />
					</th>
					<th style="text-align: right;">
						${jie_count}笔<br />
						<fmt:formatNumber value="${jie_sum}" pattern="#,###,##0.00" />
					</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
					<th>-</th>
				</tr>
			</tfoot>
		</table>
		<div style="float:right;line-height:30px;font-weight: bold">
			<div style="float:left;width:120px;">经办:${user.realname}</div>
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