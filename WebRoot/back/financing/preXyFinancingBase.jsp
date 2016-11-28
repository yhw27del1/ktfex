<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<%@ page import="java.util.*,com.kmfex.MoneyFormat,com.kmfex.model.PreFinancingBase" %>
<html>
	<head> 
		<title>融资项目风控确认</title> 

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
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script type="text/javascript"> 
			var ratyPath='/Static/js/star/rater/img';  
		 
			
		function initBusinessType(val_o){  
			 var strs = val_o.split("@");
			 var slstr;
			 $('#businessTypeId').children().remove();
			 for(var i = 0; i < strs.length; i++) {
			    slstr = strs[i].split("_"); 
			    $('#businessTypeId').append("<option value='"+slstr[0]+"'>"+slstr[1]+"</option>"); 
		     }  
		}			
		$(document).ready(function(){	
		
					   
			        /****初始化--融资期限 开始***/
			        var businessTypeId
			        var selectTermStr=$("#selectTerm").val();   
			        if('${businessTypeId}'== "") {    
			            businessTypeId=$("#businessTypeId").val();  
			            initBusinessType(selectTermStr)  
	                }else{
	                    businessTypeId='${businessTypeId}';   
		                <c:forEach var="entry" items="${listMapBts}">   
		                  if('${entry.value}'.indexOf("${businessTypeId}_") >= 0 )  
						  {   
							 initBusinessType('${entry.value}') 
							 $("#selectTerm").val('${entry.value}');
							 $("#businessTypeId").val(businessTypeId);
					      }     
		               </c:forEach> 
	                   
	                }    
	                $("#selectTerm").change(function(){     
						 var vlStr=$(this).val();
						 initBusinessType(vlStr);
						  if('day_到期一次还本付息_按日计息'==$(this).val()){             	      
							 $("#interestDayTr").css({'display':''});  
						  }else{        	      
							 $("#interestDayTr").css({'display':'none'});
							 $("#interestDay").val('0');    
						  }     
					});  
			       if('${businessTypeId}'== "day") { 
		             $("#interestDayTr").css({'display':''});  
	               }else{
	                 $("#interestDayTr").css({'display':'none'});  
	               }  
	               /****初始化--融资期限 结束***/
		   		    		      		    
		    /*$("#hyTypesState").change(function(){     
				    
			        $('#qyTypesState').children().remove();                               
			        $.post('/back/financingBaseAction!qyTypesAj',{'qyType':$(this).val(),'d':new Date().getTime()},function(data,status){                     
			  	    	var d = eval('(' + data + ')'); 
			      		var myobj =d.list;          
				    	for(var i=0;i<myobj.length;i++){
				    	  $('#qyTypesState').append("<option value='"+myobj[i].string2+"("+myobj[i].string1+")' >"+myobj[i].string2+"(营业收入:"+myobj[i].string3+",从业人员:"+myobj[i].string4+",资产总额:"+myobj[i].string5+")'</option>");                      
				   	    }            
					},'text');   
			 });*/    
			 
			 
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
	                  "preFinancingBase.shortName":{ required:true},
	                  "preFinancingBase.maxAmount":{ required:true},
	                  "preFinancingBase.preMaxAmount":{ required:true,min:1000},
	                  "preFinancingBase.rate":{ required:true}, 
	                  "financierName":{ required:true}, 
	                 // "guaranteeName":{ required:true},
	                  "preFinancingBase.purpose":{ required:true},
	                  "preFinancingBase.startDate":{ required:true,isDate:true},
	                  "preFinancingBase.endDate":{ required:true,isDate:true,compareDate: "#startDate"},
	                  "preFinancingBase.businessType.id":{ required:true} 
	                },  
	                messages: {       
	                  "preFinancingBase.shortName":{ required:"请输入项目简称"},
	                  "preFinancingBase.maxAmount":{ required:"请输入融资申请额"},
	                  "preFinancingBase.preMaxAmount":{ required:"请输入可融金额",min:"申请融资额必须大于或等于1000!"},
	                  "preFinancingBase.rate":{ required:"请输入年利率"},    
	                  "financierName":{ required:"请选择融资方"},
	                //  "guaranteeName":{ required:"请选择担保公司"},  
	                  "preFinancingBase.purpose":{ required:"保障措施必填"},  
	                  "preFinancingBase.startDate":{ required:"请选择投标开始日期"},
	                  "preFinancingBase.endDate":{ required:"请选择投标截止日期",compareDate: "截止日期必须大于开始日期!"},
	                  "preFinancingBase.businessType.id":{ required:"请输入融资期限"} 
	                }    
	        });   
			
           function changeTotal(){
			   var total = Number($('#qyzs').val())+Number($('#czzs').val())+Number($('#dbzs').val())
 
			   var totalStr="";
			   if(Number(total)>Number(8.75)){ 
			      totalStr="Ⅰ级";
			   }else if(Number(total)==Number(8.75)){ 
			      totalStr="Ⅰ级";
			   }else if(Number(6.25)<Number(total)&&Number(total)<Number(8.75)){ 
			      totalStr="Ⅱ级";
			   }else if(Number(total)==6.25){ 
			      totalStr="Ⅱ级";
			   }else if(Number(3.75)<Number(total)&&Number(total)<Number(6.25)){ 
			      totalStr="Ⅲ级";
			   }else if(Number(total)==Number(3.75)){ 
			      totalStr="Ⅲ级";
			   }else if(Number(total)<Number(3.75)){ 
			      totalStr="Ⅳ级";  
			   }  
			   
			   $("#zhzs").val(total)
			   $("#zhzsHtml").html(total+"("+totalStr+")")  
			   //$("#financingBaseZhzsNote").val(totalStr); 
			}
			$(".changeTotal").change(function(){ 
			  changeTotal();
            });	    
		    $("#submitBtn").click(function() {  
				
				 if(isNaN(parseInt($("#basePreMaxAmount").val())) || parseInt($("#basePreMaxAmount").val()) < 1000 ){
		        		$("dl dt a:eq(0)").trigger("click");
		        		alert("请填写可融资额");
		        		$("#basePreMaxAmount").focus();
		        		return false;
		        }
		        
		        if($("#qyzs").val()=="0" ||$("#czzs").val() == "0" || $("#dbzs").val() == "0" ){
		        		$("dl dt a:eq(1)").trigger("click");
		        		alert("风险评级(产品评级、融资方评级、担保机构评级)必须选择!");	
		        		return false;
		        }
		        
		       
			    if($("#financingBasePurpose").val() == "") {   
		    		$("dl dt a:eq(2)").trigger("click");
			        alert("保障措施不能为空");
			        $("#financingBasePurpose").focus();
			        return false;
		        }    
		        
		         
		      if('day'==$("#businessTypeId").val()){
				  
				    if($("#interestDay").val() == "") {   
			            alert("期限不能为空!");
			            $("#interestDay").focus(); 
			            return false;  
	                }    
	                     
					if ((Number($("#interestDay").val()))<=Number(0))  
					{  
					   alert('期限必须大于0天!');
					   $("#interestDay").focus(); 
					   return false;
					}   
				  } 
		        
                $("#setEditForm").submit();  
			}); 


			//信用确认驳回修改
			$("#noChecksubmitBtn").click(function() {   
			        $("#bohuiInfo").dialog({ 
								autoOpen: false,  
								height: 280,
								width:  400, 
								modal: true,  
								buttons: {
					            "确认驳回": function(){
					                    if($("#financingopeNote").val() == "") { 
								            alert("驳回备注不能为空");
								            $("#financingopeNote").focus()
								            return false;
					                       }   
					                       $("#opeNote").val($("#financingopeNote").val());
							               $("#setEditForm").attr("action","/back/preFinancingBaseAction!xybohui"); 
								           $("#setEditForm").submit();      
					                   }
					             },
					        close: function(){
					            //对话框关闭之前会执行此处
					             return false;
					        }
					     });  
					 $("#bohuiInfo").dialog( "open" ); 
					  return false;
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
 		     //项目详细
			KE.show({
			        id : 'preFinancingBaseNote',
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
			   // setTitle2("融资项目信用确认"); //重新设置切换tab的标题 


		    
		    <c:if test="${preFinancingBase.fxbzState=='0'}"> 
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
 
 
				    
				        
        
	
                 });
                 

				//删除文件
			    function delFile(frontId,fileId){
			       $.getJSON("/sysCommon/sysFileUpload!delete?time=" + new Date().getTime() + "&fileId="+fileId, function(data){
					if(data=="1"){  
						$("#"+frontId).remove(); 
					} 
				});
			    }
				
			
				   
				//加载担保方数据
			/*	function loadGuaranteeData(){
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
				} */
				
				//创建弹出框	 
				function createMyDialog(){
						     $("#memberInfoFinancier").dialog({
								autoOpen: false,  
								height: 400,
								width:  700, 
								modal: true,  
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
	                 //  $("#guaranteeName").val(name);
                     //  $("#guaranteeId").val(id);
                       $("#memberInfoFinancier").dialog( "destroy" );
			     } 
			    } 
				
				//风险保障处理
				function changeDanbao(){  
			     if($("#financingBaseFxbzState").val()=='0'){
			    	// $("#guaranteeName").val("");
                     //$("#guaranteeId").val("");  
                    // $("#danbaoGongsi").css({"display":"none"});
			     } 
			     if($("#financingBaseFxbzState").val()=='2'){
			    	// $("#guaranteeName").val("");
                    // $("#guaranteeId").val("");  
                    // $("#danbaoGongsi").css({"display":"none"});
			     } 
			     if($("#financingBaseFxbzState").val()=='1'){ 
                   // $("#danbaoGongsi").css({"display":""});      
			     } 
			 }
			  
			  
			  
			  function submitFinancier(){
	  			 $("#pageAjax").val(1);
			     $("#keyWordAjax").val(""); 
			     $("#memberInfoTypeAjax").val("1");
			     $("#memberInfoFinancier").dialog( "destroy" ); 
			     $("#memberInfoFinancier").attr("title","选择融资方---点击左边的单选按钮确认选择");  
			     loadFinancierData();//加载融资方数据  
				 createMyDialog();   
		         $("#memberInfoFinancier").dialog( "open" );  
		         return false; 
			  }
			  
			/*	function submitGuarantee(){
					$("#pageAjax").val(1); 
					$("#memberInfoTypeAjax").val("2");
					$("#keyWordAjax").val(""); 
					$("#memberInfoFinancier").dialog( "destroy" );
					$("#memberInfoFinancier").attr("title","选择担保方---点击左边的单选按钮确认选择");
					loadGuaranteeData();//加载担保方数据  
					createMyDialog();
					$("#memberInfoFinancier").dialog( "open" ); 
					return false;
				}*/
			  
			  
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
            
				//查询处理
				function seachMemberInfoButton(){
				    if($("#memberInfoTypeAjax").val()=='1'){
				    	loadFinancierData();
				    }
				    if($("#memberInfoTypeAjax").val()=='2'){
				    	loadGuaranteeData();
				    } 
				    return false;
				}     
				

</script>
	</head>

	<body >
		<form action="/back/preFinancingBaseAction!xyFinish" method="post" id="setEditForm">
			<input type='hidden' class='autoheight' value="auto" />
			<input type="hidden" name="id" value="${id}" />
			  <input type="hidden" name="preFinancingBase.opeNote" id="opeNote" value="${preFinancingBase.opeNote}">
	 		<div class="tab" style="height:420px;">
			<dl>
				<dt>
					<a><span style="color: red">*</span>基本信息</a><a><span style="color: red">*</span>风险评级</a><a><span style="color: red">*</span>保障措施</a><a>项目详情</a> <a>资料清单</a>
				</dt>
				<dd  style="padding-top:30px;">
					<ul style="list-style-type:none;">
					<li style="list-style-type:none;">
						<table border="0"> 
						<tr>
							<td align="right">
								项目简称：
							</td>
							<td>
								<input name="preFinancingBase.shortName"  value="${preFinancingBase.shortName}" />
							</td>
							<td>
								&nbsp;
							</td>
							<td align="right">
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td align="right">
								融资申请额：
							</td>
							<%
				    		PreFinancingBase p = (PreFinancingBase)request.getAttribute("preFinancingBase"); //.maxAmount
				    		String maxAmount = p.getMaxAmount().toString();
				    	 %>	
							<td> 
								   <input name="preFinancingBase.maxAmount" type="hidden" value="${preFinancingBase.maxAmount }" />
								   ${preFinancingBase.maxAmount } 元
							</td>
							<td>
								&nbsp;
							</td>
							<td align="right">
								<span style="color: red">*</span>可融金额：
							</td>
							<td> 
						 	        <input name="preFinancingBase.preMaxAmount" type="text"  onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" id="basePreMaxAmount" value="${preFinancingBase.preMaxAmount}" />元
 							</td>
						</tr>
						<tr>
							<td align="right">
								融资方：
							</td>
							<td> 
							     <script>document.write(name("${preFinancingBase.financier.eName}"));</script>
							     <input type="hidden" name="financierId" id="financierId" value="${preFinancingBase.financier.id}"/>
							</td>
							<td>
								&nbsp;
							</td>
							<td align="right">
								<span style="color: red">*</span>年利率：
							</td>
							<td>
								<c:if test="${empty id}">
						 	        <input name="preFinancingBase.rate" type="text"  onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>% 
							    </c:if>  
							    <c:if test="${!empty id}"> 
								   <input name="preFinancingBase.rate" type="text" value="${preFinancingBase.rate }" />%
							    </c:if>  
							</td>
						</tr>
						<tr>
						    <td align="right">
								 <span style="color: red"></span>风险保障：
							</td>
							<td> 
							  ${preFinancingBase.fxbzStateName}
					          <!--<s:select name="preFinancingBase.fxbzState" list="fxbzStateList" id="financingBaseFxbzState"  listKey="string1" listValue="string2" onchange="changeDanbao();" />-->
							</td>
						    <td>
								&nbsp;
							</td>						
							<td align="right">
								<span style="color: red">*</span>融资期限：
							</td>
							<td> 
							    <select id="selectTerm" >
							        <c:forEach var="entry" items="${listMapBts}">  
						               <option value="${entry.value}">${entry.key}</option> 
						            </c:forEach> 
						        </select> 
							 
						       <select id="businessTypeId"  name="businessTypeId" />
							</td>
							
						</tr>  
												
						<tr id="interestDayTr" style='display:none;'>   

							<td align="right"><span style="color: red">*</span>行业：</td> 
							<td >
							   <s:select name="preFinancingBase.hyType" list="hyTypes" id="hyTypesState"  listKey="string2+'('+string1+')'" listValue="string2" />
							</td> 
							<td>
								&nbsp;
							</td>
							<td align="right">
								<span style="color: red">*</span>期限：  
							</td>
							<td >
								  <input name="preFinancingBase.interestDay" id='interestDay'  type="text" value="${preFinancingBase.interestDay}"   onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/> 
							               天
							</td>  
						</tr> 
						<tr>
							
							
						</tr> 
						<tr> 
							<td align="right">
								用途：
							</td>
							<td>
							    <input id="yongtu"  name="preFinancingBase.yongtu" value="${preFinancingBase.yongtu}"> 						   
							</td>
							<td>
								&nbsp;
							</td>
							<td align="right">
									<span style="color: red">*</span>借款合同：
							</td>
							<td>
                                 <s:select name="conTemplateId"  id="conTemplateId"  list="cTemplates"   listKey="id" listValue="description" /> 
							</td>
							
						</tr> 
						
						
						<tr>							
							<td>
								&nbsp;
							</td>
							
							<td>
								&nbsp;
							</td>
						</tr>   						
						<!--  
						<tr  id="danbaoGongsi">
							<td align="right">
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
							<td align="right">
								<span style="color: red">*</span>担保公司：
							</td>
							<td> 
							 
								 <c:if test="${(preFinancingBase.guarantee)!= null}">
								   <input   type="text"   id="guaranteeName" name="guaranteeName"    value="${preFinancingBase.guarantee.eName}"  readonly="readonly"/><button class="ui-state-default" onclick="return submitGuarantee()" >选择</button>
								   <input type="hidden" name="guaranteeId"  id="guaranteeId" value="${preFinancingBase.guarantee.id}"/>
								</c:if>   
								 <c:if test="${(preFinancingBase.guarantee)== null}">
								    <c:if test="${(preFinancingBase.fxbzState)== '0'}">
	 							      <input   type="text"   id="guaranteeName"  name="guaranteeName"    readonly="readonly"/><button class="ui-state-default" onclick="return submitGuarantee()">选择</button>
								      <input type="hidden" name="guaranteeId"  id="guaranteeId"/>
								      </c:if>  
								 	<c:if test="${(preFinancingBase.fxbzState)!= '0'}">
						                                                      无
									</c:if> 
	 							</c:if>  
							
							</td>
						</tr>-->
                       </table>
				     </li>
					</ul> 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0"> 
						  	
							<tr>
								<td align="right"><span style="color: red">*</span>产品评级：
								</td>
								<td> 
									<select  name="preFinancingBase.czzs"  id="czzs" style='width:100px' class='changeTotal'>
								  	  <option value="0">请选择</option>
									  <option value="4">A(4分)</option>
									  <option value="3.5">A-(3.5分)</option>
									  <option value="3">B(3分)</option>
									  <option value="2.5">B-(2.5分)</option>
									  <option value="2">C(2分)</option>
									  <option value="1.5">C-(1.5分)</option>
									  <option value="1">D(1分)</option>
									</select>
								</td>
								<td align="left"> 
									 <textarea name="preFinancingBase.czzsNote"  id="financingBaseCzzsNote" style="height:29px;width:400px">${preFinancingBase.czzsNote}</textarea>   
								</td>
							</tr>  	
						<tr>
								<td align="right"><span style="color: red">*</span>融资方评级：
								</td>
								<td width="16%"> 
									<select name="preFinancingBase.qyzs"   id="qyzs"  style='width:100px'  class='changeTotal'>
									  <option value="0">请选择</option>
									  <option value="2">A(2分)</option>
									  <option value="1.75">A-(1.75分)</option>
									  <option value="1.5">B(1.5分)</option>
									  <option value="1.25">B-(1.25分)</option>
									  <option value="1">C(1分)</option>
									  <option value="0.75">C-(0.75分)</option>
									  <option value="0.5">D(0.5分)</option>
									</select>
								</td>
								<td align="left"> 
									<textarea name="preFinancingBase.qyzsNote"  id="financingBaseQyzsNote" style="height:29px;width:400px">${preFinancingBase.qyzsNote}</textarea> 
								</td>
							</tr> 
							<tr>
								<td align="right"><span style="color: red">*</span>担保机构评级：
								</td>
								<td> 
								  	<select  name="preFinancingBase.dbzs" id="dbzs"  style='width:100px'  class='changeTotal'>
								  	  <option value="0">请选择</option>
									  <option value="4">A(4分)</option>
									  <option value="3.5">A-(3.5分)</option>
									  <option value="3">B(3分)</option>
									  <option value="2.5">B-(2.5分)</option>
									  <option value="2">C(2分)</option>
									  <option value="1.5">C-(1.5分)</option>
									  <option value="1">D(1分)</option>
									</select>	 
								</td>							
								</td>
								<td align="left"> 
									<textarea name="preFinancingBase.dbzsNote"  id="financingBaseDbzsNote" style="height:29px;width:400px">${preFinancingBase.dbzsNote}</textarea>     
								</td>
							</tr>  	
							<tr>
								<td align="right"><span style="color: red">*</span>项目风险评级：
								   <input type="hidden" name="preFinancingBase.zhzs"  id="zhzs" value="${preFinancingBase.zhzs}"/>
								</td>
								<td id="zhzsHtml"> 
								    自动计算
								</td>
								<td align="left"> 
									 <textarea name="preFinancingBase.zhzsNote"  id="financingBaseZhzsNote" style="height:29px;width:400px">${preFinancingBase.zhzsNote}</textarea>   
								</td>
							</tr>   
						</table> 
						</li>
					</ul> 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0">  
							<tr>
								<td align="right"><span style="color: red">*</span>
								</td>
								<td colspan="2">
								    <textarea name="preFinancingBase.purpose"  id="financingBasePurpose">${preFinancingBase.purpose}</textarea> 
								</td>
							</tr>  
						</table> 
						</li>
					</ul> 
					<ul style="list-style-type:none;">
					  <li style="list-style-type:none;">
						<table border="0"> 
							<tr>
								<td align="right">
								</td>
								<td>
								 <textarea name="preFinancingBase.note"  id="preFinancingBaseNote" readonly="readonly">${preFinancingBase.note}</textarea> 
					 
								</td>
							</tr>  
						</table> 
						</li>
					</ul>  
				    <ul style="list-style-type:none;">
					    <li style="list-style-type:none;">
						   <table border="0"> 
                                <tr>
								<!-- <td align="right">
								</td> -->
								<td> 
									<span id="tooltip"></span>
									<div  id="responseFiles"> 
							        <c:forEach items="${files}" var="entry">
									     <a href="/Static/userfiles/${entry.id}" target="_blank" style="color: black"  <c:if test="${(entry.ext=='jpg')||(entry.ext=='JPG')||(entry.ext=='gif')||(entry.ext=='GIF')||(entry.ext=='jpeg')||(entry.ext=='JPEG')||(entry.ext=='bmp')||(entry.ext=='png')}"> class="tooltipImg2" </c:if>  >${entry.fileName}</a><br/>
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
	       <div id="bohuiInfo" class="mymymy" style="display: none; font-size: 10pt;" title="驳回备注">  
				  <div style="float: left;">  
					    <textarea   id="financingopeNote" style="width:350px;height:70px;"></textarea> 
				  </div> 	 
			</div>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<button class="ui-state-default" id="submitBtn">
				         确认
			</button>
			<button class="ui-state-default" id="noChecksubmitBtn" >
				        驳回
			</button>  

			
</form>





			<div id="memberInfoFinancier" class="mymymy" style="display: none; font-size: 10pt;" title="选择融资方"> 
			   <div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				  <div style="float: left;">
				            关键字&nbsp;<input type="text" id="keyWordAjax"/>
					<button class="ui-state-default" onclick="return seachMemberInfoButton()">查找</button>
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
								<td colspan="5" id="showPageData"></td>
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
