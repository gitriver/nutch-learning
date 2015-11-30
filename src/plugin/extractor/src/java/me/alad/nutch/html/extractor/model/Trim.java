package me.alad.nutch.html.extractor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.Validate;

@XmlRootElement
public class Trim extends Function {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one inner function is expected.");
		List<String> res = (List<String>) args.get(0).extract(root, context);

		for (int i = 0; i < res.size(); i++) {
			String item = res.get(i);
			if (item != null)
				item = item.trim();
			res.set(i, item);
		}

		return res;
	}

	@Override
	public String toString() {
		return "Trim []";
	}

}
