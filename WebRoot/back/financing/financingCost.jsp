<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="/Static/js/validate/jquery.metadata.js"></script> 
<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css"/>
<script>
$(function(){
	$("#form1").validate({
        rules: { 
          "financingCost.dbf":{ required:true},
          "financingCost.fxglf":{ required:true},
          "financingCost.rzfwf":{ required:true},
          "financingCost.bzj":{ required:true},
          "financingCost.fee1":{ required:true},
          "financingCost.fee7":{ required:true},
          "financingCost.fee10":{ required:true},
          "financingCost.note":{ required:true}
        },  
        messages: {      
          "financingCost.dbf":{ required:"请输入担保费"},
          "financingCost.fxglf":{ required:"请输入风险管理费"},
          "financingCost.rzfwf":{ required:"请输入融资服务费"},
          "financingCost.bzj":{ required:"请输入保证金"},
          "financingCost.fee1":{ required:"请输入风险管理费"},
          "financingCost.fee7":{ required:"请输入席位费"},
         // "financingCost.fee10":{ required:"请输入信用管理费"},
          "financingCost.note":{ required:"请输入备注"}
        }    
	});  
	
	
	$("#submitForm").click(function(){ 
		 $('#form1').submit();
	});
	
	$(".bl").keyup(function(){  
         if(!isNaN(parseFloat($(this).val())))    
         {         
            var tterm=1;  
            if(1==$("#tariff"+$(this).attr('dataIndex')).val()){
               tterm=$("#tterm").val();       
            }            
		   	$("#money"+$(this).attr('dataIndex')).val((parseFloat($("#tdmoney").val())*tterm*parseFloat($(this).val())/100).toFixed(2));   
		   	
		 }else{
		    $(this).val('0');
		    $("#money"+$(this).attr('dataIndex')).val('0');  
		 }            
	       
	});
	
	$("select[styleClass='tariff']").change(function(){          
         if(!isNaN(parseFloat($(this).val())))    
         {         
            var tterm=1;
            if(1==$("#tariff"+$(this).attr('dataIndex')).val()){  
               tterm=$("#tterm").val();    
            }          
		   	$("#money"+$(this).attr('dataIndex')).val((parseFloat($("#tdmoney").val())*tterm*parseFloat($("#bl"+$(this).attr('dataIndex')).val())/100).toFixed(2));   
		   	
		 }else{
		    $(this).val('0');
		    $("#money"+$(this).attr('dataIndex')).val('0');  
		 }            
	       
	});
	
	
})
		
</script> 
<body>

