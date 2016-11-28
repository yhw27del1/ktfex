<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
   $(function(){
   $(".table_solid").tableStyleUI(); 
   });
	function toURL(url) {
		window.location.href = url;
	}
	function del(id) {
		var cofirmDialog = new dialogHelper();
        cofirmDialog.set_Title("确认要删除此会员类型吗？");
        cofirmDialog.set_Msg("执行这个操作，此会员类型将被删除，你确认要这么做吗？");
        cofirmDialog.set_Height("180");
        cofirmDialog.set_Width("650");
        cofirmDialog.set_Buttons({
            '确定': function(ev) {
                    window.location.href = "/back/member/memberTypeAction!del?id="+id;
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        cofirmDialog.open();
		//window.location.href = "/back/member/memberTypeAction!del?id=" + id;
	}
</script>
<body>
	<form id="form1" action="/back/member/memberTypeAction!list"
		method="post">
		<input type='hidden' class='autoheight' value="auto" />
		<div id="myToolBar"
			class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<div style="float: left;">
				<input type="hidden" name="page" value="1" />
				会员类型&nbsp;
				<input class="input_box" style="width: 120px;" type="text" name="keyword" value="${keyword}" />
				<input type="submit" class="ui-state-default" value="查找" />
			</div>
			<div style="float: right; margin-right: 20px;">
				<button class="ui-state-default"
					onclick="toURL('/back/member/memberTypeAction!edit?modify=false');return false;"
                    style="display:<c:out value="${menuMap['memberType_edit']}"/>">
					新增
				</button>
				&nbsp;
			</div>
		</div>
		<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header">
						<th>
							类型名称
						</th>
						<th>
							类型代码
						</th>
						<th>
							创建日期
						</th>
						<th width="18%">
							操作
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.records}" var="membertype">
						<tr>
							<td>
								${membertype.name}
							</td>
							<td>
								${membertype.code}
							</td>
							<td>
								<fmt:formatDate value="${membertype.createDate}" type="date" />
							</td>
							<td>
								<button class="ui-state-default"
									onclick="toURL('/back/member/memberTypeAction!edit?modify=true&id=${membertype.id}');return false;"
									style="display:<c:out value="${menuMap['memberType_edit']}" />">
									修改
								</button>
								<span style="clear: both"></span>&nbsp;&nbsp;&nbsp;
								<button class="ui-state-default"
									onclick="del('${membertype.id}');return false;"
									style="display:<c:out value="${menuMap['memberType_del']}" />">
									删除
								</button>
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="8">
							<jsp:include page="/common/page.jsp"></jsp:include></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</body>