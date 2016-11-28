<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
            .selected1 {
                background-color: #f0f0f0 !important;
            }
        </style>
        <script type="text/javascript" src="/Static/js/autoheight.js"></script>
        <script type="text/javascript" src="/Static/js/jquery-selected.js"></script>
        <script type="text/javascript"
            src="/Static/js/jquery.tablemyui.js"></script>
        <script type="text/javascript" src="/Static/js/jquery.form.js"></script>
        <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
        <script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">   
 function result(data,s){  
	if(s=="success"){ 
	    if(data['code']=="1"){   
	        alert(data['tip']);    
	        $("#form1").attr('action','/back/userGroupAction!groupSelectUserList');    
	        window.location.href ='/back/userGroupAction!groupSelectUserList?id=${id}';      
		}else{  
			alert(data['tip']);    
		}
	}else{
		alert("加入成员异常");  	
	}  
}	  

 
$(document).ready(function(){ 
      $(".table_solid").tableStyleUI();  
    
      $("#addUserButton").click(function(){ 
     	    if($("#username").val() == "") {   
	             alert("操作提示：会员交易帐号不能为空!");  
	             $("#username").focus();      
	             return false;      
               }     

          	    var options={
				dataType:"json",
				success:result,
				timeout:2000
		   }    
		   $("#form1").attr('action','/back/userGroupAction!addUser');         
           $("#form1").ajaxForm(options); 
              
            
	 }); 

      $("#checkedAll").click(function() {
          if ($(this).attr("checked") == "checked") { // 全选
                  $("input[name='chkGoods']").each(function(index,element) {
                      $(this).attr("checked", true);
                      $('tbody > tr', $('#table_solid')).addClass('selected1');
                  });
              } else { // 取消全选
                  $("input[name='chkGoods']").each(function(index) {
                      $(this).attr("checked", false);
                      $('tbody > tr', $('#table_solid')).removeClass('selected1');
                  });
              }
          });

      //选择行
      //为表格行添加选择事件处理
      $('tbody > tr', $('#table_solid')).click(function(){
           var hasselected = $(this).hasClass("selected1");
          $(this)[hasselected?"removeClass":"addClass"]("selected1").find(":checkbox").attr("checked",!hasselected);
      }).hover(       //注意这里的链式调用
          function(){
              $(this).addClass('mouseOver');
          },
          function(){
              $(this).removeClass('mouseOver');
          }
      );

      $("#addToUrl").click(function() {
          var categories = $('input[name="chkGoods"]:checked').map(function() {  
              return $(this).val();  
           }).get();  
          alert(categories);
          if(categories == null || categories ==""){
              alert("请至少选择一项！");
              return false;
          }
          var groupId = $("#groupId").val();
          $.ajax({url:"/back/userGroupAction!delUsers",
              type:"post",
              dataType:"json",
              data:"usernames="+categories+"&id="+groupId,
              error:function(){alert("操作失败,请重试！");},
              success:result
          }); 
      });
});

function toURL(url){ 
   window.location.href = url; 
}  
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>   

</head> 
<body>
<form  action="" method="post" id="form1">       
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<input type="hidden" name="groupId" value="${id}" id="groupId"/>  
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all"  > 
 
	输入会员编号：&nbsp;<input type="text" name="username" id="username"  value placeholder ="仅供添加"/>  
	<button class="ui-state-default"  id="addUserButton"  style="display:<c:out value="${menuMap['userGroup_addUser']}" />">加入</button>   
	<button class="ui-state-default" onclick="toURL('/back/userGroupAction!addUsersPage?id=${id}');return false;"   style="display:<c:out value="${menuMap['userGroup_addUser']}" />"/>批量添加</button>
	<button class="ui-state-default" id="addToUrl"  onclick="javascript:;return false;"  style="display:<c:out value="${menuMap['userGroup_addUser']}" />"/>批量删除</button>
	<button class="ui-state-default" onclick="toURL('/back/userGroupAction!list');return false;"   />返回</button>  
	    查询关键字：&nbsp;<input type="text" name="keyWord" id="keyWord" style='width:120px;'  value placeholder ="仅供查询"/>
    <button class="ui-state-default"  id="search">查询</button>   
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content"  id="table_solid">
		<thead>
			<tr class="ui-widget-header "> 
			    <th width="30px">
								<label for="qx">
									<input type="checkbox" id="checkedAll" />
								</label>
							</th>
				
				<th>会员编号</th>
				<th>会员名称</th> 
				<th>所属机构</th>
				<th></th>
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr>  
			<td height="20">
                                    <input name="chkGoods" type="checkbox"
                                        value="${entry.id}" />
                                </td>
				<td>${entry.user.username}</td> 
				<td><script>document.write(name("${entry.user.realname}"));</script></td>  	 
				<td>${entry.user.org.name}</td>  	 
				<td> 
				 <button class="ui-state-default" onclick="toURL('/back/userGroupAction!delUser?groupUserId=${entry.id}&id=${entry.groupId}');return false;"  style="display:<c:out value="${menuMap['userGroup_delUser']}" />"/>删除成员</button>
 				</td>  
			</tr>
		</c:forEach>
		</tbody>
		<tbody>
			<tr>
				<td colspan="11">  
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 