package me.alad.nutch.html.extractor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

public class FunctionHolder implements Extractor {

	@XmlElementRef
	private Function value;

	/**
	 * @return the value
	 */
	public Function getValue() {
		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> extract(Object root, EvaluationContext context) throws Exception {
		return (List<String>) value.extract(root, context);
	}

	@Override
	public String toString() {
		return value != null ? value.toString() : "";
	}

}
