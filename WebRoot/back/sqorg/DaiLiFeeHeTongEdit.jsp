<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<title>新增(修改)代理费</title>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
        <script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
		<link rel="stylesheet"  href="/Static/js/validate/validateself-skin1.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
        <link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
        <link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
        <script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript">
		$(function(){
		       $("#memberbaseform").validate({
		                rules: {
		                  "dlf.orgshowCoding":{required:true},
		                  "dlf.type":{required:true},
		                  "dlf.startDate":{required:true,date:true,dateISO:true},
		                  "dlf.endDate":{required:true,date:true,dateISO:true},
		                  "dlf.checkStandard":{number:true},
		                  "dlf.allocationProportion":{number:true},
		                  "dlf.mouthForThree":{number:true},
		                  "dlf.mouthForSix":{number:true},
		                  "dlf.mouthForNine":{number:true},
                          "dlf.mouthForTwelve":{number:true},
		                },  
		                messages: {    
		                  "dlf.orgshowCoding":{required:"请输入机构编码"},
		                  "dlf.type":{required:"请选择收费类型"},
		                  "dlf.startDate":{required:"请输入合同开始时间",date:"请输入日期",dateISO:"请输入正确的日期格式"},
		                  "dlf.endDate":{required:"请输入合同到期时间",date:"请输入日期",dateISO:"请输入正确的日期格式"},
		                  "dlf.checkStandard":{number:"请输入小数"},
                          "dlf.allocationProportion":{number:"请输入小数"},
                          "dlf.mouthForThree":{number:"请输入小数"},
                          "dlf.mouthForSix":{number:"请输入小数"},
                          "dlf.mouthForNine":{number:"请输入小数"},
                          "dlf.mouthForTwelve":{number:"请输入小数"}
		                }, 
		                submitHandler: function(form) {  //通过之后回调 
		                    var flag = true;
		                    var startDate = $("[name='dlf.startDate']").val();
		                    var endDate = $("[name='dlf.endDate']").val();
		                    if(!startDate)
			                    flag = false;
		                    if(!endDate)
                                flag = false;
			                if(flag){
			                    form.submit();//提交表单，如果不写，即便通过表单也不会自动提交
			                }else{
			                    alert("合同开始日期或者结束日期格式不正确");
				            } 
                       }
		        });   

		        
		}); 

        </script>
	</head>
	<body>
		<form action="/back/sqorg/sqOrgAction!save" method="post"
			id="memberbaseform">
			<table cellspacing="2px;" >
			    <tr>
                    <td colspan="4" style="text-align: center;">
                        <span class="title" style="font-size: 20px;font-weight: bold">代理费合同</span>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <input name="dlf.id" type="hidden" value="${dlf.id}" />
                    </td>
                </tr>
				<tr>
					<td align="right">
						<span class="title">代理费合同显示名称</span>
					</td>
					<td>
						<input maxlength="18" class="input_box" style="width: 130px;" name="dlf.showName" type="text" value="${dlf.showName}" />
					</td>
                    <td align="right">
                        <span style="color: red">*</span><span class="title">代理费合同对应机构</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.orgshowCoding" type="text" value="${dlf.orgshowCoding}" />
                    </td>
                </tr>
                <tr>
                     <td align="right">
                         <span style="color: red">*</span>代理费计算类型</td>
                     <td colspan="3">
                         <s:radio list="#{'A':'A','B':'B'}" name="dlf.type" theme="simple" /> 
                         <span style="color: red;font-size: 10px;">(A类型按月不同分别核算，B类型按分配比例和每月核算标准计算)</span>
                     </td>
                </tr>
                <tr>
                    <td align="right">
                        <span style="color: red">*</span><span class="title">合同有效期开始时间</span>
                    </td>
                    <td>
                        <input class="easyui-datebox" style="width: 130px;" maxlength="10" name="dlf.startDate" type="text" id="dlf.startDate" value="<fmt:formatDate value="${dlf.startDate}" pattern="yyyy-MM-dd"/>" />
                    </td>
                   <td align="right">
                        <span style="color: red">*</span><span class="title">合同有效期结束时间</span>
                    </td>
                    <td>
                       <input class="easyui-datebox" style="width: 130px;" maxlength="10" name="dlf.endDate" type="text" id="dlf.endDate" value="<fmt:formatDate value="${dlf.endDate}" pattern="yyyy-MM-dd"/>" />
                       <span style="color: red;font-size: 10px;">(合同保存的日期为选择日期0时0分0秒)</span>
                     </td>
                </tr>     
                <tr>
                    <td align="right">
                        <span  class="title">（A类）三月期比率</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.mouthForThree" type="text" value="${dlf.mouthForThree}" />
                    </td>
                    <td align="right">
                        <span class="title">（A类）六月期比率</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.mouthForSix" type="text" value="${dlf.mouthForSix}" />
                    </td>
                </tr>     
                <tr>
                    <td align="right">
                        <span  class="title">（A类）九月期比率</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.mouthForNine" type="text" value="${dlf.mouthForNine}" />
                    </td>
                    <td align="right">
                        <span  class="title">（A类）十二月期比率</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.mouthForTwelve" type="text" value="${dlf.mouthForTwelve}" />
                    </td>
                </tr>  
                <tr>
                    <td align="right">
                        <span  class="title">（B类）分配比例</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.allocationProportion" type="text" value="${dlf.allocationProportion}" />
                    </td>
                    <td align="right">
                        <span  class="title">（B类）每月核算标准</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.checkStandard" type="text" value="${dlf.checkStandard}" />
                    </td>
                </tr>
                <tr>
                    <td align="right">
                        <span  class="title">负责人</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.director" type="text" value="${dlf.director}" />
                    </td>
                    <td align="right">
                        <span  class="title">区域</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.area" type="text" value="${dlf.area}" />
                    </td>
                </tr>  
                <tr>
                    <td align="right">
                         <span  class="title">业务类型</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.businessType" type="text" value="${dlf.businessType}" />
                    </td>
                    <td align="right">
                         <span class="title">渠道负责人</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.channelDirector" type="text" value="${dlf.channelDirector}" />
                    </td>
                </tr>  
                <tr>
                    <td align="right">
                        <span class="title">席位费标准</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.seatFee" type="text" value="${dlf.seatFee}" />
                    </td>
                    <td align="right">
                        <span  class="title">备注</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.note" type="text" value="${dlf.note}" />
                    </td>
                </tr>  
                <tr>
                    <td align="right">
                        <span  class="title">状态</span>
                    </td>
                    <td>
                        <input maxlength="18" class="input_box" style="width: 130px;" name="dlf.state" type="text" value="${dlf.state}" />
                    </td>
                </tr>  
                
				<tr>
					<td align="center" colspan="4">
						<input class="ui-state-default" type="submit" value="保存" />
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
