package com.kmfex.zhaiquan.service;

import java.util.Date;

import com.kmfex.model.InvestRecord;
import com.kmfex.zhaiquan.model.Contract;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.BaseService;
import com.wisdoor.core.vo.CommonVo;

/**
 * 债权转让记录的操作接口
 * 
 * */
public interface ContractService extends BaseService<Contract> {

	/**
	 * 按指定条件分页列出转让协议
	 * 
	 * @param sellerId
	 *            出让人Id
	 * @param buyerId
	 *            受让人Id
	 * @param sellerDate
	 * @param buyerDate
	 * @param endDate
	 *            债权到期日
	 * */
	public PageView<Contract> listByConditions(String sellerId, String buyerId,
			Date sellerDate, Date buyerDate, Date endDate,int pageNo,int pageSize);
	/**
	 * 取得下次生成的协议编码
	 * @param ir 转让的债权
	 * @param user 受让方
	 * @return
	 * @throws Exception
	 */
	public String getNextContractCode(InvestRecord ir,User user) throws Exception;
	
	public Contract getByInvestRecordAndUser(InvestRecord ir,User buyer) throws Exception;
 
 
	/**
	 * 得到最后一次盈利合计
	 * @throws Exception
	 */
	public CommonVo selectByDateTime(String investRecordId,String userId) throws Exception;
}
