<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<link href="/Static/js/tree/tabletree/styles.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/Static/js/tree/tabletree/jquery.treetable.js"></script>
<script>
	$(function(){
		//应用样式
		$("#tb tr:even td").addClass("alt");
		
	})
	
</script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 

<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<button class="ui-state-default reflash">刷新</button>
	<button class="ui-state-default" onclick="javascript:window.location.href='/back/bankLibrary/bankLibraryAction!ui'">新增</button>
	<form id="form1" action="/back/region/regionAction!list" method="post">
					<input type="hidden" name="page" value="1" />
	</form>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable">
		<thead>
			<tr class="ui-widget-header ">
				<th>标题</th>
				<th>编码</th>
				
				<th>创建时间</th>
				<th>基本操作</th>
				<th colspan="2" style="text-align: center">排序</th>
			</tr>
		</thead>
		<tbody id="tb">
		<c:forEach items="${pageView.records}" var="iter_o"  varStatus="state">
			<tr>
				
				<td>${iter_o.caption}</td>
				<td>${iter_o.code}</td>
				<td>${iter_o.createtime}</td>
				<td>
					<a href="/back/banklibrary/bankLibraryAction!del?banklibrary_id=${iter_o.id}">删除</a>
					<a href="/back/banklibrary/bankLibraryAction!ui?banklibrary_id=${iter_o.id}">修改</a>
				</td>
				<td width="30">
					<c:if test="${state.index!=0}">
						<a href="/back/banklibrary/bankLibraryAction!order_up?banklibrary_id=${iter_o.id}">向上</a>
					</c:if>
				</td>
				<td width="30">
					<c:if test="${state.last!=true}">
					<a href="/back/banklibrary/bankLibraryAction!order_down?banklibrary_id=${iter_o.id}">向下</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
			
		</tbody>
		<tfoot><tr><td colspan="8"><jsp:include page="/common/page.jsp"></jsp:include></td></tr></tfoot>
	</table>
	
</div>
</body>