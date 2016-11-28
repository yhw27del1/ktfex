<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="SHORTCUT ICON" href="/Static/images/logo_ico.ico"/>
    <title>昆投互联网金融服务平台</title>
    <link href="/Static/css/styles.css" type="text/css" media="screen" rel="stylesheet" />
    <link href="/Static/css/jquery-ui-1.8.16.custom.css" rel="stylesheet">    
    <script src="/Static/js/jquery-1.7.1.min.js"></script>
    <script src="/Static/js/jquery-ui-1.8.17.custom.min.js"></script>
        <script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script>  
    <script type="text/javascript" src="/Static/js/dialogHelper.js"></script>
    <script type="text/javascript" src="/Static/js/md5.js"></script>

	<script type="text/javascript">  
        $(document).ready(function() {        	
         
        	$( "#code" ).click(function() { 
				var timenow = new Date().getTime(); 
					$( "#imgcode" ).attr("src", "/randomMyCode.jsp?Height=35&CodeType=3&d=" + timenow);
		 	});    
				
		    $("#loginId" ).click(function() {
				  $("#form1").validate({
				     rules:{
				        username:"required",
				        password:"required",
				        userCode:"required"
				     },
				     messages: {
                        username: "请输入户名", 
                        password: "请输入密码",
                        userCode: "请输入验证码"
                    },
                    
                    submitHandler:function(form){
                        $.getJSON("/sysCommon/sysUserAction!validateCode?time="+new Date().getTime()+"&validCode="+$("#userCode").val(),function(data){			         
				     
				         if(0==data){ 
			                 messageTip("验证码不能为空!");
			                 $("#userCode").val('');
			                 $( "#code" ).click();
                             return false;   
			              }
			            if(1==data){ 
			               messageTip("验证码不正确!");
			               $("#userCode").val('');
			               $( "#code" ).click();
                           return false;
			            }
			            else{
			                 var userPassword=hex_md5($("#userPassword").val());
			                 $.getJSON("/sysCommon/sysUserAction!validateUser2?time="+new Date().getTime()+"&userName="+$("#username").val()+"&password="+userPassword,function(data){
				                 if(9==data){ 
			                        messageTip("用户名错误!");
			                        $("#userPassword").val('');	
			                        $("#userCode").val('');
			                        $( "#code" ).click();		                        
                                    return false;   
			                     }			                    
			                     if(8==data){ 
			                        messageTip("用户已被停用,请联系客服 !");
			                        $("#userPassword").val('');
			                        $("#userCode").val('');
			                        $( "#code" ).click();
                                    return false;
			                     }
			                     if(7==data){ 
			                        messageTip("长期未登录导致用户已休眠,请联系客服为您开通 !");
			                        $("#userPassword").val('');
			                        $("#userCode").val('');
			                        $( "#code" ).click();
                                    return false;
			                     }
			                     if(6==data){ 
			                     $("#userPassword").val(userPassword);
			                     //$("#form1").submit();
			                     form.submit(); 
			                     }
			                     if(5==data){ 
	                                    messageTip("投资方请下载客户端登录 !");
	                                    $("#userPassword").val('');
	                                    $("#userCode").val('');
	                                    $( "#code" ).click();
	                                    return false;
	                                 }
			                     if(0==data){ 
			                        messageTip("密码重复输入错误,账户已被锁定,请联系客服解锁 ");
			                        $("#userPassword").val('');
			                        $("#userCode").val('');
			                        $( "#code" ).click();
                                    return false;
			                     }
			                     if(1==data||2==data||3==data||4==data){ 
			                        messageTip("密码错误,还有"+data+"次登录机会");
			                        $("#userPassword").val('');
			                        $("#userCode").val('');
			                        $( "#code" ).click();
                                    return false;
			                     }
			                 });
			             }
			          });   
                      //form.submit();
                    }
				 });       
			                    			       
			   });
			});
		   
		   
		   
		   function isTop(){  
		       if(parent.window != window){ 
		       		var form = document.forms[0];  
			          form.action="/sys_/userAction!loginPage";
			          form.target="_top";
			          form.submit();
		         }
	        }
	         


        function messageTip(message){ 
	        var dlgHelper = new dialogHelper();
	     /*   dlgHelper.set_Title("系统提示");*/
	        dlgHelper.set_Msg(message);
	        dlgHelper.set_Height("150");
	        dlgHelper.set_Width("400");
	        dlgHelper.set_Buttons({
	            '关闭': function(ev) { 
	                    $(this).dialog('close'); 
	            } 
	        });      
	        dlgHelper.open();  
      }

      
      function disableEnter(event){
		 var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		 if (keyCode == 13){
		  return false;
		 }
		 }
