package me.alad.nutch.html.extractor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

@XmlRootElement
public class Expr extends Function {

	@XmlAttribute
	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		return context.getEvaluator().evaluate(root, context, value);
	}

	@Override
	public String toString() {
		return "Expr [value=" + value + "]";
	}

}
