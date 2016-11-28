<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp"%>
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
<script type="text/javascript" src="/back/four.jsp"></script>
    <table border="1px" bordercolor="#BDC7D8" cellpadding="5px"
		cellspacing="0px" width="680px;">
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
								   <fmt:formatNumber value='${memberBase.user.userAccount.balance}'   type="currency" currencySymbol=""/>&nbsp;
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
							<s:if test="memberBase.category==\"0\"">
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">企业性质：</span>
									</td>
									<td>
										<span class="content">${memberBase.companyProperty.name}&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">企业名称：</span>
									</td>
									<td colspan="1">
										<span class="content"><script>document.write(name("${memberBase.eName}"));</script>
										<s:if test="null!=memberBase.logoName">
									       (${memberBase.logoName})
									    </s:if>
										</span>
									</td>
								</tr>
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">所属行业：</span>
									</td>
									<td>
										<span class="content">${memberBase.industry.name}&nbsp;</span>
									</td>

									<td align="right">
										<span class="title">组织机构代码：</span>
									</td>
									<td>
										<span class="content">${memberBase.eOrgCode}&nbsp;</span>
									</td>
								</tr>
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">组织机构代码证：</span>
									</td>
									<td id="img<s:property value="memberBase.orgCertificate.id.substring(0,memberBase.orgCertificate.id.lastIndexOf(\".\"))"/>">
									    <s:if test="null==memberBase.orgCertificate">
									       <span style="color:red;">未上传</span>
									    </s:if>
									    <s:else>
									    <script type="text/javascript">
									      checkFile("${memberBase.orgCertificate.filepath}","img<s:property value="memberBase.orgCertificate.id.substring(0,memberBase.orgCertificate.id.lastIndexOf(\".\"))"/>");
									    </script>
									    </s:else>
									</td>
									<td align="right">
										<span class="title">营业执照代码：</span>
									</td>
									<td>
										<span class="content">${memberBase.eBusCode}&nbsp;</span>
									</td>
								</tr>
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">营业执照：</span>
									</td>
									<td id="img<s:property value="memberBase.busLicense.id.substring(0,memberBase.busLicense.id.lastIndexOf(\".\"))"/>">
									    <s:if test="null==memberBase.busLicense">
									       <span style="color:red;">未上传</span>
									    </s:if>
									    <s:else>
									    <script type="text/javascript">
									      checkFile("${memberBase.busLicense.filepath}","img<s:property value="memberBase.busLicense.id.substring(0,memberBase.busLicense.id.lastIndexOf(\".\"))"/>");
									    </script>
									    </s:else>
									</td>
									<td align="right">
										<span class="title">税务登记号码：</span>
									</td>
									<td>
										<span class="content">${memberBase.eTaxCode}&nbsp;</span>
									</td>
								</tr>
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">税务登记证：</span>
									</td>
									<td id="img<s:property value="memberBase.taxRegCertificate.id.substring(0,memberBase.taxRegCertificate.id.lastIndexOf(\".\"))"/>">
									    <s:if test="null==memberBase.taxRegCertificate">
									       <span style="color:red;">未上传</span>
									    </s:if>
									    <s:else>
									    <script type="text/javascript">
									      checkFile("${memberBase.taxRegCertificate.filepath}","img<s:property value="memberBase.taxRegCertificate.id.substring(0,memberBase.taxRegCertificate.id.lastIndexOf(\".\"))"/>");
									    </script>
									    </s:else>
									</td>
									<td align="right">
										<span class="title">法人代表：</span>
									</td>
									<td>
										<span class="content">${memberBase.eLegalPerson}&nbsp;</span>
									</td>
								</tr>
								<tr class="org_member">
									<td align="right">
										<span class="title">法人身份证号：</span>
									</td>
									<td>
										<span class="content"><script>document.write(idcard("${memberBase.idCardNo}"));</script>&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">法人身份证正面：</span>
									</td>
									<td id="img<s:property value="memberBase.idCardFrontImg.id.substring(0,memberBase.idCardFrontImg.id.lastIndexOf(\".\"))"/>">
									    <c:if test="${menuMap['idcard']=='inline'}">
										    <s:if test="null==memberBase.idCardFrontImg">
										       <span style="color:red;">未上传</span>
										    </s:if>
										    <s:else>
										    <script type="text/javascript">
										      checkFile("${memberBase.idCardFrontImg.filepath}","img<s:property value="memberBase.idCardFrontImg.id.substring(0,memberBase.idCardFrontImg.id.lastIndexOf(\".\"))"/>");
										    </script>
										    </s:else>
									    </c:if>
									    <c:if test="${menuMap['idcard']!='inline'}">
									    	无权限
									    </c:if>
									</td>
								</tr>
								<tr class="org_member">
									<td align="right">
										<span class="title">法人身份证反面：</span>
									</td>
									<td id="img<s:property value="memberBase.idCardBackImg.id.substring(0,memberBase.idCardBackImg.id.lastIndexOf(\".\"))"/>">
									    <c:if test="${menuMap['idcard']=='inline'}">
										    <s:if test="null==memberBase.idCardBackImg">
										       <span style="color:red;">未上传</span>
										    </s:if>
										    <s:else>
										    <script type="text/javascript">
										      checkFile("${memberBase.idCardBackImg.filepath}","img<s:property value="memberBase.idCardBackImg.id.substring(0,memberBase.idCardBackImg.id.lastIndexOf(\".\"))"/>");
										    </script>
										    </s:else>
									    </c:if>
									    <c:if test="${menuMap['idcard']!='inline'}">
									    	无权限
									    </c:if>
									</td>
									<td align="right">
										<span class="title">银行卡正面：</span>
									</td>
									<td id="img<s:property value="memberBase.bankCardFrontImg.id.substring(0,memberBase.bankCardFrontImg.id.lastIndexOf(\".\"))"/>">
									    <c:if test="${menuMap['bankcard']=='inline'}">
										    <s:if test="null==memberBase.bankCardFrontImg">
										       <span style="color:red;">未上传</span>
										    </s:if>
										    <s:else>
										    <script type="text/javascript">
										      checkFile("${memberBase.bankCardFrontImg.filepath}","img<s:property value="memberBase.bankCardFrontImg.id.substring(0,memberBase.bankCardFrontImg.id.lastIndexOf(\".\"))"/>");
										    </script>
										    </s:else>
									    </c:if>
									    <c:if test="${menuMap['bankcard']!='inline'}">
									    	无权限
									    </c:if>
									</td>
								</tr>
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">固定电话：</span>
									</td>
									<td>
										<span class="content">${memberBase.ePhone}&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">移动电话：</span>
									</td>
									<td>
										<span class="content"><script>document.write(phone("${memberBase.eMobile}"));</script>&nbsp;</span>
									</td>
								</tr>
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">传真：</span>
									</td>
									<td>
										<span class="content">${memberBase.eFax}&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">联系人：</span>
									</td>
									<td>
										<span class="content">${memberBase.eContact}&nbsp;</span>
									</td>
								</tr>
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">联系人固话：</span>
									</td>
									<td>
										<span class="content">${memberBase.eContactPhone}&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">联系人移动电话：</span>
									</td>
									<td>
										<span class="content">${memberBase.eContactMobile}&nbsp;</span>
									</td>
								</tr>
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">联系人传真：</span>
									</td>
									<td>
										<span class="content">${memberBase.eContactFax}&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">邮政编码：</span>
									</td>
									<td>
										<span class="content">${memberBase.ePostcode}&nbsp;</span>
									</td>
								</tr>
							</s:if>
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
								<tr height="38px" class="person_member">
									<td align="right">
										<span class="title">固定电话：</span>
									</td>
									<td>
										<span class="content">${memberBase.pPhone}&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">移动电话：</span>
									</td>
									<td>
										<span class="content"><script>document.write(phone("${memberBase.pMobile}"));</script>&nbsp;</span>
									</td>
								</tr>
								<tr height="38px" class="person_member">
									<td align="right">
										<span class="title">出生年月：</span>
									</td>
									<td>
										<span class="content"><s:date
												name="memberBase.pBirthday" format="yyyy-MM-dd" />&nbsp;</span>
									</td>
									<td align="right">
										<span class="title">身份证号：</span>
									</td>
									<td>
										<span class="content"><script>document.write(idcard("${memberBase.idCardNo}"));</script>&nbsp;</span>
									</td>
								</tr>
								<tr class="person_member">
									<td align="right">
										<span class="title">身份证正面：</span>
									</td>
									<td colspan="1" align="left" id="img<s:property value="memberBase.idCardFrontImg.id.substring(0,memberBase.idCardFrontImg.id.lastIndexOf(\".\"))"/>">
									    <c:if test="${menuMap['idcard']=='inline'}">
										    <s:if test="null==memberBase.idCardFrontImg">
										       <span style="color:red;">未上传</span>
										    </s:if>
										    <s:else>
										    <script type="text/javascript">
										      checkFile("${memberBase.idCardFrontImg.filepath}","img<s:property value="memberBase.idCardFrontImg.id.substring(0,memberBase.idCardFrontImg.id.lastIndexOf(\".\"))"/>");
										    </script>
										    </s:else>
											&nbsp; 
										</c:if>
										<c:if test="${menuMap['idcard']!='inline'}">
											无权限
										</c:if>
									</td>
									<td align="right">
										<span class="title">身份证反面：</span>
									</td>
									<td colspan="1" align="left" id="img<s:property value="memberBase.idCardBackImg.id.substring(0,memberBase.idCardBackImg.id.lastIndexOf(\".\"))"/>">
									    <c:if test="${menuMap['idcard']=='inline'}">
										    <s:if test="null==memberBase.idCardBackImg">
										       <span style="color:red;">未上传</span>
										    </s:if>
										    <s:else>
										    <script type="text/javascript">
										      checkFile("${memberBase.idCardBackImg.filepath}","img<s:property value="memberBase.idCardBackImg.id.substring(0,memberBase.idCardBackImg.id.lastIndexOf(\".\"))"/>");
										    </script>
										    </s:else>
										    &nbsp;
										 </c:if>
										 <c:if test="${menuMap['idcard']!='inline'}">
											无权限
										 </c:if>
									</td>
								</tr>
							</s:if>
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
							<s:if test="memberBase.category==\"1\"">
								<tr height="38px">
									<td align="right">
										<span class="title">银行卡正面扫描图：</span>
									</td>
									<td colspan="3" id="img<s:property value="memberBase.bankCardFrontImg.id.substring(0,memberBase.bankCardFrontImg.id.lastIndexOf(\".\"))"/>">
									    <c:if test="${menuMap['bankcard']=='inline'}">
										    <s:if test="null==memberBase.bankCardFrontImg.id">
										       <span style="color:red;">未上传</span>
										    </s:if>
										    <s:else>
										    <script type="text/javascript">
										      checkFile("${memberBase.bankCardFrontImg.filepath}","img<s:property value="memberBase.bankCardFrontImg.id.substring(0,memberBase.bankCardFrontImg.id.lastIndexOf(\".\"))"/>");
										    </script>
										    </s:else>
									    </c:if>
									    <c:if test="${menuMap['bankcard']!='inline'}">
									    	无权限
									    </c:if>
									</td>
								</tr>
								<tr height="38px" class="person_member">
									<td align="right">
										<span class="title">住址：</span>
									</td>
									<td colspan="3">
										<span class="content">${memberBase.pAddress}&nbsp;</span>
									</td>
								</tr>
								<tr>
									<td align="right">
										<span class="title">开户申请书第一页：</span>
									</td>
									<td colspan="1" align="left"  id="img<s:property value="memberBase.accountApplicationImg.id.substring(0,memberBase.accountApplicationImg.id.lastIndexOf(\".\"))"/>">
									    <c:if test="${menuMap['idcard']=='inline'}">
										    <s:if test="null==memberBase.accountApplicationImg">
										       <span style="color:red;">未上传</span>
										    </s:if>
										    <s:else>
										     <script type="text/javascript">
										      checkFile("${memberBase.accountApplicationImg.filepath}","img<s:property value="memberBase.accountApplicationImg.id.substring(0,memberBase.accountApplicationImg.id.lastIndexOf(\".\"))"/>");
										    </script>
										    </s:else>
									    </c:if>
									    <c:if test="${menuMap['idcard']!='inline'}">
									    	无权限
									    </c:if>
									</td>
									<td align="right">
										<span class="title">开户申请书第二页：</span>
									</td>
									<td colspan="1" align="left" id="img<s:property value="memberBase.accountApplicationImg1.id.substring(0,memberBase.accountApplicationImg1.id.lastIndexOf(\".\"))"/>">
									    <c:if test="${menuMap['idcard']=='inline'}">
										    <s:if test="null==memberBase.accountApplicationImg1">
										       <span style="color:red;">未上传</span>
										    </s:if>
										    <s:else>
										     <script type="text/javascript">
										      checkFile("${memberBase.accountApplicationImg1.filepath}","img<s:property value="memberBase.accountApplicationImg1.id.substring(0,memberBase.accountApplicationImg1.id.lastIndexOf(\".\"))"/>");
										    </script>
										    </s:else>
									    </c:if>
									    <c:if test="${menuMap['idcard']!='inline'}">
									    	无权限
									    </c:if>
									</td>
								</tr>
							</s:if>
							<s:if test="memberBase.category==\"0\"">
								<tr height="38px" class="org_member">
									<td align="right">
										<span class="title">详细地址：</span>
									</td>
									<td colspan="3">
										<span class="content">${memberBase.eAddress}&nbsp;</span>
									</td>
								</tr>
								<td align="right">
									<span class="title">开户申请书第一页：</span>
								</td>
								<td colspan="1" align="left" id="img<s:property value="memberBase.accountApplicationImg.id.substring(0,memberBase.accountApplicationImg.id.lastIndexOf(\".\"))"/>">
									    <s:if test="null==memberBase.accountApplicationImg">
									       <span style="color:red;">未上传</span>
									    </s:if>
									    <s:else>
									    <script type="text/javascript">
									      checkFile("${memberBase.accountApplicationImg.filepath}","img<s:property value="memberBase.accountApplicationImg.id.substring(0,memberBase.accountApplicationImg.id.lastIndexOf(\".\"))"/>");
									    </script>
									    </s:else>
								</td>
								<td align="right">
									<span class="title">开户申请书第二页：</span>
								</td>
								<td colspan="1" align="left" id="img<s:property value="memberBase.accountApplicationImg1.id.substring(0,memberBase.accountApplicationImg1.id.lastIndexOf(\".\"))"/>">
									    <s:if test="null==memberBase.accountApplicationImg1">
									       <span style="color:red;">未上传</span>
									    </s:if>
									    <s:else>
									    <script type="text/javascript">
									      checkFile("${memberBase.accountApplicationImg1.filepath}","img<s:property value="memberBase.accountApplicationImg1.id.substring(0,memberBase.accountApplicationImg1.id.lastIndexOf(\".\"))"/>");
									    </script>
									    </s:else>
								</td>
								<tr>
									<td align="right">
										<span class="title">资料清单：</span>
									</td>
									<td colspan="3" style="width: 260px; height: 180px;"
										valign="top">
										<div id="responseFiles"
											style="color: black; line-height: 30px;">
											<span class="content"> <c:if
													test="${(files)!= null && fn:length(files) > 0}">
													<c:forEach items="${files}" var="entry">
														<span style="cursor: pointer;" title="点击打开文件" onclick="openDocument('/Static/userfiles/${entry.id}');">${entry.fileName}</span>
														<br />
													</c:forEach>
												</c:if> <c:if test="${fn:length(files) == 0}">
								                                                暂无
					                            </c:if></span>
										</div>
									</td>
								</tr>
							</s:if>
							<tr height="38px">
								<td align="right">
									<span class="title">介绍人：</span>
								</td>
								<td colspan="3">
									<span class="content">${memberBase.jingbanren}&nbsp;</span>
								</td>
							</tr>
							<tr height="38px">
								<td align="right">
									<span class="title">QQ：</span>
								</td>
								<td colspan="3">
									<span class="content">${memberBase.qq}&nbsp;</span>
								</td>
							</tr>
							<tr height="38px">
								<td align="right">
									<span class="title">Email：</span>
								</td>
								<td colspan="3">
									<span class="content">${memberBase.email}&nbsp;</span>
								</td>
							</tr>
						</table>
						<div id="tooltipImg3" style="display:none;"><img src="" alt="图片预览"  width="600px" height="400px"></div>
