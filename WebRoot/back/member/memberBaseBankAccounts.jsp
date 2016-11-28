<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<title>会员详细信息</title>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script type="text/javascript">
	      function openDocument(url,title) {
		     window.open(url,'',"height='100',width='400',top='0',left='0',toolbar='no',menubar='no',scrollbars='yes', resizable='yes',location='no', status='no'");
	       }
	       
	       function checkFile(fileName,objId){
	           var ret=false;
	           $.ajax({
	              url:"/back/member/memberBaseAction!checkFile",
	              data:{"fileName":fileName},
	              type:"post",
	              success:function(data,textStatus){	                 
	                 var imgDiv="<span class=\"content\"><a onclick=\"openDocument('"+fileName+"');return false;\""+  
								"href=\"#\""+
								"target=\"_black\" style=\"color: black\" class=\"tooltipImg3\" title=\"\"><img "+  
								"class=\"img_style\" width=\"100px\" height=\"60px\""+  
								"src=\""+fileName+"\" />"+  
								"</a>&nbsp; </span>";
					 if("0"==data){
					     imgDiv=" <span style=\"color:red;\">文件不存在</span>";
					 }
				    $("#"+objId).html(imgDiv); 	            
	              },
	              error:function(textStatus,xmlHttpRequest,errorThrow){
	                alert(JSON.stringify(xmlHttpRequest));
	              }
	           });
	           
	           return ret;
	       }	       
        </script>
	</head>
	<body>
		<div class="outer">
			<div class="box">
				<div class="inner">
					<input type='hidden' class='autoheight' value="auto" />
					<div>
						<table border="1px" bordercolor="#BDC7D8" cellpadding="5px"
							cellspacing="0px" width="680px;">
							<s:if test="memberBase.category==\"1\"">
								<tr height="38px" class="person_member">
									<td align="right">
										<span class="title">姓名：</span>
									</td>
									<td>
										<span class="content"><script>document.write(name("${memberBase.pName}"));</script>&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">性别：</span>
									</td>
									<td>
										<span class="content"> <s:if
												test="memberBase.pSex==\"0\"">男</s:if> <s:if
												test="memberBase.pSex==\"1\"">女</s:if> </span>
									</td>
								</tr>
							</s:if>
							<s:elseif test="memberBase.category==\"0\"">
								<tr height="38px" class="person_member">
									<td align="right">
										<span class="title">企业名称：</span>
									</td>
									<td>
										<span class="content"><script>document.write(name("${memberBase.eName}"));</script>&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">企业性质：</span>
									</td>
									<td>
										<span class="content">${memberBase.companyProperty.name}</span>
									</td>
								</tr>
							</s:elseif>
							<tr height="38px">
								<td align="right" width="160px">
									<span class="title">用户名：</span>
								</td>
								<td width="130px">
									${memberBase.user.username}&nbsp;
								</td>
								<td align="right" width="180">
									<span class="title">等级：</span>
								</td>
								<td bordercolor="#BDC7D8" width="130">
									${memberBase.memberLevel.levelname}&nbsp;
								</td>
							</tr>
							<tr height="38px">
								<td align="right" width="160px">
									<span class="title">状态：</span>
								</td>
								<td width="130px">
									<c:if test="${memberBase.state==\"1\"}">
							待审核
							</c:if>
									<c:if test="${memberBase.state==\"2\"}">
							正常
							</c:if>
									<c:if test="${memberBase.state==\"3\"}">
							未通过审核
							</c:if>
									<c:if test="${memberBase.state==\"4\"}">
										<span style="color: red">已停用</span>
									</c:if>
									&nbsp;
								</td>
								<td align="right" width="180">
									<span class="title">账户余额：</span>
								</td>
								<td bordercolor="#BDC7D8" width="130">
									<fmt:formatNumber
										value='${memberBase.user.userAccount.balance}' type="currency"
										currencySymbol="" />
									&nbsp;
								</td>
							</tr>
							<tr height="38px">
								<td align="right" width="160px">
									<span class="title">开户机构：</span>
								</td>
								<td width="130px">
									${memberBase.user.org.name}&nbsp;
								</td>
								<td align="right" width="180">
									<span class="title">开户时间：</span>
								</td>
								<td bordercolor="#BDC7D8" width="130">
									<s:date name="memberBase.createDate"
										format="yyyy-MM-dd HH:mm:ss" />
									&nbsp;
								</td>
							</tr>
							<tr height="38px">
								<td align="right" width="160px">
									<span class="title">会员类型：</span>
								</td>
								<td width="130px">
									<span class="content"><s:if
											test="memberBase.category==0">机构</s:if> <s:if
											test="memberBase.category==1">个人</s:if>&nbsp;${memberBase.memberType.name}&nbsp;</span>
								</td>
								<td align="right" width="180">
									<span class="title">所在区域：</span>
								</td>
								<td bordercolor="#BDC7D8" width="130">
									<span class="content">${memberBase.provinceName}&nbsp;${memberBase.cityName}&nbsp;</span>
								</td>
							</tr>
							<tr height="38px">
								<td align="right">
									<span class="title">开户行：</span>
								</td>
								<td colspan="3">
									<span class="content">${memberBase.banklib.caption}&nbsp;</span>
								</td>
							</tr>
							<tr height="38px">
                                <td align="right">
                                    <span class="title">开户行全称：</span>
                                </td>
                                <td colspan="3">
                                    <span class="content">${memberBase.bank}&nbsp;</span>
                                </td>
                            </tr>
							<tr height="38px">
								<td align="right">
									<span class="title">银行账号：</span>
								</td>
								<td colspan="3">
									<span class="content"><script>document.write(bankcard("${memberBase.bankAccount}"));</script>&nbsp;</span>
								</td>
							</tr>
							<tr height="38px">
								<td align="right">
									<span class="title">银行卡正面扫描图：</span>
								</td>
								<td colspan="3"
									id="img<s:property value="memberBase.bankCardFrontImg.id.substring(0,memberBase.bankCardFrontImg.id.lastIndexOf(\".\"))"/>">
									<s:if test="null==memberBase.bankCardFrontImg">
										<span style="color: red;">未上传</span>
									</s:if>
									<s:else>
										<c:if test="${menuMap['bankcard']=='inline'}">
											<script type="text/javascript">
									      		checkFile("${memberBase.bankCardFrontImg.filepath}","img<s:property value="memberBase.bankCardFrontImg.id.substring(0,memberBase.bankCardFrontImg.id.lastIndexOf(\".\"))"/>");
									    	</script>
										</c:if>
										<c:if test="${menuMap['bankcard']!='inline'}">
											无权限
										</c:if>
									</s:else>
								</td>
							</tr>
						</table>
						<div id="tooltipImg3" style="display: none;">
							<img src="" alt="图片预览" width="600px" height="400px">
						</div>
					</div>
					<div>
						<button class="ui-state-default"
							onclick="javascript: history.go(-1);">
							返回
						</button>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