<form action='/back/financingCostAction!fei_yong_que_ren' id="form1">
     <input type='hidden' class='autoheight' value="auto" /> 
     <input type="hidden" name="id" value="${id}">
     <table style="width:100%;">
     		<tr class="ui-widget-header " align='left'> 
				<th>项目编号</th>
				<th>项目简称</th>
				<th>期限(还款方式)</th>
				<th>融资额(￥)</th>
                <th>费用</th> 
				<th>融资结余(￥)</th>  
				<th></th>
			</tr>
     		<tr> 
				<td>${financingCost.financingBase.code}</td>
				<td>
				<c:choose>
					<c:when test="${fn:length(financingCost.financingBase.shortName) > 10}">
						<c:out value="${fn:substring(financingCost.financingBase.shortName,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${financingCost.financingBase.shortName}" />
					</c:otherwise>
				</c:choose>
				</td> 
				<td>${financingCost.financingBase.businessType.returnPatternTerm}</td>
				<td ><fmt:formatNumber value='${financingCost.financingBase.currenyAmount}' type="currency" currencySymbol=""/>
				<input type='hidden'  id="tdmoney" value="${financingCost.financingBase.currenyAmount}" />
				<input type='hidden'  id="tterm" value="${financingCost.financingBase.businessType.term}" />
				
				</td>
				<td>
				<c:choose>
					 <c:when test="${fn:startsWith(financingCost.financingBase.code, 'X')}">
 					           风险管理费:<fmt:formatNumber value='${financingCost.fee1}' type="currency" currencySymbol=""/>					           
 					           融资服务费:<fmt:formatNumber value='${financingCost.fee2}' type="currency" currencySymbol=""/>
 					           还款保证金:<fmt:formatNumber value='${financingCost.bzj}' type="currency" currencySymbol=""/><br/>
 					           担保费:<fmt:formatNumber value='${financingCost.fee3}' type="currency" currencySymbol=""/>
					</c:when>
					<c:otherwise>
					     风险管理费:<fmt:formatNumber value='${financingCost.fxglf}' type="currency" currencySymbol=""/>					     		         
			                          融资服务费:<fmt:formatNumber value='${financingCost.rzfwf}' type="currency" currencySymbol=""/>
			                         还款保证金:<fmt:formatNumber value='${financingCost.bzj}' type="currency" currencySymbol=""/><br/>		
			                          担保费:<fmt:formatNumber value='${financingCost.dbf}' type="currency" currencySymbol=""/>
					</c:otherwise>
				</c:choose>   
				   <!--   <br />其他费用:<fmt:formatNumber value='${financingCost.other}' pattern="#,##0.00"/>  -->
				    <br />席位费:<fmt:formatNumber value='${financingCost.fee7}' pattern="#,##0.00"/>
				    <!-- 信用管理费:<fmt:formatNumber value='${financingCost.fee10}' pattern="#,##0.00"/>   -->    
				  </td>
                <td><fmt:formatNumber value='${financingCost.realAmount}' type="currency" currencySymbol=""/></td> 
			</tr>
     </table>
     <div>
      <hr/>
     </div>
     <table >
     		
					<c:choose>
				   <c:when test="${fn:startsWith(financingCost.financingBase.code, 'X')}">
				   		   <tr class="ui-widget-header " align='left'> <th>费用名称</th><th>收费方式</th><th >比例</th><th>实际费用</th></tr>
				    	 	<tr><td colspan="4"><s:actionerror cssStyle="font-size:13px;color:#B82525"/><s:actionmessage cssStyle="font-size:13px;;color:#4BD634"/></td></tr>
							<tr><td>风险管理费:</td><td><s:select list="#{'0':'一次收费','1':'按月收费'}" name="financingCost.fee1_tariff" id="tariff1"  dataIndex="1" styleClass='tariff'/></td><td><input type="text" name='financingCost.fee1_bl' value="${financingCost.fee1_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl' id="bl1" dataIndex="1"/>%</td><td><input type="text" name='financingCost.fee1' value="<fmt:formatNumber value='${financingCost.fee1}' type="currency" currencySymbol=""/>" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"  id="money1"/></td></tr>
							<tr><td>融资服务费:</td><td><s:select list="#{'0':'一次收费','1':'按月收费'}" name="financingCost.fee2_tariff" id="tariff3"   dataIndex="2" styleClass='tariff'/></td><td><input type="text" name='financingCost.fee2_bl' value="${financingCost.fee2_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl' id="bl3" dataIndex="2"/>%</td><td><input type="text" name='financingCost.fee2' value="<fmt:formatNumber value='${financingCost.fee2}' type="currency" currencySymbol=""/>" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"  id="money3"/></td></tr>
							<tr><td>还款保证金:</td><td><s:select list="#{'0':'一次收费'}" name="financingCost.bzj_tariff" id="tariff2"   dataIndex="3" styleClass='tariff'/></td><td><input type='text'  name='financingCost.bzj_bl' value="${financingCost.bzj_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl'  id="bl2" dataIndex="3"/>%</td><td><input type='text'  name='financingCost.bzj' value="${financingCost.bzj}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" id="money2"/></td></tr>							
							<tr><td>担保费:</td><td><s:select list="#{'0':'一次收费','1':'按月收费'}" name="financingCost.fee3_tariff" id="tariff4"   dataIndex="4" styleClass='tariff'/></td><td><input type="text" name='financingCost.fee3_bl' value="${financingCost.fee3_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl' id="bl4" dataIndex="4"/>%</td><td><input type="text" name='financingCost.fee3' value="<fmt:formatNumber value='${financingCost.fee3}' type="currency" currencySymbol=""/>" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"  id="money4"/></td></tr>

					</c:when>
					<c:otherwise>
					 	<tr><td colspan="4"><s:actionerror cssStyle="font-size:13px;color:#B82525"/><s:actionmessage cssStyle="font-size:13px;;color:#4BD634"/></td></tr>
						<tr class="ui-widget-header " align='left'> <th>费用名称</th><th >收费方式</th><th>比例</th><th>金额</th></tr>
						<tr><td>风险管理费:</td><td><s:select list="#{'0':'一次收费','1':'按月收费'}" name="financingCost.fxglf_tariff" id="tariff5"   dataIndex="5" styleClass='tariff'/></td><td><input type='text'  name='financingCost.fxglf_bl' value="${financingCost.fxglf_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl' id="bl5" dataIndex="5"/>%</td><td><input type='text'  name='financingCost.fxglf' value="${financingCost.fxglf}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"  id="money5"/></td></tr>						
						<tr><td>融资服务费:</td><td><s:select list="#{'0':'一次收费','1':'按月收费'}" name="financingCost.rzfwf_tariff"  id="tariff7"  dataIndex="6"  styleClass='tariff'/></td><td><input type='text'  name='financingCost.rzfwf_bl' value="${financingCost.rzfwf_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl' id="bl7" dataIndex="6"/>%</td><td><input type='text'  name='financingCost.rzfwf' value="${financingCost.rzfwf}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"  id="money7"/></td></tr>
						<tr><td>还款保证金:</td><td><s:select list="#{'0':'一次收费'}" name="financingCost.bzj_tariff"  id="tariff6"   dataIndex="7" styleClass='tariff'/></td><td><input type='text'  name='financingCost.bzj_bl' value="${financingCost.bzj_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl'  dataIndex="7" id="bl6"/>%</td><td><input type='text'  name='financingCost.bzj' value="${financingCost.bzj}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" id="money6"/></td></tr>
						<tr><td>担保费:</td><td><s:select list="#{'0':'一次收费','1':'按月收费'}" name="financingCost.dbf_tariff"   id="tariff8"  dataIndex="8" styleClass='tariff'/></td><td><input type="text" name='financingCost.dbf_bl' value="${financingCost.dbf_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl'  id="bl8" dataIndex="8"/>%</td><td><input type="text" name='financingCost.dbf' value="${financingCost.dbf}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"  id="money8"/></td></tr>


					</c:otherwise>
				</c:choose> 
		<!-- <tr><td>其他费用:</td><td><s:select list="#{'0':'一次收费'}" name="financingCost.other_tariff"   id="tariff9"  dataIndex="9" styleClass='tariff'/></td><td><input type="text" name='financingCost.other_bl' value="${financingCost.other_bl}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')" size='10' class='bl'  id="bl9" dataIndex="9"/>%</td><td><input type="text" name='financingCost.other' value="${financingCost.other}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"  id="money9"/></td></tr>-->
 
	    <s:if test="feeFlag!=\"1\"">  
					       <tr ><td>席位费:</td><td colspan="3">
					       <input type='text'  name='financingCost.fee7' size='10' value="${financingCost.fee7}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>
					        (<font color="red">融资方的席位费今年还没有缴纳!请确认缴费金额,签约时将按此金额代扣!</font>)
					       </td></tr>  
		</s:if>    
 		<!--<s:if test="creditFlag!=\"1\"">  
	       <tr ><td>信用管理费:</td><td  colspan="3">
	       <input type='text'  name='financingCost.fee10'  size='10' value="${financingCost.fee10}" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>
	        (<font color="red">融资方的信用管理费今年还没有缴纳!请确认缴费金额,签约时将按此金额代扣!</font>)
	       </td></tr>  
	    </s:if> --> 
		
		<tr><td>备注:</td><td colspan='3'><textarea name="financingCost.note" style="width:510px;height:100px;">${financingCost.note}</textarea></tr>
		<tr><td colspan='4' align='center'><button  class='ui-state-default' id="submitForm">提交</button></td></tr>
	</table>
</form>
</body>