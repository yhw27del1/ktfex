<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".table_solid").tableStyleUI();
		$(".modify_button").css({"font-size":"13px","padding":"3px 5px 3px 5px","cursor":"pointer"}).button().click(function(){
			var id = $(this).attr("target");
			window.location.href="/back/activity/lend!lend_ui?id="+id;
		});
		$(".del_button").css({"font-size":"13px","padding":"3px 5px 3px 5px","cursor":"pointer"}).button().click(function(){
			var id = $(this).attr("target");
			window.location.href="/back/activity/lend!del?id="+id;
		});
		$("#startDate,#endDate").datepicker({
			changeYear: true,
	        dateFormat: "yy-mm-dd"
	    });
	   $("#ui-datepicker-div").css({'display':'none'});
	});
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		
		<div style="float: left;line-height:25px;">
			<form id="form1" action="/back/activity/lend!invest_lend_list" method="post">
				<input type="hidden" name="page" value="1" />
				<label for="username">会员&nbsp;</label><input type="text" id="username" placeholder="会员名称/会员编号" name="keyWord" value="${keyWord }"/>
				<label for="username">按合约的<s:select list="#{'1':'签约日期','-1':'到期日期'}" value="type" name="type"/>:</label>
				<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询"/>
			</form>
		</div>
		<div style="float: right;">
			<button  class="ui-state-default"  onclick="window.location.href='/back/activity/lend!lend_ui'">新增</button>
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
						限制金额
					</th>
					<th>
						签约日期
					</th>
					<th>
						到期日期
					</th>
					<th>
						创建日期
					</th>
					<th>
						备注
					</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="iter">
					<tr>
						<td>
							${iter.user.username}
						</td>
						<td>
							<script>document.write(name("${iter.user.realname}"));</script>
						</td>
						<td>
							${iter.money}
						</td>
						<td>
							<fmt:formatDate value="${iter.startDate}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.endDate}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.createDate}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							${iter.memo}
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