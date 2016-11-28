package com.wisdoor.core.service;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.vo.PageForProcedureVO;

/***
 * 
 * @author   
 * 数据操作封装类
 */
public interface BaseService<T> { 
	/**新增相关**/   
	public Serializable insert(T obj) throws EngineException ; 
	public void insert(List<T> objs) throws EngineException ; 
	
	/**删除相关**/  
	public void delete(Serializable id) throws EngineException ;
	public void delete(List<Serializable> ids) throws EngineException ; 
	
	/**修改相关**/
	public void update(T obj) throws EngineException ;
	public void update(List<T> objs) throws EngineException ;
	
    /**执行HQL语句**/
	public void executeHql(String hql) throws EngineException ;
	
	/**加载单个对象**/  
 	public T selectById(Serializable id); 
	public T selectById(String sqlHql, String... value);
	public T selectById(String sqlHql, Object[] queryParams);
	public T selectByHql(String sqlHql); 
	
	/**普通查询**/
	public List<T> getCommonListData(String sqlHql);
	public List<T> getScrollDataCommon(String sqlHql, String... value);
	public List<T> getScrollData(final String sqlHql, final String[] value, final int start, final int number);
	public List<T> getScrollData(final String sqlHql,final String value, final int start, final int number);
	public long getScrollDataCount(final String sqlHql,final String... value);
	public long getScrollDataCount(String sqlHql);
	
	/**分组统计查询**/
	public QueryResult<Object> groupHqlQuery(final String hql) throws Exception;	
	
	/*
	 * 根据criteria返回实体列表
	 */
	public List<T> getListForCriteria(DetachedCriteria criteria);
	/**
	 * 获取查询和或分页数据 
	 * @param firstindex 开始索引
	 * @param maxresult 需要获取的记录数
	 * @return
	 */
	public Long getScrollCount(final  String wherejpql, final List<String> queryParams, final  LinkedHashMap<String, String> orderby) throws Exception;
	public double getScrollSum(final  String wherejpql, final List<String> queryParams, final  LinkedHashMap<String, String> orderby) throws Exception;
	public QueryResult<T> getScrollData(int firstindex, int maxresult, String wherejpql, List<String> queryParams,LinkedHashMap<String, String> orderby) throws Exception;
	
	public QueryResult<T> getScrollData(int firstindex, int maxresult, String wherejpql, List<String> queryParams) throws Exception;
	
	public QueryResult<T> getScrollData(int firstindex, int maxresult, LinkedHashMap<String, String> orderby) throws Exception;
	
	public QueryResult<T> getScrollData(int firstindex, int maxresult) throws Exception;

	public QueryResult<T> getScrollData(String wherejpql, List<String> queryParams) throws Exception; 
	
	public QueryResult<T> getScrollData( String wherejpql, List<String> queryParams, LinkedHashMap<String, String> orderby) throws Exception;
	 
	public QueryResult<T> getScrollData(String  jpql) throws Exception; 
	
	public QueryResult<T> getScrollData()  throws Exception;
	public ArrayList<LinkedHashMap<String,Object>> resultsetToList(ResultSet rs);
	/**
	 * 执行存储过程的方法（返回记录集列表）
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
	public ArrayList<LinkedHashMap<String,Object>> callProcedureForList(String procedureName, final Map<Integer, Object> inParamList,final OutParameterModel outParameter);
	/**
	 * 调用存储过程，返回值列表
	 * @param procedureName 存储过程名称
	 * @param inParamList   
	 * @param outParameter
	 * @return key为下标
	 */
	public HashMap<Integer,Object> callProcedureForParameters(String procedureName, final Map<Integer, Object> inParamList,final Map<Integer, Integer> outParameter);
	public String generationExcuteProdure(String procedureName, int paramListLength);
	
	class OutParameterModel{
		public OutParameterModel(int index,int type){
			this.index = index;
			this.type = type;
		}
		private int index = 0;
		private int type = 0;
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		
		
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		/**
		 * 判断是否有效,如果type为0，表示无效
		 * @return
		 */
		public boolean valid(){
			return (this.type == 0 ? false:true);
			
		}
		
	}
	
	public ArrayList<LinkedHashMap<String,Object>> selectListWithJDBC(String sql)throws Exception;
	public int selectcount(String sql)throws Exception;
	public float selectsum(String sql)throws Exception;
	
	public PageForProcedureVO callProcedureForPage(String procedureName, final Map<Integer, Object> inParamList);
	public boolean callProcedure(String procedureName, final Map<Integer, Object> inParamList);
    /* 
     * 聚集函数的计算结果
     * avg(), sum(), max(), count()
     */
	public Object queryJhFunction(String hql);
	/**
	 * 查询单个对象
	 * @param fields
	 * @param tablename
	 * @param wheresql
	 * @param printSQL
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryForObject(String fields, String tablename, String wheresql,Object[] args,boolean printSQL) throws Exception;
	public Map<String,Object> queryForObject(String fields, String tablename, String wheresql,Object[] args) throws Exception;
	public List<Map<String,Object>> queryForList(String fields, String tablename, String wheresql,Object[] args, int page, int pagesize) throws Exception;
	public List<Map<String,Object>> queryForList(String fields, String tablename, String wheresql,Object[] args, int page, int pagesize, boolean printSQL) throws Exception;
	public List<Map<String,Object>> queryForList(String fields, String tablename, String wheresql,Object[] args) throws Exception;
	public List<Map<String,Object>> queryForList(String fields, String tablename, String wheresql,Object[] args, boolean printSQL) throws Exception;
	public int queryForListTotal(String fieldname,String tablename, String wheresql,Object[] args) throws Exception;
	public int queryForListTotal(String fieldname,String tablename, String wheresql,Object[] args, boolean printSQL) throws Exception;
	
	public int update(String sql,Object[] args,boolean printSQL) throws Exception;;
	
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated public Map<String,Object> queryForObject(String fields, String tablename, String wheresql,boolean printSQL) throws Exception;
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated public Map<String,Object> queryForObject(String fields, String tablename, String wheresql) throws Exception;
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated public List<Map<String,Object>> queryForList(String fields, String tablename, String wheresql, int page, int pagesize) throws Exception;
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated public List<Map<String,Object>> queryForList(String fields, String tablename, String wheresql, int page, int pagesize, boolean printSQL) throws Exception;
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated public List<Map<String,Object>> queryForList(String fields, String tablename, String wheresql) throws Exception;
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated public List<Map<String,Object>> queryForList(String fields, String tablename, String wheresql, boolean printSQL) throws Exception;
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated public int queryForListTotal(String fieldname,String tablename, String wheresql) throws Exception;
	/**
	 * <span style="color:red">不建议使用，有SQL注入风险,看接口里，有新方法</span>
	 */
	@Deprecated public int queryForListTotal(String fieldname,String tablename, String wheresql, boolean printSQL) throws Exception;
	
	
	
	@Deprecated public int update(Map<String,Object> key_value,String tables,String wherestr, boolean printSQL)throws Exception;
}
