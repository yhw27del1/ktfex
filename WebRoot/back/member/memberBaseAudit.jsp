<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<title>会员审核页面</title>
		<script type="text/javascript"
			src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript"
			src="/Static/js/tree/dhtmlXTree/dhtmlxcommon.js"></script>
		<script type="text/javascript"
			src="/Static/js/tree/dhtmlXTree/dhtmlxtree.js"></script>
		<script type="text/javascript"
			src="/Static/js/validate/jquery.validate.js"></script>
		<link rel="stylesheet" type="text/css"
			href="/Static/js/tree/dhtmlXTree/dhtmlxtree.css">
		<link rel="stylesheet"
			href="/Static/js/validate/validateself-skin1.css"
			type="text/css" />
		<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		<script type="text/javascript">
		function openDocument(url) {
		window.open(url,'',"height='100',width='400',top='0',left='0',toolbar='no',menubar='no',scrollbars='yes', resizable='yes',location='no', status='no'");
	    }
	    $(function() {
	    changeState();
		$("#memberauditform").validate( {
			rules : {
				"reason" : {
					required : true
				},
				"state" : {
					required : true
				}
			},
			messages : {
				"reason" : {
					required : "请填写不通过的原因"
				},
				"state" : {
					required : "请选择是否通过审核"
				}
			},
           submitHandler: function(form) {   
              //可以加入判断/  
                  var state=$("input[name='state']:checked").val();
	              if(state=="2"){
                    if(checkForm1()){
                        form.submit();
                    }            
                  }else{
                        form.submit();
                  }
            }  
		});
		$("#reason").blur(function(){
          var area=$(this);
          var max=200; //获取maxlength的值
          if(max>0){
             if(area.val().length>max){ //textarea的文本长度大于maxlength
             area.val(area.val().substr(0,max)); //截断textarea的文本重新赋值
           }
         }
       }); 
	});
	
	/**
	*控制审核不通过的原因的显示和隐藏：只有不通过审核才需要填写原因
	*/
	function changeState(){
	var state=$("input[name='state']:checked").val();
	if(state=="2"){
	    //通过验证
	    checkForm1(); 
	    $(".reason").hide();
	}else if(state=="3"){
	//不通过
	    $(".reason").show();
	  }	  
	}
function checkForm1(){
<s:if test="0==memberBase.category">//机构
		 //组织机构代码扫描图
		  <s:if test="null==memberBase.orgCertificate">  
		   if($.trim($("#orgCertificate").val())=="")
		   	{ 
		      alert('组织机构代码证扫描图必须上传！');
		      $("#orgCertificate").focus();    
		      return false;  		    
		   	}

		  </s:if>
		  //营业执照扫描图
		  <s:if test="null==memberBase.busLicense">   
		   if($.trim($("#busLicense").val())=="")
		   	{ 
		      alert('营业执照扫描图必须上传！');
		      $("#busLicense").focus();   
		      return false; 		    
		   	}
 		      
		  </s:if>
		  //法人身份证正面扫描图
		  <s:if test="null==memberBase.idCardFrontImg">     
		   if($.trim($("#idCardFrontImg").val())=="")
		   	{ 
		      alert('法人身份证正面扫描图必须上传！');
		      $("#idCardFrontImg").focus();   
		      return false;  			    
		   	}		  
	      
		  </s:if>
		  //法人身份证反面扫描图
		  <s:if test="null==memberBase.idCardBackImg">  
		   if($.trim($("#idCardBackImg").val())=="")
		   	{ 
		      alert('法人身份证反面扫描图必须上传！');
		      $("#idCardBackImg").focus(); 
		      return false;  			    
		   	}		     
	      
		  </s:if>

	</s:if>
	<s:elseif test="1==memberBase.category">//个人
		  //身份证正面扫描图  
		  <s:if test="null==memberBase.idCardFrontImg">     
		   if($.trim($("#idCardFrontImg").val())=="")  
		   	{ 
		      alert('身份证正面扫描图必须上传！');
		      $("#idCardFrontImg").focus(); 
		      return false;  			    
		   	}		  
	      
		  </s:if>
		  //身份证反面扫描图
		  <s:if test="null==memberBase.idCardBackImg">    
		   if($.trim($("#idCardBackImg").val())=="")
		   	{ 
		      alert('身份证反面扫描图必须上传！');
		      $("#idCardBackImg").focus(); 
		      return false;  			    
		   	}		    
	      
		  </s:if>
	
	</s:elseif>
	
  //银行卡正面扫描图
  /*<s:if test="null==memberBase.bankCardFrontImg">    
  		   if($.trim($("#bankCardFrontImg").val())=="")
		   	{ 
		      alert('银行卡正面扫描图必须上传！');  
		      $("#bankCardFrontImg").focus(); 
		      return false; 		    
		   	}    
       
  </s:if>*/
  //会员申请表第一页
  /*<s:if test="null==memberBase.accountApplicationImg">     
		   if($.trim($("#accountApplicationImg").val())=="")
		   	{ 
		      alert('会员申请表第一页必须上传！');
		      $("#accountApplicationImg").focus(); 
		      return false;     		    
		   	}  
   
  </s:if>*/
	
   return true;     
}	
	
	
</script>
	</head>

	<body>
		<input type='hidden' class='autoheight' value="auto" />
		 <form action="/back/member/memberAuditAction!audit" method="post" id="memberauditform" enctype="multipart/form-data">
            <%@include file="memberInforUp.jsp" %>

			<input type="hidden" name="isModify" value="true" />
			<input type="hidden" name="membertype" value="${membertype}" />
			<table cellspacing="5px;">
				<tr>
					<td align="left">
						<span style="color: red;">*</span><span class="title">是否通过审核：</span>
					</td>
				</tr>
				<tr>
					<td>						
						<input checked="checked" type="radio" name="state" value="3" onclick="changeState()">
						不通过&nbsp;&nbsp;
						<input type="radio" name="state" value="2" onclick="changeState()">
						通过
					</td>
				</tr>
				<tr class="reason">
					<td align="left">
						<span style="color: red;">*</span><span class="title">不通过的原因：</span>
					</td>
				</tr>
				<tr class="reason">
					<td>
						<textarea style="width: 472px;border-width: 1px; border-color: #BDC7D8; border-style: solid; background: #FFFFFF;" name="reason" id="reason" rows="10" cols="40"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="hidden" name="memberId" value="${memberId}" />
						<input class="ui-state-default" type="submit" value="提交" />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<button class="ui-state-default"
							onclick="javascript: history.go(-1);return false;">
							返回
						</button>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
