package me.alad.nutch.html.extractor.evaluation;

import javax.xml.namespace.NamespaceContext;

import me.alad.nutch.html.extractor.core.Content;

import org.w3c.dom.Element;

public class XPathContext extends EvaluationContext {

	private final NamespaceContext nsContext;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public XPathContext(Evaluator<XPathContext> engine, Content content, Element root, NamespaceContext nsContext) {
		super((Evaluator) engine, content, root);
		this.nsContext = nsContext;
	}

	/**
	 * @return the root
	 */
	@Override
	public Element getMainRoot() {
		return (Element) mainRoot;
	}

	/**
	 * @return the nsContext
	 */
	public NamespaceContext getNsContext() {
		return nsContext;
	}

}
