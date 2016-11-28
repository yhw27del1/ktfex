<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head> 
		<title>融资项目发布修改</title> 

	    <script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
	    <script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
	    <link rel="stylesheet" href="/Static/css/page/page-skin1.css" type="text/css"/> 
	    <link rel="stylesheet" href="/Static/css/simpleTabs.css" type="text/css"/>
	    <script type="text/javascript" src="/Static/js/simpleTabs.js"></script>   
	    <link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
	    <script type="text/javascript" src="/Static/js/jquery.uploadify-v2.1.0/swfobject.js"></script>
        <script type="text/javascript" src="/Static/js/jquery.uploadify-v2.1.0/jquery.uploadify.v2.1.0.min.js"></script>
        <script type="text/javascript" src="/Static/js/open.js"></script>
        <script type="text/javascript" src="/Static/js/kindeditor/kindeditor-min.js"></script>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		<script type="text/javascript" src="/Static/js/star/rater/js/jquery.raty.min.js"></script> 
		<script type="text/javascript"> 
		var ratyPath='/Static/js/star/rater/img';  
	 	$(function() {   
	       //加星级   
		  $("#demo1").raty({half:true,hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,number:5,start:${financingBase.qyzs},click:function(score, evt) {
                   $("#qyzs").val(score);
             }}); 
		  $("#demo2").raty({half:true,hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,number:5,start:${financingBase.fddbzs},click:function(score, evt) {
                   $("#fddbzs").val(score);
             }}); 
		  $("#demo3").raty({half:true,hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,number:5,start:${financingBase.czzs},click:function(score, evt) {
                   $("#czzs").val(score);
             } }); 
		  $("#demo4").raty({half:true,hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,number:5,start:${financingBase.dbzs},click:function(score, evt) {
                   $("#dbzs").val(score);
             }}); 
		  $("#demo5").raty({half:true,hintList:[arrRater[4],arrRater[3],arrRater[2],arrRater[1],arrRater[0]],path:ratyPath,number:5,start:${financingBase.zhzs},click:function(score, evt) {
                   $("#zhzs").val(score);
             }}); 
		  $("#demo1").css({'width':'130px'}); 
		  $("#demo2").css({'width':'130px'});  
		  $("#demo3").css({'width':'130px'});  
		  $("#demo4").css({'width':'130px'});  
		  $("#demo5").css({'width':'130px'});     
		});


		
		$(document).ready(function(){	
			   jQuery(function(){ 
			        jQuery.validator.methods.compareDate = function(value, element) {   
			            var startDate = $('#startDate').val(); 
			            return new Date(Date.parse(startDate.replace(/-/g, "/"))) <= new Date(Date.parse(value.replace(/-/g, "/"))); 
			        }; 
			        
			        jQuery.validator.addMethod("isDate", function(value, element){
			        	var ereg = /^(\d{1,4})(-|\/)(\d{1,2})(-|\/)(\d{1,2})$/;
			        	var r = value.match(ereg);
			        	if (r == null) {
			        		return false;
			        	}
			        	var d = new Date(r[1], r[3] - 1, r[5]);
			        	var result = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[5]);
			        	return this.optional(element) || (result);
			        }, "请输入正确的日期!"); 
			        
			        jQuery.validator.methods.daNowDate = function(value, element) {   
			            	var   curDate=new Date();  
			            	curDate=curDate.format("yyyy-MM-dd"); 
			            	return new Date(Date.parse(curDate.replace(/-/g, "/"))) <= new Date(Date.parse(value.replace(/-/g, "/")));   
			            };  　　
			   });   
			 $("#setEditForm").validate({
	                rules: {  
	                  "financingBase.shortName":{ required:true},
	                  "financingBase.maxAmount":{ required:true,min:10000},
	                  "financingBase.rate":{ required:true}, 
	                  "financingBase.financier.eName":{ required:true}, 
	                  "financingBase.guarantee.eName":{ required:true},
	                  "financingBase.startDate":{ required:true,isDate:true},
	                  "financingBase.endDate":{ required:true,isDate:true,compareDate: "#startDate"},
	                  "inancingBase.businessType.id":{ required:true} 
	                },  
	                messages: {       
	                  "financingBase.shortName":{required:"请输入项目简称"},
	                  "financingBase.maxAmount":{required:"请输入融资额",min:"融资额必须大于10000!"},
	                  "financingBase.rate":{ required:"请输入年利率"},    
	                  "financingBase.financier.eName":{ required:"请选择融资方"},
	                  "financingBase.guarantee.eName":{ required:"请选择担保公司"},    
	                  "financingBase.startDate":{ required:"请选择投标开始日期"},
	                  "financingBase.endDate":{ required:"请选择投标截止日期",compareDate: "截止日期必须大于开始日期!"},
	                  "inancingBase.businessType.id":{ required:"请输入融资期限"} 
	                }    
	        });   
				    
		    $("#submitBtn").click(function() {
			     //alert($("#setEditForm").valid());
			     
			     //if($("#financingBaseFxbzState").val()=='1'){  
				   // if($("#guaranteeId").val() == "") { 
				        	//$("#financingBaseFxbzState").val("0");
		            // }    
			    // }  
		    	 $("#setEditForm").submit(); 
				
			}); 
		     //保障措施
			KE.show({
			        id : 'financingBasePurpose',
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
			 //项目详细信息
			KE.show({
			        id : 'financingBaseNote',
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
						 
			//担保情况
			KE.show({
			        id : 'financingBaseGuaranteeNote',
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
			        
  
		     



		    
		    <c:if test="${financingBase.fxbzState=='0'}"> 
		            $("#danbaoGongsi").css({"display":"none"});
	         </c:if>   
	   
			//时间控件
		    $("#startDate").datepicker({
		        showOn: 'button',
		        buttonImageOnly: false,
		        //changeMonth: true,
		        //changeYear: true,
		        numberOfMonths: 2,
		        dateFormat: "yy-mm-dd",
		        minDate: +1 
		        //maxDate: "+1M"
		    });
		    
		    $("#endDate").datepicker({
		        showOn: 'button',
		        buttonImageOnly: false,
		        //changeMonth: true,
		        //changeYear: true,
		        numberOfMonths: 2,
		        dateFormat: "yy-mm-dd",
		        minDate: +1 
		    });
		    $("#ui-datepicker-div").css({'display':'none'});
		    
			$("#file_image").uploadify({
					        'uploader': '/Static/js/jquery.uploadify-v2.1.0/uploadify.swf',
					        'script': '/sysCommon/sysFileUpload!saveUploadFiles?time='+ new Date().getTime(),
					        'cancelImg': '/Static/js/jquery.uploadify-v2.1.0/cancel.png',
					        'folder': '/Static/userfiles/',
					        'fileDataName' :'filedata',
					        'queueID': 'fileQueue',
					        'buttonText': '请选择资料',  
					        'auto': true,   
					        'multi': true,
					        'fileExt':'*.gif;*.jpg;*.bmp;*.png;*.doc;*.docx;*.xls;*.xlsx;*.zip;*.war;*.pdf',
					        'fileDesc':'*.gif;*.jpg;*.bmp;*.png;*.doc;*.docx;*.xls;*.xlsx;*.zip;*.war;*.pdf',  
					        onSelect: function(e, queueId, fileObj){
					        	$('#tooltip').html("");
					        	$('#tooltip').html("<img src='/Static/images/uploading.gif' style='border:0;' />正在附件加载中，请稍后！");
					        },
					        onComplete:function(event,queueId,fileObj,response,data){ 
					        	$('#tooltip').html(""); 
					        	  var file = eval("("+response+")"); 
					        	  if((file[0].ext=='jpg')||(file[0].ext=='JPG')||(file[0].ext=='gif')||(file[0].ext=='GIF')||(file[0].ext=='jpeg')||(file[0].ext=='JPEG')||(file[0].ext=='bmp')||(file[0].ext=='png'))
					        	  {
									  $("#responseFiles").append("<span id="+(file[0].frontId)+"><a  href=\"/Static/userfiles/"+(file[0].id)+"\" target=\"_blank\"  style=\"color: black\" class=\"tooltipImg2\"  >"+(file[0].fileName)+"</a><img src='/Static/images/no.gif' style='border:0;' onclick=\"delFile('"+file[0].frontId+"','"+file[0].id+"');\"/><input type=\"hidden\" name=\"fileIds\" value=\""+(file[0].id)+"\" /><br/></span>");  
							        	  
					        	  }else{
									  $("#responseFiles").append("<span id="+(file[0].frontId)+"><a  href=\"/Static/userfiles/"+(file[0].id)+"\" target=\"_blank\"  style=\"color: black\"  >"+(file[0].fileName)+"</a><img src='/Static/images/no.gif' style='border:0;' onclick=\"delFile('"+file[0].frontId+"','"+file[0].id+"');\"/><input type=\"hidden\" name=\"fileIds\" value=\""+(file[0].id)+"\" /><br/></span>");  
						        	  
					        	  }
						       
					        }
	          });    
					
					$("#submitFinancier").click(function (){ 
					     $("#pageAjax").val(1);
					     $("#keyWordAjax").val(""); 
					     $("#memberInfoTypeAjax").val("1");
					     $("#memberInfoFinancier").dialog( "destroy" ); 
					     $("#memberInfoFinancier").attr("title","选择融资方---点击左边的单选按钮确认选择");  
					     loadFinancierData();//加载融资方数据  
						 createMyDialog();   
				         $("#memberInfoFinancier").dialog( "open" );  
				         return false; 
				    }); 
	

					$("#submitGuarantee").click(function (){ 
					     $("#pageAjax").val(1); 
					     $("#memberInfoTypeAjax").val("2");
					     $("#keyWordAjax").val(""); 
						 $("#memberInfoFinancier").dialog( "destroy" );
						 $("#memberInfoFinancier").attr("title","选择担保方---点击左边的单选按钮确认选择");
						 loadGuaranteeData();//加载担保方数据  
						 createMyDialog();   
				         $("#memberInfoFinancier").dialog( "open" ); 
				         return false;
				    });
    
    
                  //查询处理
				  $("#seachMemberInfoButton").click(function (){ 
					     if($("#memberInfoTypeAjax").val()=='1'){
		                      loadFinancierData();
					     }
			             if($("#memberInfoTypeAjax").val()=='2'){
			                  loadGuaranteeData();
					     } 
				    });     
				    
				        
        
	
                 });
				
				
				//删除文件
			    function delFile(frontId,fileId){
			       $.getJSON("/sysCommon/sysFileUpload!delete?time=" + new Date().getTime() + "&fileId="+fileId, function(data){
					if(data=="1"){  
						$("#"+frontId).remove(); 
					} 
				});
			    }
				
				//加载融资方数据
				function loadFinancierData(){ 
				  $.getJSON("/back/financingBaseAction!financiers?time="+new Date().getTime()+"&page="+$("#pageAjax").val()+"&showRecord="+$("#showRecordAjax").val()+"&keyWord="+$("#keyWordAjax").val(),function(data){ 
				          $("#showPageData").html("");
				          $("#showListData").html("");
				          $("#showPageData").html(data.pageData);
				          $.each(data.listData,function(key,value){ 
			   	 			    var showListData="";
			   	 			    showListData +="<tr>"; 
			   	 	            showListData +="<td> <input type=\"radio\" onclick=\"checkMember('"+data.listData[key].id+"','"+data.listData[key].showName+"');\"/></td>";							
			   	 	            showListData +="<td>"+data.listData[key].showName+"</td>"; 
			   	 			    showListData +="<td>"+data.listData[key].provinceName+data.listData[key].cityName+"</td>"; 
								showListData +="<td>"+data.listData[key].industryName+"</td>"; 
								showListData +="<td>"+data.listData[key].companyPropertyName+"</td>";
								showListData +="<td>"+data.listData[key].showPhone+"</td>"; 
								showListData +="<td>"+data.listData[key].showMobile+"</td>";  
					 		    showListData +="</tr>";  
					 		    $("#showListData").append(showListData);
			   	 		    });
				      }); 
				   }
				   
				//加载担保方数据
				function loadGuaranteeData(){
				   $.getJSON("/back/financingBaseAction!guarantees?time="+new Date().getTime()+"&page="+$("#pageAjax").val()+"&showRecord="+$("#showRecordAjax").val()+"&keyWord="+$("#keyWordAjax").val(),function(data){ 
				          $("#showPageData").html("");
				          $("#showListData").html("");
				          $("#showPageData").html(data.pageData);
				          $.each(data.listData,function(key,value){ 
			   	 			    var showListData="";
			   	 			    showListData +="<tr>"; 
			   	 			    showListData +="<td> <input type=\"radio\" onclick=\"checkMember('"+data.listData[key].id+"','"+data.listData[key].showName+"');\"/></td>"; 								
			   	 			     showListData +="<td>"+data.listData[key].showName+"</td>"; 
				   	 			 showListData +="<td>"+data.listData[key].provinceName+data.listData[key].cityName+"</td>"; 
								showListData +="<td>"+data.listData[key].industryName+"</td>"; 
								showListData +="<td>"+data.listData[key].companyPropertyName+"</td>"; 
								showListData +="<td>"+data.listData[key].showPhone+"</td>"; 
								showListData +="<td>"+data.listData[key].showMobile+"</td>";  
					 		    showListData +="</tr>";  
					 		    $("#showListData").append(showListData);
			   	 		});
				   });        
				} 
				//创建弹出框	 
				function createMyDialog(){
						     $("#memberInfoFinancier").dialog({
								autoOpen: false,  
								height: 400,
								width:  700, 
								modal: true,  
								/*buttons: {
									"确定": function() {
										$(this).dialog( "close" );
										return false;
									} 
								},*/
								close: function() {
									//对话框关闭之前会执行此处
								}
					     }); 
				};
					
				function toPage(page){ 
				    $("#pageAjax").val(page);
				     if($("#memberInfoTypeAjax").val()=='1'){
				         loadFinancierData();
				     }
		             if($("#memberInfoTypeAjax").val()=='2'){
		                 loadGuaranteeData();
				     }
		            
				}
				function prePage(){  
					 $("#pageAjax").val(parseInt($("#pageAjax").val())-1);
					 if($("#memberInfoTypeAjax").val()=='1'){
				         loadFinancierData();
				     }
		             if($("#memberInfoTypeAjax").val()=='2'){
		                 loadGuaranteeData();
				     }
				}
				function nextPage(){  
					 $("#pageAjax").val(parseInt($("#pageAjax").val())+1);
					 if($("#memberInfoTypeAjax").val()=='1'){
				         loadFinancierData();
				     }
		             if($("#memberInfoTypeAjax").val()=='2'){
		                 loadGuaranteeData();
				     }
				}
				
				//选择处理
				function checkMember(id,name){  
			     if($("#memberInfoTypeAjax").val()=='1'){
                       $("#financierName").val(name);
                       $("#financierId").val(id);
                       $("#memberInfoFinancier").dialog( "destroy" );
			     }
	             if($("#memberInfoTypeAjax").val()=='2'){
	                   $("#guaranteeName").val(name);
                       $("#guaranteeId").val(id);
                       $("#memberInfoFinancier").dialog( "destroy" );
			     } 
			    } 
				
				//风险保障处理
				function changeDanbao(){  
			     if($("#financingBaseFxbzState").val()=='0'){
			    	 $("#guaranteeName").val("");
                     $("#guaranteeId").val("");  
                     $("#danbaoGongsi").css({"display":"none"});
			     }
			     if($("#financingBaseFxbzState").val()=='2'){  
			    	 $("#guaranteeName").val("");
                     $("#guaranteeId").val("");  
                     $("#danbaoGongsi").css({"display":""});
			     }  
			     if($("#financingBaseFxbzState").val()=='1'){ 
                     $("#danbaoGongsi").css({"display":""});      
			     } 
			 }
</script>
	</head>

	<body >
		<form action="/back/financingBaseAction!edit" method="post" id="setEditForm">
			<input type='hidden' class='autoheight' value="auto" />
			<input type="hidden" name="id" value="${id}" />
			
	 		<div class="tab" style="height:380px;">
			<dl>
				<dt>
					<a><span style="color: red">*</span>基本信息</a><a><span style="color: red">*</span>风险评级</a><a>保障措施</a><a>项目详情</a><a>资料清单</a>
				</dt>
				<dd  style="padding-top:30px;">
					<ul style="list-style-type:none;">
					<li style="list-style-type:none;">
						<table border="0"> 
						<tr>
							<td align="right">
								<span style="color: red">*</span>项目简称：
							</td>
							<td>
								<input name="financingBase.shortName" type="text" value="${financingBase.shortName}" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<span style="color: red">*</span>融资申请额：
							</td>
							<td>
								<c:if test="${empty id}">
						 	        <input name="financingBase.maxAmount" type="text"  onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>元
							    </c:if>  
							    <c:if test="${!empty id}"> 
								   <input name="financingBase.maxAmount" type="text" value="${financingBase.maxAmount }" />
							    </c:if>  
							</td>
						</tr>
						<tr>
							<td align="right">
								<span style="color: red">*</span>可融金额：
							</td>
							<td> 
							        <input name="financingBase.curCanInvest" type="hidden" value="${financingBase.curCanInvest }" />
						 	        ${financingBase.curCanInvest}元
 							</td>
						</tr> 
						<tr>
							<td align="right">
								<span style="color: red">*</span>年利率：
							</td>
							<td>
								<c:if test="${empty id}">
						 	        <input name="financingBase.rate" type="text"  onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>% 
							    </c:if>  
							    <c:if test="${!empty id}"> 
								   <input name="financingBase.rate" type="text" value="${financingBase.rate }" />%
							    </c:if>  
							</td>
						</tr>
						<tr>
							<td align="right">
								<span style="color: red">*</span>融资期限：
								</td>
							<td> 
						       <s:select name="financingBase.businessType.id" list="businessTypes" listKey="id" listValue="returnPatternTerm" />月
							</td>
						</tr>
						<tr>
							<td align="right">
								<span style="color: red">*</span>融资方：
							</td>
							<td> 
							     <input    type="text"  name="financingBase.financier.eName"  id="financierName"  value="${financingBase.financier.eName}"     readonly="readonly"/><button class="ui-state-default" id="submitFinancier">选择</button>
							     <input type="hidden" name="financingBase.financier.id" id="financierId" value="${financingBase.financier.id}"/>
							</td>
						</tr>
						<c:if test="${empty id}"> 
					       <tr>
							<td align="right">
								<span style="color: red">*</span>风险保障：
							</td>
							<td> 
					             <s:select name="financingBase.fxbzState" list="fxbzStateList" id="financingBaseFxbzState"  listKey="string1" listValue="string2" onchange="changeDanbao();"/>
							</td>  
						</tr>
						<tr  id="danbaoGongsi">
							<td align="right">
								<span style="color: red">*</span>担保公司：
							</td>
							<td> 
							 <c:if test="${(financingBase.guarantee)!= null}">
							   <input   type="text"   id="guaranteeName" name="financingBase.guarantee.eName"  value="${financingBase.guarantee.showName}"  readonly="readonly"/><button class="ui-state-default" id="submitGuarantee">选择</button>
							   <input type="hidden" name="financingBase.guarantee.id"  id="guaranteeId" value="${financingBase.guarantee.id}"/>
							</c:if>   
							 <c:if test="${(financingBase.guarantee)== null}">
							   <input   type="text"   id="guaranteeName" name="financingBase.guarantee.eName"     readonly="readonly"/><button class="ui-state-default" id="submitGuarantee">选择</button>
							   <input type="hidden" name="financingBase.guarantee.id"  id="guaranteeId"/>
							</c:if>  
							</td>
						</tr> 
						
						</c:if>  
					   <c:if test="${!empty id}"> 
					   <tr>
							<td align="right">
								<span style="color: red">*</span>风险保障：
							</td>
							<td> 
					            ${financingBase.fxbzStateName}  
							</td>
						</tr>
						<tr  id="danbaoGongsi">
							<td align="right">
								<span style="color: red">*</span>担保公司：
							</td>
							<td> 
							 <c:if test="${(financingBase.guarantee)!= null}">
							   ${financingBase.guarantee.eName} 
							</c:if>   
							 <c:if test="${(financingBase.guarantee)== null}">
							        无
							</c:if>  
							</td>
						</tr>
						
					    
						</c:if>  						
						
						
						<tr>
							<td align="right">
								<span style="color: red">*</span>投标开始：
							</td>
							<td>
								<input name="financingBase.startDate" type="text" value="<fmt:formatDate value="${financingBase.startDate}" pattern="yyyy-MM-dd"/>"  id="startDate" readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<td align="right">
								<span style="color: red">*</span>投标截止：
							</td>
							<td>
								<input name="financingBase.endDate" type="text" value="<fmt:formatDate value="${financingBase.endDate }" pattern="yyyy-MM-dd"/>"  id="endDate" readonly="readonly"/>
							</td>
						</tr> 
						<tr>
							<td align="right"> 用途：
							</td>
							<td>
								<textarea name="financingBase.yongtu" style="height:40px;width:200px">${financingBase.yongtu }</textarea> 
							</td>
						</tr> 
                       </table>
				     </li>
					</ul>
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0" align="left">
						<tr>
								<td align="right" width="20%"><span style="color: red">*</span>企业基本信用指数：
								</td>
								<td width="14%">
									<div id="demo1"></div>
									
								</td>
								<td align="left">
								    <input id="qyzs" type="hidden" name="financingBase.qyzs" value="${financingBase.qyzs}"> 
									<textarea name="financingBase.qyzsNote"  id="financingBaseQyzsNote" style="height:29px;width:400px">${financingBase.qyzsNote}</textarea> 
								</td>
							</tr>  	
							 <tr>
								<td align="right"><span style="color: red">*</span>法人代表信用指数：
								</td>
								<td>
								   <div id="demo2"></div> 
								  
								</td>
								<td align="left">
								 <input id="fddbzs" type="hidden" name="financingBase.fddbzs" value="${financingBase.fddbzs}"> 
								  <textarea name="financingBase.fddbzsNote"  id="financingBaseFddbzsNote" style="height:29px;width:400px">${financingBase.fddbzsNote}</textarea>   
								</td>
							</tr>  	
							<tr>
								<td align="right"><span style="color: red">*</span>履约偿债能力指数：
								</td>
								<td>
									<div id="demo3"></div>
									
								</td>
								<td align="left">
								     <input id="czzs" type="hidden" name="financingBase.czzs" value="${financingBase.czzs}"> 
									 <textarea name="financingBase.czzsNote"  id="financingBaseCzzsNote" style="height:29px;width:400px">${financingBase.czzsNote}</textarea>   
								</td>
							</tr>  	
							<tr>
								<td align="right"><span style="color: red">*</span>担保情况指数：
								</td>
								<td>
									<div id="demo4"></div> 
								
								</td>
								<td align="left">
									<input id="dbzs" type="hidden" name="financingBase.dbzs" value="${financingBase.dbzs}">
									<textarea name="financingBase.dbzsNote"  id="financingBaseDbzsNote" style="height:29px;width:400px">${financingBase.dbzsNote}</textarea>     
								</td>
							</tr>  	
							<tr>
								<td align="right"><span style="color: red">*</span>综合推荐指数：
								</td>
								<td>
									<div id="demo5"></div> 
									
								</td>
								<td align="left">
								     <input id="zhzs" type="hidden" name="financingBase.zhzs" value="${financingBase.zhzs}">
									 <textarea name="financingBase.zhzsNote"  id="financingBaseZhzsNote" style="height:29px;width:400px">${financingBase.zhzsNote}</textarea>   
								</td>
							</tr>   
						  
						</table> 
						</li>
					</ul> 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0" align="left"> 
							<tr>
								<td align="right">保障措施：
								</td>
								<td colspan="2"> 
								  <textarea name="financingBase.purpose"  id="financingBasePurpose">${financingBase.purpose}</textarea> 
								</td>
							</tr>  
						</table> 
						</li>
					</ul> 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0"> 
							<tr>
								<td align="right">项目详情：
								</td>
								<td>
								    <textarea name="financingBase.note"  id="financingBaseNote">${financingBase.note}</textarea> 
								</td>
							</tr>  
						</table> 
						</li>
					</ul>  
					<ul style="list-style-type:none;">
					    <li style="list-style-type:none;">
						  <table border="0"> 
                                <tr>
								<td align="right">资料清单：
								</td>
								<td>
									<input type="file" id="file_image" name="filedata" class="text ui-widget-content ui-corner-all" />&nbsp;<br/>
									<span id="tooltip"></span>
									<div  id="responseFiles"> 
							        <c:forEach items="${files}" var="entry">
									     <a href="/Static/userfiles/${entry.id}" target="_blank"  style="color: black"  <c:if test="${(entry.ext=='jpg')||(entry.ext=='JPG')||(entry.ext=='gif')||(entry.ext=='GIF')||(entry.ext=='jpeg')||(entry.ext=='JPEG')||(entry.ext=='bmp')||(entry.ext=='png')}"> class="tooltipImg2" </c:if>  >${entry.fileName}</a><br/>
									</c:forEach> 
									</div>
			 					</td>
							</tr>
						</table>
						</li>
					</ul>  
				</dd>
			</dl>
		</div> 
			
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<button class="ui-state-default" id="submitBtn">
				保存
			</button>
</form>



			<div id="memberInfoFinancier" class="mymymy" style="display: none; font-size: 10pt;" title="选择融资方"> 
			   <div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				  <div style="float: left;">
				            关键字&nbsp;<input type="text" id="keyWordAjax"/>
					<button class="ui-state-default" id="seachMemberInfoButton">查找</button>
					 <input type="hidden" id="pageAjax" value="1"/>
					 <input type="hidden" id="memberInfoTypeAjax"/>
					 <input type="hidden" id="showRecordAjax" value="10"/>
				  </div> 	
				</div>
				<div class="dataList ui-widget">
					<table class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header "> 
								<th> 
								</th> 
								<th>
								          会员名称
								</th> 
								<th>
									地区
								</th>
								<th>
									行业
								</th> 
								<th>
									公司性质
								</th>
								<th>
									固定电话
								</th>
								<th>
									移动电话
								</th>  
							</tr>
						</thead>
						<tbody id="showListData">   
						</tbody>
						<tbody>  
							<tr>
								<td colspan="7" id="showPageData"></td>
							</tr>
						</tbody>
					</table> 
				</div>
			</div>

		<script type="text/javascript">
		    setIframeHeight(400);
		</script>
	</body>
</html>