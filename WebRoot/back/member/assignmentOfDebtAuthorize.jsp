<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>

<script type="text/javascript">
	$(function() {
		$(".table_solid").tableStyleUI();
		
				  
		    $("#checkedAll").click(function() {
	            if ($(this).attr("checked") == "checked") { // 全选
	                    $("input[name='checkUsers']").each(function(index,element) {  
	                        $(this).attr("checked", true);      
	                    });     
	                } else { // 取消全选
	                    $("input[name='checkUsers']").each(function(index) {  
	                        $(this).attr("checked", false);       
	                    });
	                }
	            });
	            
	
	
		    $("#allStops").click(function() { 
	           var checkedValues = new Array();  
				$("[name='checkUsers']").each(function(){          
					if($(this).is(':checked'))   
					{   
						checkedValues.push($(this).val());
					}   
				});    
		       var usernames=checkedValues.join(',');   
		       if(usernames == null || usernames ==""){
	                   alert("请至少选择一项！");
	                   return false;
	            }  
               assignments('/back/member/memberBaseAction!assignmentsStop',usernames);  
               return false;      
                
		    });        
		           
		    $("#allStarts").click(function() {       
	           var checkedValues = new Array();  
				$("[name='checkUsers']").each(function(){          
					if($(this).is(':checked'))   
					{   
						checkedValues.push($(this).val());
					}   
				});    
		       var usernames=checkedValues.join(',');   
		       if(usernames == null || usernames ==""){
	                   alert("请至少选择一项！");
	                   return false;
	            }     
                assignments('/back/member/memberBaseAction!assignmentsStart',usernames);   
                return false;      
		    });   
		    
		    
		    $("input[assignmentsStartId]").click(function() {
		        var usernames=$(this).attr("assignmentsStartId");  
                assignments('/back/member/memberBaseAction!assignmentsStart',usernames); 
                return false;              
		    });         
		    $("input[assignmentsStopId]").click(function() {
		        var usernames=$(this).attr("assignmentsStopId");         
                assignments('/back/member/memberBaseAction!assignmentsStop',usernames);     
                return false;      
		    });         
	              
	});
	
	
	
  function assignments(url,checkUsers){      
         $.get(url,{'usernames':checkUsers,'d':new Date().getTime()},function(data,status){                                
  	        if(data['code']=="1"){                  
	           alert(data['tip']); 
	           $("#form1").attr("action","/back/member/memberBaseAction!assignment");                      
	           $("#form1").submit();                   
		    }else{    
			  alert(data['tip']);      
		    } 
		},'json');    
    }
    
