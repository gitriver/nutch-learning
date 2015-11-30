package me.alad.nutch.html.extractor.model;

import java.util.List;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

public interface Extractor {

	/**
	 * Extracts related data from the part of document represented by root.
	 */
	List<?> extract(Object root, EvaluationContext context) throws Exception;

}
