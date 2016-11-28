<%@ page language="java" import="java.util.*,com.kmfex.*" pageEncoding="UTF-8"%>
<%@page import="com.kmfex.model.ContractKeyData"%>
<%@ include file="/common/taglib.jsp" %>
<html>
	<head>
		<title>${record.financingBase.shortName}-借款合同</title>
		<style>
			body{padding:0;margin:0;text-align:left;font-size:13px;overflow-x:hidden}
			table td{font-size:13px;}
			.key{color:#D72222;text-decoration: underline;}
			.head{font-weight: bold;}
		</style>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		<script>
	
		function doprint(){
			window.open("/back/investBaseAction!pdf?invest_record_id=${record.id}");
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
		<c:if test="${contract.id!=null}"><div style="position:absolute;top:0px;right:30px;"><input type="button" value="下载合同" onclick="doprint()"/></div></c:if>
		<div style="width:900px;overflow-x:hidden;margin-left:auto;margin-right:auto;margin-top:30xp;">
			<div>
				<h1 class="bold center">
					借款合同
					<input id="financingBaseId" type="hidden" value="${financingBaseId}" />
				</h1>
			</div>
			<div class="bold right">
				合同编号：${contract.contract_numbers}
			</div>
			<div class="bold">
				甲方（出借人）：${contract.first_party}
			</div>
			<div class="bold">
				乙方（借款人）：
				<c:if test="${contract.investor_make_sure!=null}">${contract.second_party}</c:if>
				<c:if test="${contract.investor_make_sure==null}">
					<c:if test="${fn:length(contract.second_party) >= 4}">******${fn:substring(contract.second_party,fn:length(contract.second_party)-4,fn:length(contract.second_party))}</c:if>
					<c:if test="${fn:length(contract.second_party) < 4}">${fn:substring(contract.second_party,0,1)}******</c:if>
				</c:if>
			</div>
			<div class="bold">丙方（投融资服务人）：昆投互联网金融交易</div>
			<div class="l2">鉴于，</div>
			<div class="l2">
				甲、乙双方是丙方的注册会员，甲方持有合法资金并自愿寻求投资对象，获取投资收益，乙方欲通过丙方之平台借款用于
				<span class="key">${contract.purpose}</span>，丙方是依法注册成立的投资咨询机构，具有规范、安全交易服务的完善机制，促进甲、乙双方投融资。为此，本合同甲、乙、丙三方根据我国《民法通则》、《合同法》、民间借贷政策法规、金融管理法规等相关法律法规，在自愿、平等、协商一致的基础上，签订本合同，以资共同遵守：
			</div>
			<div class="l2">
				<span class="bold">第一条</span> 甲方通过丙方向乙方提供借款人民币：
				<span class="key"><fmt:formatNumber value="${contract.loan_allah}" pattern="0.00" />
				</span>元（大写：
				<span class="key">${contract.loan_uppercase}</span>）。
			</div>
			<div class="l4">
				借款期限为
				<span class="bold">${loan_term_str}</span>，自
				<span class="key"><fmt:formatDate value="${contract.start_date}" pattern="yyyy年MM月dd日" />
				</span>起至
				<span class="key"><fmt:formatDate value="${contract.end_date}" pattern="yyyy年MM月dd日" />
				</span>。
			</div>
			<div class="l2">
				<span class="bold">第二条</span> 借款日利率为：
				<span class="key"><fmt:formatNumber value="${contract.interest_rate}" pattern="0.00" />%</span> 。利息总额为：
				<span class="key"><fmt:formatNumber value="${contract.interest_allah}" pattern="0.00" />
				</span>元（大写：
				<span class="key">${contract.interest_uppercase} </span>）。
			</div>
			<div class="l2">
				<span class="bold">第三条</span> 还款方式为到期一次还本付息。
			</div>
			<div class="l4">
				乙方应当于 <span class="key"><fmt:formatDate value="${contract.end_date}" pattern="yyyy年MM月dd日" /></span>前，按照本合同及丙方交易规则之规定，一次性清偿全部借款本息。
			</div>
			<div class="l4">
				若还款日逢国家法定节假日的，则相应还款日顺延至该节假日后的第一个工作日；乙方应付利息不变。
			</div>
			<div class="l2">
				<span class="bold">第四条</span> 乙方借款的具体用途为：
				<span class="key">${contract.purpose}</span>。乙方应按约定用途使用借款，不得将借款挪作他用或者用于违法用途。
			</div>
			<div class="l2">
				<span class="bold">第五条</span> 甲方同意：丙方从其为甲方设立的会员交易账户中划转本合同约定的借款金额作为向乙方支付的借款。
			</div>
			<div class="l2">
				<span class="bold">第六条</span> 丙方将款项划转至乙方交易账户，该款项到达乙方交易账户之日为实际交付借款日。
			</div>
			<div class="l2">
				<span class="bold">第七条</span> 甲方授权丙方向乙方收取归还的本金及利息。丙方收到乙方归还的本金及利息后应于两个工作日内将相应款项拨付至甲方交易账户。
			</div>
			<div class="l2">
				<span class="bold">第八条</span> 乙方授权丙方办理针对乙方指定账户的委托银行扣款相关事宜，该委托扣款用于乙方归还本金、支付利息、逾期利息、违约金、赔偿款以及实现债权所产生的所有费用。
			</div>
			<div class="l2">
				<span class="bold">第九条</span> 因本合同第六、七、八、九条约定的款项划转，依据税法规定，如果产生所得税、营业税、个人所得税等纳税义务，则甲方、乙方应当各自依法纳税。丙方不承担甲乙双方投融资交易发生的税款；丙方并不在甲乙双方投融资交易中赚取差价。
			</div>
			<div class="l2">
				<span class="bold">第十条</span> 借款交付后，丙方有权对乙方借款的使用进行管理与监控。
			</div>
			<div class="l2">
				<span class="bold">第十一条</span> 甲方授权丙方对乙方履行本合同的情况进行管理与监控并在乙方发生违约时向乙方追索违约金及相应损失。
			</div>
			<div class="l2">
				<span class="bold">第十二条</span> 关于借款合同之从合同担保合同事宜的约定：
			</div>
			<div class="l4">
				一、甲方授权丙方代表出借人，以债权人（出借人）的名义，与担保人，签订担保合同（包括保证合同、抵押合同、质押合同以及其它具有担保性质的合同），甲方授权丙方签署与担保有关的法律文件和办理抵押登记等与担保有关的法律手续。丙方在代表债权人（出借人）签订担保合同和签署与担保有关的其它法律文件时，在担保合同尾部“债权人”（出借人）以及与担保有关的其它法律文件之签章处，加盖债权人之代理人丙方的公章而且该签章与债权人本人之签章具有同等法律效力，债权人承担因签章所产生的法律责任，并且债权人承诺在任何情况下不得向代理人昆投互联网金融交易提出任何形式的主张。若发生债务人和担保人不履行约定还款义务的，丙方不承担任何法律责任。
			</div>
			<div class="l4">
				二、甲方授权丙方监督债务人和担保人按照各方签署的相关合同履行具体的合同义务，当乙方（借款人）未能按本合同约定足额归还本金或利息或者出现担保人不履行担保责任的情况，丙方代替债权人向乙方或者担保人行使合同约定权利。
			</div>
			<div class="l4">
				三、丙方为甲方办理担保事宜过程中，应尽到勤勉义务，甲方不再要求丙方承担其它任何责任或履行其他义务。甲方应当就代理人办理代理事项支付合理的代理报酬，并承担代理事务发生的开支。代理报酬和开支可以直接从债务人或者担保人支付的款项中扣除。
			</div>
			<div class="l2">
				<span class="bold">第十三条</span> 乙方应根据丙方的要求及时提供相应的文件及资料，并保证所提供资料、文件的真实性与合法性。
			</div>
			<div class="l2">
				<span class="bold">第十四条</span> 丙方有权对乙方提交的资料、文件的合法性、真实性进行调查；对乙方的资信、资产状况进行调查。
			</div>
			<div class="l2">
				<span class="bold">第十五条</span> 乙方法定代表人更换、改变住所或经营场所以及减少注册资金时，应事先<span class="key">15日</span>书面通知丙方；乙方因实行承包、租赁、联营、股份制改造、分立、被兼并（合并）、对外投资及其他原因而改变经营管理方式或产权组织形式时，应提前<span class="key">10日</span>通知丙方，并向丙方书面报告债务情况或还款措施；乙方居住地、联系方式、单位的变迁、资产情况、负债情况等发生变更或者涉及诉讼、仲裁的，应当在变更或者诉讼仲裁发生后三日内书面通知丙方。
			</div>
			<div class="l2">
				<span class="bold">第十六条</span> 乙方须服从丙方对其使用借款资金情况、实际用途和有关生产经营、财务活动等借后活动进行的管理与风险监控。
			</div>
			<div class="l2">
				<span class="bold">第十七条</span> 甲方可通过丙方平台将本合同项下的债权转让予丙方平台的其他投资人会员，无须事前或事后告知乙方，也无须征得乙方同意。此种转让在丙方进行登记备案即可。丙方认为需要将该转让告知乙方时，丙方无需甲方再次授权而向乙方进行告知。
			</div>
			<div class="l2">
				<span class="bold">第十八条</span> 本合同履行期间，出现下列情况任意一项或多项时，甲方授权丙方有权要求乙方提前归还本金，支付利息、逾期利息、违约金和赔偿金等。乙方授权丙方其执行甲方的要求。且甲、乙双方同意丙方在乙方出现以下违约行为时有权单方解除本合同：
			</div>
			<div class="l4">
				一、乙方向丙方提供虚假证明材料；
			</div>
			<div class="l4">
				二、乙方违反合同关于借款用途的相关法律规定的；
			</div>
			<div class="l4">
				三、乙方拒绝丙方对借款使用情况、实际用途进行监督检查的；
			</div>
			<div class="l4">
				四、乙方拒绝纠正或者其实际行为表明不纠正违反本合同的行为的；
			</div>
			<div class="l4">
				五、乙方负有数额较大的债务或可能涉及重大的诉讼或仲裁程序及其他法律纠纷，足以影响其偿债能力的；
			</div>
			<div class="l4">
				六、乙方不配合丙方对其经营的公司进行巡检与管理的；
			</div>
			<div class="l4">
				七、乙方任何一期借款未按时或足额还款的；
			</div>
			<div class="l4">
				八、乙方其他任何一笔借款（即其他任何负债）未按时、足额还款（含一期）的；
			</div>
			<div class="l4">
				九、乙方经营的公司出现经营恶化、面临被查封、停业、行业整顿或被处置资产等情形；
			</div>
			<div class="l4">
				十、乙方在本合同项下的借款本息全部清偿前，未经丙方书面同意向第三人提供担保的（因本合同乙方的担保人要求乙方提供反担保，且反担保协议经甲方同意的情形除外）；
			</div>
			<div class="l4">
				十一、乙方发生其他足以影响其偿还债务能力事件的。
			</div>
			<div class="l2">
				<span class="bold">第十九条</span> 一旦发生上述任何一条违约事件，甲方有权采取以下任何一项或多项措施：
			</div>
			<div class="l4">
				一、乙方出现本合同第十九条约定的情形或违反本合同约定的，最终导致丙方解除本合同、提前收回全部借款本息时，除结清全部借款本金及利息（计算至结清日当天）外，乙方应另行向甲方支付借款金额的5%作为违约金，若违约金不足以偿付甲方、丙方因索赔而产生的各项费用时，甲方和丙方都有权继续向乙方索赔。
			</div>
			<div class="l4">
				二、乙方未按本合同约定按时、足额偿还借款本息的，自逾期之日起，每逾期一天应向甲方，按应还款金额的<span class="key">${zhinajinbili}%</span>/日的标准支付滞纳金。
			</div>
			<div class="l4">
				三、在本合同项下的借款本息全部清偿前，乙方的联系方式、实际居住地、工作单位、资产、负债等情况发生变化，未在变更后三日内书面通知丙方的，上述行为每发生一次，乙方应向甲方支付500元/项（次）的违约金，可由丙方代收。
			</div>
			<div class="l4">
				四、在本合同履行期间，乙方拒不履行还款义务或无力清偿欠款，而最终导致甲方付诸法律的，除承担全部费用（包括但不限于律师代理费、鉴定费、公告费、评估费、差旅费及其他行使债权所产生的费用）外，乙方还应向甲方支付借款总额的<span class="key">5%</span>作为违约金。
			</div>
			<div class="l2">
				<span class="bold">第二十条</span> 丙方有权根据本合同第十九条的约定行使甲方授权的相关权利，在本合同履行过程中，丙方仅履行代收、代转已经从乙方收到款项的责任，在乙方以及担保人不能如约履行还款义务时，丙方不承担向甲方支付、赔付、垫付任何款项等的责任。
			</div>
			<div class="l2">
				<span class="bold">第二十一条</span> 若甲方要求丙方向债务人追收欠款的，甲方需预付并承担追债费用。
			</div>
			<div class="l2">
				<span class="bold">第二十二条</span> 本合同履行期间，乙方提前偿还借款的，应向丙方提出书面申请。乙方仍应按照本合同第二条之约定支付相应的利息。
			</div>
			<div class="l2">
				<span class="bold">第二十三条</span> 本合同履行期间，如果乙方公司转让、变更等必须征得丙方的书面同意，否则，丙方有权要求乙方承担本合同项下的违约责任，丙方有权直接通知甲方及其他全部出借人解除与乙方的借款合同，提前收回全部借款本息，同时乙方向甲方及丙方各支付人民币10000元的违约金。
			</div>
			<div class="l2">
				<span class="bold">第二十四条</span> 乙方同意丙方将借款金额的<span class="key"><fmt:formatNumber value="${bzj_bl}" pattern="0.00"/>%</span>冻结在乙方交易账户中作为本合同履约保证金。乙方按时足额归还本息后，丙方将归还履约保证金。乙方不按时足额归还本金支付利息的，丙方不再归还履约保证金。
			</div>
			<div class="l2">
				<span class="bold">第二十五条</span> 乙方承诺如不能按时、足额还款付息，届时放弃抗辩权并愿意接受人民法院强制执行。
			</div>
			<div class="l2">
				<span class="bold">第二十六条</span> 本合同在履行过程中如发生纠纷，合同各方同意秉持自愿公平的原则进行协商解决，协商解决不成的，提交丙方所在地有管辖权的法院处理。
			</div>
			<div class="l2">
				<span class="bold">第二十七条</span> 本合同为电子合同，由丙方在电子平台发布，经甲乙双方以会员账号登录丙方电子平台进行确认（电子签署）即后生效，电子签署的日期，即是本合同成立和签订的时间，与纸质合同具同等法律效力。必要时，三方可打印本电子合同为纸质文件签字盖章，自己备查或者办理公证、银行事务等。本合同有效期直至乙方清偿全部借款本金、利息为止，如果发生违约金、赔偿金、维权费用，其有效期直至清偿违约金、赔偿金以及实现债权所产生的费用为止。
			</div>
			<div class="l2">
				<span class="bold">第二十八条</span> 本合同及甲乙双方的电子签署记录以电子数据形式保存于丙方服务器中。甲乙双方可登录丙方电子平台查阅或打印本合同。本合同各方同意：该电子数据，是解决本合同争议的有效证据。如果法院、仲裁委、政府机关为解决争议需要该电子数据的纸质文档，则以丙方打印并加盖丙方行政公章的纸质文档为有效证据。
			</div>
			<div class="l2">
				<span class="bold">第二十九条</span> 若因公证、法律诉讼等原因需要本合同纸质文本打印、复印件时，该打印、复印件上需加盖丙方合同专用行政公章方有效。
			</div>
			<div class="l2">
				<span class="bold">第三十条</span> 本合同所载的“交易账户”为甲和乙方申请成为丙方会员后，在丙方电子平台设立开通的用于投资或借款及相关款项划转的账户，详细内容见甲、乙方各自与丙方签订的《会员协议》。
			</div>
			<div class="l2">
				<span class="bold">第三十一条</span> 本合同所载的“会员账号”为甲、乙双方成为丙方会员后丙方为其分配的会员代码同时也是其在丙方电子平台交易账户的账号，详见为甲、乙双方各自与丙方签订的《会员协议》。
			</div>
			<div class="l2">
				<span class="bold">第三十二条</span> 甲、乙双方各自与丙方签订的《会员协议》中涉及借款事宜的条款与本合同有冲突的，按《会员协议》的约定执行。
			</div>
			<div class="l2">
				<span class="bold">第三十三条</span> 陈述和保证：
			</div>
			<div class="l4">
				<span class="bold">甲乙双方陈述：</span>甲方通过丙方网站或电子平台以及丙方的当面或者电话、短信或者电子邮件等的资信、征信介绍，认识了乙方，已经与乙方形成特定的信任关系和伙伴关系，甲乙双方不是互相不认识的不特定关系；
			</div>
			<div class="l4">
				<span class="bold">甲方陈述和保证：</span>甲方委托丙方提供投资咨询服务而向丙方指定的账户存入资金，其来源合法，该存款不属于丙方吸收甲方存款，也不属于丙方集资，也不属于委托丙方贷款，甲方知晓丙方的服务性质是居间撮合、受托中介投资。
			</div>
			<div class="l4">
				<span class="bold">乙方陈述和保证：</span>乙方知晓丙方是受托于甲方的投资咨询服务机构，丙方的服务性质是咨询投融资项目。根据甲、乙双方委托丙方代甲、乙方划转借款，不属于丙方向乙方发放借款款项。乙方保证严格按照合同规定使用借款，如有违约，自愿及时自行承担全部违约责任和赔偿经济损失。
			</div>
			<div class="l4">
				<span class="bold">丙方陈述和保证：</span>丙方拒绝向甲乙双方提供吸收存款获利、集资获利、高利贷、洗钱、非法套现的服务、便利、信息或者渠道。丙方的服务性质，是受甲、乙双方委托，依照现行国家政策法律法规提供投融资咨询服务。
			</div>
			<div class="l2">
				<span class="bold">第三十四条</span> 本合同自签署之日起生效。合同的附件与本合同具有相同的法律效力。
			</div>



			<br />
			<div class="bold">
				甲方会员编号：${contract.first_party_code}
			</div>
			<div class="bold">
				甲方身份证号：${contract.first_id}
			</div>
			<div class="bold">
				签署时间：
				<fmt:formatDate value="${contract.investor_make_sure}" pattern="yyyy年MM月dd日" />
			</div>
			<br />
			<div class="bold">
				乙方会员编号：${contract.second_party_code}
			</div>
			<div class="bold">
				乙方营业执照号码：${contract.second_party_yyzz}
			</div>
			<div class="bold">
				乙方组织机构代码：${contract.second_party_zzjg}
			</div>
			<div class="bold">
				签署时间：
				<fmt:formatDate value="${contract.financier_make_sure}" pattern="yyyy年MM月dd日" />
			</div>
			<br />
			<div class="bold">
				丙方：昆投互联网金融交易
			</div>
			<div class="bold">
				法定代表人：XXX
			</div>
			<div class="bold">
				电子合同发布时间：
				<fmt:formatDate value="${contract.fbsj}" pattern="yyyy-MM-dd HH:mm:ss" />
			</div>

		</div>
		
		
	</body>
</html>
