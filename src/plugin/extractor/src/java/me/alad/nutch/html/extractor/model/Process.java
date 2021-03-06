package me.alad.nutch.html.extractor.model;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.Validate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class Process extends Function {

	@XmlIDREF
	@XmlAttribute(name = "processor")
	private ProcessorDef processor;

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one inner functions are expected.");

		List<Object> res = (List<Object>) args.get(0).extract(root, context);
		for (int i = 0; i < res.size(); i++) {
			res.set(i, processor.getProcessorInstance().process(res.get(i)));
		}
		return res;
	}

	@Override
	public String toString() {
		return "Process [" + super.toString() + "]";
	}

}
