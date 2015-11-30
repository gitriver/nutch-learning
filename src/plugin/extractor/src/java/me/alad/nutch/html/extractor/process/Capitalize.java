package me.alad.nutch.html.extractor.process;

import org.apache.commons.lang3.StringUtils;

public class Capitalize implements Processor {

	@Override
	public Object process(Object input) {
		return StringUtils.capitalize((String) input);
	}

}