function toURL(url){ 
   window.location.href = url; 
}  
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/dialogHelper.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<form action="/back/member/memberBaseAction!assignment" id="form1">

		<div id="myToolBar"
			class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<input type="hidden" name="page" value="1" /> <input type="hidden"
				name="id" value="1" /> 用户名<input type="text" name="keyWord"
				value="${keyWord}" style="width:85px;" /> &nbsp;
				状态：<s:select list="#{'':'全部','1':'启用','0':'停用'}"   listKey="key" listValue="value" name="useFlag"/>
			<!-- 
				用户类型：<s:select list="#{'4':'全部','T':'投资人','R':'融资方','D':'担保公司'}"   listKey="key" listValue="value" name="memberType"/>
				&nbsp;持有债权&nbsp;&gt;=<input type="text" name="queryBalance" value="${queryBalance}" style="width:70px;" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>元&nbsp;
				债权数量 &gt;=<input type="text" name="queryFrozenAmount" value="${queryFrozenAmount}" style="width:70px;" onkeyup="this.value=this.value.replace(/[^(\d|\.)]/g,'')"/>笔&nbsp;
				 -->
			&nbsp;<input type="submit" class="ui-state-default" value="查询" />(关键字中可以是用户名模糊查询)
			&nbsp;&nbsp;<s:checkbox name="isoverdate" value="false" fieldvalue="true" label="只查看未到期会员"></s:checkbox>只查看未到期会员
			 <c:if test="${fn:length(pageView.records)>0}">
			&nbsp;&nbsp;<input type="submit" class="ui-state-default" value="批量停用" id="allStops"/>
			&nbsp;&nbsp;<input type="submit" class="ui-state-default" value="批量启用" id="allStarts"/>
			</c:if>
			
		</div>

		<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content">
				<tbody>
					<tr>
						<!-- 
				<td colspan="6"> 
					    <c:if test="${!empty pageView.records}">
					        &nbsp;&nbsp;&nbsp;&nbsp; <a style="color:red;"  href="#"  title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
 				         </c:if>
 				</td>
 			 -->
					</tr>
				</tbody>
			</table>
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
					<th width="80px;">
					   <c:if test="${fn:length(pageView.records)>0}">
						<label for="qx">
							<input type="checkbox" id="checkedAll" />全选 
						</label>
					   </c:if>
					</th>
						<!-- 
					<th>
						交易账户
					</th>
					-->
						<th>用户名</th>
						<!-- 
					<th>
						持有债权
					</th>
					<th>
					           债权数量  
					</th>
					 -->
						<th>授权截止日期</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.records}" var="account">
						<tr align="center">
						     <td height="20" ">
									<input name="checkUsers" type="checkbox"
										value="${account.username}" />
								</td>
							<td>${account.username}</td>
							<!-- 
						<td>
						    ${account.username}
						</td>  
						<td>
                            ${account.username}
                        </td>  
                        <td>
                            ${account.username}
                        </td>  
                         -->
							<td>${account.endDate}</td>
							<td>
								  <c:if test="${account.useFlag=='1'}"><span style="color:#4169E1;">正常使用</span></c:if>
					              <c:if test="${account.useFlag=='0'}"><span style="color:red;">停用</span></c:if>						
							</td>
							<td>
							  <c:if test="${account.useFlag=='1'}"><input type="submit" class="ui-state-default" value="停用" assignmentsStopId="${account.username}"/></c:if>
                              <c:if test="${account.useFlag=='0'}"><input type="submit" class="ui-state-default" value="启用" assignmentsStartId="${account.username}"/></c:if>  
							<!-- 
							<a href="memberBaseAction!assignmentUserInfo">查看会员详细信息</a>
							 -->
							</td>  

						</tr>
					</c:forEach>
					<tr>
						<td colspan="10"><jsp:include page="/common/page.jsp"></jsp:include></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
	<form id="form1" action="/back/member/memberBaseAction!newAuthorizer">
		<tbody>
			<tr>

				<td>用户名<input type="text" name="newAuthorizerUsername"
					id="newAuthorizerUsername" value="${keyWord}" style="width:85px;" />
				</td>
				<td>到期时间<input type="text" name="newAuthorizerEndDate"
					id="newAuthorizerEndDate" value="" style="width:85px;" />
				</td>
				<td><input type="button" class="ui-state-default" value="快速新增"
					onclick="checkNewAuthorizerInfo()">
				</td>
				<script type="text/javascript">
					function checkNewAuthorizerInfo() {
						
						if (isNull($("#newAuthorizerUsername").val())) {
							alert("用户名不能为空!");
							return false;
						}
						if (!isMember($("#newAuthorizerUsername").val())) {
                            alert("会员格式不正确！请输入正确的会员账户名!");
                            return false;
                        }
						if (isNull($("#newAuthorizerEndDate").val())) {
							alert("到期时间不能为空!");
							return false;
						}
						if (!isDate($("#newAuthorizerEndDate").val())) {
							alert("到期时间格式不正确！正确格式：'20120101'!");
							return false;
						}
						
						$.getJSON("/back/member/memberBaseAction!validateNewAuthorizer?time="+ new Date().getTime()+ "&newAuthorizerUsername="+ $("#newAuthorizerUsername").val(),null,
										function(data,status) {
											if ("2" == data["result"]) {
												if (confirm('该用户已经存在，是否更新为新的授权有效日期？')) {
													$.getJSON("/back/member/memberBaseAction!swb?time=" + new Date().getTime()
																			+ "&newAuthorizerUsername=" + $("#newAuthorizerUsername").val()
																			+ "&newAuthorizerEndDate=" + $("#newAuthorizerEndDate").val(),
																	function(data1,status) {
																	           
																			if("1"==data1["newresult"]){
																			     alert("更新授权用户信息成功！");
																			}else{
																			    
																			}
																			window.location.href="/back/member/memberBaseAction!assignment?keyWord="+$("#newAuthorizerUsername").val();
																	});
												}
												return true;
											}else if ("1" == data["result"]) {
												$.getJSON("/back/member/memberBaseAction!swb?time="
                                                                            + new Date().getTime()
                                                                            + "&newAuthorizerUsername="
                                                                            + $("#newAuthorizerUsername").val()
                                                                            + "&newAuthorizerEndDate="
                                                                            + $("#newAuthorizerEndDate").val(),null,
                                                                    function(data2) {
                                                                            if("1"==data2["newresult"]){
                                                                                 alert("更新授权用户信息成功！");
                                                                            }
                                                                            window.location.href="/back/member/memberBaseAction!assignment?keyWord="+$("#newAuthorizerUsername").val();
                                                                    });
											}else if ("0" == data["result"]) {
												alert("系统中无登录名为" + $("#newAuthorizerUsername").val()+ "的用户！请重新输入");
												return false;
											}else{
											    alert("未知错误，请联系系统管理员");
											}
											
											
										});
						//$("#form1").submit();
						return true;
					}

					/* 
					用途：检查输入字符串是否为空或者全部都是空格 
					输入：str 
					返回： 
					如果全是空返回true,否则返回false 
					 */
					function isNull(str) {
						if (str == "")
							return true;
						var regu = "^[ ]+$";
						var re = new RegExp(regu);
						return re.test(str);
					}

					/* 
					用途：判断是否是日期 
					输入：date：日期；fmt：日期格式 
					返回：如果通过验证返回true,否则返回false 
					 */
					function isDate(date) {
						if (date == "")
							return false;
						var regu = "^[2][0][0-9]{2}([0][1-9]|[1][0-2])([0-2][0-9]|[3][0-1])$";
						var re = new RegExp(regu);
						return re.test(date);
					}
					
					
					/* 
					用途：判断是否是日期 
					输入：date：日期；fmt：日期格式 
					返回：如果通过验证返回true,否则返回false 
					 */
					function isMember(date) {
						if (date == "")
							return false;
						var regu = "^[0-9]{11}$";
						var re = new RegExp(regu);
						return re.test(date);
					}
				</script>
			</tr>
		</tbody>
	</form>
</body>
<script>
	setIframeHeight(100);
</script>