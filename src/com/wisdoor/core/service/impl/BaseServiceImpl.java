package com.wisdoor.core.service.impl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.BaseService;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.utils.GenericsUtils;
import com.wisdoor.core.vo.PageForProcedureVO;

@SuppressWarnings("unchecked")
public abstract class BaseServiceImpl<T> implements BaseService<T> {
	private HibernateTemplate hibernateTemplate;
	private LogsService logsService;
	protected JdbcTemplate jdbcTemplate;
	protected Class<T> entityClass = GenericsUtils.getSuperClassGenricType(this
			.getClass());   

	@Transactional
	public Serializable insert(T obj) throws EngineException {
		try {
			Serializable s = this.getHibernateTemplate().save(obj);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}         

	}

	@Transactional
	public void insert(List<T> objs) throws EngineException {
		this.getHibernateTemplate().saveOrUpdateAll(objs);
	}

	@Transactional
	public void update(T obj) throws EngineException {
		try {
			this.getHibernateTemplate().saveOrUpdate(obj);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void update(List<T> objs) throws EngineException {
		this.getHibernateTemplate().saveOrUpdate(objs);
	}

	@Transactional
	public void delete(Serializable id) throws EngineException {
		T obj = null;
		try {
			obj = (T) this.getHibernateTemplate().get(this.entityClass, id);
			this.getHibernateTemplate().delete(obj);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void delete(List<Serializable> ids) throws EngineException {
		for (Serializable id : ids) {
			this.delete(id);
		}
	}

	@Transactional
	public void executeHql(String Hql) throws EngineException {
		this.getHibernateTemplate().bulkUpdate(Hql);
	}

	@Transactional
	public T selectById(Serializable id) {
		return (T) this.getHibernateTemplate().get(this.entityClass, id);
	}

	@Transactional
	public T selectById(String hql, String... value) {
		List<T> objects = this.getScrollDataCommon(hql, value);

		if (null != objects) {
			if (0 == objects.size()) {
				return null;
			} else {
				return objects.get(0);
			}
		} else {
			return null;
		}
	}

	@Transactional
	public List<T> getCommonListData(String hql) {
		return (List<T>) this.getHibernateTemplate().find(hql);
	}

	@Transactional
	public List<T> getScrollDataCommon(String hql, String... value) {
		return (List<T>) this.getHibernateTemplate().find(hql, value);
	}

	@Transactional
	public List<T> getScrollDataCommon(String hql, Object[] queryParams) {
		return (List<T>) this.getHibernateTemplate().find(hql, queryParams);
	}

	@Transactional
	public List<T> getScrollData(final String hql, final String[] value,
			final int start, final int number) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);

				for (int i = 0; i < value.length; ++i) {
					query = query.setString(i, value[i]);
				}

				query.setFirstResult(start);
				query.setMaxResults(number);

				return (List<T>) query.list();
			}
		});
	}

	@Transactional
	public List<T> getScrollData(final String queryString, final String value,
			final int start, final int number) {
		String values[] = { value };
		return this.getScrollData(queryString, values, start, number);
	}

	@Transactional
	public T selectByHql(String queryString, String... value) {
		List<T> objects = this.getScrollDataCommon(queryString, value);

		if (null != objects) {
			if (0 == objects.size()) {
				return null;
			} else {
				return objects.get(0);
			}
		} else {
			return null;
		}
	}

	@Transactional
	public T selectById(String queryString, Object[] queryParams) {
		List<T> objects = this.getScrollDataCommon(queryString, queryParams);

		if (null != objects) {
			if (0 == objects.size()) {
				return null;
			} else {
				return objects.get(0);
			}
		} else {
			return null;
		}
	}

	@Transactional
	public T selectByHql(String hql) {
		List list = this.getHibernateTemplate().find(hql);
		if (null != list && !list.isEmpty())
			return (T) list.get(0);
		return null;
	}

	@Transactional
	public long getScrollDataCount(final String hql, final String... value) {
		Object count = this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery("select count(*) "
								+ hql);

						for (int i = 0; i < value.length; ++i) {
							query = query.setString(i, value[i]);
						}

						return query.iterate().next();
					}
				});

		return (Long) count;
	}

	@Transactional
	public long getScrollDataCount(String hql) {
		String[] value = new String[0];
		return this.getScrollDataCount(hql, value);
	}

	@Transactional
	public QueryResult<T> getScrollData(int firstindex, int maxresult,
			LinkedHashMap<String, String> orderby) throws Exception {
		return getScrollData(firstindex, maxresult, null, null, orderby);
	}

	@Transactional
	public QueryResult<T> getScrollData(String wherejpql,
			List<String> queryParams, LinkedHashMap<String, String> orderby)
			throws Exception {
		return getScrollData(-1, -1, wherejpql, queryParams, orderby);
	}

	@Transactional
	public QueryResult<T> getScrollData(int firstindex, int maxresult,
			String wherejpql, List<String> queryParams) throws Exception {
		return getScrollData(firstindex, maxresult, wherejpql, queryParams,
				null);
	}

	@Transactional
	public QueryResult<T> getScrollData(String wherejpql,
			List<String> queryParams) throws Exception {
		return getScrollData(-1, -1, wherejpql, queryParams, null);
	}

	@Transactional
	public QueryResult<T> getScrollData(String jpql) throws Exception {
		return getScrollData(-1, -1, jpql, null, null);
	}

	@Transactional
	public QueryResult<T> getScrollData(int firstindex, int maxresult)
			throws Exception {
		return getScrollData(firstindex, maxresult, null, null, null);
	}

	@Transactional
	public QueryResult<T> getScrollData() throws Exception {
		return getScrollData(-1, -1);
	}

	@Transactional
	public QueryResult<T> getScrollData(final int firstindex,
			final int maxresult, final String wherejpql,
			final List<String> queryParams,
			final LinkedHashMap<String, String> orderby) throws Exception {
		QueryResult qr = new QueryResult<T>();

		Object count = this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String entityname = getGenericType(0).getSimpleName();
						String hql = "select count(*) from "
								+ entityname
								+ " o "
								+ (wherejpql == null
										|| "".equals(wherejpql.trim()) ? ""
										: "where " + wherejpql);

						Query query = session.createQuery(hql);

						if (queryParams != null && queryParams.size() > 0) {
							for (int i = 0; i < queryParams.size(); i++) {
								query = query.setString(i, queryParams.get(i));
							}
						}
						return query.iterate().next();
					}
				});

		qr.setTotalrecord((Long) count);

		List<T> resultlist = this.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String entityname = getGenericType(0).getSimpleName();
						String hql = "select o from "
								+ entityname
								+ " o "
								+ (wherejpql == null
										|| "".equals(wherejpql.trim()) ? ""
										: "where " + wherejpql)
								+ buildOrderby(orderby);
						Query query = session.createQuery(hql);
						if (queryParams != null && queryParams.size() > 0) {
							for (int i = 0; i < queryParams.size(); i++) {
								query = query.setString(i, queryParams.get(i));
							}
						}
						if (firstindex != -1 && maxresult != -1) {
							query.setFirstResult(firstindex);
							query.setMaxResults(maxresult);
						}

						return (List<T>) query.list();
					}
				});
		qr.setResultlist(resultlist);

		return qr;

	}

	@Transactional
	public Long getScrollCount(final String wherejpql,
			final List<String> queryParams,
			final LinkedHashMap<String, String> orderby) throws Exception {
		Object count = this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String entityname = getGenericType(0).getSimpleName();
						String hql = "select count(*) from "
								+ entityname
								+ " o "
								+ (wherejpql == null
										|| "".equals(wherejpql.trim()) ? ""
										: "where " + wherejpql);

						Query query = session.createQuery(hql);

						if (queryParams != null && queryParams.size() > 0) {
							for (int i = 0; i < queryParams.size(); i++) {
								query = query.setString(i, queryParams.get(i));
							}
						}
						return query.iterate().next();
					}
				});
		return (Long) count;
	}
	
	@Transactional
	public double getScrollSum(final String wherejpql,
			final List<String> queryParams,
			final LinkedHashMap<String, String> orderby) throws Exception {
		Object count = this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String entityname = getGenericType(0).getSimpleName();
						String hql = "select nvl(sum(o.money),0) from "
								+ entityname
								+ " o "
								+ (wherejpql == null
										|| "".equals(wherejpql.trim()) ? ""
										: "where " + wherejpql);

						Query query = session.createQuery(hql);

						if (queryParams != null && queryParams.size() > 0) {
							for (int i = 0; i < queryParams.size(); i++) {
								query = query.setString(i, queryParams.get(i));
							}
						}
						return query.iterate().next();
					}
				});
		return (Double) count;
	}

	public List<T> getListForCriteria(DetachedCriteria criteria) {

		return (List<T>) this.getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * 组装order by语句
	 * 
	 * @param orderby
	 * @return
	 */
	protected final static String buildOrderby(
			LinkedHashMap<String, String> orderby) {
		try {
			StringBuffer orderbyql = new StringBuffer("");
			if (orderby != null && orderby.size() > 0) {
				orderbyql.append(" order by ");
				for (String key : orderby.keySet()) {
					orderbyql.append("o.").append(key).append(" ").append(
							orderby.get(key)).append(",");
				}
				orderbyql.deleteCharAt(orderbyql.length() - 1);
			}
			return orderbyql.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";

		}
	}

	/**
	 * 获取实体的名称
	 * 
	 * @param <E>
	 * @param clazz
	 *            实体类
	 * @return
	 */
	protected final static <E> String getEntityName(Class<E> clazz) {
		try {
			String entityname = clazz.getSimpleName();
			Entity entity = clazz.getAnnotation(Entity.class);
			if (entity.name() != null && !"".equals(entity.name())) {
				entityname = entity.name();
			}
			return entityname;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected static <E> String getCountField(Class<E> clazz) throws Exception {
		String out = "o";
		try {
			PropertyDescriptor[] propertyDescriptors = Introspector
					.getBeanInfo(clazz).getPropertyDescriptors();
			for (PropertyDescriptor propertydesc : propertyDescriptors) {
				Method method = propertydesc.getReadMethod();
				if (method != null
						&& method.isAnnotationPresent(EmbeddedId.class)) {
					PropertyDescriptor[] ps = Introspector.getBeanInfo(
							propertydesc.getPropertyType())
							.getPropertyDescriptors();
					out = "o."
							+ propertydesc.getName()
							+ "."
							+ (!ps[1].getName().equals("class") ? ps[1]
									.getName() : ps[0].getName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public Class getGenericType(int index) {
		Type genType = getClass().getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new RuntimeException("Index outof bounds");
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public LogsService getLogsService() {
		return logsService;
	}

	@Resource
	public void setLogsService(LogsService logsService) {
		this.logsService = logsService;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	/**
	 * 判断字符串是否非空:字符串为空的条件是str为null或者str去掉空格后等于""。
	 * 
	 * @param string
	 *            字符串
	 * @return true 如果字符串为空;false 如果字符串非空
	 * 
	 * */
	public boolean isEmpty(String string) {
		return null == string || "".equals(string.replaceAll(" ", ""));
	}

	public String trim(String string) {
		if (null != string) {
			return string.replaceAll(" ", "");
		}
		return string;
	}
	
	/**
	 * 将resultset转成list<map>
	 * @param rs ResultSet
	 * @return 转换结果
	 */
	public ArrayList<LinkedHashMap<String,Object>> resultsetToList(ResultSet rs) {
		ArrayList<LinkedHashMap<String,Object>> result = new ArrayList<LinkedHashMap<String,Object>>();
		
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columncount = rsmd.getColumnCount();
			String [] columnnames = new String[columncount];
			for(int x = 1; x<= columncount; x++){
				columnnames[x-1] = rsmd.getColumnName(x);
			}
			for(;rs.next();){
				LinkedHashMap<String, Object> record = new LinkedHashMap<String, Object>();
				for(String cn : columnnames){
					Object value = rs.getObject(cn);
					record.put(cn.toLowerCase(), value);
				}
				result.add(record);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	/**
	 * 执行存储过程的方法（返回列表）
	 * 
	 * @param procedureName
	 *            存储过程的名称
	 * @param inParamList
	 *            备注：Map<String, Object> paramList 其中String 参数： Object输入的参数对象
	 *            存储过程参数的信息 
	 * @param outParameter 
	 * 				返回的对象属性
	 * @return 封装过的list
	 */
	public ArrayList<LinkedHashMap<String,Object>> callProcedureForList(String procedureName, final Map<Integer, Object> inParamList,final OutParameterModel outParameter) {
		ArrayList<LinkedHashMap<String,Object>> result = null;
		try {
			synchronized(this){
				if (StringUtils.isNotBlank(procedureName)) {
					int inParamListLength = 0;
					if (!inParamList.isEmpty()) {
						inParamListLength = inParamList.size();
					}
					if ( outParameter!=null && outParameter.valid() ) {
						inParamListLength++;
					}
					
					final String executeProcedure = generationExcuteProdure(procedureName, inParamListLength );
					result = (ArrayList<LinkedHashMap<String,Object>>)this.getHibernateTemplate().execute(new HibernateCallback<ArrayList<LinkedHashMap<String,Object>>>() {
						@Override
						public ArrayList<LinkedHashMap<String,Object>> doInHibernate(Session session) throws HibernateException, SQLException {
							Transaction transaction = session.beginTransaction();
							ArrayList<LinkedHashMap<String,Object>> result = null;
							DataSource ds = null ;
							Connection conn = null ;
							CallableStatement cstmt = null ;
							ResultSet rs = null;
							try {
								ds= SessionFactoryUtils.getDataSource(session.getSessionFactory());
								conn = ds.getConnection();
								cstmt = conn.prepareCall(executeProcedure);
								if (!inParamList.isEmpty()) {
									Set<Entry<Integer, Object>> entrys = inParamList.entrySet();
									for (Entry<Integer, Object> entry : entrys) {
										cstmt.setObject(entry.getKey(), entry.getValue());
									}
								}
								if ( outParameter!=null && outParameter.valid()) {
									cstmt.registerOutParameter(outParameter.getIndex(), outParameter.getType());
								}
								cstmt.execute();
								
								rs = (ResultSet)cstmt.getObject(outParameter.getIndex());
								result = resultsetToList(rs);
								rs.close();
								cstmt.close();
								conn.close();
								rs = null;
								cstmt = null;
								conn = null;
							} catch (Exception e) {
								e.printStackTrace();
							}finally{
								transaction.commit();
								
							}
							return result;
						}
					});
					
				} else {
					throw new RuntimeException(" the procedureName is null!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	/**
	 * 执行存储过程
	 * @param procedureName
	 * @param inParamList
	 * @return
	 */
	public boolean callProcedure(String procedureName, final Map<Integer, Object> inParamList) {
		boolean result = false;
		try {
			synchronized(this){
				if (StringUtils.isNotBlank(procedureName)) {
					int inParamListLength = 0;
					if (!inParamList.isEmpty()) {
						inParamListLength = inParamList.size();
					}
					
					final String executeProcedure = generationExcuteProdure(procedureName, inParamListLength );
					result = (Boolean)this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
						@Override
						public Object doInHibernate(Session session) throws HibernateException, SQLException {
							Transaction transaction = session.beginTransaction();
							DataSource ds = null ;
							Connection conn = null ;
							CallableStatement cstmt = null ;
							boolean result = false;
							try {
								ds= SessionFactoryUtils.getDataSource(session.getSessionFactory());
								conn = ds.getConnection();
								cstmt = conn.prepareCall(executeProcedure);
								if (!inParamList.isEmpty()) {
									Set<Entry<Integer, Object>> entrys = inParamList.entrySet();
									for (Entry<Integer, Object> entry : entrys) {
										cstmt.setObject(entry.getKey(), entry.getValue());
									}
								}
								result = cstmt.execute();
								cstmt.close();
								conn.close();
								cstmt = null;
								conn = null;
							} catch (Exception e) {
								e.printStackTrace();
							}finally{
								transaction.commit();
							}
							return result;
						}
					});
					
				} else {
					throw new RuntimeException(" the procedureName is null!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Transactional
	public QueryResult<Object> groupHqlQuery(final String hql) throws Exception {
		QueryResult qr = new QueryResult<Object>();
		List<Object> resultlist=this.getHibernateTemplate().executeFind(new HibernateCallback()
		{
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException
			{ 
 				Query query = session.createQuery(hql);
				return (List<Object>) query.list();  
			}  
		});
		qr.setResultlist(resultlist);
		return qr;
	}
	
	
	
	/**
	 * 调用存储过程，返回值列表
	 * @param procedureName 存储过程名称
	 * @param inParamList   
	 * @param outParameter
	 * @return key为下标
	 */
	@Override
	public HashMap<Integer, Object> callProcedureForParameters(String procedureName, final Map<Integer, Object> inParamList, final Map<Integer, Integer> outParameter) {
		HashMap<Integer, Object> result = null;
		try {
			synchronized(this){
				if (StringUtils.isNotBlank(procedureName)) {
					int inParamListLength = 0, outParamListLength = 0;
					if (!inParamList.isEmpty()) {
						inParamListLength = inParamList.size();
					}
					if (!outParameter.isEmpty()) {
						outParamListLength = outParameter.size();
					}

					final String executeProcedure = generationExcuteProdure(procedureName, inParamListLength + outParamListLength);
					result = (HashMap<Integer, Object>) this.getHibernateTemplate().execute(new HibernateCallback<HashMap<Integer, Object>>() {
						@Override
						public HashMap<Integer, Object> doInHibernate(Session session) throws HibernateException, SQLException {
							Transaction transaction = session.beginTransaction();
							HashMap<Integer, Object> result = new HashMap<Integer, Object>();
							DataSource ds = null;
							Connection conn = null;
							CallableStatement cstmt = null;
							try {
								ds = SessionFactoryUtils.getDataSource(session.getSessionFactory());
								conn = ds.getConnection();
								cstmt = conn.prepareCall(executeProcedure);
								if (!inParamList.isEmpty()) {
									Set<Entry<Integer, Object>> entrys = inParamList.entrySet();
									for (Entry<Integer, Object> entry : entrys) {
										cstmt.setObject(entry.getKey(), entry.getValue());
									}
								}
								if (!outParameter.isEmpty()) {
									Set<Entry<Integer, Integer>> entrys = outParameter.entrySet();
									for (Entry<Integer, Integer> entry : entrys) {
										cstmt.registerOutParameter(entry.getKey(), entry.getValue());
									}

								}
								cstmt.execute();
								if (!outParameter.isEmpty()) {
									Set<Entry<Integer, Integer>> entrys = outParameter.entrySet();
									for (Entry<Integer, Integer> entry : entrys) {
										result.put(entry.getKey(), cstmt.getObject(entry.getKey()));
									}
								}
								cstmt.close();
								conn.close();
								cstmt = null;
								conn = null;

							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								transaction.commit();
							}

							return result;
						}
					});

				} else {
					throw new RuntimeException(" the procedureName is null!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成存储过程的方法
	 * 
	 * @param procedureName
	 *            存储过程的名称
	 * @param paramListLength
	 *            参数的个数
	 * @return 生成的执行的存储过程
	 */
	public String generationExcuteProdure(String procedureName, int paramListLength) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("{ call ");
		sql.append(StringUtils.trim(procedureName));
		if (paramListLength > 0) {
			sql.append("(");
			for (int i = 0; i < paramListLength; i++) {
				if (i != 0) {
					sql.append(",");
				}
				sql.append("?");
			}
			sql.append(")");
		}
		sql.append("}");
		return sql.toString();
	}
	
	@Override
	public ArrayList<LinkedHashMap<String,Object>> selectListWithJDBC(String sql) throws Exception {
		ArrayList<LinkedHashMap<String,Object>> result =null;
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		DataSource ds = SessionFactoryUtils.getDataSource(session.getSessionFactory());
		Connection conn = ds.getConnection() ;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql); 
		
		result = resultsetToList(rs);
		rs.close();
		st.close();
		conn.close();
		session.close();
		
		return result;
	}

	@Override
	public int selectcount(String sql) throws Exception {
		int result = 0;
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		DataSource ds = SessionFactoryUtils.getDataSource(session.getSessionFactory());
		Connection conn = ds.getConnection() ;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql); 
		 
		if(rs.next()){
			result = rs.getInt(1);
		}
		
		rs.close();
		st.close();
		conn.close();
		session.close();
		return result;
	}

	@Override
	public float selectsum(String sql) throws Exception {
		float result = 0f;
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		DataSource ds = SessionFactoryUtils.getDataSource(session.getSessionFactory());
		Connection conn = ds.getConnection() ;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql); 
		if(rs.next()){
			result = rs.getInt(1);
		}
		rs.close();
		st.close();
		conn.close();
		session.close();
		return result;
	}
	
	//约定List<OutParameterModel> outParameter
	//第一个为：分页总数；第二个为：记录总数；第三个为：记录列表
	public PageForProcedureVO callProcedureForPage(String procedureName, final Map<Integer, Object> inParamList) {
		final List<OutParameterModel> outParameter = new ArrayList<OutParameterModel>();
		OutParameterModel o1 = new OutParameterModel(inParamList.size()+1, OracleTypes.INTEGER);
		OutParameterModel o2 = new OutParameterModel(inParamList.size()+2, OracleTypes.INTEGER);
		OutParameterModel o3 = new OutParameterModel(inParamList.size()+3, OracleTypes.CURSOR);
		outParameter.add(o1);
		outParameter.add(o2);
		outParameter.add(o3);
		final PageForProcedureVO vo = new PageForProcedureVO();
		ArrayList<LinkedHashMap<String,Object>> result = null;
		try {
			synchronized(this){
				if (StringUtils.isNotBlank(procedureName)) {
					int inParamListLength = 0;
					if (!inParamList.isEmpty()) {
						inParamListLength = inParamList.size();
					}
					if ( outParameter!=null ) {
						for(OutParameterModel o:outParameter){
							if(o.valid()){
								inParamListLength++;
							}
						}
					}
					
					final String executeProcedure = generationExcuteProdure(procedureName, inParamListLength );
					result = (ArrayList<LinkedHashMap<String,Object>>)this.getHibernateTemplate().execute(new HibernateCallback<ArrayList<LinkedHashMap<String,Object>>>() {
						@Override
						public ArrayList<LinkedHashMap<String,Object>> doInHibernate(Session session) throws HibernateException, SQLException {
							Transaction transaction = session.beginTransaction();
							ArrayList<LinkedHashMap<String,Object>> rrr = null;
							DataSource ds = null ;
							Connection conn = null;
							CallableStatement cstmt = null ;
							ResultSet rs = null;
							try {
								ds= SessionFactoryUtils.getDataSource(session.getSessionFactory());
								conn = ds.getConnection();
								cstmt = conn.prepareCall(executeProcedure);
								if (!inParamList.isEmpty()) {
									Set<Entry<Integer, Object>> entrys = inParamList.entrySet();
									for (Entry<Integer, Object> entry : entrys) {
										cstmt.setObject(entry.getKey(), entry.getValue());
									}
								}
								if ( outParameter!=null ) {
									for(OutParameterModel o:outParameter){
										if(o.valid()){
											cstmt.registerOutParameter(o.getIndex(), o.getType());
										}
									}
								}
								cstmt.execute();
								int p0 = cstmt.getInt(outParameter.get(0).getIndex());
								int p1 = cstmt.getInt(outParameter.get(1).getIndex());
								rs = (ResultSet)cstmt.getObject(outParameter.get(2).getIndex());
								rrr = resultsetToList(rs);
								vo.setTotalpage(p0);
								vo.setTotalrecord(p1);
								vo.setResult(rrr);
								rs.close();
								cstmt.close();
								conn.close();
								rs = null;
								cstmt = null;
								conn = null;
							} catch (Exception e) {
								e.printStackTrace();
							}finally{
								transaction.commit();
								
							}
							return rrr;
						}
					});
					
				} else {
					throw new RuntimeException(" the procedureName is null!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
		
	}
    /* 
     * 聚集函数的计算结果
     * avg(), sum(), max(), count()
     */
	@SuppressWarnings("unchecked")  
	@Override
	public Object queryJhFunction(final String hql) {
		return this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(final Session session) throws HibernateException, SQLException { 
				final Query query = session.createQuery(hql); 
				Iterator iter = query.iterate();
				Object result = iter.next();
				return result;
			}
		}); 
	}

	@Override
	public List<Map<String, Object>> queryForList(String fields, String tablename, String wheresql,Object[] args, int page, int pagesize,boolean printSQL) {
		if(page <= 0 && pagesize > 0 ) page = 1;
		else if( page > 0 && pagesize <= 0) pagesize = 15;
		int row_start = (page - 1) * pagesize;
		int row_end = page * pagesize;
		StringBuilder sql = new StringBuilder("select ").append(fields).append(" from ").append(tablename);
		if(StringUtils.isNotBlank(wheresql)){
			sql.append(" where ").append(wheresql);
		}
		if(page > 0 && pagesize > 0 ){
			sql.insert(0, "select * from ( select a.*, rownum rn from (").append(") a where rownum <= "+ row_end +" ) where rn > ").append(row_start);
		}
		if(printSQL) { 
			printSQL(sql.toString(), args);
		}
		try{
			return this.jdbcTemplate.queryForList(sql.toString(), args);
		} catch (EmptyResultDataAccessException empty) {
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> queryForList(String fields, String tablename, String wheresql,Object[] args, int page, int pagesize) {
		return this.queryForList(fields, tablename, wheresql, args, page, pagesize, false);
	}
	
	
	@Override
	public int queryForListTotal(String fieldname,String tablename, String wheresql,Object[] args,boolean printSQL) throws Exception {
		StringBuilder sql = new StringBuilder("select ").append(fieldname).append(" from ").append(tablename);
		if(StringUtils.isNotBlank(wheresql)){
			sql.append(" where ").append(wheresql);
		}
		sql.insert(0,"select count(1) from (").append(")");
		if(printSQL) System.out.println(sql.toString());
		return this.jdbcTemplate.queryForInt(sql.toString(), args);
	}
	
	@Override
	public int queryForListTotal(String fieldname,String tablename, String wheresql,Object[] args) throws Exception {
		return this.queryForListTotal( fieldname, tablename,  wheresql,args, false);
	}

	@Override
	public List<Map<String, Object>> queryForList(String fields, String tablename, String wheresql ,Object[] args) throws Exception {
		return this.queryForList(fields, tablename, wheresql, args, 0, 0, false);
	}
	@Override
	public List<Map<String, Object>> queryForList(String fields, String tablename, String wheresql,Object[] args, boolean printSQL) throws Exception {
		return this.queryForList(fields, tablename, wheresql,args, 0, 0,printSQL);
	}

	@Override
	public Map<String, Object> queryForObject(String fields, String tablename, String wheresql,Object[] args, boolean printSQL) throws Exception {
		StringBuilder sql = new StringBuilder("select ").append(fields).append(" from ").append(tablename);
		if(StringUtils.isNotBlank(wheresql)){
			sql.append(" where ").append(wheresql);
		}
		if(printSQL) {
			printSQL(sql.toString(), args);
		}
		try {
			return this.jdbcTemplate.queryForMap(sql.toString(), args);
		} catch (EmptyResultDataAccessException empty) {
			return null;
		}
	}

	@Override
	public Map<String, Object> queryForObject(String fields, String tablename, String wheresql,Object[] args) throws Exception {
		return this.queryForObject(fields, tablename, wheresql,args, false);
	}
	
	
	
	@Override
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated
	public List<Map<String, Object>> queryForList(String fields, String tablename, String wheresql, int page, int pagesize,boolean printSQL) {
		if(page <= 0 && pagesize > 0 ) page = 1;
		else if( page > 0 && pagesize <= 0) pagesize = 15;
		int row_start = (page - 1) * pagesize;
		int row_end = page * pagesize;
		StringBuilder sql = new StringBuilder("select ").append(fields).append(" from ").append(tablename);
		if(StringUtils.isNotBlank(wheresql)){
			sql.append(" where ").append(wheresql);
		}
		if(page > 0 && pagesize > 0 ){
			sql.insert(0, "select * from ( select a.*, rownum rn from (").append(") a where rownum <= "+ row_end +" ) where rn > ").append(row_start);
		}
		if(printSQL) System.out.println(sql.toString());
		try{
			return this.jdbcTemplate.queryForList(sql.toString());
		} catch (EmptyResultDataAccessException empty) {
			return null;
		}
	}
	
	@Override
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated
	public List<Map<String, Object>> queryForList(String fields, String tablename, String wheresql, int page, int pagesize) {
		return this.queryForList(fields, tablename, wheresql, page, pagesize, false);
	}
	
	
	@Override
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated
	public int queryForListTotal(String fieldname,String tablename, String wheresql,boolean printSQL) throws Exception {
		StringBuilder sql = new StringBuilder("select ").append(fieldname).append(" from ").append(tablename);
		if(StringUtils.isNotBlank(wheresql)){
			sql.append(" where ").append(wheresql);
		}
		sql.insert(0,"select count(1) from (").append(")");
		if(printSQL) System.out.println(sql.toString());
		return this.jdbcTemplate.queryForInt(sql.toString());
	}
	
	@Override
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated
	public int queryForListTotal(String fieldname,String tablename, String wheresql) throws Exception {
		return this.queryForListTotal( fieldname, tablename,  wheresql, false);
	}
	
	@Override
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated
	public List<Map<String, Object>> queryForList(String fields, String tablename, String wheresql ) throws Exception {
		return this.queryForList(fields, tablename, wheresql, 0, 0, false);
	}
	@Override
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated
	public List<Map<String, Object>> queryForList(String fields, String tablename, String wheresql, boolean printSQL) throws Exception {
		return this.queryForList(fields, tablename, wheresql, 0, 0,printSQL);
	}
	
	@Override
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated
	public Map<String, Object> queryForObject(String fields, String tablename, String wheresql, boolean printSQL) throws Exception {
		StringBuilder sql = new StringBuilder("select ").append(fields).append(" from ").append(tablename);
		if(StringUtils.isNotBlank(wheresql)){
			sql.append(" where ").append(wheresql);
		}
		if(printSQL) System.out.println(sql.toString());
		try {
			return this.jdbcTemplate.queryForMap(sql.toString());
		} catch (EmptyResultDataAccessException empty) {
			return null;
		}
	}
	
	@Override
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated
	public Map<String, Object> queryForObject(String fields, String tablename, String wheresql) throws Exception {
		return this.queryForObject(fields, tablename, wheresql, false);
	}
	
	
	

	@Override
	public int update(Map<String, Object> key_value, String tables, String wherestr,boolean printSQL) throws Exception {
		int result = 0;
		ArrayList<Object> values = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("update ").append(tables).append(" set ");
		Set<String> keys = key_value.keySet();
		for(Iterator<String> key_iter = keys.iterator(); key_iter.hasNext();){
			String key = key_iter.next();
			Object value = key_value.get(key);
			values.add(value);
			sql.append(key).append(" = ? ");
			if(key_iter.hasNext()) sql.append(",");
		}
		sql.append(wherestr);
		if(printSQL) System.out.println(sql.toString());
		result = this.jdbcTemplate.update(sql.toString(),values.toArray());
		return result;
	}

	@Override
	public int update(String sql, Object[] args, boolean printSQL) throws Exception {
		int result = 0;
		if(printSQL){
			printSQL(sql,args);
		}
		result = this.jdbcTemplate.update(sql.toString(),args);
		return result;
	}
	
	
	private void printSQL(String sql,Object[] args){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String printsql = new String(sql);
			for(Object obj : args){
				if(obj instanceof String){
					printsql = printsql.replaceFirst("\\?", "'"+obj.toString()+"'");
				}else if(obj instanceof Integer || obj instanceof Double ){
					printsql = printsql.replaceFirst("\\?", obj.toString() );
				}else if(obj instanceof Date ){
					printsql = printsql.replaceFirst("\\?", "'"+sdf.format(obj)+"'");
				}else{
					printsql = printsql.replaceFirst("\\?", obj.toString() );
				}
				
			}
			System.out.println(printsql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
}
