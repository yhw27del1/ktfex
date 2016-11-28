<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	
});
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3
}
.agreement{cursor:pointer;}
.agreement:HOVER{text-decoration: underline;}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
	<form action="">
	<input type="hidden" name="page" value="1"/>
	<input type="hidden" name="id" value="${id}"/>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						日志时间
					</th>
					<th>
						修改者IP
					</th>
					<th>
						修改人账户/姓名
					</th>
					<th>
						内容
					</th>
					<th>
						原数据
					</th>
					<th>
						新数据
					</th>
					<th>
                        关联用户
                    </th>
					
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
						  <fmt:formatDate value="${entry.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss" />
						</td>
						<td>
							${entry.modifyIP}
						</td>
						<td>
							${entry.changer.username}/${entry.changer.realname}
						</td>
						<td>
							${entry.content}
						</td>
						<td>
							${entry.firstData}
						</td>
						<td>
                            ${entry.endData}
                        </td>
                        <td>
                            ${entry.modifyModel}
                        </td>
						
					</tr>
				</c:forEach>
				<tr>
					<td colspan="6">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
