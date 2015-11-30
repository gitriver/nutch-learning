package me.alad.nutch.html.extractor.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

public class ExtractTo extends FunctionHolder {

	@XmlIDREF
	@XmlAttribute(required = true)
	private Field field;

	@XmlAttribute
	private String prefix;

	@XmlAttribute
	private String suffix;

	public Field getField() {
		return field;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	@Override
	public String toString() {
		return "ExtractTo [field=" + field + ", value=" + getValue() + "]";
	}

	@Override
	public List<String> extract(Object root, EvaluationContext context) throws Exception {
		List<String> list = super.extract(root, context);
		if (list != null && (getPrefix() != null || getSuffix() != null)) {
			List<String> dataList = new ArrayList<String>();
			for (String val : list) {
				if (val != null && !"".equals(val)) {
					if (getPrefix() != null) {
						val = getPrefix() + val;
					}

					if (getSuffix() != null) {
						val += getSuffix();
					}
				}
				dataList.add(val);
			}
			return dataList;
		}
		return list;
	}

}
