package me.alad.nutch.html.extractor.model;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.Validate;

import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class Raw extends Function {

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");
		List<?> res = args.get(0).extract(root, context);
		return context.getEvaluator().getRaw(context, res);
	}

	@Override
	public String toString() {
		return "Raw [" + super.toString() + "]";
	}
}
