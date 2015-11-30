package me.alad.nutch.html.extractor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

@XmlRootElement
public class Map extends Function {

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		return null;
	}

}
