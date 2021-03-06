package me.alad.nutch.html.extractor.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

@XmlRootElement
public class Default extends Function {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() >= 2, "At least two inner functions are expected.");

		for (int i = 0; i < args.size(); i++) {
			List<String> res = (List<String>) args.get(i).extract(root, context);
			if (!isEmpty(res))
				return res;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Decides whether the given list is empty or not
	 */
	private boolean isEmpty(List<String> res) {
		// Event if at least one item in the list is not empty, the list is not
		// empty
		for (String item : res) {
			if (!StringUtils.isEmpty(item))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Default [" + super.toString() + "]";
	}

}
