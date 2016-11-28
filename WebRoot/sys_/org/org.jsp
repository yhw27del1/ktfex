<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head> 
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/tree/selecttree/jstree/css.js"></script>
<script src="/Static/js/tree/selecttree/jstree/tree_component.js" type="text/javascript"></script>
<script src="/Static/js/tree/selecttree/common.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/Static/js/tree/selecttree/jstree/tree_component.css" />
<script type="text/javascript" src="/Static/js/tree/selecttree/jstree/css.js"></script>  
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>   
    <title>机构管理</title>  
	<script type="text/javascript">
	 $(document).ready(function(){   
	  		  jQuery.validator.addMethod("checkShowCoding", function(value, element){
				        var result = false; 
				        $.ajaxSetup({
				            async: false
				        });
				        var param = {
				            showCoding: value
				        };
				        $.post("/sysCommon/sysOrgAction!validateOrg", param, function(data){ 
 				            result = (0 == data);
				        }); 
				        $.ajaxSetup({
				            async: true
				        });
				        return result;
				    }, "机构编码已经存在");  
			   
		  $("#form").validate({ 
		                rules: { 
		                  "parent_id":{ required:true},  
		                  "org.name":{ required:true},  
		                  "org.showCoding":{ required:true,checkShowCoding:true}
		                },  
		                messages: {      
		                  "parent_id":{ required:"请输入上级机构"}, 
		                  "org.name":{ required:"请输入名称"}, 
		                  "org.showCoding":{ required:"机构编码"}  
		                }    
		        })   
		});

		$(document).ready(function(){ 
			$("#submitNewEdit").click(function() {  
                 $("#form").submit(); 
			});  
			
		   <c:if test="${id==0}">
		        initSelect("classic","请选择上级机构","parent_id","/sys_/orgAction!selectTree");   
		        //setTitle2("新增机构"); //重新设置切换tab的标题
		   </c:if>  
		    <c:if test="${id!=0}"> 
			   // setTitle2("修改机构"); //重新设置切换tab的标题
		    </c:if>  
  
			  
		}); 
	</script>
  </head> 
  <body>  
    <form action="/sys_/orgAction!edit" method="post" id="form">
        <input type='hidden' class='autoheight' value="auto" /> 
        <input type="hidden" name="id" value="${id}"/> 
        <table border="0">
            <tr><td align="right"><span style="color:red">*</span>上级机构：</td>
            <td> 
            <c:if test="${id==0}">
		        <input type="text" name="parent_id"  id="parent_id"  type="text"  /> 
		    </c:if>   
		     <c:if test="${id!=0}">
		         ${org.parent.name}  
		    </c:if>   
            </td>
            </tr>
            <tr><td align="right"><span style="color:red">*</span>名称：</td><td><input  name="org.name"  type="text"  value="${org.name}"/></td></tr>

		        <tr>
					<td align="right"><span style="color:red">*</span>机构编码(唯一)：</td>
					<td colspan="3" >
					         <c:if test="${id==0}">
						        <input  name="org.showCoding"  type="text"  value="${org.showCoding}"/>	
						    </c:if>   
						     <c:if test="${id!=0}">
						         ${org.showCoding}  
						    </c:if>  
						   
					</td>
				</tr>
				<tr>
				    <td align="right">
								<span style="color: red">*</span>机构类型：
					</td>
							<td> 
					             <s:select name="org.type" list="orgtypeList" id="orgtype"  listKey="string1" listValue="string2" cssStyle="padding:1px;width:155px;"/>
							</td>
				</tr>
			    <tr>
					<td align="right">简称/品牌：</td>
					<td colspan="3" >
						   <input  name="org.shortName"  type="text"  value="${org.shortName}"/>	
					</td>
				</tr>
			    <tr>
					<td align="right">联系手机：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.mobile"  type="text"  value="${org.orgContact.mobile}"/>	
					</td>
				</tr>
				<tr>
					<td align="right">联系座机：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.phone"  type="text"  value="${org.orgContact.phone}"/>	
					</td>
				</tr>	
				<tr>
					<td align="right">地址：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.address"  type="text"  value="${org.orgContact.address}"/>	
					</td>
				</tr>	
				<tr>
					<td align="right">邮编：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.postalcode"  type="text"  value="${org.orgContact.postalcode}"/>	
					</td>
				</tr>	      
           
           
           
            <tr><td colspan="2"><button class="ui-state-default" id="submitNewEdit"  style="display:<c:out value="${menuMap['org_Add']}" />">保存</button> </td></tr>
        </table>    
    </form> 
  </body>
</html>
