package com.wisdoor.core.cache;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.wisdoor.core.utils.XmlUtils;
 
 
/**
 * @author  
 * @since 2011-12-8
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class CacheConfigUtils {

	private static Log log = LogFactory.getLog(CacheConfigUtils.class);

	private Document document = null;

	public CacheConfigUtils() throws Exception {
		InputStream is = CacheConfigUtils.class.getResourceAsStream("/cacheConfig.xml");
		if (is != null)  
			document = XmlUtils.readXmlDocument(is, false, "cacheConfig");
	}

	/**
	 * 获得xmlRoot
	 * 
	 * @return
	 * @throws Exception
	 */
	public Element getXmlRootElement() throws Exception {
		if (document != null)
			return document.getDocumentElement();
		return null;
	}

	/**
	 * 获得节点
	 * 
	 * @param elementName
	 * @return
	 */
	public Element getElement(String elementName) {
		Element rootElement = null;
		try {
			rootElement = getXmlRootElement();
		} catch (Exception e) {
			log.error("Error getting Service Engine XML root element");
		}
		return XmlUtils.firstChildElement(rootElement, elementName);
	}

	/**
	 * 获得节点的属性
	 * 
	 * @param elementName
	 * @param attrName
	 * @return
	 */
	public String getElementAttr(String elementName, String attrName) {
		Element element = getElement(elementName);
		if (element == null)
			return null;
		return element.getAttribute(attrName);
	}

	/**
	 * 获得自定义计划
	 * 
	 * @return
	 */
	public List<CacheIndex> getCacheConfig() {
		Element cacheConfig = getElement("cache-config");
		List<CacheIndex> result = new ArrayList<CacheIndex>();
		List plans = XmlUtils.childElementList(cacheConfig, "cache");
		if (plans != null) {
			Iterator i = plans.iterator();
			while (i.hasNext()) {
				Element e = (Element) i.next();
				result.add(new CacheIndex(e.getAttribute("name"), e.getAttribute("updatesort"),e.getAttribute("cachetype"), 
						e.getAttribute("sorthql"), e.getAttribute("selecthql"),e.getAttribute("maxresult"),e.getAttribute("paramtype")));
			}
		}
		return result;
	}

}
