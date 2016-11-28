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
				<th>
					积数
				</th>
				<th>
					年利率(%)
				</th>
			</tr>
		</thead>
		<c:set var="sum" value="0"></c:set>
		<c:set var="lx" value="0"></c:set>
		<c:set var="count" value="0"></c:set>
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
					${dt.sum}
				</td>
				<td class="xlsMoney">
					${main.main_rate}
				</td>
			</tr>
		</tbody>
		</c:forEach>
	</table>
</body>
