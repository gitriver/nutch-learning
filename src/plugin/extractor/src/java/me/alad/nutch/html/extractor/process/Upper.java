package me.alad.nutch.html.extractor.process;

public class Upper implements Processor {

	@Override
	public Object process(Object input) {
		return ((String) input).toUpperCase();
	}

}
