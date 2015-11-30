package me.alad.nutch.html.extractor.model;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

public abstract class Rooted implements Extractor {

	@XmlAttribute
	private String root;

	/**
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

	protected List<?> getRoots(Object rootObj, EvaluationContext context) throws Exception {
		if (root == null)
			return Arrays.asList(rootObj);
		return context.getEvaluator().evaluate(rootObj, context, root);
	}

}
