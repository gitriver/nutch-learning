package me.alad.nutch.html.extractor.model;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

@XmlRootElement
public class Constant extends Function {

	@XmlAttribute
	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) {
		return Arrays.asList(value);
	}

	@Override
	public String toString() {
		return "Constant [value=" + value + "]";
	}

}
