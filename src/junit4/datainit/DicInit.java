package junit4.datainit;

 

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wisdoor.core.model.Dictionary;
import com.wisdoor.core.service.DictionaryService;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.DictionaryUtils;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.vo.CommonVo;

/**
 * 初始化规则
 */
public class DicInit {
	private static DictionaryService dictionaryService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			dictionaryService = (DictionaryService) cxt.getBean("dictionaryImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void initData() throws Exception {  
		//一级
		//dictionaryService.insert(new Dictionary("001", "中小企业划型标准"));
		//dictionaryService.insert(new Dictionary("002", "融资项目贷款方式"));
		
		//二级
  /*	List<CommonVo> cvs=DictionaryUtils.getListByUrlKey("http://localhost:8080/sys_/dictionary/dic_002_1.jsp?rm="+StringUtils.genRandomNum(10),"list");
		for(CommonVo cv :cvs){
			System.out.println("1、"+cv.getString1());
			System.out.println("2、"+cv.getString2());
			String parent =cv.getString1().substring(0,3);
			System.out.println("parent="+parent);
			//dictionaryService.insert(new Dictionary(cv.getString1(), cv.getString2(),parent));
			
			System.out.println("3、"+cv.getString3());
			System.out.println("4、"+cv.getString4());
			System.out.println("5、"+cv.getString5());  
		}*/
		//三级
/*		List<Dictionary> list=dictionaryService.getCommonListData("from Dictionary where parent='001' ");
		 for(Dictionary d:list){
			 System.out.println("id="+d.getId()+",name="+d.getName());
			   
			 List<CommonVo>  cvs=DictionaryUtils.getListByUrlKey2("http://localhost:8080/sys_/dictionary/dic_001_2.jsp","list"+d.getId()); 
			 for(CommonVo cv:cvs){
				   System.out.println("1、"+cv.getString1());
					System.out.println("2、"+cv.getString2());
					String parent =cv.getString1().substring(0,5);
					System.out.println("parent="+parent); 
					String json="{\"yysr\":\""+cv.getString3()+"\",\"cyry\":\""+cv.getString4()+"\",\"zczh\":\""+cv.getString5()+"\"}";
					System.out.println("json="+json); 
					
					System.out.println("3、"+cv.getString3());
					System.out.println("4、"+cv.getString4());
					System.out.println("5、"+cv.getString5());
					//dictionaryService.insert(new Dictionary(cv.getString1(),cv.getString2(), json, parent));
			 }	 
		 }*/
	}
}
