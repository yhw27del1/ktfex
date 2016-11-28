<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<link href="/Static/js/tree/tabletree/styles.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/Static/js/tree/tabletree/jquery.treetable.js"></script>
<script>
	$(function(){
		$.treetable.defaults={
			id_col:1,//ID td列 {从0开始}
			parent_col:0,//父ID td列
			handle_col:2,//加上操作展开操作的 td列
			open_img:"/Static/js/tree/tabletree/images/minus.gif",
			close_img:"/Static/js/tree/tabletree/images/plus.gif"
		};
		$("#tb").treetable();
		//应用样式
		$("#tb tr:even td").addClass("alt");
		$("#tb tr").find("td:eq(1)").addClass("spec");
		$("#tb tr:even").find("td:eq(1)").removeClass().addClass("specalt");
		$('.controllor_image').click(function(){
			
			var iframe = $("iframe",parent.document);
			iframe.height($("#mytable").height()+50);
		});
	})
	function settag(tag,id){
		$.post('/back/region/regionAction!settag?region_id='+id+'&tag='+tag,null,function(data,states){
			window.location.reload();
		});
	}
</script>
<body>
<input type='hidden' class='autoheight' value="auto" /> 

<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
	<button class="ui-state-default reflash">刷新</button>
	<button class="ui-state-default" onclick="javascript:window.location.href='/back/regionAction!ui'">新增</button>
	<!-- <button class="ui-state-default" disabled="disabled">删除所选</button>
	关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default">搜索</button>
	 -->
	 <form id="form1" action="/back/region/regionAction!list"
					method="post">
					<input type="hidden" name="page" value="1" />
	</form>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content" id="mytable">
		<thead>
			<tr class="ui-widget-header ">
				<th width="30" style="display:none">parent</th>
				<th width="30" style="display:none">ID</th>
				<th>地点名称(长)</th>
				<th>地点名称(短)</th>
				<th>地点编码</th>
				<th>是否启用</th>
				<th>可用操作</th>
			</tr>
		</thead>
		<tbody id="tb">
		<c:forEach items="${pageView.records}" var="iter_o" >
			<tr>
				<td align='center' style="display:none">${iter_o.areaparentcode=="0000"?"":iter_i.areaparentcode}</td>
				<td align='center' style="display:none">${iter_o.areacode}</td>
				<td>${iter_o.areaname_l}</td>
				<td>${iter_o.areaname_s}</td>
				<td>${iter_o.areacode}</td>
				<td><c:if test="${iter_o.enabled==true}">已启用&nbsp;&nbsp;<a style='cursor:pointer' onclick='settag(false,"${iter_o.id}")'>置为未启用</a></c:if><c:if test="${iter_o.enabled==false}">未启用&nbsp;&nbsp;<a style='cursor:pointer' onclick='settag(true,"${iter_o.id}")'>置为启用</a></c:if></td>
				<td>
					<button onclick="javascript:window.location.href='/back/regionAction!ui?region_parent_id=${iter_o.id}'" class="ui-state-default">添加</button>
					&nbsp;&nbsp;<button onclick="javascript:window.location.href='/back/regionAction!ui?region_id=${iter_o.id}'" class="ui-state-default">修改名称</button>
					&nbsp;&nbsp;<button onclick="javascript:if(confirm('该操作会删除该类下所有子类，并不可恢复，确定删除吗？')){window.location.href='/back/regionAction!del?region_id=${iter_o.id}'}" class="ui-state-default">删除</button>
				</td>
			</tr>
			<c:forEach items="${iter_o.children}" var="iter_i" >
				<tr>
					<td align='center' style="display:none">${iter_i.areaparentcode=="0000"?"":iter_i.areaparentcode}</td>
					<td align='center' style="display:none">${iter_i.areacode}</td>
					<td>${iter_i.areaname_l}</td>
					<td>${iter_i.areaname_s}</td>
					<td>${iter_i.areacode}</td>
					<td><c:if test="${iter_i.enabled==true}">已启用&nbsp;&nbsp;<a style='cursor:pointer' onclick='settag(false,"${iter_i.id}")'>置为未启用</a></c:if><c:if test="${iter_i.enabled==false}">未启用&nbsp;&nbsp;<a style='cursor:pointer' onclick='settag(true,"${iter_i.id}")'>置为启用</a></c:if></td>
					<td>
						<button onclick="javascript:window.location.href='/back/regionAction!ui?region_id=${iter_i.id}'" class="ui-state-default">修改名称</button>
						&nbsp;&nbsp;<button onclick="javascript:if(confirm('该操作不可恢复，确定删除吗？')){window.location.href='/back/regionAction!del?region_id=${iter_i.id}'}" class="ui-state-default">删除</button>
					</td>
				</tr>
			</c:forEach>
		</c:forEach>
			
		</tbody>
		<tfoot><tr><td colspan="7"><jsp:include page="/common/page.jsp"></jsp:include></td></tr></tfoot>
	</table>
	
</div>
</body>