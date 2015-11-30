package me.alad.nutch.html.extractor.evaluation;

import me.alad.nutch.html.extractor.core.Content;

import org.jsoup.nodes.Element;

public class CssContext extends EvaluationContext {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CssContext(Evaluator<CssContext> engine, Content content, Element root) {
		super((Evaluator) engine, content, root);
	}

	/**
	 * @return the root
	 */
	@Override
	public Element getMainRoot() {
		return (Element) mainRoot;
	}

}
