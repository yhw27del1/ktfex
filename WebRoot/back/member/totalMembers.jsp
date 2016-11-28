<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
});
</script>
<script type="text/javascript">
	function toURL(url) {
		window.location.href = url;
	}
	function del(id) {
		window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 
<form action="">
	<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				审核日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						机构编码
					</th>
					<th>
						机构名称
					</th>
					<th>
						会员总数
					</th>
					<th>
						投资人总数
					</th>
					<th>
						融资方总数
					</th>
					<th>
						担保公司总数
					</th>
					<th>
						银行总数
					</th>
					<th>
						其他总数
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${totalMembers}" var="tm">
					<tr>
						<td>
							${tm.orgCode}
						</td>
						<td>
						    ${tm.orgName}
						</td>
						<td>
						     ${tm.total}
						</td>
						<td>
							${tm.total_T}
						</td>
						<td>
							${tm.total_R}
						</td>
						<td>
							${tm.total_D}
						</td>
						<td>
							${tm.total_Y}
						</td>
						<td>
							${tm.total_Q}
						</td>
					</tr>
				</c:forEach>
				<tr style="font-weight:bold;font-size:18px;">
				<td></td>
				<td>共：<s:property value="totalMembers.size()" />个机构</td>
				<td>${sum_total}</td>
				<td>${sum_total_T}</td>
				<td>${sum_total_R}</td>
				<td>${sum_total_D}</td>
				<td>${sum_total_Y}</td>
				<td>${sum_total_Q}</td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
