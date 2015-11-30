package me.alad.nutch.html.extractor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

@XmlRootElement
public class Json extends Function {

	private static final Logger LOGGER = LoggerFactory.getLogger(Json.class);

	@XmlAttribute(required = true)
	private String regex;

	@XmlAttribute
	private String page;

	@XmlAttribute
	private String totalPage;

	@XmlAttribute
	private int pageSize;

	@XmlAttribute
	private String condition;

	// @XmlIDREF
	@XmlElement(name = "jattr")
	private List<JsonAttr> jsonAttrs;

	@XmlElement(name = "jlist")
	private List<JsonList> jsonList;

	public String getRegex() {
		return regex;
	}

	public String getCondition() {
		return condition;
	}

	public String getPage() {
		return page;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public List<JsonList> getJsonList() {
		return jsonList;
	}

	public List<JsonAttr> getJsonAttrs() {
		return jsonAttrs;
	}

	public void setJsonAttrs(List<JsonAttr> jsonAttrs) {
		this.jsonAttrs = jsonAttrs;
	}

	@Override
	public List<JSON> extract(Object root, EvaluationContext context) throws Exception {
		Element rootEle = (Element) context.getMainRoot();
		if (regex != null && !"".equals(regex.trim())) {
			try {
				String jsonData = "";
				Pattern pattenr = Pattern.compile(regex);
				Matcher matcher = pattenr.matcher(rootEle.html());
				if (matcher.find()) {
					jsonData = matcher.group(1);
					jsonData = Util.jsonObjFilter(jsonData, rootEle.baseUri());
					JSON jsonObj = JSON.parseObject(jsonData); // 数组反序列化
					if (jsonObj != null) {
						List<JSON> list = new ArrayList<JSON>();
						list.add(jsonObj);
						return list;
					}
				}
			} catch (Exception e) {
				LOGGER.warn("JSON parse Error:" + e);
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "JSON Parse [" + regex + "]";
	}
}
