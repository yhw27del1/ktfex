<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<title>${financing.code }/${financing.shortname }</title>
		<style>
			body{padding:0;margin:0;}
			.mytable{font-size:13px;border-collapse:collapse;border:solid 1px #000;width:100%;}
			.mytable td,.mytable th{border:solid 1px #000;}
			.opencontract_a{color: -webkit-link;
text-decoration: underline;
cursor: pointer;}
			.opencontract_a:HOVER{text-decoration: underline;}
		</style>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script type="text/javascript">
			function opencontract(url){
				window.open(url);
			}	
		</script>
	</head>
	<body>
			<table class="mytable">
				<thead>
					<tr>
						<th width="70" valign="middle">项目编号</th>
						<th width="150" valign="middle">项目简称</th>
						<th width="110" valign="middle">投标人账户/姓名</th>
						<th width="110" valign="middle">投标人身份证</th>
						<th width="100" valign="middle">融资方交易帐号</th>
						<th width="50" valign="middle">投标金额</th>
						<th width="60" valign="middle">投标日期</th>
						<th>合同编号及状态</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${records}" var="item">
						<tr>
							<td valign="middle">${financing.code }</td>
							<td valign="middle">${financing.shortname }</td>
							<td valign="middle">
								${item.username}/<script>document.write(name("${item.realname}"));</script>
							</td>
							<td valign="middle">
								<script>document.write(idcard("${item.idcardno}"));</script>
							</td>
							<td valign="middle">${financing.username }</td>
							<td align="right" valign="middle">
								<fmt:formatNumber value='${item.investamount}' pattern="#,###,##0.00" />
							</td>
							<td valign="middle">
								<fmt:formatDate value="${item.createdate}" pattern="yyyy-MM-dd" />
							</td>
							<td valign="middle">
								<a onclick="opencontract('/back/investBaseAction!agreement_ui2?invest_record_id=${item.id}')" id="${item.id}" class="opencontract_a">${item.contract_numbers}</a>/
								<c:if test="${item.investor_make_sure==null}">未确认</c:if>
								<c:if test="${item.investor_make_sure!=null}">已确认</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</body>
</html>


