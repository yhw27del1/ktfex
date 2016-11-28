<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<style>
.a_button {
	cursor: pointer;
}

.a_button:HOVER {
	text-decoration: underline;
}

.ui-helper-hidden-accessible{
	display:none;
}
#ui-datepicker-div{display:none}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".table_solid").tableStyleUI();
		$(".modify_button").css({"font-size":"13px","padding":"3px 5px 3px 5px","cursor":"pointer"}).button().click(function(){
			var id = $(this).attr("target");
			window.location.href="/back/activity/iphone5!relevance_ui?relation_id="+id;
		});
		$(".del_button").css({"font-size":"13px","padding":"3px 5px 3px 5px","cursor":"pointer"}).button().click(function(){
			var id = $(this).attr("target");
			window.location.href="/back/activity/iphone5!relevance_del?relation_id="+id;
		});
		$("#startDate,#endDate").datepicker({
			changeYear: true,
	        dateFormat: "yy-mm-dd"
	    });
	    $("#submit_button").button().click(function(){
	    	$("#form1").submit();
	    })
	});
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		
		<div style="float: left;line-height:25px;">
			<form id="form1" action="/back/activity/iphone5!relevance_list" method="post">
				<input type="hidden" name="page" value="1" />
				<label for="username">交易帐号:</label><input type="text" id="username" name="keyWord" value="${keyWord }"/>
				<label for="username">按合约的<s:select list="#{'1':'签约日期','-1':'到期日期'}" value="type" name="type"/>:</label><input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate"/>
				<s:select list="#request.rules" listKey="id" name="code" listValue="code+'-'+title" headerKey="" headerValue="不过滤" cssStyle="padding:1px;"></s:select>
				<span id="submit_button">提交</span>
			</form>
		</div>
		<div style="float: right;">
			<button class="ui-state-default reflash">
				刷新
			</button>
			<button  class="ui-state-default"  onclick="window.location.href='/back/activity/iphone5!relevance_ui'"  style="display:<c:out value="${menuMap['relevance_input']}" />">新增</button>
		</div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						用户帐号
					</th>
					<th>
						用户名称
					</th>
					<th>
						政策编码
					</th>
					<th>
						政策限额
					</th>
					<th>
						签约日期
					</th>
					<th>
						结束日期
					</th>
					<th>
						创建日期
					</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="iter">
					<tr>
						<td>
							${iter.account.user.username}
						</td>
						<td>
							<script>document.write(name("${iter.account.user.realname}"));</script>
						</td>
						<td>
							${iter.rule.code}
						</td>
						<td>
							${iter.rule.value}
						</td>
						<td>
							<fmt:formatDate value="${iter.from}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.end}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.createdtime}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							<span class="modify_button" target="${iter.id}">修改</span>
							<span class="del_button" target="${iter.id}">删除</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tbody>
				<tr>
					<td colspan="5">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>