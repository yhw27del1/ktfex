<%--
2012-08-21、22 aora修改此文件，增加“负责人地址”、“负责人邮编”、“创建人”、“创建日期”和“状态”的显示
2012-08-23 aora修改此文件，将“创建人”一列由原来的“username”值改为“realname”的值
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<link href="/Static/js/tree/tabletree/styles.css" rel="stylesheet" type="text/css" />
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/tree/tabletree/jquery.treetable.js"></script>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
function del(id){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("确认要删除此机构吗？");
        dlgHelper.set_Msg("执行这个操作，此机构和此机构所有下级机构将被删除，你确认要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                window.location.href = "/sys_/orgAction!del?id="+id; 
                $(this).dialog('close');
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 
}

$(document).ready(function(){  
		$.treetable.defaults={
			id_col:0,//ID td列 {从0开始}
			parent_col:3,//父ID td列
			handle_col:1,//加上操作展开操作的 td列
			open_img:"/Static/js/tree/tabletree/images/minus.gif",
			close_img:"/Static/js/tree/tabletree/images/plus.gif"
		};
		   
		$("#tb").treetable();
		
		//应用样式
		$("#tb tr:even td").addClass("alt");
		$("#tb tr").find("td:eq(1)").addClass("spec");
		$("#tb tr:even").find("td:eq(1)").removeClass().addClass("specalt");
				
		//隐藏数据列,只用于运算，显示出来没有意义
		$("#tb tr").find("td:eq(0)").hide();
		$("#tb tr").find("td:eq(3)").hide();
		$("#mytable tr:eq(0)").find("th:eq(0)").hide();
		$("#mytable tr:eq(0)").find("th:eq(3)").hide();
	}); 
</script>  
<body>
<form id="form1" action="/sys_/orgAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<div >
  	<button class="ui-state-default" onclick="toURL('/sys_/orgAction!ui');return false;"  style="display:<c:out value="${menuMap['org_add']}" />">新增机构</button> 
</div> 
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content"  id="mytable" >
		<thead>
			<tr class="ui-widget-header "> 
				<th>id</th>
				<th width="20%">机构名称</th>
				<th width="6%">上级机构</th> 
				<th>parentId</th>				
				<th width="6%">编码</th>
				<th width="6%">简称</th>
				<th width="8%">机构类型</th>
				
				<th width="7%">联系手机</th>
				<th width="7%">联系座机</th>
				<th width="10%">地址</th>
				<!--  <th width="9%">负责人邮编</th>-->
				<th width="6%">创建人</th>
				<th width="7%">创建日期</th>
				<th width="6%">状态</th>
				<th width="15%">操作</th>
			</tr>
		</thead>
		<tbody id="tb" >
		<c:forEach items="${pageView.records}" var="org">
			<tr> 
			    <td>${org.id}</td>
				<td>${org.name}</td>
				<td><c:if test="${org.parent.id != 1}">${org.parent.name}</c:if></td> 
				<td><c:if test="${org.parent.id != 1}">${org.parent.id}</c:if> </td>
				<td>${org.showCoding}</td>
				<td>${org.shortName}</td> 
				<td><c:if test="${org.type == 0}">保证机构</c:if>
				    <c:if test="${org.type == 1}">服务中心</c:if>
				    <c:if test="${org.type == 2}">保证+服务中心</c:if>
				</td>
				
				<td>${org.orgContact.mobile}</td> 
				<td>${org.orgContact.phone}</td>
				<td>${org.orgContact.address}</td>
				<!--  <td>${org.orgContact.postalcode}</td>-->
				<td>${org.createBy.realname}</td>
				<td><fmt:formatDate value="${org.createDateBy}" pattern="yyyy-MM-dd"/></td>
				<td><c:if test="${org.state == 0}">正常</c:if><c:if test="${org.state == 1}"><span style="color: red;">已注销</span></c:if></td>
				<td><c:if test="${org.state == 0}"><button class="ui-state-default"  onclick="toURL('/sys_/orgAction!ui?id=${org.id}');return false;"   style="display:<c:out value="${menuMap['org_edit']}" />"/>修改</button>&nbsp;</c:if><button  class="ui-state-default"  onclick="del('${org.id}');return false;"  style="display:<c:out value="${menuMap['org_del']}" />">删除</button></td>
			</tr>
		</c:forEach> 
		</tbody>
	</table> 
</div>
</form>
</body>