<%@ page language="java" import="java.util.*,com.kmfex.*,com.kmfex.model.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<html>
	<head>
		<title>${financingBase.shortName}-借款合同</title>
		<style>
			body{padding:0;margin:0;text-align:left;font-size:13px;overflow-x:hidden}
			table td{font-size:13px;}
			.key{color:#D72222;text-decoration: underline;}
			.head{font-weight: bold;}
		</style>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		<script>
		$(function(){
			$("#make_sure").click(function(){
				$.post("/back/investBaseAction!agreement?invest_record_id=${record.id}",{},function(){
					//parent.window.location.refresh();
					//window.location.reload();
					window.close();
				});
			});
			$("#invest_sure").click(function(){
				var id = $("#financingBaseId").val();
				var money = $("#money").text();
				var url = "/back/investBaseAction!invest?id="+id+"&invest.money="+money+"&time="+new Date().getTime();
				$.getJSON(url,function(data){
					alert(data.msg);
					window.location.href="/back/financingBaseAction!investList";
				});
			});
		});
		function doprint(){
			
			window.open("/back/investBaseAction!pdf?invest_record_id=${record.id}","","width=700,height=40");
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
	 <input type='hidden' class='autoheight' value="auto" />  
		<c:if test="${contract.id!=null}"><div style="position:absolute;top:1px;right:30px;"><input type="button" value="下载合同" onclick="doprint()"/></div></c:if>
		<div style="width:900px;overflow-x:hidden;margin-left:auto;margin-right:auto;margin-top:30xp;"  id="maindiv">
			
				
				
				<div><h1 class="bold center">借款合同<input id="financingBaseId" type="hidden" value="${financingBaseId}"/></h1></div>
				<div class="bold right">合同编号：${contract.contract_numbers}</div>
				<div class="bold">甲方（出借人）：${contract.first_party}</div>
				<div class="bold">乙方（借款人）：<c:if test="${contract.investor_make_sure!=null}">${contract.second_party}</c:if><c:if test="${contract.investor_make_sure==null}">
				<c:if test="${fn:length(contract.second_party) >= 4}">******${fn:substring(contract.second_party,fn:length(contract.second_party)-4,fn:length(contract.second_party))}</c:if>
				<c:if test="${fn:length(contract.second_party) < 4}">${fn:substring(contract.second_party,0,1)}******</c:if>
				
				</c:if></div>
				<div class="bold">丙方（投融资服务人）：昆投互联网金融交易</div>
				<div class="l2">鉴于，</div>
				<div class="l2">甲乙双方是丙方的注册会员，甲方持有合法资金并自愿寻求投资对象，获取投资收益，乙方需要资金维持经营/创业/发展事业/或者个人事务，丙方是依法注册成立的投资咨询机构，具有规范、安全交易服务的完善机制，帮助交易双方放心投融资。为此，本合同三方根据我国《民法通则》、《合同法》、民间借贷政策法规、金融管理法规等相关法律法规，在自愿、平等、协商一致的基础上，签订本合同，各方信守。</div>
				<div class="l2"><span class="bold">第一条</span> 甲方通过丙方向乙方提供借款人民币： <span class="key">_</span>元（大写： <span class="key">_</span>）。</div>
				<div class="l2">借款期限 ${loan_term_str}，自 <span class="key"><fmt:formatDate value="${contract.start_date}" pattern="yyyy年MM月dd日"/></span>起至 <span class="key"><fmt:formatDate value="${contract.end_date}" pattern="yyyy年MM月dd日"/></span>。（实际交付借款日与本合同期限起始日不一致的，从实际交付借款日起计算借款期限）。</div>
				<div class="l2"><span class="bold">第二条</span> 借款年利率为： <span class="key">${loan_lx_}%</span>。利息总额为： <span class="key">_</span>元（大写：<span class="key">_</span>）。</div>
				<div class="l2"><span class="bold">第三条</span> 还款方式为以下第 <span class="key">${contract.payment_method}</span> 种：</div>
				<div class="l4">1. 到期一次还本付息；</div>
				<div class="l4">2. 按月等额本息还款： 等额每月还本金： <span class="key">_</span>元（大写： <span class="key">_</span>），等额每月还利息： <span class="key">_</span>元（大写： <span class="key">_</span>）。</div>
				<div class="l4">3. 按月等额付息，到期一次还本： 等额每月还息：  <span class="key">_</span>元（大写：<span class="key">_</span> ）。</div>
				<div class="l2"><span class="bold">第四条</span> 若上条约定的还款方式为按月等额本息还款，则每月 <c:if test="${contract.payment_method==2}"><span class="key">${contract.repayment_term==0?"":contract.repayment_term}</span></c:if>日为还款日，每月应还金额合计为： <c:if test="${contract.payment_method==2}"><span class="key">_</span></c:if>元（大写： <c:if test="${contract.payment_method==2}"><span class="key">_</span></c:if>）。若还款日逢国家法定节假日则当月还款日提前至该假期前的最后一个工作日。若当月无该日期的，当月还款日提前至该日期前的最后一个工作日。</div>
				<div class="l2"><span class="bold">第五条</span> 乙方借款的具体用途为： <span class="key">${contract.purpose}</span>。乙方应按约定用途使用借款，不得将借款挪作他用或者用于违法用途。</div>
				<div class="l2"><span class="bold">第六条</span> 甲方同意：丙方从丙方为甲方设立的会员交易账户中划转本合同约定的借款金额作为向乙方支付的借款。</div>
				<div class="l2"><span class="bold">第七条</span> 丙方将款项划转至乙方交易账户，该款项到达乙方交易账户之日为实际交付借款日。</div>
				<div class="l2"><span class="bold">第八条</span> 甲方授权丙方向乙方收取归还的本金及利息。丙方收到乙方归还的本金及利息后应于两个工作日内将相应款项拨付至甲方交易账户。</div>
				<div class="l2"><span class="bold">第九条</span> 乙方授权丙方办理针对乙方指定账户的委托银行扣款相关事宜，该委托扣款用于乙方归还本金、支付利息、逾期利息、违约金、赔偿款（如果发生）。</div>
				<div class="l2"><span class="bold">第十条</span> 因本合同第六、七、八、九条约定的款项划转，依据税法规定，如果产生所得税、营业税、个人所得税等纳税义务，则甲方、乙方各自依法纳税。丙方不承担甲乙双方投融资交易发生的税款；丙方并不在甲乙双方投融资交易中赚取差价，丙方因提供服务获取的收费及其税款承担在第十一条具体规定。</div>
				<div class="l2"><span class="bold">第十一条</span> 乙方向丙方支付如下“投融资咨询技术服务”费用：</div>
				<%
					ContractKeyData ckd = (ContractKeyData)request.getAttribute("contract");
					String fxglf_upl = MoneyFormat.format(Double.toString(ckd.getRiskmanagement_cost_all_allah()),true); 
					String rzfwf_upl = MoneyFormat.format(Double.toString(ckd.getService_cost_all_allah()),true); 
					String all_upl = MoneyFormat.format(Double.toString(ckd.getService_cost_all_allah()+ckd.getRiskmanagement_cost_all_allah()),true); 
				%>
				<div class="l2">一、风险管理费： <span class="key">_</span>元（大写： <span class="key">_</span>）。</div>
				<div class="l2">二、融资服务费： <span class="key">_</span>元（大写： <span class="key">_</span>）。</div>
				<div class="l2">合计： <span class="key">_</span>元（大写： <span class="key">_</span>）。</div>  
				<div class="l2">上述费用乙方同意丙方在向乙方交易账户拨付借款时一次性扣收。无论本合同履行情况如何，上述费用，乙方都无权要求丙方退还。丙方因提供投融资咨询服务获得的收益所产生的税款，由丙方承担。</div>
				<div class="l2"><span class="bold">第十二条</span> 借款交付后，丙方有权对乙方借款的使用进行管理与监控。</div>
				<div class="l2"><span class="bold">第十三条</span> 甲方授权丙方对乙方履行本合同的情况进行管理与监控并在乙方发生违约时向乙方追索违约金及相应损失。</div>
				<div class="l2"><span class="bold">第十四条</span> 关于借款合同之从合同担保合同事宜的约定：</div>
				<div class="l2">一、甲方授权丙方代表出借人，以债权人（出借人）的名义，与借款人找来的担保人，签订担保合同，包括保证合同、抵押合同、质押合同以及其它具有担保性质的合同，甲方并授权丙方签署与担保有关的其它法律文件和办理抵押登记等与担保有关的法律手续。丙方在代表债权人（出借人）签订担保合同和签署与担保有关的其它法律文件时，在担保合同尾部“债权人”（出借人）以及与担保有关的其它法律文件之签章处，加盖债权人之代理人昆投互联网金融交易的公章而且该签章与债权人本人之签章具有同等法律效力，该签章产生的法律结果全部由债权人承受，并且债权人承诺不向代理人昆投互联网金融交易提出索赔请求，债权人的代理人对债务人和担保人不履行付款义务不承担任何法律责任。</div>
				<div class="l2">二、甲方并且授权丙方督促债务人和担保人履行合同，当乙方（借款人）未能按本合同约定足额归还本金或利息时，或者担保人不履行义务时，丙方代理债权人向乙方或者担保人进行追索，并签订借款合同或者担保合同的补充协议。</div>
				<div class="l2">三、甲方的代表和代理人昆投互联网金融交易在代理甲方办理担保事宜过程中，只要做到对甲方忠诚和尽力即可，甲方不再对代理人附加其它任何责任和义务。并且，甲方应当就代理人办理代理事项支付合理的代理报酬，并承担代理事务发生的开支。代理报酬和开支可以直接从债务人或者担保人支付的款项中扣除。</div>
				<div class="l2"><span class="bold">第十五条</span> 乙方应根据丙方的要求提供相应的文件及资料，并保证所提供资料、文件的真实性与合法性。</div>
				<div class="l2"><span class="bold">第十六条</span> 丙方有权对乙方提交的资料、文件的合法性、真实性进行调查；对乙方的资信、资产状况进行调查。</div>
				<div class="l2"><span class="bold">第十七条</span> 乙方法定代表人更换、改变住所或经营场所以及减少注册资金时，应事先书面通知丙方；乙方因实行承包、租赁、联营、股份制改造、分立、被兼并（合并）、对外投资及其他原因而改变经营管理方式或产权组织形式时，应提前通知丙方，并落实债务和还款措施；其居住地、联系方式、单位的变迁、资产情况、负债情况等发生变更或者涉及诉讼、仲裁，必须在变更或者诉讼仲裁发生后三日内书面通知丙方。</div>
				<div class="l2"><span class="bold">第十八条</span> 乙方须服从丙方对其使用借款资金情况、实际用途和有关生产经营、财务活动等借后活动进行的管理与风险监控。</div>
				<div class="l2"><span class="bold">第十九条</span> 甲方可通过丙方将本合同涉及的债权向在丙方正式注册的其他投资人会员进行转让，而无须事前或事后告知乙方，也无须征得乙方同意。此种转让在丙方进行登记备案即可。丙方认为需要将该转让告知乙方时，丙方可以无需甲方再次授权而向乙方进行告知。</div>
				<div class="l2"><span class="bold">第二十条</span> 本合同履行期间，出现下列情况任意一项或多项时，甲方授权丙方有权要求乙方提前归还本金，支付利息、逾期利息、违约金和赔偿金。而乙方授权丙方并同意丙方执行甲方的要求。且甲乙双方同意丙方有权选择解除本合同：</div>
				<div class="l2">一、乙方向丙方提供虚假证明材料；</div>
				<div class="l2">二、乙方违反合同关于借款用途的相关规定的；</div>
				<div class="l2">三、乙方拒绝丙方对借款使用情况、实际用途进行监督检查的；</div>
				<div class="l2">四、乙方拒绝纠正或者其实际行为表明不纠正违反本合同的行为的；</div>
				<div class="l2">五、乙方负有数额较大的债务或卷入、可能卷入重大的诉讼或仲裁程序及其他法律纠纷，足以影响其偿债能力的；</div>
				<div class="l2">六、乙方不配合丙方对其经营的公司进行巡检与管理的；</div>
				<div class="l2">七、乙方任何一期未按时或足额还款的；</div>
				<div class="l2">八、乙方其他任何一笔借款（即其他任何负债）未按时、足额还款（含一期）的；</div>
				<div class="l2">九、乙方经营的公司出现经营恶化、面临被查封、停业/行业整顿或被处置资产等情形；</div>
				<div class="l2">十、乙方在本合同项下的借款本息全部清偿前，未经丙方书面同意向第三人提供担保的（因本合同乙方的担保人要求乙方反担保，且反担保协议经甲方同意的情形除外）；</div>
				<div class="l2">十一、 乙方发生其他足以影响其偿还债务能力事件的。</div>
				<div class="l2"><span class="bold">第二十一条</span> 一旦发生上述任何一条违约事件，甲方有权采取以下任何一项或多项措施：</div>
				<div class="l2">一、乙方出现本合同第二十条约定的情形或违反本合同其他约定的，最终导致丙方解除本合同、提前收回全部借款本息时，除结清全部借款本金及利息（计算至结清日当天）外，乙方应另行向甲方支付借款金额的<span class="key">5%</span>作为违约金，若违约金不足以偿付甲方、丙方因索赔而产生的各项费用时，甲方和丙方都有权继续向乙方索赔。</div>
				<div class="l2">二、乙方未按本合同约定按时、足额偿还借款本息的，自逾期之日起应向甲方每日按应还款金额的<span class="key">0.1%</span>支付滞纳金。</div>
				<div class="l2">三、在本合同项下的借款本息全部清偿前，乙方及乙方的联系方式、实际居住地、工作单位、资产、负债等情况发生变化，未在变更后三天内书面通知丙方的，上述行为每发生一次，乙方应向丙方支付<span class="key">500</span>元/项（次）的违约金。</div>
				<div class="l2">四、在本合同履行期间，乙方拒不履行还款义务或无力清偿欠款，而最终导致甲方付诸法律的，除承担全部费用外，乙方还应向甲方支付借款额的<span class="key">5%</span>作为违约金。</div>
				<div class="l2">尽管甲乙双方在第二十条授权丙方，以及丙方根据该授权行使权利，但是，</div>
				<div class="l2">丙方没有向甲方支付、赔付、垫付任何款项的经济责任，丙方只有代收、代转已经从乙方收到的债款的义务和经济责任。甲方要求丙方向债务人追收欠款的，甲方需预付追债费用。</div>
				<div class="l2"><span class="bold">第二十二条</span> 本合同履行期间，乙方提前偿还借款的，应向丙方提出申请。丙方同意提前还款的，乙方除按实际用款期间（不足一个月的，按一个月计）支付利息外，还应另向丙方支付一个月的利息作为违约金。丙方收到此违约金后全额划转至甲方交易账户。</div>
				<div class="l2"><span class="bold">第二十三条</span> 本合同履行期间，如果乙方公司转让、变更等必须征得丙方的书面同意，否则转让行为无效。丙方有权直接通知甲方及其他全部出借人解除与乙方的借款合同、提前收回全部借款本息，同时乙方向甲方及丙方各支付人民币<span class="key">10000</span>元的违约金。</div>
				<div class="l2"><span class="bold">第二十四条</span> 乙方同意丙方将借款金额<span class="key"><fmt:formatNumber value="${bzj_bl}" pattern="0.00"/>%</span>的款项冻结在乙方交易账户中作为履约保证金。乙方按时足额归还本息后，履约保证金解冻。乙方不按时足额归还本金支付利息的，丙方将履约保证金没收。</div>
				<div class="l2"><span class="bold">第二十五条</span> 乙方承诺如不能按时、足额还款付息，届时放弃抗辩权并愿意接受人民法院强制执行。</div>
				<div class="l2"><span class="bold">第二十六条</span> 本合同在履行过程中如发生纠纷，合同各方同意秉持自愿公平的原则进行协商解决，协商解决不成的，提交丙方所在地有管辖权的法院处理。</div>
				<div class="l2"><span class="bold">第二十七条</span> 本合同为电子合同，由丙方在电子平台发布，该发布是签订合同的邀约，经甲乙双方以会员账号登录丙方电子平台进行确认（电子签署）即后生效，电子签署的日期和时间，即是本合同成立和签订的时间，与纸质合同具同等法律效力。必要时，三方可打印本电子合同为纸质文件签字盖章，自己备查或者办理公证、银行事务等。本合同有效期直至乙方清偿全部借款本金、利息为止，如果发生违约金、赔偿金、维权费用，其有效期直至清偿违约金、赔偿金、维权费用为止。</div>
				<div class="l2"><span class="bold">第二十八条</span> 本合同及甲乙双方的电子签署记录以丙方电子平台的电子数据形式保存于丙方服务器中。甲乙双方可登录丙方电子平台查阅、打印本合同。</div>
				<div class="l2">本合同各方同意：该电子数据，是解决本合同争议的有效证据。如果法院、仲裁委、政府机关为解决争议需要该电子数据的纸质文档，则以丙方打印并加盖丙方行政公章的纸质文档为有效证据。</div>
				<div class="l2"><span class="bold">第二十九条</span> 若因公证、法律诉讼等原因需要本合同纸质文本打印、复印件时，该打印、复印件上需加盖丙方合同专用行政公章方有效。</div>
				<div class="l2"><span class="bold">第三十条</span> 本合同所载的“交易账户”为甲/乙方申请成为丙方会员后，在丙方电子平台设立开通的用于投资/借款及相关款项划转的账户，详细内容见甲/乙方各自与丙方签订的《会员协议》。</div>
				<div class="l2"><span class="bold">第三十一条</span> 本合同所载的“会员账号”为甲/乙双方成为丙方会员后丙方为其分配的会员代码同时也是其在丙方电子平台交易账户的账号，详见为甲/乙双方各自与丙方签订的《会员协议》。</div>
				<div class="l2"><span class="bold">第三十二条</span> 甲/乙双方各自与丙方签订的《会员协议》中涉及借款事宜的条款与本合同无冲突的，按《会员协议》的约定执行。</div>
				<div class="l2"><span class="bold">第三十三条</span> 陈述和保证：</div>
				<div class="l2"><span class="bold">甲乙双方陈述：</span>甲方通过丙方网站和电子平台以及丙方的当面或者电话、短信或者电子邮件的资信、征信介绍，认识了乙方，已经与乙方形成特定的信任关系和伙伴关系，甲乙双方不是互相不认识的不特定关系；</div>
				<div class="l2"><span class="bold">甲方陈述和保证：</span>甲方委托丙方提供投资咨询服务而向丙方指定的账户存入资金，其来源合法，该存款不属于丙方吸收甲方存款，也不属于丙方集资，也不属于委托丙方贷款，甲方知晓丙方的服务性质是居间撮合、受托中介投资。</div>
				<div class="l2"><span class="bold">乙方陈述和保证：</span>乙方知晓丙方是受托于甲方的投资咨询服务机构，丙方的服务性质是居间撮合、受托中介投融资。丙方因为受托于甲乙双方而向乙方划转借款，不属于丙方向乙方发放贷款。乙方保证严格按照合同规定使用借款，如有违约，自觉承担全部违约责任和经济损失。</div>
				<div class="l2"><span class="bold">丙方陈述和保证：</span>丙方拒绝向甲乙双方提供吸收存款获利、集资获利、高利贷、洗钱、非法套现的服务、便利、信息或者渠道。丙方的服务性质，是受甲乙双方委托，居间撮合民间借贷和现行国家政策法律法规许可的投融资咨询服务。丙方保证居间中立和公允。</div>
				<br/>
				<div class="bold">甲方会员编号：${contract.first_party_code}</div>
				<div class="bold">甲方身份证号：${contract.first_id}</div>
				<div class="bold">签署时间：<fmt:formatDate value="${contract.investor_make_sure}" pattern="yyyy年MM月dd日"/></div>
				<br/>
				<div class="bold">乙方会员编号：${contract.second_party_code}</div>
				<div class="bold">乙方营业执照号码：${contract.second_party_yyzz}</div>
				<div class="bold">乙方组织机构代码：${contract.second_party_zzjg}</div>
				<div class="bold">乙方身份证号：${second_party_idcard}</div>
				<div class="bold">签署时间：<fmt:formatDate value="${contract.financier_make_sure}" pattern="yyyy年MM月dd日"/></div>
				<br/>
				<div class="bold">丙方：昆投互联网金融交易</div>
				<div class="bold">法定代表人：XXX</div>
				<div class="bold">电子合同发布时间：<fmt:formatDate value="${contract.fbsj}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
				<c:if test="${user.userType=='T'&&record==null}">
					<!--  <div><input type="button" id="invest_sure" value="确认" /></div> -->
				</c:if>
		</div>
		
		<!-- <input type="button" value="abc" id="invest_sure"/> -->
		
	</body>
</html>
