<%@ page language="java" import="java.util.*,com.kmfex.*" pageEncoding="UTF-8"%>
<%@page import="com.kmfex.model.ContractKeyData"%>
<%@ include file="/common/taglib.jsp" %>
<html>
	<head>
		<title>${record.financingBase.shortName}-借款合同</title> 
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script>
			$(function(){
				
				$('#download_link').click(function(){
					var html = $("html")[0].outerHTML;
					html = encodeURI(html);
					$("#download_form_html").val(html);
					$("#download_form").submit();
				});
			})
		</script> 
		<style>
			 body{padding:0;margin:0;text-align:left;font-size:13px;overflow-x:hidden}
			table td{font-size:14px;}
			.key{color:#D72222;text-decoration: underline;}
			.head{font-weight: bold;}
			div{line-height:30px;}
			.bold{font-weight:bold;height:30px;}
			.right{text-align:right}
			.center{text-align:center;}
			.l2{text-indent:28px;line-height:30px;height:30px;}
			.l4{text-indent:56px;line-height:30px;height:30px;}
			#main_table{margin:0 auto;max-width:760px;}
		</style>
		
	</head>
	<body> 
		<form action="/back/investBaseAction!pdf" id="download_form" method="post">
			<input type="hidden" name="num" value="${contract.contract_numbers}"/>
			<input type="hidden" name="html" id="download_form_html"/> 
		</form>
		<c:if test="${contract.id!=null}"><div style="position:fixed;top:0px;right:30px;"><a href="#" id="download_link">下载合同</a></div></c:if>	 
          <table width="100%" id="main_table">
            <tr>
				<td class="bold center"><h2>借款合同
					<input id="financingBaseId" type="hidden" value="${financingBaseId}" /></h2></td>
			</tr> 
			<tr>
				<td class="bold right">合同编号：${contract.contract_numbers}</td>
			</tr>
			<tr>
				<td class="bold">甲方（出借人）：${contract.first_party}</td>
			</tr> 
			<tr>
				<td class="bold">乙方（借款人）：
				<c:if test="${contract.investor_make_sure!=null}">${contract.second_party}</c:if>
				<c:if test="${contract.investor_make_sure==null}">
					<c:if test="${fn:length(contract.second_party) >= 4}">******${fn:substring(contract.second_party,fn:length(contract.second_party)-4,fn:length(contract.second_party))}</c:if>
					<c:if test="${fn:length(contract.second_party) < 4}">${fn:substring(contract.second_party,0,1)}******</c:if>
				</c:if>
			</td>
			</tr>
			<tr>
				<td class="bold">丙方（投融资服务人）：昆投互联网金融交易</td>
			</tr>
			<tr><td class="l2">鉴于，</td></tr>
			<tr><td class="l2">
				甲、乙双方是丙方的注册会员，甲方持有合法资金并自愿寻求投资对象，获取投资收益，乙方欲通过丙方之平台借款用于
				<span class="key">${contract.purpose}</span>，丙方是依法注册成立的投资咨询机构，具有规范、安全交易服务的完善机制，促进甲、乙双方投融资。为此，本合同甲、乙、丙三方根据我国《民法通则》、《合同法》、民间借贷政策法规、金融管理法规等相关法律法规，在自愿、平等、协商一致的基础上，签订本合同，以资共同遵守：
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第一条</span> 甲方通过丙方向乙方提供借款人民币：
				<span class="key"><fmt:formatNumber value="${contract.loan_allah}" pattern="0.00" />
				</span>元（大写：
				<span class="key">${contract.loan_uppercase}</span>）。
			</td></tr>
			<tr><td class="l4">
				借款期限为
				<span class="bold">${loan_term_str}</span>，自
				<span class="key"><fmt:formatDate value="${contract.start_date}" pattern="yyyy年MM月dd日" />
				</span>起至
				<span class="key"><fmt:formatDate value="${contract.end_date}" pattern="yyyy年MM月dd日" />
				</span>。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二条</span> 借款日利率为：
				<span class="key"><fmt:formatNumber value="${contract.interest_rate}" pattern="0.00" />‰<span style="font-size:10px;margin-bottom:-10px;">o</span></span> 。利息总额为：
				<span class="key"><fmt:formatNumber value="${contract.interest_allah}" pattern="0.00" />
				</span>元（大写：
				<span class="key">${contract.interest_uppercase} </span>）。
			</td></tr>
			<tr>
				<td class="l2">本款约定之利息自借款人（即乙方）按照丙方之交易规则及约定，进行电子签约之日起计算，借款期限的起始日以实际签约日的日期为准，以此计算借款期限的到期日期。</td>
			</tr>
			<tr><td class="l2">
				<span class="bold">第三条</span> 还款方式为到期一次还本付息。
			</td></tr>
			<tr><td class="l4">
				乙方应当于 <span class="key"><fmt:formatDate value="${contract.end_date}" pattern="yyyy年MM月dd日" />  11：30 </span>前，按照本合同及丙方交易规则之规定，一次性清偿全部借款本息。
			</td></tr>
			<tr><td class="l2">
				若本条约定之还款日逢国家法定节假日，则相应还款日应提前至该节假日前的最后一个交易日。若当月无该日期的，相应还款日提前至该日期前的最后一个交易日。
			</td></tr>
			<tr class="l2">
				<td>本条约定之交易日，是指丙方交易平台提供相关投融资中介服务业务的时间，一般为9：00（am）-17:00（pm），特殊时间以交易平台的具体通知为准。</td>
			</tr>
			<tr><td class="l2">
				<span class="bold">第四条</span> 乙方借款的具体用途为：
				<span class="key">${contract.purpose}</span>。乙方应按约定用途使用借款，不得将借款挪作他用或者用于违法用途。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第五条</span> 甲方同意：丙方从其为甲方设立的会员交易账户中划转本合同约定的借款金额作为向乙方支付的借款。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第六条</span> 丙方应将甲方支付之本合同约定借款，按照交易平台之交易规则，支付至乙方开设的融资方账户内。
			</td></tr>
			<tr>
				<td class="l2">借款金额到达乙方交易账户后，乙方可向丙方申请提现，丙方应根据平台之交易规则及本合同之约定，完成相关款项冻结及提现事宜。</td>
			</tr>
			<tr>
				<td class="l2"><span class="bold">第七条</span> 甲方授权丙方向乙方收取其归还的本金及利息以及约定的其他违约金等。丙方收到乙方归还的上述款项后应于三个交易日内将相应款项经结算后拨付至甲方对应的交易账户。</td>
			</tr>
			<tr>
				<td class="l2">鉴于丙方结算时间的考虑，若乙方还款时间为节假日前的最后一个交易日，甲方同意并容忍丙方结算时间顺延至节假日后的第一个交易日，丙方应在顺延后的三个交易日内完成。</td>
			</tr>
			<tr><td class="l2">
				<span class="bold">第八条</span> 乙方授权丙方办理针对乙方指定账户的委托银行扣款相关事宜（包括但不限于对其交易账户及交易账户对应之银行账户的委托扣款），该委托扣款用于乙方归还本金、支付利息、逾期利息、违约金、赔偿款（视具体情况而定）。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第九条</span> 因本合同第六、七、八、九条约定的款项划转，依据税法规定，如果产生所得税、营业税、个人所得税等纳税义务，则甲方、乙方应当各自依法纳税。丙方不承担甲乙双方投融资交易发生的税款；丙方并不在甲乙双方投融资交易中赚取差价。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第十条</span> 借款交付后，丙方有权对乙方借款的使用进行管理与监控。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第十一条</span> 甲方授权丙方对乙方履行本合同的情况进行管理与监控并在乙方发生违约时向乙方追索违约金及相应损失。
			</td></tr>
			<tr>
				<td class="l2"><span class="bold">第十二条</span> 关于借款合同之从合同担保合同事宜的约定：</td>
			</tr>
			<tr>
				<td class="l2">一、甲方授权丙方代表出借人，以债权人（出借人）的名义，与借款人委托的担保人，签订担保合同，包括但不限于保证合同、抵押合同、质押合同以及其它具有担保性质的合同。</td>
			</tr>
			<tr class="l2">
				<td>鉴于实际操作原因，甲方授权丙方签署与担保有关的其它法律文件，并办理相关抵押登记等与担保有关的法律手续和相关事宜；丙方于相应担保合同及相关法律文件中权利人处加盖印鉴即对甲方发生法律效力，该签章产生的所有权利义务全部由债权人享有和承担，同时，甲方承诺不就该委托事项及委托事项所产生的法律效果，向丙方提出任何索赔请求及要求丙方承担任何责任。</td>
			</tr>
			<tr class="l2">
				<td>丙方对债务人和担保人不履行付款及/或代偿义务，就本合同及担保合同不承担任何除中介服务之外的法律责任。</td>
			</tr>
			<tr>
				<td class="l2">二、甲方授权丙方督促债务人和担保人履行融资项目有关的全部合同，当乙方（借款人）未能按本合同约定足额归还本金或利息时，或者担保人不履行义务时，丙方有权代表债权人向乙方或者担保人进行追索，并签订借款合同或者担保合同的补充协议。</td>
			</tr>
			<tr>
				<td class="l2">三、丙方在代理甲方办理相关担保及追索事宜过程中，只要做到对甲方忠诚和尽力即可，甲方不应再对代理人附加其它任何责任和义务。并且，甲方应当就代理人办理代理事项支付合理的代理报酬，并承担代理事务发生的所有开支。代理报酬和开支丙方有权直接从债务人或者担保人支付的款项中予以扣除。</td>
			</tr>
			<tr>
				<td class="l2"><span class="bold">第十三条</span> 乙方应根据丙方的要求提供相应的文件及资料，并保证所提供资料、文件的真实性、完整性与合法性。</td>
			</tr>
			<tr>
				<td class="l2"><span class="bold">第十四条</span> 丙方有权对乙方提交的资料、文件的合法性、完整性、真实性进行调查；对乙方的资信、资产状况进行调查，乙方应当提供所有必要的容忍和协助，以保证丙方的调查全面、详细且真实。</td>
			</tr>
			<tr><td class="l2">
				<span class="bold">第十五条</span> 乙方法定代表人更换、改变住所或经营场所以及减少注册资金时，应事先书面通知丙方；乙方因实行承包、租赁、联营、股份制改造、分立、被兼并（合并）、对外投资及其他原因而改变经营管理方式或产权组织形式时，应提前通知丙方，并落实债务和还款措施；其居住地、联系方式、单位的变迁、资产情况、负债情况等发生变更或者涉及诉讼、仲裁，必须在变更或者诉讼仲裁发生后三日内书面通知丙方。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第十六条</span> 乙方须服从丙方对其借款资金的使用情况、实际用途和有关生产经营、财务活动等借后活动进行的必要的管理与风险监控。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第十七条</span> 甲方有权通过丙方，将本合同项下的债权向在丙方正式注册的其他投资人会员进行转让，而无须征得乙方同意。此种转让在丙方进行登记备案即可生效。丙方将相关债权转让的事宜，通过其交易平台予以公布（包括但不限于在通知栏、融资信息发布栏等版块内公布），即视为甲方向乙方履行了通知义务，该通知丙方可以无须甲方再次授权而为之，相应法律效果由甲方、乙方承担。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第十八条</span> 本合同履行期间，出现下列情况任意一项或多项时，基于保护甲方合法权益之考虑，丙方有权代表甲方要求乙方提前归还本金，支付利息、逾期利息、违约金和赔偿金。而乙方授权丙方并同意丙方执行甲方的要求。且甲乙双方同意丙方有权选择解除本合同：
			</td></tr>
			<tr><td class="l4">
				一、乙方向丙方提供虚假证明材料；
			</td></tr>
			<tr><td class="l4">
				二、乙方违反合同关于借款用途的相关法律规定的；
			</td></tr>
			<tr><td class="l4">
				三、乙方拒绝丙方对借款使用情况、实际用途进行监督检查的；
			</td></tr>
			<tr><td class="l4">
				四、乙方拒绝纠正或者其实际行为表明不纠正违反本合同的行为的；
			</td></tr>
			<tr><td class="l4">
				五、乙方负有数额较大的债务或可能涉及重大的诉讼或仲裁程序及其他法律纠纷，足以影响其偿债能力的；
			</td></tr>
			<tr><td class="l4">
				六、乙方不配合丙方对其经营的公司进行巡检与管理的；
			</td></tr>
			<tr><td class="l4">
				七、　　针对本合同约定借款，乙方累计出现需要保证机构代偿款项的逾期还款违约行为的；
　　前述逾期还款不应当包括乙方在保证人代偿期间，清偿当期应还款额的情况。
保证人代偿期间，是指保证人签订的《保证合同》中约定的，保证人应当代为清偿被担保债务的期间，分为3日、7日、15日不等，具体以《保证合同》约定为准。
			</td></tr>
			<tr><td class="l4">
				八、乙方经营的公司出现经营恶化、面临被查封、停业、行业整顿或被处置资产等情形；
			</td></tr>
			<tr><td class="l4">
				九、乙方在本合同项下的借款本息全部清偿前，未经丙方书面同意向第三人提供担保的（因本合同乙方的担保人要求乙方提供反担保，且反担保协议经甲方同意的情形除外）；
			</td></tr>
			<tr><td class="l4">
				十、乙方发生其他足以影响其偿还债务能力事件的。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第十九条</span> 乙方违反本合同第十八条之约定或发生下列任何一条违约事件，甲方有权采取以下任何一项或多项措施：
			</td></tr>
			<tr><td class="l4">
				一、乙方出现本合同第十九条约定的情形或违反本合同约定的，最终导致丙方解除本合同、提前收回全部借款本息时，除结清全部借款本金及利息（计算至结清日当天）外，乙方应另行向甲方支付借款金额的5%作为违约金，若违约金不足以偿付甲方、丙方因索赔而产生的各项费用时，甲方和丙方都有权继续向乙方索赔。
			</td></tr>
			<tr><td class="l4">
				二、乙方未按本合同约定按时、足额偿还借款本息的，自逾期之日起应向甲方每日按应还款金额的
				<span class="key">${contract.contractTemplate.zhinajinbili}</span>支付滞纳金。
			</td></tr>
			<tr><td class="l4">
				三、在本合同项下的借款本息全部清偿前，乙方的联系方式、实际居住地、工作单位、资产、负债等情况发生变化，未在变更后三日内书面通知丙方的，上述行为每发生一次，乙方应向丙方支付500元/项（次）的违约金。
			</td></tr>
			<tr><td class="l4">
				四、在本合同履行期间，乙方拒不履行还款义务或无力清偿欠款，而最终导致甲方付诸法律的，乙方应承担由此所产生的全部费用，包括但不限于律师代理费、评估费、鉴定费、差旅费等，同时有权按照本条第二款之约定主张违约金。
			</td></tr>
			<tr>
				<td class="l2"> 尽管甲乙双方在第十八条授权丙方，以及丙方根据该授权行使权利，但是，丙方没有向甲方支付、赔付、垫付任何款项的经济责任，丙方只有代收、代转已经从乙方收到的债款的义务和经济责任。甲方要求丙方向债务人追收欠款的，甲方需预付追债费用。</td>
			</tr>
			<tr><td class="l2">
				<span class="bold">第二十条</span> 本合同履行期间，乙方提前偿还借款的，应向丙方提出书面申请。丙方同意提前还款的，乙方除按实际用款期间支付利息外，还应另向丙方支付 _天的利息作为违约金。丙方收到此违约金后全额划转至甲方交易账户。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十一条</span> 本合同履行期间，如果乙方公司发生股权转让、变更等情况的，必须征得丙方的书面同意，未经丙方书面同意，乙方不得签署或实施任何协议或行为，由此导致的全部责任由乙方自行承担。丙方有权直接通知甲方及其他全部出借人解除与乙方的借款合同、提前收回全部借款本息，同时乙方向甲方及丙方各支付人民币<span class="key">10000</span>元的违约金。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十二条</span> 乙方同意丙方将借款金额的<span class="key"><fmt:formatNumber value="${bzj_bl}" pattern="0.00"/>%</span>冻结在乙方交易账户中作为履约保证金。乙方按时足额归还本息后，丙方将归还履约保证金。乙方不按时足额归还本金支付利息的，丙方不再归还履约保证金。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十三条</span> 乙方承诺如不能按时、足额还款付息，届时放弃抗辩权并愿意接受人民法院强制执行。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十四条</span> 本合同在履行过程中如发生纠纷，合同各方同意秉持自愿公平的原则进行协商解决，协商解决不成的，提交丙方所在地有管辖权的法院处理。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十五条</span> 本合同为电子合同，由丙方在电子平台发布，经甲乙双方以会员账号登录丙方电子平台进行确认（电子签署）即后生效，电子签署的日期，即是本合同成立和签订的时间，与纸质合同具同等法律效力。必要时，三方可打印本电子合同为纸质文件签字盖章，自己备查或者办理公证、银行事务等。本合同有效期直至乙方清偿全部借款本金、利息为止，如果发生违约金、赔偿金、维权费用，其有效期直至清偿违约金、赔偿金以及实现债权所产生的费用为止。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十六条</span> 本合同及甲乙双方的电子签署记录以电子数据形式保存于丙方服务器中。甲乙双方可登录丙方电子平台查阅或打印本合同。本合同各方同意：该电子数据，是解决本合同争议的有效证据。如果法院、仲裁委、政府机关为解决争议需要该电子数据的纸质文档，则以丙方打印并加盖丙方行政公章的纸质文档为有效证据。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十七条</span> 若因公证、法律诉讼等原因需要本合同纸质文本打印、复印件时，该打印、复印件上需加盖丙方合同专用行政公章方有效。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十八条</span> 本合同所载的“交易账户”为甲和乙方申请成为丙方会员后，在丙方电子平台设立开通的用于投资或借款及相关款项划转的账户，详细内容见甲、乙方各自与丙方签订的《会员协议》。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第二十九条</span> 本合同所载的“会员账号”为甲、乙双方成为丙方会员后丙方为其分配的会员代码同时也是其在丙方电子平台交易账户的账号，详见为甲、乙双方各自与丙方签订的《会员协议》。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第三十条</span> 甲、乙双方各自与丙方签订的《会员协议》中涉及借款事宜的条款与本合同有冲突的，按《会员协议》的约定执行。
			</td></tr>
			<tr><td class="l2">
				<span class="bold">第三十一条</span> 陈述和保证：
			</td></tr>
			<tr><td class="l4">
				<span class="bold">甲乙双方陈述：</span>甲方通过丙方网站或电子平台以及丙方的当面或者电话、短信或者电子邮件等的资信、征信介绍，认识了乙方，已经与乙方形成特定的信任关系和伙伴关系，甲乙双方不是互相不认识的不特定关系；
			</td></tr>
			<tr><td class="l4">
				<span class="bold">甲方陈述和保证：</span>甲方委托丙方提供投资咨询服务而向丙方指定的账户存入资金，其来源合法，该存款不属于丙方吸收甲方存款，也不属于丙方集资，也不属于委托丙方贷款，甲方知晓丙方的服务性质是居间撮合、受托中介投资。
			</td></tr>
			<tr><td class="l4">
				<span class="bold">乙方陈述和保证：</span>乙方知晓丙方是受托于甲方的投资咨询服务机构，丙方的服务性质是咨询投融资项目。根据甲、乙双方委托丙方代甲、乙方划转借款，不属于丙方向乙方发放借款款项。乙方保证严格按照合同规定使用借款，如有违约，自愿及时自行承担全部违约责任和赔偿经济损失。
			</td></tr>
			<tr><td class="l4">
				<span class="bold">丙方陈述和保证：</span>丙方拒绝向甲乙双方提供吸收存款获利、集资获利、高利贷、洗钱、非法套现的服务、便利、信息或者渠道。丙方的服务性质，是受甲、乙双方委托，依照现行国家政策法律法规提供投融资咨询服务。
			</td></tr> 
			<tr><td class="bold">
				甲方会员编号：${contract.first_party_code}
			</td></tr>
			<tr><td class="bold">
				甲方身份证号：${contract.first_id}
			</td></tr>
			<tr><td class="bold">
				签署时间：
				<fmt:formatDate value="${contract.investor_make_sure}" pattern="yyyy年MM月dd日" />
			</td></tr> 
			<tr><td class="bold">
				乙方会员编号：${contract.second_party_code}
			</td></tr>
			<tr><td class="bold">
				乙方营业执照号码：${contract.second_party_yyzz}
			</td></tr>
			<tr><td class="bold">
				乙方组织机构代码：${contract.second_party_zzjg}
			</td></tr>
			<tr><td class="bold">
				签署时间：
				<fmt:formatDate value="${contract.financier_make_sure}" pattern="yyyy年MM月dd日" />
			</td></tr> 
			<tr><td class="bold">
				丙方：昆投互联网金融交易
			</td></tr>
			<tr><td class="bold">
				法定代表人：XXX
			</td></tr>
			<tr><td class="bold">
				电子合同发布时间：
				<fmt:formatDate value="${contract.fbsj}" pattern="yyyy-MM-dd HH:mm:ss" />
			</td></tr> 
		</table>
		
	</body>
</html>
