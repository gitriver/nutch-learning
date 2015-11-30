package org.apache.nutch.indexwriter.txt;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.util.ObjectCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TxtMappingReader {
	public static Logger LOG = LoggerFactory.getLogger(TxtMappingReader.class);

	private Configuration conf;

	private Map<String, TxtConfig> fieldConfigMap = new LinkedHashMap<String, TxtConfig>();

	private String requiredSourceField = null;

	public static synchronized TxtMappingReader getInstance(Configuration conf) {
		ObjectCache cache = ObjectCache.get(conf);
		TxtMappingReader instance = (TxtMappingReader) cache.getObject(TxtMappingReader.class.getName());
		if (instance == null) {
			instance = new TxtMappingReader(conf);
			cache.setObject(TxtMappingReader.class.getName(), instance);
		}
		return instance;
	}

	protected TxtMappingReader(Configuration conf) {
		this.conf = conf;
		parseMapping();
	}

	private void parseMapping() {
		InputStream ssInputStream = null;
		ssInputStream = conf.getConfResourceAsInputStream(conf.get(TxtConstants.MAPPING_FILE, "txtindex-mapping.xml"));

		InputSource inputSource = new InputSource(ssInputStream);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputSource);
			Element rootElement = document.getDocumentElement();
			NodeList fieldList = rootElement.getElementsByTagName("field");
			if (fieldList.getLength() > 0) {

				for (int i = 0; i < fieldList.getLength(); i++) {
					Element element = (Element) fieldList.item(i);

					String dest = element.getAttribute("dest");// 目标
					String source = element.getAttribute("source");// 来源

					if (source == null || "".equals(source.trim())) {
						source = dest;
					}
					String constant = element.getAttribute("constant");// 常量数据
					String type = element.getAttribute("type");// 数据类型
					String format = element.getAttribute("format");// 格式化
					String concat = element.getAttribute("concat");// 连接
					String joinsym = element.getAttribute("joinsym");// 连接符

					String requireStr = element.getAttribute("require");// 连接符
					boolean require = false;
					try {
						require = Boolean.valueOf(requireStr);
					} catch (Exception e) {

					}

					fieldConfigMap.put(dest, new TxtConfig(dest, source, constant, type, format, concat, joinsym, require));

					LOG.info("dest: " + dest + " source: " + source + " constant: " + constant + " type: " + type + " format: " + format + " concat: " + concat
							+ " joinsym: " + joinsym);
				}
			}
			NodeList copyFieldList = rootElement.getElementsByTagName("copyField");
			if (copyFieldList.getLength() > 0) {
				for (int i = 0; i < copyFieldList.getLength(); i++) {
					Element element = (Element) copyFieldList.item(i);
					LOG.info("source: " + element.getAttribute("source") + " val: " + element.getAttribute("val"));
				}
			}

			NodeList requiredFieldItem = rootElement.getElementsByTagName("requiredSourceField");
			if (requiredFieldItem.getLength() > 1) {
				LOG.info("More than one requiredSourceField definitions found in solr index mapping");
			} else if (requiredFieldItem.getLength() == 0) {
				LOG.warn("No requiredSourceField definition found in solr index mapping using, default 'id'");
			} else {
				requiredSourceField = requiredFieldItem.item(0).getFirstChild().getNodeValue();
			}
		} catch (MalformedURLException e) {
			LOG.warn(e.toString());
		} catch (SAXException e) {
			LOG.warn(e.toString());
		} catch (IOException e) {
			LOG.warn(e.toString());
		} catch (ParserConfigurationException e) {
			LOG.warn(e.toString());
		}
	}

	public Map<String, TxtConfig> getFieldConfigMap() {
		return fieldConfigMap;
	}

	public void setFieldConfigMap(Map<String, TxtConfig> fieldConfigMap) {
		this.fieldConfigMap = fieldConfigMap;
	}

	public String getRequiredSourceField() {
		return requiredSourceField;
	}

	public void setRequiredSourceField(String requiredSourceField) {
		this.requiredSourceField = requiredSourceField;
	}

}
