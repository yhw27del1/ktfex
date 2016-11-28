<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>   
<script type="text/javascript">   
 /**
*验证图片类型(jpg,jpeg,png,gif)
*/
function isValidImageType(fileName){
    var temp=fileName.split(".");
    var length=temp.length; 
    var ret=false;
    var typeName=temp[length-1]; 
    typeName=typeName.toLowerCase(); 
    if(""==fileName){
        ret=true;
    }else if("jpg"==typeName  || "png"==typeName || "gif"==typeName || "jpeg"==typeName){
       ret=true;     
    }
    return ret;
}   
$(document).ready(function(){ 
      $(".table_solid").tableStyleUI();
         
      
      $("#addImage").click(function(){ 
     	     if($("#image").val() == "") {   
	             alert("操作提示：请选择上传的图片!");  
	             $("#image").focus();      
	             return false;      
               } 
                 
     	     if(!isValidImageType($("#image").val())) {   
	             alert("操作提示：图片类型不对,图片类型必须为jpg,jpeg,png,gif!");   
	             $("#image").focus();         
	             return false;         
               }       
           $("#addImage").css({'display':'none'});      
		   $("#form1").attr('action','/back/financingBaseAction!saveDbhtFile');               
           $("#form1").submit(); 
              
            
	 }); 
});

function toURL(url){ 
   window.location.href = url; 
}  
</script>

</head> 
<body>
<form  action='/back/financingBaseAction!dbhtFilesList' method="post" id="form1"  enctype="multipart/form-data">          
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<input type="hidden" name="id" value="${id}"/>  
<div id="myToolBar"  > 
 
	选择图片：&nbsp;<input type="file" name="image" id="image" />    
	&nbsp;&nbsp;&nbsp;<button class="ui-state-default"  id="addImage">上传担保合同</button> 
	  
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">  
				<th>名称</th>
				<th>上传日期</th>
				<th>缩略图</th>				
				<th></th>
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr>  	 
				<td>${entry.fileName}</td>  	 
				<td>${entry.uploadtime}</td>  
				<td><img src="/Static/userfiles/9527/${entry.id}" width="50" height="30" /></td>  	    	 
				<td> 
				 <button class="ui-state-default" onclick="toURL('/back/financingBaseAction!delDbhtFile?dbhtFileId=${entry.id}&id=${id}');return false;"  />删除</button>
 				</td>  
			</tr>
		</c:forEach>
		</tbody>
		<tbody>
			<tr>
				<td colspan="4">  
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 