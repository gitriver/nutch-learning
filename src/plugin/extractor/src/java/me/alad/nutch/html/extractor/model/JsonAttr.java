package me.alad.nutch.html.extractor.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

@XmlRootElement
public class JsonAttr extends Function {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonAttr.class);

	@XmlAttribute(required = true)
	@XmlID
	private String key;

	@XmlAttribute(required = true)
	private String extractTo;

	@XmlAttribute
	private String split;

	@XmlAttribute
	private String indexForm;

	@XmlAttribute
	private String prefix;

	@XmlAttribute
	private String suffix;

	@XmlAttribute
	private boolean cleanHtml;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getExtractTo() {
		return extractTo;
	}

	public void setExtractTo(String extractTo) {
		this.extractTo = extractTo;
	}

	public String getIndexForm() {
		return indexForm;
	}

	public void setIndexForm(String indexForm) {
		this.indexForm = indexForm;
	}

	public String getPrefix() {
		if (prefix == null) {
			return "";
		}
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		if (suffix == null) {
			return "";
		}
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isCleanHtml() {
		return cleanHtml;
	}

	public void setCleanHtml(boolean cleanHtml) {
		this.cleanHtml = cleanHtml;
	}

	@Override
	public String toString() {
		return "JSON Attr [name=" + key + "]";
	}

	@Override
	public List<String> extract(Object root, EvaluationContext context) throws Exception {
		if (root instanceof JSONObject) {
			try {
				String val = "";
				JSONObject json = (JSONObject) root;
				if (key.contains(".")) {
					String[] jsonKey = StringUtils.split(key, ".");
					for (int i = 0; i < jsonKey.length - 1; i++) {
						json = json.getJSONObject(jsonKey[i]);
					}
					val = json.getString(jsonKey[jsonKey.length - 1]);
				} else {
					val = json.getString(key);
				}
				if (StringUtils.isNotBlank(val)) {
					if (cleanHtml) {
						try {
							val = Jsoup.clean(val, Whitelist.none());
						} catch (Exception e) {
							LOGGER.warn("Jsoup clean html failure:" + e);
						}
					}
					List<String> result = new ArrayList<String>();
					if (StringUtils.isNotBlank(split)) {
						String[] vals = StringUtils.split(val, split);
						index(result, vals);
					} else {
						result.add(getPrefix() + val + getSuffix());
					}
					return result;
				}
			} catch (Exception e) {
				LOGGER.warn("JSON Attr Parse Error:" + e);
			}
		}
		return null;
	}

	private void index(List<String> result, String[] vals) {
		if (result == null || vals == null) {
			return;
		}

		if (StringUtils.isNotBlank(getIndexForm())) {
			String[] idxs = StringUtils.split(getIndexForm(), Fragment.ExtractToSplit);
			int valSize = vals.length;
			for (String idx : idxs) {
				try {
					int index = Integer.valueOf(idx);
					if (index > valSize) {
						break;
					}
					result.add(getPrefix() + vals[index] + getSuffix());
				} catch (Exception e) {
					LOGGER.warn("json indexForm index exception :" + e);
				}
			}
		}
	}

}
