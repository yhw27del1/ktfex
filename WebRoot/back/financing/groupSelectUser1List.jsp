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
 function result(data,s){  
	if(s=="success"){ 
	    if(data['code']=="1"){   
	        alert(data['tip']);    
	        $("#form1").attr('action','/back/userGroupAction!groupSelectUser1List');    
	        window.location.href ='/back/userGroupAction!groupSelectUser1List?id=${id}';      
		}else{  
			alert(data['tip']);    
		}
	}else{
		alert("加入成员异常");  	
	}  
}	  
 function result2(data,s){  
	if(s=="success"){ 
	    if(data['code']=="1"){    
	        alert(data['tip']);      
	        $("#form1").attr('action','/back/userGroupAction!groupSelectUser1List');    
	        window.location.href ='/back/userGroupAction!groupSelectUser1List?id=${id}';      
		}else{  
			alert(data['tip']);      
		}
	}else{
		alert("清除体验完成成员异常");  	
	}  
}	  
 function result3(data,s){  
	if(s=="success"){ 
	    if(data['code']=="1"){    
	        alert(data['tip']);      
	        $("#form1").attr('action','/back/userGroupAction!groupSelectUser1List');    
	        window.location.href ='/back/userGroupAction!groupSelectUser1List?id=${id}';      
		}else{  
			alert(data['tip']);      
		}
	}else{
		alert("删除所有成员异常");  	
	}  
}	  
$(document).ready(function(){ 
      $(".table_solid").tableStyleUI();
        
      $("#clearUserButton").click(function(){
        var options={
				dataType:"json",
				success:result2,   
				timeout:2000
		   }    
		   $("#form1").attr('action','/back/userGroupAction!clearInvestCount');                    
           $("#form1").ajaxForm(options);    
      
      }); 
      $("#delUsersButton").click(function(){
        var options={
				dataType:"json",
				success:result3,   
				timeout:2000
		   }    
		   $("#form1").attr('action','/back/userGroupAction!delGroupUsers');                    
           $("#form1").ajaxForm(options);    
      
      }); 
      
      
      $("#addUserButton").click(function(){ 
     	     if($("#username").val() == "") {   
	             alert("操作提示：会员交易帐号不能为空!");  
	             $("#username").focus();      
	             return false;      
               }     
     	     if($("#investMaxCount").val() == "") {   
	             alert("操作提示：最大允许次数不能为空!");  
	             $("#investMaxCount").focus();      
	             return false;      
               }     
     	     if($("#investMaxMoney").val() == "") {   
	             alert("操作提示：单笔允许最大金额不能为空!");     
	             $("#investMaxMoney").focus();      
	             return false;         
               }     

          	    var options={
				dataType:"json",
				success:result,
				timeout:2000
		   }    
		   $("#form1").attr('action','/back/userGroupAction!addUser1');            
           $("#form1").ajaxForm(options); 
              
            
	 }); 


      $("#search").click(function(){ 
    	  $("#form1").submit();
      }); 
 	 
});

function toURL(url){ 
   window.location.href = url; 
}  
</script>

</head> 
<body>
<form  action='/back/userGroupAction!groupSelectUser1List' method="get" id="form1">       
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/>
<input type="hidden" name="id" value="${id}" id="groupId"/>  
<div id="myToolBar" class="   ui-widget-header "  > 
    
	输入会员编号：&nbsp;<input type="text" name="username" id="username" style='width:120px;' value placeholder ="仅供新增"/>  
	最大允许次数：&nbsp;<input type="text" name="investCount" id="investCount" value="${investCount}" style='width:30px;'/>   
	单笔允许最大金额：&nbsp;<input type="text" name="investMaxMoney" id="investMaxMoney" value="${investMaxMoney}"  style='width:60px;'/>  
	<button class="ui-state-default"  id="addUserButton" style="display:<c:out value="${menuMap['userGroup_addUser']}" />" >加入</button> 
	 查询关键字：&nbsp;<input type="text" name="keyWord" id="keyWord" style='width:120px;'  value placeholder ="仅供查询"/>
	<button class="ui-state-default"  id="search">查询</button>   
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>会员</th> 
				<th>所属机构</th>
				<th>允许最大次数</th>
				<th>投标次数</th>
				<th>单笔允许最大金额</th> 
				<th><button class="ui-state-default"  id="clearUserButton">清除</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="ui-state-default"  id="delUsersButton">删除全部成员</button> </th>
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr>  
				<td>${entry.user.username}<br/>
				${entry.user.realname}</td>  	 
				<td>${entry.user.org.name}</td>  	 
				<td>${entry.investMaxCount}</td>  	 
				<td>${entry.investCount}</td>  	 
				<td>${entry.investMaxMoney}</td>  	 
				<td> 
				 <button class="ui-state-default" onclick="toURL('/back/userGroupAction!delUser1?groupUserId=${entry.id}&id=${entry.groupId}');return false;"  style="display:<c:out value="${menuMap['userGroup_delUser']}" />"/>删除成员</button>
 				</td>  
			</tr>
		</c:forEach>
		</tbody>
		<tbody>
			<tr>
				<td colspan="6">  
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body> 