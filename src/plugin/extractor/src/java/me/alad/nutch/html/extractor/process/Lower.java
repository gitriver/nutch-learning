package me.alad.nutch.html.extractor.process;

public class Lower implements Processor {

	@Override
	public Object process(Object input) {
		return ((String) input).toLowerCase();
	}

}
