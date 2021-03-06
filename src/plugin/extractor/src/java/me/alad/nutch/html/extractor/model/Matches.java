package me.alad.nutch.html.extractor.model;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

@XmlRootElement
public class Matches extends Function {

	// private String pattern;

	private Pattern compiledPattern;

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	@XmlAttribute
	public void setPattern(String pattern) {
		// this.pattern = pattern;
		this.compiledPattern = Pattern.compile(pattern);
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		return Arrays.asList(compiledPattern.matcher(null).matches());
	}

}
