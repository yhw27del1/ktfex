<%@ page language="java" import="java.util.*"  contentType="application/msexcel"  pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%
response.setHeader("Content-Disposition", "inline;filename="+ request.getAttribute("msg")+".xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<style>
   .xlsMoney{mso-number-format:"0\.00";}
   .xlsDate{mso-number-format:"yyyy\/mm\/dd";}
   .xlsText{mso-number-format:"\@";}
</style>
	<body>
		<div style="margin: 50px auto; font-weight: bold;">
			<table style="font-size: 13px; font-weight: bold">
				<thead>
					<tr>
						<th>
							序号
						</th>
						<th>
							会员名称
						</th>
						<th>
							会员编号
						</th>
						<th>
							应发利息
						</th>
						<th>
							备注
						</th>
						<th style="text-align: right;">
							积数1
						</th>
						<th style="text-align: right;">
							年利率1(%)
						</th>
						<th style="text-align: right;">
							利息1
						</th>
						<th style="text-align: right;">
							积数2
						</th>
						<th style="text-align: right;">
							年利率2(%)
						</th>
						<th style="text-align: right;">
							利息2
						</th>
						<th>
							积数
						</th>
					</tr>
				</thead>
				<br />
				<c:forEach items="${detail}" var="dt" varStatus="mm">
				<tbody>
					<tr>
						<td class="xlsText">
							${mm.count}
						</td>
						<td class="xlsText">
							<c:if test="${menuMap['name']=='inline'}">
								${dt.realname}
							</c:if>
							<c:if test="${menuMap['name']!='inline'}">
								<c:choose>
									<c:when test="${fn:length(dt.realname) == 0}">
										无
									</c:when>
									<c:when test="${fn:length(dt.realname) > 1}">
										<c:out value="${fn:substring(dt.realname,0,1)}****" />
									</c:when>
									<c:otherwise>
										****
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<td class="xlsText">
							${dt.username}
						</td>
						<td class="xlsMoney">
							${dt.lx}
						</td>
						<td class="xlsText">
							${main.memo}
						</td>
						<td class="xlsMoney">
							${dt.sum1}
						</td>
						<td class="xlsMoney">
							${dt.r1}
						</td>
						<td class="xlsMoney">
							${dt.lx1}
						</td>
						<td class="xlsMoney">
							${dt.sum2}
						</td>
						<td class="xlsMoney">
							${dt.r2}
						</td>
						<td class="xlsMoney">
							${dt.lx2}
						</td>
						<td class="xlsMoney">
							${dt.sum}
						</td>
					</tr>
				</tbody>
				</c:forEach>
			</table>
		</div>
	</body>
