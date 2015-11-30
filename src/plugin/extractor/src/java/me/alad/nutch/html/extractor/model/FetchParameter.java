package me.alad.nutch.html.extractor.model;

import javax.xml.bind.annotation.XmlAttribute;

public class FetchParameter {

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String value;

	public FetchParameter() {
	}

	public FetchParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "FetchParameter [name=" + name + ", value=" + value + "]";
	}

}
