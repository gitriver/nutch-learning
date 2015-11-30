package me.alad.nutch.html.extractor.evaluation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.alad.nutch.html.extractor.core.Content;

public class TextEvaluator implements Evaluator<TextEvaluationContext> {

	@Override
	public List<?> evaluate(Object root, TextEvaluationContext context, String expression) throws Exception {
		if ("line".equals(expression)) {
			List<String> res = new ArrayList<String>();
			while (true) {
				String line = context.getReader().readLine();
				if (line != null)
					res.add(line);
				else
					return res;
			}
		} else if (".".equals(expression)) {
			return Arrays.asList(root);
		}
		return null;
	}

	@Override
	public List<?> getAttribute(TextEvaluationContext context, List<?> input, String name) throws Exception {
		return null;
	}

	@Override
	public List<?> getText(TextEvaluationContext context, List<?> input) throws Exception {
		return input;
	}

	@Override
	public List<?> getRaw(TextEvaluationContext context, List<?> input) throws Exception {
		return input;
	}

	@Override
	public TextEvaluationContext createContext(Content content) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(content.getData(), content.getEncoding()));
		return new TextEvaluationContext(this, content, reader);
	}

	@Override
	public String getName() {
		return "txt";
	}

}
