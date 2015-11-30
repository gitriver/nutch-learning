package me.alad.nutch.html.extractor.model;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

@XmlRootElement(name = "field-value")
public class FieldValue extends Function {

	@XmlIDREF
	@XmlAttribute
	private Field field;

	public Field getField() {
		return field;
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) {
		return Arrays.asList(context.getCurrentDoc().getField(field.getName()));
	}

	@Override
	public String toString() {
		return "FieldValue [field=" + field + "]";
	}

}
