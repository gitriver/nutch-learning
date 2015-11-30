package me.alad.nutch.html.extractor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.Validate;

@XmlRootElement
public class Attribute extends Function {

	@XmlAttribute
	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");
		List<?> res = args.get(0).extract(root, context);
		return context.getEvaluator().getAttribute(context, res, name);
	}

	@Override
	public String toString() {
		return "Attribute [" + (name != null ? "name=" + name + ", " : "") + super.toString() + "]";
	}

}
