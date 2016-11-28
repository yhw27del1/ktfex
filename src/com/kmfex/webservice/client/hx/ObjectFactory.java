package com.kmfex.webservice.client.hx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.kmfex.webservice.client.hx package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _FailedLiquidationEntryType_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "type");
	private final static QName _FailedLiquidationEntryAccountNo_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "accountNo");
	private final static QName _FailedLiquidationEntryRemark_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "remark");
	private final static QName _FailedLiquidationEntryFlag_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "flag");
	private final static QName _FailedLiquidationEntryMerAccountNo_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "merAccountNo");
	private final static QName _XMLDocumentFirstTagName_QNAME = new QName(
			"http://util.B2B.hitrust.com", "firstTagName");
	private final static QName _ResponseDZ022Version_QNAME = new QName(
			"http://response.B2B.hitrust.com", "version");
	private final static QName _ResponseDZ022Signature_QNAME = new QName(
			"http://response.B2B.hitrust.com", "signature");
	private final static QName _ResponseDZ022MerchantTrnxNo_QNAME = new QName(
			"http://response.B2B.hitrust.com", "merchantTrnxNo");
	private final static QName _ResponseDZ022Language_QNAME = new QName(
			"http://response.B2B.hitrust.com", "language");
	private final static QName _ResponseDZ022Code_QNAME = new QName(
			"http://response.B2B.hitrust.com", "code");
	private final static QName _ResponseDZ022BankTxSerNo_QNAME = new QName(
			"http://response.B2B.hitrust.com", "bankTxSerNo");
	private final static QName _ResponseDZ022ServerTime_QNAME = new QName(
			"http://response.B2B.hitrust.com", "serverTime");
	private final static QName _ResponseDZ022SignFlag_QNAME = new QName(
			"http://response.B2B.hitrust.com", "signFlag");
	private final static QName _ResponseDZ022TrnxCode_QNAME = new QName(
			"http://response.B2B.hitrust.com", "trnxCode");
	private final static QName _ResponseDZ022SignatureAlgorithm_QNAME = new QName(
			"http://response.B2B.hitrust.com", "signature_Algorithm");
	private final static QName _ResponseDZ022Document_QNAME = new QName(
			"http://response.B2B.hitrust.com", "document");
	private final static QName _ResponseDZ022Message_QNAME = new QName(
			"http://response.B2B.hitrust.com", "message");
	private final static QName _ResponseDZ009FailedAccountChecks_QNAME = new QName(
			"http://response.B2B.hitrust.com", "failedAccountChecks");
	private final static QName _ResponseDZ009BatchNo_QNAME = new QName(
			"http://response.B2B.hitrust.com", "batchNo");
	private final static QName _ResponseDZ009Workday_QNAME = new QName(
			"http://response.B2B.hitrust.com", "workday");
	private final static QName _ResponseDZ008FailedLiquidations_QNAME = new QName(
			"http://response.B2B.hitrust.com", "failedLiquidations");
	private final static QName _ReAccountRegEntryDealerOperNo_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "dealerOperNo");
	private final static QName _ReAccountRegEntryErrorInfo_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "errorInfo");
	private final static QName _ResponseDZ018InoutDetails_QNAME = new QName(
			"http://response.B2B.hitrust.com", "inoutDetails");
	private final static QName _ResponseDZ032BoothNo_QNAME = new QName(
			"http://response.B2B.hitrust.com", "boothNo");
	private final static QName _ResponseDZ032AccountNo_QNAME = new QName(
			"http://response.B2B.hitrust.com", "accountNo");
	private final static QName _ResponseDZ020AccountRegs_QNAME = new QName(
			"http://response.B2B.hitrust.com", "accountRegs");
	private final static QName _InOutDetailCheckEntryTrnxType_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "trnxType");
	private final static QName _InOutDetailCheckEntryTrnxCodeHis_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "trnxCodeHis");
	private final static QName _InOutDetailCheckEntryMerTxSerNoHis_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "merTxSerNoHis");
	private final static QName _InOutDetailCheckEntryAccountName_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "accountName");
	private final static QName _InOutDetailCheckEntryBankTxSerNoHis_QNAME = new QName(
			"http://entry.response.B2B.hitrust.com", "bankTxSerNoHis");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.kmfex.webservice.client.hx
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link ArrayOfInOutDetailCheckEntry }
	 * 
	 */
	public ArrayOfInOutDetailCheckEntry createArrayOfInOutDetailCheckEntry() {
		return new ArrayOfInOutDetailCheckEntry();
	}

	/**
	 * Create an instance of {@link SignIn }
	 * 
	 */
	public SignIn createSignIn() {
		return new SignIn();
	}

	/**
	 * Create an instance of {@link ResponseDZ009 }
	 * 
	 */
	public ResponseDZ009 createResponseDZ009() {
		return new ResponseDZ009();
	}

	/**
	 * Create an instance of {@link SubAccountSigned }
	 * 
	 */
	public SubAccountSigned createSubAccountSigned() {
		return new SubAccountSigned();
	}

	/**
	 * Create an instance of {@link ReAccountRegEntry }
	 * 
	 */
	public ReAccountRegEntry createReAccountRegEntry() {
		return new ReAccountRegEntry();
	}

	/**
	 * Create an instance of {@link ResponseDZ008 }
	 * 
	 */
	public ResponseDZ008 createResponseDZ008() {
		return new ResponseDZ008();
	}

	/**
	 * Create an instance of {@link ReconciliationResponse }
	 * 
	 */
	public ReconciliationResponse createReconciliationResponse() {
		return new ReconciliationResponse();
	}

	/**
	 * Create an instance of {@link SignOffResponse }
	 * 
	 */
	public SignOffResponse createSignOffResponse() {
		return new SignOffResponse();
	}

	/**
	 * Create an instance of {@link ResponseDZ017 }
	 * 
	 */
	public ResponseDZ017 createResponseDZ017() {
		return new ResponseDZ017();
	}

	/**
	 * Create an instance of {@link InGoldRequestResponse }
	 * 
	 */
	public InGoldRequestResponse createInGoldRequestResponse() {
		return new InGoldRequestResponse();
	}

	/**
	 * Create an instance of {@link InOutGoldDetailedCheckResponse }
	 * 
	 */
	public InOutGoldDetailedCheckResponse createInOutGoldDetailedCheckResponse() {
		return new InOutGoldDetailedCheckResponse();
	}

	/**
	 * Create an instance of {@link ResponseDZ007 }
	 * 
	 */
	public ResponseDZ007 createResponseDZ007() {
		return new ResponseDZ007();
	}

	/**
	 * Create an instance of {@link SignOff }
	 * 
	 */
	public SignOff createSignOff() {
		return new SignOff();
	}

	/**
	 * Create an instance of {@link ResponseDZ020 }
	 * 
	 */
	public ResponseDZ020 createResponseDZ020() {
		return new ResponseDZ020();
	}

	/**
	 * Create an instance of {@link TradingMarketBankBalanceResponse }
	 * 
	 */
	public TradingMarketBankBalanceResponse createTradingMarketBankBalanceResponse() {
		return new TradingMarketBankBalanceResponse();
	}

	/**
	 * Create an instance of {@link ResponseDZ010 }
	 * 
	 */
	public ResponseDZ010 createResponseDZ010() {
		return new ResponseDZ010();
	}

	/**
	 * Create an instance of {@link SubAccountSignedResponse }
	 * 
	 */
	public SubAccountSignedResponse createSubAccountSignedResponse() {
		return new SubAccountSignedResponse();
	}

	/**
	 * Create an instance of {@link InGoldRegistrationRequest }
	 * 
	 */
	public InGoldRegistrationRequest createInGoldRegistrationRequest() {
		return new InGoldRegistrationRequest();
	}

	/**
	 * Create an instance of {@link TradingMarketBankBalance }
	 * 
	 */
	public TradingMarketBankBalance createTradingMarketBankBalance() {
		return new TradingMarketBankBalance();
	}

	/**
	 * Create an instance of {@link ArrayOfFailedAccountCheckEntry }
	 * 
	 */
	public ArrayOfFailedAccountCheckEntry createArrayOfFailedAccountCheckEntry() {
		return new ArrayOfFailedAccountCheckEntry();
	}

	/**
	 * Create an instance of {@link FailedLiquidationEntry }
	 * 
	 */
	public FailedLiquidationEntry createFailedLiquidationEntry() {
		return new FailedLiquidationEntry();
	}

	/**
	 * Create an instance of {@link LiquidationResponse }
	 * 
	 */
	public LiquidationResponse createLiquidationResponse() {
		return new LiquidationResponse();
	}

	/**
	 * Create an instance of {@link XMLDocument }
	 * 
	 */
	public XMLDocument createXMLDocument() {
		return new XMLDocument();
	}

	/**
	 * Create an instance of {@link OutGoldAuditResultSend }
	 * 
	 */
	public OutGoldAuditResultSend createOutGoldAuditResultSend() {
		return new OutGoldAuditResultSend();
	}

	/**
	 * Create an instance of {@link SurrenderResponse }
	 * 
	 */
	public SurrenderResponse createSurrenderResponse() {
		return new SurrenderResponse();
	}

	/**
	 * Create an instance of {@link ResponseDZ022 }
	 * 
	 */
	public ResponseDZ022 createResponseDZ022() {
		return new ResponseDZ022();
	}

	/**
	 * Create an instance of {@link SignInResponse }
	 * 
	 */
	public SignInResponse createSignInResponse() {
		return new SignInResponse();
	}

	/**
	 * Create an instance of {@link FailedAccountCheckEntry }
	 * 
	 */
	public FailedAccountCheckEntry createFailedAccountCheckEntry() {
		return new FailedAccountCheckEntry();
	}

	/**
	 * Create an instance of {@link ResponseDZ021 }
	 * 
	 */
	public ResponseDZ021 createResponseDZ021() {
		return new ResponseDZ021();
	}

	/**
	 * Create an instance of {@link SubAccountSync }
	 * 
	 */
	public SubAccountSync createSubAccountSync() {
		return new SubAccountSync();
	}

	/**
	 * Create an instance of {@link InGoldRegistrationRequestResponse }
	 * 
	 */
	public InGoldRegistrationRequestResponse createInGoldRegistrationRequestResponse() {
		return new InGoldRegistrationRequestResponse();
	}

	/**
	 * Create an instance of {@link ArrayOfReAccountRegEntry }
	 * 
	 */
	public ArrayOfReAccountRegEntry createArrayOfReAccountRegEntry() {
		return new ArrayOfReAccountRegEntry();
	}

	/**
	 * Create an instance of {@link ResponseDZ016 }
	 * 
	 */
	public ResponseDZ016 createResponseDZ016() {
		return new ResponseDZ016();
	}

	/**
	 * Create an instance of {@link ResponseDZ018 }
	 * 
	 */
	public ResponseDZ018 createResponseDZ018() {
		return new ResponseDZ018();
	}

	/**
	 * Create an instance of {@link ResponseDZ032 }
	 * 
	 */
	public ResponseDZ032 createResponseDZ032() {
		return new ResponseDZ032();
	}

	/**
	 * Create an instance of {@link OutGoldResponse }
	 * 
	 */
	public OutGoldResponse createOutGoldResponse() {
		return new OutGoldResponse();
	}

	/**
	 * Create an instance of {@link InOutGoldDetailedCheck }
	 * 
	 */
	public InOutGoldDetailedCheck createInOutGoldDetailedCheck() {
		return new InOutGoldDetailedCheck();
	}

	/**
	 * Create an instance of {@link Liquidation }
	 * 
	 */
	public Liquidation createLiquidation() {
		return new Liquidation();
	}

	/**
	 * Create an instance of {@link ArrayOfFailedLiquidationEntry }
	 * 
	 */
	public ArrayOfFailedLiquidationEntry createArrayOfFailedLiquidationEntry() {
		return new ArrayOfFailedLiquidationEntry();
	}

	/**
	 * Create an instance of {@link Reconciliation }
	 * 
	 */
	public Reconciliation createReconciliation() {
		return new Reconciliation();
	}

	/**
	 * Create an instance of {@link OutGold }
	 * 
	 */
	public OutGold createOutGold() {
		return new OutGold();
	}

	/**
	 * Create an instance of {@link ResponseDZ012 }
	 * 
	 */
	public ResponseDZ012 createResponseDZ012() {
		return new ResponseDZ012();
	}

	/**
	 * Create an instance of {@link ResponseDZ015 }
	 * 
	 */
	public ResponseDZ015 createResponseDZ015() {
		return new ResponseDZ015();
	}

	/**
	 * Create an instance of {@link InGoldRequest }
	 * 
	 */
	public InGoldRequest createInGoldRequest() {
		return new InGoldRequest();
	}

	/**
	 * Create an instance of {@link Surrender }
	 * 
	 */
	public Surrender createSurrender() {
		return new Surrender();
	}

	/**
	 * Create an instance of {@link InOutDetailCheckEntry }
	 * 
	 */
	public InOutDetailCheckEntry createInOutDetailCheckEntry() {
		return new InOutDetailCheckEntry();
	}

	/**
	 * Create an instance of {@link OutGoldAuditResultSendResponse }
	 * 
	 */
	public OutGoldAuditResultSendResponse createOutGoldAuditResultSendResponse() {
		return new OutGoldAuditResultSendResponse();
	}

	/**
	 * Create an instance of {@link SubAccountSyncResponse }
	 * 
	 */
	public SubAccountSyncResponse createSubAccountSyncResponse() {
		return new SubAccountSyncResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "type", scope = FailedLiquidationEntry.class)
	public JAXBElement<String> createFailedLiquidationEntryType(String value) {
		return new JAXBElement<String>(_FailedLiquidationEntryType_QNAME,
				String.class, FailedLiquidationEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "accountNo", scope = FailedLiquidationEntry.class)
	public JAXBElement<String> createFailedLiquidationEntryAccountNo(
			String value) {
		return new JAXBElement<String>(_FailedLiquidationEntryAccountNo_QNAME,
				String.class, FailedLiquidationEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "remark", scope = FailedLiquidationEntry.class)
	public JAXBElement<String> createFailedLiquidationEntryRemark(String value) {
		return new JAXBElement<String>(_FailedLiquidationEntryRemark_QNAME,
				String.class, FailedLiquidationEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "flag", scope = FailedLiquidationEntry.class)
	public JAXBElement<String> createFailedLiquidationEntryFlag(String value) {
		return new JAXBElement<String>(_FailedLiquidationEntryFlag_QNAME,
				String.class, FailedLiquidationEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "merAccountNo", scope = FailedLiquidationEntry.class)
	public JAXBElement<String> createFailedLiquidationEntryMerAccountNo(
			String value) {
		return new JAXBElement<String>(
				_FailedLiquidationEntryMerAccountNo_QNAME, String.class,
				FailedLiquidationEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://util.B2B.hitrust.com", name = "firstTagName", scope = XMLDocument.class)
	public JAXBElement<String> createXMLDocumentFirstTagName(String value) {
		return new JAXBElement<String>(_XMLDocumentFirstTagName_QNAME,
				String.class, XMLDocument.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ022.class)
	public JAXBElement<XMLDocument> createResponseDZ022Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ022.class)
	public JAXBElement<String> createResponseDZ022Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ022.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ArrayOfFailedAccountCheckEntry }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "failedAccountChecks", scope = ResponseDZ009.class)
	public JAXBElement<ArrayOfFailedAccountCheckEntry> createResponseDZ009FailedAccountChecks(
			ArrayOfFailedAccountCheckEntry value) {
		return new JAXBElement<ArrayOfFailedAccountCheckEntry>(
				_ResponseDZ009FailedAccountChecks_QNAME,
				ArrayOfFailedAccountCheckEntry.class, ResponseDZ009.class,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "batchNo", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009BatchNo(String value) {
		return new JAXBElement<String>(_ResponseDZ009BatchNo_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ009.class)
	public JAXBElement<XMLDocument> createResponseDZ009Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "workday", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009Workday(String value) {
		return new JAXBElement<String>(_ResponseDZ009Workday_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ009.class)
	public JAXBElement<String> createResponseDZ009SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ009.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "accountNo", scope = FailedAccountCheckEntry.class)
	public JAXBElement<String> createFailedAccountCheckEntryAccountNo(
			String value) {
		return new JAXBElement<String>(_FailedLiquidationEntryAccountNo_QNAME,
				String.class, FailedAccountCheckEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "merAccountNo", scope = FailedAccountCheckEntry.class)
	public JAXBElement<String> createFailedAccountCheckEntryMerAccountNo(
			String value) {
		return new JAXBElement<String>(
				_FailedLiquidationEntryMerAccountNo_QNAME, String.class,
				FailedAccountCheckEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ021.class)
	public JAXBElement<XMLDocument> createResponseDZ021Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ021.class)
	public JAXBElement<String> createResponseDZ021Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ021.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "batchNo", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008BatchNo(String value) {
		return new JAXBElement<String>(_ResponseDZ009BatchNo_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ008.class)
	public JAXBElement<XMLDocument> createResponseDZ008Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "workday", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008Workday(String value) {
		return new JAXBElement<String>(_ResponseDZ009Workday_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ008.class)
	public JAXBElement<String> createResponseDZ008SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ArrayOfFailedLiquidationEntry }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "failedLiquidations", scope = ResponseDZ008.class)
	public JAXBElement<ArrayOfFailedLiquidationEntry> createResponseDZ008FailedLiquidations(
			ArrayOfFailedLiquidationEntry value) {
		return new JAXBElement<ArrayOfFailedLiquidationEntry>(
				_ResponseDZ008FailedLiquidations_QNAME,
				ArrayOfFailedLiquidationEntry.class, ResponseDZ008.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "dealerOperNo", scope = ReAccountRegEntry.class)
	public JAXBElement<String> createReAccountRegEntryDealerOperNo(String value) {
		return new JAXBElement<String>(_ReAccountRegEntryDealerOperNo_QNAME,
				String.class, ReAccountRegEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "accountNo", scope = ReAccountRegEntry.class)
	public JAXBElement<String> createReAccountRegEntryAccountNo(String value) {
		return new JAXBElement<String>(_FailedLiquidationEntryAccountNo_QNAME,
				String.class, ReAccountRegEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "errorInfo", scope = ReAccountRegEntry.class)
	public JAXBElement<String> createReAccountRegEntryErrorInfo(String value) {
		return new JAXBElement<String>(_ReAccountRegEntryErrorInfo_QNAME,
				String.class, ReAccountRegEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "merAccountNo", scope = ReAccountRegEntry.class)
	public JAXBElement<String> createReAccountRegEntryMerAccountNo(String value) {
		return new JAXBElement<String>(
				_FailedLiquidationEntryMerAccountNo_QNAME, String.class,
				ReAccountRegEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ017.class)
	public JAXBElement<XMLDocument> createResponseDZ017Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ017.class)
	public JAXBElement<String> createResponseDZ017Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ017.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ016.class)
	public JAXBElement<XMLDocument> createResponseDZ016Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ016.class)
	public JAXBElement<String> createResponseDZ016Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ016.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ018.class)
	public JAXBElement<XMLDocument> createResponseDZ018Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ArrayOfInOutDetailCheckEntry }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "inoutDetails", scope = ResponseDZ018.class)
	public JAXBElement<ArrayOfInOutDetailCheckEntry> createResponseDZ018InoutDetails(
			ArrayOfInOutDetailCheckEntry value) {
		return new JAXBElement<ArrayOfInOutDetailCheckEntry>(
				_ResponseDZ018InoutDetails_QNAME,
				ArrayOfInOutDetailCheckEntry.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ018.class)
	public JAXBElement<String> createResponseDZ018SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ018.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ032.class)
	public JAXBElement<XMLDocument> createResponseDZ032Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "boothNo", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032BoothNo(String value) {
		return new JAXBElement<String>(_ResponseDZ032BoothNo_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "accountNo", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032AccountNo(String value) {
		return new JAXBElement<String>(_ResponseDZ032AccountNo_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ032.class)
	public JAXBElement<String> createResponseDZ032SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ032.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ007.class)
	public JAXBElement<XMLDocument> createResponseDZ007Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ007.class)
	public JAXBElement<String> createResponseDZ007Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ007.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ020.class)
	public JAXBElement<XMLDocument> createResponseDZ020Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ArrayOfReAccountRegEntry }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "accountRegs", scope = ResponseDZ020.class)
	public JAXBElement<ArrayOfReAccountRegEntry> createResponseDZ020AccountRegs(
			ArrayOfReAccountRegEntry value) {
		return new JAXBElement<ArrayOfReAccountRegEntry>(
				_ResponseDZ020AccountRegs_QNAME,
				ArrayOfReAccountRegEntry.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ020.class)
	public JAXBElement<String> createResponseDZ020SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ020.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ010.class)
	public JAXBElement<XMLDocument> createResponseDZ010Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ010.class)
	public JAXBElement<String> createResponseDZ010Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ010.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ012.class)
	public JAXBElement<XMLDocument> createResponseDZ012Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ012.class)
	public JAXBElement<String> createResponseDZ012Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ012.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "version", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015Version(String value) {
		return new JAXBElement<String>(_ResponseDZ022Version_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015Signature(String value) {
		return new JAXBElement<String>(_ResponseDZ022Signature_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "merchantTrnxNo", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015MerchantTrnxNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022MerchantTrnxNo_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "language", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015Language(String value) {
		return new JAXBElement<String>(_ResponseDZ022Language_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "code", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015Code(String value) {
		return new JAXBElement<String>(_ResponseDZ022Code_QNAME, String.class,
				ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "bankTxSerNo", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015BankTxSerNo(String value) {
		return new JAXBElement<String>(_ResponseDZ022BankTxSerNo_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "serverTime", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015ServerTime(String value) {
		return new JAXBElement<String>(_ResponseDZ022ServerTime_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signFlag", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015SignFlag(String value) {
		return new JAXBElement<String>(_ResponseDZ022SignFlag_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "trnxCode", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015TrnxCode(String value) {
		return new JAXBElement<String>(_ResponseDZ022TrnxCode_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "signature_Algorithm", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015SignatureAlgorithm(
			String value) {
		return new JAXBElement<String>(_ResponseDZ022SignatureAlgorithm_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link XMLDocument }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "document", scope = ResponseDZ015.class)
	public JAXBElement<XMLDocument> createResponseDZ015Document(
			XMLDocument value) {
		return new JAXBElement<XMLDocument>(_ResponseDZ022Document_QNAME,
				XMLDocument.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://response.B2B.hitrust.com", name = "message", scope = ResponseDZ015.class)
	public JAXBElement<String> createResponseDZ015Message(String value) {
		return new JAXBElement<String>(_ResponseDZ022Message_QNAME,
				String.class, ResponseDZ015.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "trnxType", scope = InOutDetailCheckEntry.class)
	public JAXBElement<String> createInOutDetailCheckEntryTrnxType(String value) {
		return new JAXBElement<String>(_InOutDetailCheckEntryTrnxType_QNAME,
				String.class, InOutDetailCheckEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "accountNo", scope = InOutDetailCheckEntry.class)
	public JAXBElement<String> createInOutDetailCheckEntryAccountNo(String value) {
		return new JAXBElement<String>(_FailedLiquidationEntryAccountNo_QNAME,
				String.class, InOutDetailCheckEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "trnxCodeHis", scope = InOutDetailCheckEntry.class)
	public JAXBElement<String> createInOutDetailCheckEntryTrnxCodeHis(
			String value) {
		return new JAXBElement<String>(_InOutDetailCheckEntryTrnxCodeHis_QNAME,
				String.class, InOutDetailCheckEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "merTxSerNoHis", scope = InOutDetailCheckEntry.class)
	public JAXBElement<String> createInOutDetailCheckEntryMerTxSerNoHis(
			String value) {
		return new JAXBElement<String>(
				_InOutDetailCheckEntryMerTxSerNoHis_QNAME, String.class,
				InOutDetailCheckEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "accountName", scope = InOutDetailCheckEntry.class)
	public JAXBElement<String> createInOutDetailCheckEntryAccountName(
			String value) {
		return new JAXBElement<String>(_InOutDetailCheckEntryAccountName_QNAME,
				String.class, InOutDetailCheckEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "merAccountNo", scope = InOutDetailCheckEntry.class)
	public JAXBElement<String> createInOutDetailCheckEntryMerAccountNo(
			String value) {
		return new JAXBElement<String>(
				_FailedLiquidationEntryMerAccountNo_QNAME, String.class,
				InOutDetailCheckEntry.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://entry.response.B2B.hitrust.com", name = "bankTxSerNoHis", scope = InOutDetailCheckEntry.class)
	public JAXBElement<String> createInOutDetailCheckEntryBankTxSerNoHis(
			String value) {
		return new JAXBElement<String>(
				_InOutDetailCheckEntryBankTxSerNoHis_QNAME, String.class,
				InOutDetailCheckEntry.class, value);
	}

}
