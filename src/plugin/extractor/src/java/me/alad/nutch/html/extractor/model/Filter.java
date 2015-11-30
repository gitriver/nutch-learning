package me.alad.nutch.html.extractor.model;

import javax.xml.bind.annotation.XmlElementRef;

public class Filter {

	@XmlElementRef
	private Function value;

	/**
	 * @return the value
	 */
	public Function getValue() {
		return value;
	}

}
