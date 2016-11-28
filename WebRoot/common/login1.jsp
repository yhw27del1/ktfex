<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="SHORTCUT ICON" href="/Static/images/logo_ico.ico"/>
	<!-- <link rel="bookmark" href="/Static/images/logo_ico.png" type="image/x-icon" /> -->
    <title>昆明商企业金融交易服务登录</title>
    <link href="/Static/css/styles.css" type="text/css" media="screen" rel="stylesheet" />
    <link href="/Static/css/jquery-ui-1.8.16.custom.css" rel="stylesheet">    
    <script src="/Static/js/jquery-1.7.1.min.js"></script>
    <script src="/Static/js/jquery-ui-1.8.17.custom.min.js"></script>
    <script type="text/javascript" src="/Static/js/jquery.keyboard.extension-typing.js"></script>
    <link href="/Static/css/keyboard.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="/Static/js/jquery.keyboard.js"></script>
    <script type="text/javascript" src="/Static/js/dialogHelper.js"></script>
    <script type="text/javascript" src="/Static/js/md5.js"></script>
	    <script type="text/javascript">  
        $(document).ready(function() {   
        	
         
        	$( "#code" ).click(function() { 
				var timenow = new Date().getTime(); 
					$( this ).attr("src", "/randomMyCode.jsp?Height=14&CodeType=3&d=" + timenow);
		 	});    
			
            $("#userName").blur(function() {
                if ($(this).val() == "") {
                    $(this).css("border-color", "red");
                 }
                else
                    $(this).css("border-color", "#D9D6C4");
            });
			
			
            
			 $("#userPassword").blur(function() {
                if ($(this).val() == "") {
                    $(this).css({"border-color":"red"});
                 }
                else
                {
                    $(this).css("border-color", "#D9D6C4");
                } 
            });
			$("#userCode").blur(function() {
                if ($(this).val() == "") {
                    $(this).css({"border-color":"red"});
                 }
                else
                {
                    $(this).css("border-color", "#D9D6C4");
                } 
            });		
			   $("#loginId" ).click(function() {
				  
			     if ($("#userName").val() == "") { 
                    messageTip("用户名不能为空!");
                    $("#userCode").val('');
                    $( "#code" ).click();
                    return false;
                 }
				 if ($("#userPassword").val() == "") { 
                    messageTip("密码不能为空!");
                    $("#userCode").val('');
                    $( "#code" ).click();
                    return false;
                 }
                 if ($("#userCode").val() == "") { 
                    messageTip("验证码不能为空!");
                    $( "#code" ).click();
                    return false;
                 }	
                 
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
			                 $.getJSON("/sysCommon/sysUserAction!validateUser2?time="+new Date().getTime()+"&userName="+$("#userName").val()+"&password="+userPassword,function(data){
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
			                      $("#form1").submit(); 
			                     }
			                     if(5==data){ 
	                                    messageTip("投资人不允许从此入口登录 !");
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
				   return false;
		
			   });
			   $("#loginIdReset" ).click(function() {
				   $("input[name]:first").focus();
			        $("input[name]").each(function(){
						$(this).val("");
				    });
				    return false;
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
	         

           /* $("#loginbtn").click(function() {
                var k = 0;
                var ajaxhtml = "";
                $(".logininput").each(function(i, obj) {
                    if ($(obj).val().trim() == "") {
                        k++;
                        $(this).css("border-color", "red");
                        $(this).focus();
                        return false;
                    }
                });
                if (k != 0) return;
                ajaxhtml = $("#loginbtn").html();
                $("#loginbtn").html("Loading....  <img src='loading.gif' alt='Announcement' /> ");
                $("#loginbtn").attr("disabled", "disabled");

            })
        });*/
        function messageTip(message){ 
	        var dlgHelper = new dialogHelper();
	        dlgHelper.set_Title("系统提示");
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
     /*    function vilidateCodew(){ 
	         
                  $.getJSON("/sysCommon/sysUserAction!validateCode?time="+new Date().getTime()+"&validCode="+$("#userCode").val(),function(data){
				         if(0==data){ 
			                 messageTip("验证码不能为空!");
			                 $("#userCode").val('');
                             return false;   
			              }
			            if(1==data){ 
			               messageTip("验证码不正确!")
			               $("#userCode").val('');
                           return false;
			            } 
			       }); 
				   return false; 
      }  */ 
      
      function disableEnter(event){
		 var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		 if (keyCode == 13){
		  return false;
		 }
} 
</script>
	
    <style type="text/css">
	.STYLE1 
	{
		font-size: 23px;
		font-family:"黑体";
		color:#4F81BD;
		font-weight:bold;
		padding:5px 20px 5px 20px;
	}
    </style>
</head>
<body id="login" onload="isTop();">
 <!--<script type="text/javascript">  
    $.post("/mobile/mobileAction!isFm",null,function(d,status){ 
	    if(d.m=="2"){    
	       window.location.href ='/m/login.jsp';
		}
	});
 </script>-->
    <div id="wrappertop"></div>
    <div id="wrapper">
        <div id="content">
          <div id="header">
			<img src="/Static/images/logo1.png">
		  </div>
            <div id="darkbanner" class="banner320">
                 <h2>系统登录-授权服务机构登录</h2>
                 
            </div>
            <div id="darkbannerwrap"></div>
            <form id="form1" name="form1" action="j_spring_security_check" method="post" >
            <fieldset class="form">
                <p>
                    <label class="loginlabel" for="user_name">
                           &nbsp;&nbsp;&nbsp;&nbsp;用户名:</label>
                    <input class="logininput ui-keyboard-input ui-widget-content ui-corner-all" name="username" id="userName" type="text" value="" style="width:230px;" />
                </p>
                <p>
                    <label class="loginlabel" for="userPassword">
                       &nbsp;&nbsp;&nbsp; 密&nbsp;&nbsp;&nbsp;&nbsp;码:</label>
                    <span>
                        <input id="userPassword" name="password" type="password" class="logininput ui-widget-content ui-corner-all" style="width:230px;"/>
                        <!-- 
                        <img id="passwd" class="tooltip" alt="单击打开虚拟键盘" title="单击打开虚拟键盘" src="/Static/images/keyboard.png" />
						 -->
					</span>
					<br/> 
				</p>
			 <p>
                   <label class="loginlabel" for="userPassword"> &nbsp;&nbsp;&nbsp;&nbsp;验证码:</label>
                    <span style="float:left;"> 
                          <input id="userCode" name="userCode" type="text" class="logininputpwd ui-widget-content ui-corner-all" />  <!--  onblur="vilidateCodew();"-->
					</span>
					<span>   
                          &nbsp;&nbsp;<img id="code" border="0" src="/randomMyCode.jsp?Height=14&d=<%=Math.random() * 100000%>&CodeType=3" alt="点击刷新" title="点击刷新" height="30" width="80px;"/>
					</span>
					<br/> 
				</p>
				<div style="text-align:center">
                	<button id="loginId" class="positive" >登录</button>
                	<!--  <button id='loginIdReset' class="positive">重置</button>-->
                	  <!-- 取登录的错误信息 -->
                	 <br/>
                	 <br/> 
                 	 <br/>  
					<font color="red">${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message} </font>
                	<s:actionerror cssStyle="font-size:13px;color:#B82525"/>
                </div>
            </fieldset>
		</form>
        </div>
    </div>
    <div id="wrapperbottom_branding">
        <div id="wrapperbottom_branding_text">
           Copyright ©. 金融交易服务有限公司  All rights reserved. 
		</div>
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
