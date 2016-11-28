<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
        <script type="text/javascript">
        	$(function(){
        		var options={dataType:"json",success:result};
        		$("#chargeform").ajaxForm(options);
        	});
        	function checkfm(){
			    var upload = document.getElementById("upload").value;
			    if(upload==""){
			         alert("请选择会员现金充值文件");
			         return false;  
			    }else{
			    	return true;
			    }
			}
        	function charge_import_submit(){
        		if(checkfm()){
        			$("#showResult").html("正在导入，请稍后。");
        			$("#daoru").attr("disabled",true);
        			$("#daoru").attr("mmmmmmm","mmmmmmm");
        			$("#chargeform").submit();
        		}
        		return false;
			}
        	function result(d,s){
        		$("#daoru").attr("disabled",false);
        		$("#daoru").attr("nnnnnnn","nnnnnnn");
				if(s=="success"){
				    if(d.message=="success"){
				    	$("#chargeform").resetForm();
						$("#showResult").html("导入成功"+d.tip);
					}else{
						$("#chargeform").resetForm();
						$("#showResult").html(d.message);
					}
				}else{
					$("#chargeform").resetForm();
					$("#showResult").html("服务器未响应，请稍后重试");
				}
			}
        </script>
<body>
<form action="/back/accountDeal/accountDealAction!charge_import" method="post"  enctype="multipart/form-data" id="chargeform">
	<table style="padding:0 0;">
		<tbody>
			<tr>
				<td style="width:25%;text-align:right;">
					会员现金充值批量导入
				</td>
				<td style="width:25%;">
					<s:file name="upload"  theme="simple" id="upload"></s:file>&nbsp;
				</td>
				<td style="width:50%;">
					<input id="daoru" value="批量充值" type="button" onclick="return charge_import_submit();"/><br />
					<span id="showResult" style="color:red;"></span>
				</td>
			</tr>
		</tbody>
	</table>
</form>
</body>