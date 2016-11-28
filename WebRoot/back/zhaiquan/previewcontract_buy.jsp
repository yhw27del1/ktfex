<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>债权转让协议</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script>
		$(function(){
			$("#confirm").button().click(function(){
				$.ajax( {
					url : "/back/zhaiquan/buyingAction!buy",
					data : {
						"zhaiQuanId" : '${ivr_id}',
						"buyingPrice" : '${price}',
						"zqfwf" : '${zqfwf}',
						"zqsf" : '${zqsf}'
					},
					type : "post",
					async: false,
					success : function(data, textStatus) {
						if("true"==data.buyResult){
						   alert("下达受让指令成功！");
						   window.close();
						}else{
						   alert(data.buyErrorMsg+"下达受让指令失败。");
						   window.close();
						}
					},
					error:function(textStatus,htmlXmlRequest,errorThrow){
					    alert(textStatus);
					}
				});
			});
		});
		
		function doprint(){
			window.open("/back/zhaiquan/contractAction!pdf?id=${this_.id}");			
		}
	</script>
	<style>
		div{line-height:30px;}
		.bold{font-weight:bold}
		.right{text-align:right}
		.center{text-align:center;}
		.l2{text-indent:2em;}
		.l4{text-indent:4em;}
	</style>
  </head>
  
  <body>
   <!-- <div style="position:absolute;top:0px;right:30px;"><input type="button" value="下载合同" onclick="doprint()"/></div> -->
  <div style="width:800px; overflow:hidden;margin: 0 auto;">
  	
  	<div><h1 class="bold center">债权转让协议</h1></div>
  	<div class="bold right">协议编号：${this_.xieyiCode }</div>
  	<div class="bold">甲方（债权出让方）：${this_.seller.realname}</div>
  	<div class="bold">乙方（债权受让方）：${this_.buyer.realname}</div>
  	<div class="bold">丙方（债权转让服务人）：昆投互联网金融交易</div>
  	<div class="l2">鉴于，</div>
  	<div class="l2">甲乙双方是丙方的注册会员，甲方持有通过丙方平台投资的债权，甲方希望将该债权转让给乙方，乙方愿意通过丙方平台购买甲方转让的债权，丙方则依法获得云南省人民政府金融办公室“鼓励提供投融资咨询服务”，取得相应的营业执照，具有规范、安全交易服务的完善机制，帮助交易双方放心投融资。为此，本合同三方根据我国《民法通则》、《合同法》、民间借贷政策法规、金融管理法规等相关法律法规，在自愿、平等、协商一致的基础上，签订本合同，各方信守。</div>
  	<div class="l2"><span class="bold">第一条</span> 甲方转让的债权是甲方通过丙方平台投资获得，并在丙方平台登记备案的权益，相关信息如下：</div>
  	<div class="l4">1. 该债权在丙方平台的编号为： ${this_.zhaiQuanCode}；</div>
  	<div class="l4">2. 该债权对应的借款合同（以下简称：主合同）的合同编号为： ${this_.contract_numbers}；</div>
  	<div class="l4">3. 该债权到期时间为<font color="red"><fmt:formatDate value="${this_.endDate}" pattern="yyyy年MM月dd日"/></font>；</div>
  	<div class="l4">4. 该债权尚未归还的本息合计为：<font color="red"><fmt:formatNumber  value="${this_.syje}" type="currency" /></font> 元（大写：<font color="red">${this_.syje_dx }</font>）。</div>
  	<div class="l2"><span class="bold">第二条</span> 甲乙双方约定的债权转让价款为：<font color="red"><fmt:formatNumber  value="${this_.price}" type="currency"/></font> 元（大写：<font color="red">${this_.price_dx }</font>）。</div>
  	<div class="l2">甲方保证：该债权是甲方已经正确履行主合同约定的全部义务而取得的清洁债权；如果该债权存在瑕疵导致某种义务和风险，则全部由甲方承担和解决。</div>
  	<div class="l2"><span class="bold">第三条</span> 甲乙双方各自分别按照上述转让价款金额的<font color="red">0.2%</font>向丙方支付债权转让服务费。甲乙双方同意丙方直接从甲乙双方在丙方设立的交易账户中扣收本服务费，此项服务费不予退还。</div>
  	<div class="l2"><span class="bold">第四条</span> 本协议签订当日，转让的债权从甲方交易账户划转至乙方交易账户，转让价款金额从乙方交易账户划转至甲方交易账户。同时，依照主合同约定甲方享有的所有权利无条件转移到乙方。乙方、丙方承诺遵守并履行主合同的各项约定。</div>
  	<div class="l2"><span class="bold">第五条</span> 如果该债权转让涉及缴纳法定税金，并且税收法律规定和税务机关责令丙方代扣代缴税金，出让方授权丙方代扣代缴税金；否则丙方无权也无义务代扣代缴税金。</div>
  	<div class="l2"><span class="bold">第六条</span> 在本协议履行过程中发生的纠纷，各方应友好协商解决；协商不成的，提交丙方所在地有管辖权的人民法院处理。</div>
  	<div class="l2"><span class="bold">第七条</span> 本协议为电子协议，由丙方在电子平台发布，该发布是签订协议的邀约，经甲乙双方以会员账号登录丙方电子平台进行确认（电子签署）立即生效，电子签署的日期和时间，即是本协议成立和签订的时间，与纸质合同具同等法律效力。必要时，三方可打印本电子协议为纸质文件签字盖章，自己备查或者办理公证、银行事务等。本协议有效期至主合同履行完毕为止。</div>
  	<div class="l2"><span class="bold">第八条</span> 若因公证、法律诉讼等原因需要本协议纸质文本打印、复印件时，该打印、复印件上需加盖丙方行政公章方有效。</div>
  	<div class="l2"><span class="bold">第九条</span> 本协议所载的“交易账户”为甲/乙方申请成为丙方会员后，在丙方电子平台设立开通的用于投资/借款及相关款项划转的账户，详细内容见甲/乙方各自与丙方签订的《会员协议》。</div>
  	<div class="l2"><span class="bold">第十条</span> 本协议所载的“会员账号”为甲/乙双方成为丙方会员后丙方为其分配的会员代码同时也是其在丙方电子平台交易账户的账号，详见甲/乙双方各自与丙方签订的《会员协议》。</div>
  	<div class="l2"><span class="bold">第十一条</span> 甲/乙双方各自与丙方签订的《会员协议》中涉及借款事宜的条款与本协议无冲突的，按《会员协议》的约定执行。</div>
  	<div class="bold">甲方会员编号：${this_.seller.username }</div>
  	<div class="bold">签署时间：<fmt:formatDate value="${this_.sellerDate}" pattern="yyyy-MM-dd"/></div>
  	<div class="bold">乙方会员编号：${this_.buyer.username }</div>
  	<div class="bold">签署时间：<fmt:formatDate value="${this_.buyerDate}" pattern="yyyy-MM-dd"/></div>
  	<div class="bold">丙方：昆投互联网金融交易</div>
  	<div class="bold">法定代表人：XXX</div>
  	<div class="bold">电子协议发布时间：<fmt:formatDate value="${this_.fbrq}" pattern="yyyy-MM-dd"/></div>
  	<!--  <div><span id="confirm">确认</span></div>-->
  </div>
  
  </body>
</html>
