<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags' %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/Static/css/newtemplate/common.css" type="text/css" />
<title>左侧导航栏</title>
<jsp:include page="/common/import.jsp"></jsp:include>
</head>

<style type="text/css">
</style>

<script  type="text/javascript"> 
function changemenu(tar){
	$(".right_main_nav").css("height","auto");
	$(".right_main_nav ul").each(function(){
		if($(this).attr("tar")==tar){
			var clicked = $(this).show().children("li[clicked]");
			if(clicked.length>0){
				clicked.children("a:first").trigger("click");
			}else{
				$(this).children("li:first").children("a:first").trigger("click");
			}
			
		}else{
			$(this).hide();
		}
		
	});
	resizeheight();
	
}
$(function(){

	$(".list_detail li a").css({'cursor':'pointer'}).click(function(){
		
//		$(this).css({'font-size':'15px','color':'#FF751A','height':'35px','line-height':'35px','vertical-align':'middle'})//a css
//		  .parent().attr("clicked","true")// li attr
//		  .css({'border-left':'4px solid #FF751A','background-color':'#EBEBEB','padding':'0 0 0 14px','font-weight':'bold' })// li css
//		  .siblings().css({'background-color':'#fff','border-left':'none','font-weight':'normal'}).
//		  removeAttr("clicked").children("a")
//		  .css({'font-size':'13px','color':'#333','height':'27px','line-height':'27px','vertical-align':'middle'});

	    //新写法
	    $(this).attr("class","list_detail_click_li_a")
	    .parent().attr("class","list_detail_click_li")
	    .siblings().removeAttr("class","list_detail_click_li")
	    .children("a").removeAttr("class","list_detail_click_li_a");
	    
		window.top.frames['manFrame'].location=$(this).attr("href");
		return false;
	});
	
	
	resizeheight();
	
});

function resizeheight(){
	if($(".right_main_nav").height()<$(window.top.frames['leftFrame']).height()-10){
		$(".right_main_nav").height($(window.top.frames['leftFrame']).height()-2);
	}else{
		$(".right_main_nav").css("height","auto");
	}
}
</script>
	<body>
		<div id="left_content" >
		     
			 <div class="main_nav">
				 <div class="right_main_nav">
				    
				 	<s:iterator value="leftMenuMap" id="rMap" status="sta">
				 		<s:if test="#sta.index==0"><ul class="list_detail" tar="${rMap.key}"></s:if>
					 	<s:else><ul class="list_detail" style="display:none" tar="${rMap.key }"></s:else>
					 		<s:iterator value="#rMap.value" id="menu">
							     <li><a href="${menu.href}">${menu.name}</a></li> 
							</s:iterator>  
						</ul>
					</s:iterator>
				 </div>
				   
             </div>		 		 
		</div>
		
	</body>
</html>