</script>
	
   <!--   <style type="text/css">
	.STYLE1 
	{
		font-size: 23px;
		font-family:"黑体";
		color:#4F81BD;
		font-weight:bold;
		padding:5px 20px 5px 20px;
	}
    </style>-->
</head>

<body id="login" onload="isTop();">
<div id="page">
    <div id="header" class="siteHeader">
      <div class="innerBox">
       <div class="header cf">
         <h1 class="siteTitle">
            <a href="/">
               <img class="" alt="" src="//assets.firstp2p.com/skins/chedai/images/logo-big_wx.png"/>
            </a>
         </h1>
       </div>
      </div>
   </div>
   <div id="main" class="main innerBox">
      <div class="content">
         <div class="loginForm accessForm cf">
            <form id="form1" name="form1" class="iconLabelForm" action="j_spring_security_check" method="post" >
                            <h1>系统登录</h1>
                                                     
                            <div class="field">
                                <label for="username" class="fi fi-user"></label>
                                <input id="username" name="username" placeholder="用户名"  maxlength="20" data-rules="minlength:1" tabindex="1" type="text">
                                
                            </div>
                            <div class="field">
                                <label for="password" class="fi fi-key"></label>
                                <input id="userPassword" name="password" placeholder="密码"  data-rules="rangelength:6--20"  tabindex="2" type="password">
                                
                            </div>
                            <div class="field verifyField">
                                <label for="userCode" class="fi fi-puzzle-piece"></label>
                                <input class="required" aria-required="true" name="userCode" id="userCode" placeholder="验证码"  maxlength="5" data-tules="digits:true" data-messages="digits:验证码好像错了" tabindex="3" type="text">
                                <span class="code fieldNote clingNote">
                                <a id="code" title="点击刷新" href="#">
                                   <img  id="imgcode" src="/randomMyCode.jsp?Height=35&d=<%=Math.random() * 100000%>&CodeType=3" alt=""></img>
                                <i>点击<br>刷新</i>
                                </a>
                                </span>
                            </div>
                            <!-- <div class="fieldNote" style="margin-top:-10px;margin-bottom: 6px;font-size:12px;">
                                忘记密码？<a href="/index.php?user&amp;q=going/getpwd">点击找回</a>
                            </div> -->
                            <div class="btnLine">
                                <!--<button class="btn" id="loginId">登录</button>-->
                                <input class="btn" id="loginId" type="submit" value="登录"/>
                                <!--  <p class="fieldNote">还不是会员？ <a href="/index.php?user&amp;q=going/getreg">点击免费注册</a></p>-->
                                <p><font color="red">${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message} </font>
                	             <s:actionerror cssStyle="font-size:13px;color:#B82525"/>
                	             </p>
                            </div>
                </form>
         </div>
      </div>
   </div>
   <footer id="footer"></footer>
</div>

    <%  
	   Enumeration e=session.getAttributeNames();  
	   while(e.hasMoreElements()){
	       String sessionName=(String)e.nextElement(); 
		   if(!"_TXPT_AUTHKEY".equals(sessionName))
		   {
		     session.removeAttribute(sessionName); 
		   }
		    
	   }
	 %>   
</body>
