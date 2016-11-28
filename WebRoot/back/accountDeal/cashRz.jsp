<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/open.js"></script>
<script type="text/javascript">
	function toURL(url){
		   window.location.href = url;
	}
	$(function() { 
		$("#charge").click(function() {
			var chargeAmount = $("#chargeAmount").val();
			if($("#userName").val()== "") {     
               alert("输入融资方用户名！");
               //$("#userName").focus();      
               return false;
	        }
			if (chargeAmount == "") {
				alert("请输入提现金额，再进行申请操作");
				//$("#chargeAmount").focus();   
				return false;
			} else {
				if($("#note").val()== "") {     
	               alert("备注必须填写！");
	              // $("#note").focus();    
	               return false;
		        }
	        
				if(chargeAmount>parseFloat($("#showBalance").html())){   
					alert("提现账户余额不足，请检查提现的交易账号可用余额，再进行申请操作。");
					//$("#chargeAmount").focus();      
					return false;   
				}else{ 
			    	$("#charge").css({'display':'none'});       
					$.getJSON("/back/accountDeal/accountDealAction!toCashRz?time="+ new Date().getTime()+ "&chargeAmount="+ chargeAmount+ "&userName="+$("#userName").val()+ "&note="+$("#note").val(),function(data){
				        if(data['code']=="1"){ 
				            $("#userName").val("");  
				            $("#note").val("");  
				            $("#chargeAmount").val("");    
				            $("#showName").html("");  
				            $("#showBalance").html("");         
			                alert(data['tip']);  
						}else{    
							alert(data['tip']);  
						} 
					});
				}
			}
            $("#charge").css({'display':''});  
		});
		
	  $("#userName").blur(function(){         
	  		 if($.trim($(this).val()).length>9) {       
                getCashRzR($(this).val());                  
	         }   
	    });
	});
	
function getCashRzR(userName){
   $.getJSON("/back/accountDeal/accountDealAction!getCashRzR?time="+ new Date().getTime()+ "&userName="+userName,function(data){ 
		    if(data['code']=="1"){       
                $("#showName").html(data['realname']);  
                $("#showBalance").html(data['balance']);       
              
			}else{  
			    $("#showName").html("");  
                $("#showBalance").html(""); 
				alert(data['tip']);    
			} 
		  return false;
	});
}
	
	
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<div class="dataList ui-widget" style="border:0;">
	 <span style='color:red;'>请先输入融资方用户名,光标离开融资方用户名输入框后,动态显示出此融资方信息，核对信息再提交申请！</span>
	 <span style='color:red;'><br/><br/></span>
	</div>
	<table>
		<tbody> 
			<tr><td align="right">融资方用户名：</td><td ><input type="text" name="userName" size="20" id="userName"/></td></tr>
			 
			<tr><td  align="right">融资方名称：</td><td  id="showName" style='color:red;'></td></tr>
			 
			<tr><td  align="right">融资方可用余额：</td><td  id="showBalance" style='color:red;'></td></tr>
			 
			<tr><td  align="right">提现金额：</td><td ><input type="text" name="chargeAmount" size="20" onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" id="chargeAmount" /></td></tr>
			
			<tr><td  align="right">备注：</td><td ><input type="text" name="note" size="20" value="保证金" id="note" /></td></tr>
			 
			<tr><td  colspan="2"  align="center"><br/></td></tr>
			<tr><td  colspan="2"  align="center"><button id="charge" class="ui-state-default mybutton">提交申请</button></td></tr>
		</tbody>
	</table>
 
</body>