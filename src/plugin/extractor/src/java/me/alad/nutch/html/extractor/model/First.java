package me.alad.nutch.html.extractor.model;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.Validate;

@XmlRootElement
public class First extends Function {

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");
		List<?> list = args.get(0).extract(root, context);
		return list.size() > 0 ? Arrays.asList(list.get(0)) : list;
	}

	@Override
	public String toString() {
		return "First [" + super.toString() + "]";
	}
}
