<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib prefix="s" uri="/struts-tags"%>
 <%@ include file="/common/taglib.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/Static/css/newtemplate/common.css" type="text/css" />
<jsp:include page="/common/import.jsp"></jsp:include>
<title>管理系统</title>

<script>
	$(function(){
		$("#changepassword").css({'cursor':'pointer'}).click(function(){
			//alert($(this).attr("href"));
			window.top.frames['manFrame'].location=$(this).attr("href");
			return false;
		});
		$("#tuichu").click(function(){			
			window.location.href='/sys_/userAction!loginPage?time='+new Date().getTime();
			return false;
			
		});
		
		$("#nav_top_b ul li").click(function(){
            $(this).attr("class","nav-item dl-selected").siblings().attr("class","nav-item");
            window.top.frames['leftFrame'].changemenu($(this).attr("tar"));
        }).each(function(){

				$("#1m3m24").addClass("nav-order");
				$("#1m3m25").addClass("nav-storage");
				$("#1m3m31").addClass("nav-user");
				$("#1m3m61").addClass("nav-cost");
				$("#1m3m410").addClass("nav-distribution");
				$("#1m3m520").addClass("nav-home");
				$("#1m3m4000").addClass("nav-marketing");
				$("#1m3m4").addClass("nav-monitor");
				$("#1m3m21").addClass("nav-register");
				$("#1m3m82").addClass("nav-supplier");
		});
		<%
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
	    %>

	    var days = <%=sdf2.format(now)%>;
	    var date = new Date("<%=sdf.format(now)%>");
	    var serverState = "未知";
	    var hours = null;
	    var minutes = null;
	    var seconds = null;
	    function show_server_time(){
	        date.setSeconds(date.getSeconds()+1);
	        hours = date.getHours()>9?date.getHours():"0"+date.getHours();
	        minutes = date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes(); 
	        seconds = date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds(); 
	        document.getElementById("show_server_time").innerHTML = hours+":"+minutes+":"+seconds;
	    }
	    setInterval(show_server_time,1000);

	    function reset_time(){
            $.post("/back/announcementAction!getSysTime",function(data,state){
                date = new Date(data['time']);
            },'json');
        }
        setInterval(reset_time,60000);
		
		$("#show_server_time").mouseover(function(){
			var this_ = $(this);
			$.post("/back/announcementAction!getSysTime",function(data,state){
				var date = new Date(data['time']);
				var hours = date.getHours()>9?date.getHours():"0"+date.getHours();
				var minutes = date.getMinutes()>9?date.getMinutes():"0"+date.getMinutes(); 
				var seconds = date.getSeconds()>9?date.getSeconds():"0"+date.getSeconds();
				var time_ = hours+":"+minutes+":"+seconds; 
				this_.attr("title",time_+"\t"+data['state']);
			},'json');
		});
		
		//获取服务器上的状态信息
		/*function getNewState(){
			$.post("/back/accountDealAction!checkState",function(data,state){
				serverState = data['state'];
				document.getElementById("show_server_state").innerHTML = serverState;
			},'json');
		}
		setInterval(getNewState,20000);*/
		
		
	   //获取服务器上的个人通知 
	    setInterval(getNewSysNotice,10*60*1000);        
		function getNewSysNotice(){  
			$.post("/sys_/notice/noticeAc!isExist?t="+new Date().getTime(),function(data,state){        
		         if(data['flag']=="1"){    
		         	$("#newSysNotice").attr("href","/sys_/notice/noticeAc!detail?id="+data['id']);    
	   	            $("#newSysNotice").attr("target","_blank");       
		            $("#newSysNotice").css({'display':''});         
		                              
				}else{  
					$("#newSysNotice").css({'display':'none'});   
				}
			},'json');     
		}   
	   	$('#newSysNotice').click(function(){ 
		   $("#newSysNotice").css({'display':'none'});           
	    });	
    
    
     
		 
		      
	})
window.onbeforeunload = function() {return '';};
</script>

</head>

<body>
		
<div class="header_content"  id="">
     <div class="logo" style="width:206px;height: 40px" >
     		<img src="/Static/images/newtemplate/LOGO-01.png" width="206" height="40" style="float:left;"/>
     	
     </div> 
     
    <div class="header_content_center">
        <div class="dl-header-nav" id="nav_top_b">
        <ul class="nav-list ks-clear">
            <s:iterator value="rootmenulist" status="sta">
            <s:if test="#sta.index==0">                         
               <li class="nav-item dl-selected" tar="${coding}" >
                  <div id="${coding}" class="nav-item-inner">${name}</div>
               </li>
             </s:if>
            <s:else>              
                <li class="nav-item" tar="${coding}">
                   <div id="${coding}" class="nav-item-inner">${name}</div>
               </li>    
            </s:else>          
        </s:iterator>
        </ul>
        </div>
    </div>
     <div class="right_nav" style="float: right;position: relative;height: 40px;line-height: 40px;width: 600px;">
            <div class="text_right" style="margin-right:20px;padding:0;height: 20px;line-height: 20px;padding: 10px 0 0 0">
		        <ul style="list-style:none; margin:0;">

		             <li style="float: left;text-align:center;height: 20px;line-height: 20px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width="20px" height="20px" alt=" " src="/Static/images/newtemplate/user_info.png"/> </li>
		             <li class="" style="float: left;text-align:center;height: 20px;line-height: 20px;">
		                欢迎你, <a class="white" href="#">${user.realname}</a>  
		           &nbsp;<a title='亲,您有新通知!' id="newSysNotice" style="display:none"><img src="/sys_/notice/notice1.gif" id="refreshImage"/></a>&nbsp;&nbsp;</li>
		             <li style="float: left;text-align:center;height: 20px;line-height: 20px;"><img src="/Static/images/newtemplate/block.png" width="2px" height="20px" />
		             &nbsp;&nbsp;<img src="/Static/images/newtemplate/clock.png" width="20px" height="20px" /></li>
		            <li style="float: left;text-align:center;height: 20px;line-height: 20px;"><a href="#">
		                <%=sdf2.format(new Date())%> <span id="show_server_time"></span>&nbsp;</a>&nbsp;</li>
		            <li style="float: left;text-align:center;height: 20px;line-height: 20px;"><img src="/Static/images/newtemplate/block.png" width="2px" height="20px" />
		            &nbsp;<img src="/Static/images/newtemplate/return.png" width="20px" height="20px" alt="点击此处退出系统"/></li>
		            <li style="float: left;text-align:center;height: 20px;line-height: 20px;"><a href="javascript:void(0)" class="hit" id="tuichu">退出</a></li>
		        </ul>
         </div>
      </div>
</div>
</body>
</html>
