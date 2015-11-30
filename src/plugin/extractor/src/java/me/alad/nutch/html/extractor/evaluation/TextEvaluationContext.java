package me.alad.nutch.html.extractor.evaluation;

import java.io.BufferedReader;

import me.alad.nutch.html.extractor.core.Content;

public class TextEvaluationContext extends EvaluationContext {

	private BufferedReader reader;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TextEvaluationContext(Evaluator<TextEvaluationContext> evaluator, Content content, BufferedReader reader) {
		super((Evaluator) evaluator, content, null);
		this.reader = reader;
	}

	/**
	 * @return the reader
	 */
	public BufferedReader getReader() {
		return reader;
	}

}
