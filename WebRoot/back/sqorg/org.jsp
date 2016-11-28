<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head> 
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/tree/selecttree/jstree/css.js"></script>
<script src="/Static/js/tree/selecttree/jstree/tree_component.js" type="text/javascript"></script>
<script src="/Static/js/tree/selecttree/common.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/Static/js/tree/selecttree/jstree/tree_component.css" />
<script type="text/javascript" src="/Static/js/tree/selecttree/jstree/css.js"></script>  
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>   
    <title>机构管理</title>  
	<script type="text/javascript">
	 $(document).ready(function(){   
	  		 
			   
		  $("#form").validate({ 
		                rules: { 
		                  "parent_id":{ required:true},  
		                  "org.name":{ required:true},   
		                  "orgBaseCity":{ required:true},
		                  "org.shortName":{ required:true}
		                },  
		                messages: {      
		                  "parent_id":{ required:"请输入父机构"}, 
		                  "org.name":{ required:"请输入名称"}, 
		                  "orgBaseCity":{ required:"所在区域不能为空"},
		                  "org.shortName":{ required:"请输入机构简称"}  
		                }    
		        })   
		});

		$(document).ready(function(){ 
		
			$("#submitNewEdit").click(function() {  
                 $("#form").submit(); 
			});  
			 
  
  		    $('#orgBase_province').change(function(){
			    $.post('/back/region/regionAction!getchildren?region_id='+$(this).val(),null,function(data,status){
			    	$('#orgBase_city').children().remove();
			    	$('#orgBase_city').html(data);
				},'text');  
				$('#orgShowCoding').val("");
			});
			
			 
  		    $('#orgBase_city').change(function(){ 
  		        //异步生成随机数 
  		        $.post("/back/sqOrgAction!ajaxShowCoding",
				{
					showCoding: this.value		
				},function(data, status)
				{ 
					if("success" == status)
					{  
						    if(data.message=="0"){ 
					            $('#orgBase_city').children().remove();
				             }else{ 
				                $('#orgShowCoding').val(data.message);
				             }
						
					}
				}
				); 
				 
			});
			
			//初始选择云南
			$("#orgBase_province").val("0053");  
			$.post('/back/region/regionAction!getchildren?region_id=0053',null,function(data,status){
			    	$('#orgBase_city').children().remove();
			    	$('#orgBase_city').html(data);
			},'text');  
			  
		}); 
		function case_(this_){
			if(this_.value==0){
				$("#choseSponsor_button").show();
			}else{
				$("#choseSponsor_button").hide();
			}
		}
		
		
		
		
		/*function submitGuarantee(obj){
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
			$("#guaranteeName").val(name);
            $("#guaranteeId").val(id);
            $("#memberInfoFinancier").dialog( "destroy" );
	    } 
	</script>
  </head> 
  <body>  
  		   <c:if test="${id==0}">
		        <form action="/back/sqOrgAction!sqAdd" method="post" id="form">
                <input type='hidden' class='autoheight' value="auto" /> 
		   </c:if>  
		    <c:if test="${id!=0}"> 
		        <form action="/back/sqOrgAction!sqEdit" method="post" id="form">
                <input type='hidden' class='autoheight' value="auto" /> 
                <input type="hidden" name="id" value="${id}"/> 
		    </c:if>  

        
        
        <table border="0"> 
            <tr><td align="right"><span style="color:red">*</span>机构名称：</td>
                    <td><input  name="org.name"  type="text"  value="${org.name}"/></td></tr>
            
							
				 <c:if test="${id==0}">
					<tr>
						<td align="right">
							<span style="color: red">*</span><span class="title">所在区域：</span>
						</td>
						<td>
						
							<s:select cssClass="input_select" list="regions_province"
								id="orgBase_province"   headerKey="" headerValue="请选择" listKey="areacode"
								listValue="areaname_s" cssStyle="padding:1px;width:70px;"></s:select>
								<s:select cssClass="input_select" list="regions_city"
								id="orgBase_city"  name="orgBaseCity" listKey="areacode"
								listValue="areaname_s" cssStyle="padding:1px;width:80px;"></s:select>
								
						</td>      
					</tr>
				</c:if>  
		        <tr>
					<td align="right">机构编码(唯一)：</td>
					<td colspan="3" > 
					 <input  name="org.showCoding"  type="text"  value="${org.showCoding}" readonly="readonly" id="orgShowCoding"/>(系统自动生成,不允许修改)	 
					</td>
				</tr>
				<tr>
				    <td align="right">
								<span style="color: red">*</span>机构类型：
					</td>
					<td colspan="3"> 
					    <s:select name="org.type" list="orgtypeList" id="orgtype"  listKey="string1" listValue="string2" cssStyle="padding:1px;width:155px;" onchange="case_(this)" value="#request.org.type"/>
						<!--  <span id="choseSponsor_button" <c:if test="${org.type == 1 || org.type==null}">style="display:none"</c:if>>
							 <input type="text" name="guaranteeName" id="guaranteeName" readonly="readonly" <c:if test="${org.guarantee!=null }">value="${org.guarantee.eName}"</c:if>/>
							 <button class="ui-state-default"  onclick="submitGuarantee(this);return false">选择</button>
							 <button class="ui-state-default" onclick="guaranteeName.value='';guaranteeId.value='';return false;">清除</button>
							 <input type="hidden" name="guarantee_id" id="guaranteeId" <c:if test="${org.guarantee!=null }">value="${org.guarantee.id}"</c:if>/>
						</span>-->
					</td>
				</tr>
				<tr>
					<td align="right"><span style="color: red">*</span>机构简称：</td>
					<td colspan="3" >
						   <input  name="org.shortName"  type="text"  value="${org.shortName}"/>	
					</td>
				</tr>
				<tr>
					<td align="right">联系人：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.linkMan"  type="text"  value="${org.orgContact.linkMan}"/>	
					</td>
				</tr>
			    <tr>
					<td align="right">联系手机：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.mobile"  type="text"  value="${org.orgContact.mobile}"/>	
					</td>
				</tr>
				<tr>
					<td align="right">联系座机：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.phone"  type="text"  value="${org.orgContact.phone}"/>	
					</td>
				</tr>	
				<tr>
					<td align="right">地址：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.address"  type="text"  value="${org.orgContact.address}"/>	
					</td>
				</tr>	
				<tr>
					<td align="right">邮编：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.postalcode"  type="text"  value="${org.orgContact.postalcode}"/>	
					</td>
				</tr>	      
           		<tr>
					<td align="right">QQ：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.qq"  type="text"  value="${org.orgContact.qq}"/>	
					</td>
				</tr>
           		<tr>
					<td align="right">Email：</td>
					<td colspan="3" >
						   <input  name="org.orgContact.email"  type="text"  value="${org.orgContact.email}"/>	
					</td>
				</tr>
           
            <tr><td colspan="2"><button class="ui-state-default" id="submitNewEdit"  style="display:<c:out value="${menuMap['org_Add']}" />">保存</button> </td></tr>
        </table>    
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
  </body>
</html>
