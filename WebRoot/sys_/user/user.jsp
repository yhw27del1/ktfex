<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head> 
		<title>角色</title>
 <script type="text/javascript" src="/Static/js/tree/selecttree/jstree/css.js"></script>
<script src="/Static/js/tree/selecttree/jstree/tree_component.js" type="text/javascript"></script>
<script src="/Static/js/tree/selecttree/common.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/Static/js/tree/selecttree/jstree/tree_component.css" />
<script type="text/javascript" src="/Static/js/tree/selecttree/jstree/css.js"></script>  
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
 <link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>  
 
		<script type="text/javascript">
		
         $(function() {   
			  jQuery.validator.addMethod("checkUserName", function(value, element){
				        var result = false; 
				        $.ajaxSetup({
				            async: false
				        });
				        var param = {
				            userName: value
				        };
				        $.post("/sysCommon/sysUserAction!validateUser", param, function(data){
 				            result = (0 == data);
				        }); 
				        $.ajaxSetup({
				            async: true
				        });
				        return result;
				    }, "用户名已经存在"); 
			  
		       $("#userSetRoleForm").validate({
		                rules: { 
		                  "parent_id":{ required:true},  
		                  "user.username":{ required:true,checkUserName:true},  
		                  //"user.password":{ required:true},  
		                  "user.realname":{ required:true}
		                },  
		                messages: {      
		                  "parent_id":{ required:"请选择所属机构"}, 
		                  "user.username":{ required:"请输入用户名"}, 
		                  //"user.password":{ required:"请输入密码"}, 
		                  "user.realname":{ required:"请输入姓名"}   
		                }    
		        });   
		})
		
		   $(document).ready(function() {
					//添加选中项
					$("#add").click(function() {
						var options = $("#select1 option:selected");
						options.remove();
						options.appendTo("#select2");
						return false;
					});
			
					//添加所有
					$("#addAll").click(function() {
						var options = $("#select1 option");
						options.remove();
						options.appendTo("#select2");
						return false;
					});
			
					//删除选中
					$("#remove").click(function() {
						var options = $("#select2 option:selected");
						options.remove();
						options.appendTo("#select1");
						return false;
					});
			
					//删除全部
					$("#removeAll").click(function() {
						var options = $("#select2 option");
						options.remove();
						options.appendTo("#select1");
						return false;
					});
					
					$("#submitRole").click(function() {
						$("#userSetRoleForm").submit();
					});
					
					
					$("#userSetRoleForm").submit(function(){
						var htmlstr = "";
						var options = $("#select2 option");
						for(var i = 0; i < options.length; i++){
							htmlstr = htmlstr + options[i].value + ",";
						}
						$("#roleIds").val(htmlstr);
					});
					<c:if test="${id==0}">
					    initSelect("classic","选择所属机构","parent_id","/sys_/orgAction!selectTree");
					    //setTitle2("新增管理员"); //重新设置切换tab的标题
					</c:if>    
				    <c:if test="${id!=0}"> 
					    //setTitle2("修改管理员"); //重新设置切换tab的标题
				    </c:if>   
				});
				
	     function moveOption(e1, e2){  
		      for(var i=0;i<e1.options.length;i++){  
			       if(e1.options[i].selected){
				        var e = e1.options[i];
				        e2.options.add(new Option(e.text, e.value));
				        e1.remove(i);
				        i=i-1
			          }  
		        }
		        return false;
		    }
          </script>
	</head>

	<body>
		<form action="/sys_/userAction!edit" method="post" id="userSetRoleForm">
		<input type='hidden' class='autoheight' value="auto" /> 
		    <input type="hidden" name="id" value="${id}"/> 
			<table border="0"> 
				 <tr>
					<td  colspan="4">提示：<span style="color:red">新建用户密码为123456登录时请及时修改</span> 
					</td>
				</tr> 
			     <tr>
					<td align="right"><span style="color:red">*</span>用户名：</td>
					<td colspan="3" >
						   <c:if test="${id==0}"><input  name="user.username"  type="text"  value="${user.username}"/></c:if>
						<c:if test="${id!=0}">${user.username}</c:if> 
					</td>
				</tr> 
                <c:if test="${id==0}"><input  name="user.password"  type="hidden"  value="123456"/> </c:if>  
				<tr>
					<td align="right"><span style="color:red">*</span>姓名：</td>
					<td colspan="3" >
						   <input  name="user.realname"  type="text"  value="${user.realname}"/>	
					</td>
				</tr> 
				<tr>
					<td align="right">所属机构：</td>
					<td colspan="3" >
					        <c:if test="${id==0}"><input type="text" name="parent_id"  id="parent_id"  type="text"  /></c:if><c:if test="${id!=0}">${user.org.name}</c:if>    
					</td>
				</tr> 
				<tr>
					<td align="right">手机：</td>
					<td colspan="3" >
						   <input  name="user.userContact.mobile"  type="text"  value="${user.userContact.mobile}"/>	
					</td>
				</tr>
				<tr>
					<td align="right">座机：</td>
					<td colspan="3" >
						   <input  name="user.userContact.phone"  type="text"  value="${user.userContact.phone}"/>	
					</td>
				</tr>	
				<tr>
					<td align="right">家庭地址：</td>
					<td colspan="3" >
						   <input  name="user.userContact.address"  type="text"  value="${user.userContact.address}"/>	
					</td>
				</tr>	
				<tr>
					<td align="right">邮编：</td>
					<td colspan="3" >
						   <input  name="user.userContact.postalcode"  type="text"  value="${user.userContact.postalcode}"/>	
					</td>
				</tr>				
				 <tr>  
				    <td></td> 
					<td >未选角色</td> 
					<td></td> 
					<td>已选角色</td> 
				</tr> 

				<tr> 
				    <td></td> 
					<td width="110" >
						<select multiple="multiple" id="select1" style="width: 200px; height: 200px" onchange="moveOption(document.getElementById('select1'),document.getElementById('select2'));" id="select1" >
							<s:iterator value="#request.notInUserRoleList" status="statu"
								id="role">
								<option value="${role.id }">
									${role.name}
								</option>
							</s:iterator>
						</select>
					</td>
					<td width="80">
					<button class="ui-state-default" id="addAll">全部添加</button> 
					<br />
					<!-- <button class="ui-state-default" id="add">添加选中</button>
					<br />
					<button class="ui-state-default" id="remove">删除选中</button>
					<br /> -->
					<button class="ui-state-default" id="removeAll">删除全部</button> 
					</td>
					<td >
						<select multiple="multiple" id="select2" name="userRoleArray" style="width: 200px; height: 200px" onchange="moveOption(document.getElementById('select2'),document.getElementById('select1'));" id="select2">
							<s:iterator value="#request.inUserRoleList" status="statu"
								id="role">
								<option value="${role.id }">
									${role.name}
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>
			</table>
			<input type="hidden" name="roleIds" id="roleIds" /> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="ui-state-default" id="submitRole"  style="display:<c:out value="${menuMap['user_add']}" />">保存</button>
		</form>
	</body>
</html>
