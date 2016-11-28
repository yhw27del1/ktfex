package com.wisdoor.core.service;
 
import java.io.Serializable;
import java.util.List;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;

/*** 
 * 用户(个人、企业、管理员
 * @author   
 */
public interface UserService  extends BaseService<User>{ 
	/*
	 * 判断用户是否存在
	 */
	public User findUser(String userName);
	public Serializable insertUser(User obj) throws EngineException; 
	public User findUser(String userName,String password);
	public User findUser(String userName,boolean notPassword,String userType);
	
	//通过摊位号及子账户来查询交易商
	public User getUser(String username,String accountNo);
	
	//取在华夏银行做过三方存管的会员(typeFlag='1',enabled=true,visible=true,flag='2')
	public List<User> getUserForHxbank();
	
	public List<User> getUserForUse();
	
	public String autocomplate(String term);
	
	public boolean updateOldBalance();
}
