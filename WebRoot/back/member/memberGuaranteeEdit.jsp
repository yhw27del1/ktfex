<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/common/import.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>新增修改担保公司附加信息</title>
		<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		
		<script type="text/javascript"
			src="/Static/js/star/rater/js/jquery.raty.min.js"></script>
		<script type="text/javascript" src="/Static/js/kindeditor/kindeditor-min.js"></script>
		<script type="text/javascript"> 
			
		
			var ratyPath='/Static/js/star/rater/img';  
		 	$(function() {    
		      //加星级   
			  $("#demo1").raty({half:true,hintList:[],path:ratyPath,number:5,start:${memberGuarantee.tjzs},click:function(score, evt) {
                       $("#tjzs").val(score);   
                 }}); 
			  $("#demo1").css({'width':'130px'});    
			  
		        //项目详细信息
				KE.show({
				        id : 'note',
				        width:'700px',
				        height:'300px',
						resizeMode : 1,
						allowFileManager : true,
						/*图片上传的SERVLET路径*/
						imageUploadJson : "/upload/uploadImage.html", 
						/*图片管理的SERVLET路径*/     
						fileManagerJson : "/upload/uploadImgManager.html",
						/*允许上传的附件类型*/
						accessoryTypes : "doc|xls|pdf|txt|ppt|rar|zip",
						/*附件上传的SERVLET路径*/  
						accessoryUploadJson : "/upload/uploadAccessory.html"
				});
								 
	    
			});
			
			function checkNumber(obj){
			   obj.value=obj.value.replace(/[^(\d)]/g,'')
			}		
				
				
			</script>
	</head>
	<body>
		<form action="/back/member/memberGuaranteeAction!save" method="post">
			<input type='hidden' class='autoheight' value="auto" />
			<table cellspacing="8px"> 
							<tr>
					<td align="right">
						<span class="title">名称：</span>
					</td>
					<td>
						${memberGuarantee.memberBase.eName}
					</td>
				</tr>
				<tr>
					<td align="right">
						<span class="title">交易系统推荐指数：</span>
					</td>
					<td>
						<input type="hidden" name="id" value="${id}" />
						<input type="hidden" name="memberGuarantee.memberBase.id" value="${memberGuarantee.memberBase.id}" />
						<input type="hidden" name="memberGuarantee.type" value="${memberGuarantee.type}" />
						<input type="hidden" name="memberGuarantee.createMoney" value="${memberGuarantee.createMoney}" />
						<input type="hidden" name="memberGuarantee.createYear" value="${memberGuarantee.createYear}" />
						<input type="hidden" name="memberGuarantee.empNumber" value="${memberGuarantee.empNumber}" />
						<input type="hidden" name="memberGuarantee.jingyanNumber" value="${memberGuarantee.jingyanNumber}" />
						<input type="hidden" name="memberGuarantee.bank" value="${memberGuarantee.bank}" />
						<input type="hidden" name="memberGuarantee.dbedType" value="${memberGuarantee.dbedType}" />
						<input type="hidden" name="memberGuarantee.jzdType" value="${memberGuarantee.jzdType}" />
						<input type="hidden" name="memberGuarantee.daichanRate" value="${memberGuarantee.daichanRate}" />
						
						<input class="input_box" type="hidden" name="memberGuarantee.tjzs"
							value="${memberGuarantee.tjzs}" id="tjzs" />
						<div id="demo1"></div>
					</td>
				</tr> 
				<tr>
					<td align="right">
						<span>详细：</span>
					</td>
					<td>
						 <textarea id="note" name="memberGuarantee.note" >${memberGuarantee.note}</textarea> 
					</td>
				</tr>
				<tr align="center">
					<td colspan="2">
						<input class="ui-state-default" type="submit" value="保存" />
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
