package me.alad.nutch.html.extractor.evaluation;

import me.alad.nutch.html.extractor.core.Content;
import me.alad.nutch.html.extractor.core.ExtractedDoc;

public abstract class EvaluationContext {

	protected Content content;
	protected Object mainRoot;
	protected final Evaluator<EvaluationContext> evaluator;
	protected ExtractedDoc currentDoc;

	public EvaluationContext(Evaluator<EvaluationContext> evaluator, Content content, Object mainRoot) {
		this.evaluator = evaluator;
		this.content = content;
		this.mainRoot = mainRoot;
	}

	/**
	 * @return the engine
	 */
	public Evaluator<EvaluationContext> getEvaluator() {
		return evaluator;
	}

	/**
	 * @return the content
	 */
	public Content getContent() {
		return content;
	}

	public void setMainRoot(Object root) {
		this.mainRoot = root;
	}

	/**
	 * @return the root
	 */
	public Object getMainRoot() {
		return mainRoot;
	}

	/**
	 * @return the currentDoc
	 */
	public ExtractedDoc getCurrentDoc() {
		return currentDoc;
	}

	/**
	 * @param currentDoc
	 *            the currentDoc to set
	 */
	public void setCurrentDoc(ExtractedDoc currentDoc) {
		this.currentDoc = currentDoc;
	}

}
