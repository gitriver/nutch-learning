package me.alad.nutch.html.extractor.model;

import java.net.URLDecoder;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.Validate;

@XmlRootElement
public class Decode extends Function {

	private static final String UTF_8 = "UTF-8";

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");

		List<String> res = (List<String>) args.get(0).extract(root, context);
		for (int i = 0; i < res.size(); i++) {
			String url = res.get(i);
			res.set(i, URLDecoder.decode(url, UTF_8));
		}
		return res;
	}

	@Override
	public String toString() {
		return "Decode [" + super.toString() + "]";
	}

}
