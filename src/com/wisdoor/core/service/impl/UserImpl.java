package com.wisdoor.core.service.impl;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.MD5;

@Service
@Transactional
public class UserImpl extends BaseServiceImpl<User> implements UserService {
	@Resource AccountService accountService; 
 
	@Override
	public User findUser(String userName,String password) {
		String queryString = "from User c where c.username = ? and c.password=? ";//and c.userType='3' ";
		User user=selectById(queryString, new String[] { String.valueOf(userName), String.valueOf(password)});
		return user;
	}
	@Override
	public User findUser(String userName,boolean nopassword,String userType) {
		String queryString = "from User c where c.username = ? and c.userType='"+userType+"' ";
		User user=selectById(queryString, new String[] { String.valueOf(userName)});
		return user;
	}
	@Override
	public User findUser(String userName) {
		String queryString = "from User c where c.username = ? ";
		User user=selectById(queryString, new String[] { String.valueOf(userName)});
		return user;
	}
	@Override
	@Transactional
	public Serializable insertUser(User obj) throws EngineException
	{
		Account account=accountService.createAccount();
		account.setUser(obj); 
		obj.setUserAccount(account);
		obj.setPassword(MD5.MD5Encode(obj.getPassword()));
		return this.insert(obj);
	}
	//取在华夏银行做过三方存管的会员，即username不为空，accountNo不为空
	@Override
	@Transactional
	public List<User> getUserForHxbank(){
		String queryString = "from User c where c.flag='2' and c.typeFlag='1' and c.enabled=true and c.visible=true ";
		return this.getCommonListData(queryString);
	}
	//取日切用户
	@Override
	@Transactional
	public List<User> getUserForUse(){
		String queryString = "from User c where ( c.userType='T' or c.userType='R' or c.userType='D' or c.userType='Q') and c.userAccount.id is not null ";
		return this.getCommonListData(queryString);
	}
	
	//通过摊位号及子账户来查询交易商,flag=2表示交易系统已经同步,银行已经同步(有子账号)
	@Override
	@Transactional
	public User getUser(String username,String accountNo){
		String queryString = "from User c where c.flag='2' and c.username = ? and c.accountNo=? ";
		User user=selectById(queryString, new String[] { String.valueOf(username), String.valueOf(accountNo)});
		return user;
	}
	@SuppressWarnings("unchecked")
	@Override
	public String autocomplate(final String term) {
		String result = (String) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public String doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuilder sb = new StringBuilder("[");
						List list = session.createQuery("select username, realname from User where username like '"+term+"%' ").setFirstResult(1).setMaxResults(10).list();
						for(int x = 0; x<list.size(); x++){
							if(x!=0){sb.append(",");}
							Object[] obj = (Object[])list.get(x);
							sb.append("{\"label\":\"").append(obj[0]).append("-").append(obj[1]).append("\",");
							sb.append("\"value\":\"").append(obj[0]).append("\"}");
						}
						sb.append("]");
						return sb.toString();
					}
				});

		return result;
	}
	
	//所有账户的oldbalance更新为balance值
	@Override
	@Transactional
	public boolean updateOldBalance(){
		System.out.println("updateOldBalance");
		String sql = "update sys_account a set a.old_balance=a.balance_ ";
		int u = this.jdbcTemplate.update(sql);
		System.out.println(sql);
		System.out.println("result:"+u);
		return true;
	}
}