<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head> 
		<title>融资项目申请修改</title> 

	    <script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
	    <script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
	    <link rel="stylesheet" href="/Static/css/page/page-skin1.css" type="text/css"/> 
	    <link rel="stylesheet" href="/Static/css/simpleTabs.css" type="text/css"/>
	    <script type="text/javascript" src="/Static/js/simpleTabs.js"></script>   
	    <link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
	    <link rel="stylesheet" href="/Static/js/uploadify 3.2.1/uploadify.css" type="text/css"/> 
        <script type="text/javascript" src="/Static/js/uploadify 3.2.1/jquery.uploadify.js"></script>
        <script type="text/javascript" src="/Static/js/open.js"></script>
        <script type="text/javascript" src="/Static/js/kindeditor/kindeditor-min.js"></script>
		<script type="text/javascript"> 
		
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
			   jQuery(function(){   
			   
			   
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
	                  "preFinancingBase.maxAmount":{ required:true,min:1000},
	                  "preFinancingBase.rate":{ required:true}, 
	                  "financierName":{ required:true}, 
	                 // "guaranteeName":{ required:true},
	                  "businessTypeId":{ required:true},
	                  "preFinancingBase.yongtu":{ required:true}
	                },  
	                messages: {       
	                  "preFinancingBase.shortName":{ required:"请输入项目简称"},
	                  "preFinancingBase.maxAmount":{ required:"请输入融资额",min:"申请融资额必须大于或等于1000!"},
	                  "preFinancingBase.rate":{ required:"请输入年利率"},    
	                  "financierName":{ required:"请选择融资方"},
	                //  "guaranteeName":{ required:"请选择担保公司"},
	                  "businessTypeId":{ required:"请选择融资期限"},
	                  "preFinancingBase.yongtu":{ required:"请填写用途"}
	                }    
	        });   
				    
		    $("#submitBtn").click(function() {  

		    	
				    if($("#maxAmount").val() == "") { 
			             alert("融资申请额不能为空");
			            $("#maxAmount").focus(); 
			            return false;
	                }    
	                
	              /*if($("#guaranteeName").val() == ""&&$("#financingBaseFxbzState").val()=='1') { 
			             alert("担保公司不能为空");
			            $("#guaranteeName").focus(); 
			            return false;
	                }   */ 
	                
			        if ((Number($("#maxAmount").val()))<Number(1000))
					{  
					   alert('融资申请金额必须大于1000元!');
					   $("#maxAmount").focus(); 
					   return false;
					}  
					
				
    
				  if('day'==$("#businessTypeId").val()){
				  
				    if($("#interestDay").val() == "") { 
			            alert("期限天数不能为空!");
			            $("#interestDay").focus(); 
			            return false;
	                }    
	                     
					if ((Number($("#interestDay").val()))<=Number(0))  
					{  
					   alert('期限天数必须大于0天!');
					   $("#interestDay").focus(); 
					   return false;
					}   
				  } 
				   
		    	  $("#setEditForm").submit(); 
				
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
						 
	  
  
		      

		   


		    
		    <c:if test="${preFinancingBase.fxbzState=='10'}"> 
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
		   		'removeCompleted' : false,
		        height        : 30,
		        swf           : '/Static/js/uploadify 3.2.1/uploadify.swf',
		        uploader      : '/sysCommon/sysFileUpload!saveUploadFiles',
		        width         : 120,
		        'buttonText'  : '选择上传文件',
		        'fileTypeExts' : '*.gif; *.jpg; *.png',
		        'preventCaching' : true,
		        'progressData'   :'percentage',
		        'onUploadError' : function(file, errorCode, errorMsg, errorString) {
		            alert('uploadError: The file ' + file.name + ' could not be uploaded: ' + errorString);
		        },
		        'onUploadSuccess' : function(file, data, response) {
		        	var json = $.parseJSON(data);
		            if( response){
		            	$("#responseFiles").append("<input type=\"hidden\" name=\"fileIds\" value=\""+(json[0].id)+"\" id=\""+file.id+"\"/>");
		            }
		        },
		        'onCancel' : function(file) {
		        	var file_input = $("[name='fileIds'][id='"+file.id+"']");
		        	var fileid = file_input.val();
		            $.post("/sysCommon/sysFileUpload!delete",{"fileId":fileid},function(data,state){
		            	if(data == 1){
		            	    file_input.remove();
		            	}else{
		            		alert("删除失败");
		            	}
		            },"json");
		        }
		    });
    
                  //查询处理
				  $("#seachMemberInfoButton").click(function (){ 
				         $("#pageAjax").val('1');   
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
			/*	function changeDanbao(){  
			     if($("#financingBaseFxbzState").val()=='15'){
			    	 $("#guaranteeName").val("");
                     $("#guaranteeId").val("");  
                     $("#danbaoGongsi").css({"display":"none"});
			     } 
			    if($("#financingBaseFxbzState").val()=='10'){
                     $("#danbaoGongsi").css({"display":""});			     
			     }  
			     if($("#financingBaseFxbzState").val()=='12'){ 
                     $("#danbaoGongsi").css({"display":""});                    
			     } 
			 }
			 */
			 
			 function submitFinancier(obj){
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
			 
			/* function submitGuarantee(obj){
			 		
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
			 
</script>
	</head>

	<body >
		<form action="/back/preFinancingBaseAction!edit" method="post" id="setEditForm">
			<input type='hidden' class='autoheight' value="auto" />
			<input type="hidden" name="id" value="${id}" />
			
	 		
						<table border="0"> 
						<tr>
							<td align="right">
								<span style="color: red">*</span>项目简称：
							</td>
							<td>
								<input name="preFinancingBase.shortName" type="text" value="${preFinancingBase.shortName}" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<span style="color: red">*</span>融资申请额：
							</td>
							<td>
								<c:if test="${empty id}">
						 	        <input name="preFinancingBase.maxAmount" type="text"  onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"  id="maxAmount"/>
						 	                  元
							    </c:if>  
							    <c:if test="${!empty id}"> 
								   <input name="preFinancingBase.maxAmount" type="text" value="${preFinancingBase.maxAmount }"  id="maxAmount"/>
							    </c:if>  
							</td>
						</tr>
						<tr id="businessTypeIdTr">
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
							<td align="right">
								<span style="color: red">*</span>期限：  
							</td>
							<td colspan="3">
								  <input name="preFinancingBase.interestDay" id='interestDay'  type="text" value="${preFinancingBase.interestDay}"  onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>
							               天
							</td>   
						</tr> 	
						<tr>
							<td align="right">
								<span style="color: red">*</span>年利率：
							</td>
							<td>
								<c:if test="${empty id}">
						 	        <input name="preFinancingBase.rate" type="text"  onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>
						 	        % 
							    </c:if>  
							    <c:if test="${!empty id}"> 
								   <input name="preFinancingBase.rate" type="text" value="${preFinancingBase.rate }" />
								   %
							    </c:if>  
							</td>
						</tr> 				 				
					 
						<tr>
							<td align="right">
								<span style="color: red">*</span>融资方：
							</td>
							<td> 
							     <input type="text" name="financierName" id="financierName" value="${preFinancingBase.financier.eName}"     readonly="readonly"/><button class="ui-state-default"  onclick="return submitFinancier(this)">选择</button>
							     <input type="hidden" name="financierId" id="financierId" value="${preFinancingBase.financier.id}"/>
							</td>
						</tr>
						
						 <tr>
						<td align="right" align="right">行业：</td> 
				                    <td >
				                    <s:select name="preFinancingBase.hyType" list="hyTypes" id="hyTypesState"  listKey="string2+'('+string1+')'" listValue="string2" />
				                    </td> 
						</tr>
						
						<tr>
						<td align="right" align="right">贷款分类：</td> 
				                    <td >
				                    <s:select name="preFinancingBase.dkType" list="dkTypes" id="dkTypesState"  listKey="string2+'('+string1+')'" listValue="string2" />
				                    </td> 
						</tr>
						
				<c:if test="${empty id}"> 
				         <tr>
							<td align="right">
								风险保障：
							</td>
							<td> 
					             <s:select name="preFinancingBase.fxbzState" list="fxbzStateList" id="financingBaseFxbzState"  listKey="string1" listValue="string2"/>
							</td>
						</tr>
						<!--  <tr  id="danbaoGongsi">
							<td align="right">
								担保方：
							</td>
							<td> 
							 <c:if test="${(preFinancingBase.guarantee)!= null}">
							   <input   type="text"   name="guaranteeName"   id="guaranteeName"   value="${preFinancingBase.guarantee.eName}"  readonly="readonly"/><button class="ui-state-default" onclick="return submitGuarantee(this)">选择</button>
							   <input type="hidden" name="guaranteeId"  id="guaranteeId" value="${preFinancingBase.guarantee.id}"/>
							</c:if>   
							 <c:if test="${(preFinancingBase.guarantee)== null}">   
							   <input   type="text" name="guaranteeName"   id="guaranteeName"    readonly="readonly"/><button class="ui-state-default"  onclick="return submitGuarantee(this)">选择</button>
							   <input type="hidden" name="guaranteeId"  id="guaranteeId"/>
							</c:if>  
							</td>
						</tr>-->
						 
						
						</c:if>  
					   <c:if test="${!empty id}">
		                 <tr>
							<td align="right">
								<span style="color: red">*</span>风险保障：
							</td>
							<td> 
					              ${preFinancingBase.fxbzStateName}  
							</td>  
						</tr>
						<!--  <tr  id="danbaoGongsi">
							<td align="right">
								<span style="color: red">*</span>担保公司：
							</td>
							<td> 

							</td>
						</tr>-->			   
					   </c:if>  
						<tr>
							<td align="right"> <span style="color: red">*</span>用途：
							</td>
							<td>
								<textarea name="preFinancingBase.yongtu" style="height:40px;width:200px">${preFinancingBase.yongtu }</textarea> 
							</td>
						</tr> 
						<tr>
								<td align="right">项目详情：
								</td>
								<td>
								    <textarea name="preFinancingBase.note"  id="financingBaseNote">${preFinancingBase.note}</textarea> 
								</td>
							</tr>  
							<tr>
								<td align="right" valign="top">资料清单：</td>
								<td>
									<input type="file" name="filedata" id="file_image"/><br/>
									<span id="tooltip"></span>
									<div  id="responseFiles"> 
							        <c:forEach items="${files}" var="entry">
									     <a href="/Static/userfiles/${entry.id}" target="_top"  style="color: blue"  <c:if test="${(entry.ext=='jpg')||(entry.ext=='JPG')||(entry.ext=='gif')||(entry.ext=='GIF')||(entry.ext=='jpeg')||(entry.ext=='JPEG')||(entry.ext=='bmp')||(entry.ext=='png')}"> class="tooltipImg" </c:if>  >${entry.fileName}</a><br/>
									</c:forEach> 
									</div>
			 					</td>
							</tr>
                       </table>
				    
						
						 
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
