package me.alad.nutch.html.extractor.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@XmlRootElement
public class JsonList extends Function {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonList.class);

	@XmlAttribute(required = true)
	@XmlID
	private String key;

	@XmlElement(name = "jattr")
	private List<JsonAttr> jsonAttrs;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<JsonAttr> getJsonAttrs() {
		return jsonAttrs;
	}

	public void setJsonAttrs(List<JsonAttr> jsonAttrs) {
		this.jsonAttrs = jsonAttrs;
	}

	@Override
	public String toString() {
		return "JSON List Attr [name=" + key + "]";
	}

	@Override
	public List<JSONArray> extract(Object root, EvaluationContext context) throws Exception {
		if (root instanceof JSONObject) {
			try {
				JSONObject json = (JSONObject) root;
				JSONArray jsonArray = json.getJSONArray(key);
				if (jsonArray != null) {
					List<JSONArray> result = new ArrayList<JSONArray>();
					result.add(jsonArray);
					return result;
				}
			} catch (Exception e) {
				LOGGER.warn("JSON List Attr Parse Error:" + e);
			}
		}
		return null;
	}

}
