package com.wisdoor.core.action;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Transaction;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.TransactionService;
import com.wisdoor.core.trigger.Trigger;
import com.wisdoor.struts.BaseAction;
/**
 * 事务action
 * @author eclipse
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class TransactionAction extends BaseAction implements Preparable{
	
	private String transaction_id;
	private Transaction transaction;
	
	@Resource private Scheduler scheduler;
	
	@Resource TransactionService transactionService;
	/**
	 * 显示事务列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception{
		PageView<Transaction> pageView = new PageView<Transaction>(getShowRecord(), getPage());
		pageView.setQueryResult(this.transactionService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult()));
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		return "list";
	}
	/**
	 * 编辑/新增事务
	 * @return
	 * @throws Exception
	 */
	public String ui() throws Exception{
		return "ui";
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception{
		if(transaction_id==null || "".equals(transaction_id)){
			this.transactionService.insert(this.transaction);
		}else{
			this.transactionService.update(this.transaction);
		}
		reload();
		return "edit";
	}
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public String del() throws Exception{
		this.transactionService.delete(this.transaction_id);
		reload();
		return "del";
	}

	@Override
	public void prepare() throws Exception {
		if(transaction_id==null || "".equals(transaction_id)){
			transaction = new Transaction();
		}else{
			transaction = this.transactionService.selectById(transaction_id);
		}
		
	}
	
	
	private void reload(){
		
		try {
			org.quartz.Trigger trigger = scheduler.getTrigger("cron", Scheduler.DEFAULT_GROUP);
			JobDetail jd = scheduler.getJobDetail(trigger.getJobName(), trigger.getJobGroup());
			JobDataMap jdm = jd.getJobDataMap();
			MethodInvokingJobDetailFactoryBean bean = (MethodInvokingJobDetailFactoryBean)jdm.get("methodInvoker");
			Trigger tri =(Trigger)bean.getTargetObject();
			tri.reload();
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transactionId) {
		transaction_id = transactionId;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
	
}
