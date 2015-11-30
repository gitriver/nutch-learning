package me.alad.nutch.html.extractor.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

import me.alad.nutch.html.extractor.convert.Converter;

public class TypeDef {

	@XmlAttribute
	@XmlID
	private String name;

	private String converter;

	private Converter converterInstance;

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setConverter(String converter) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.converter = converter;
		converterInstance = (Converter) Class.forName(converter).newInstance();
	}

	public String getConverter() {
		return converter;
	}

	public Converter getConverterInstance() {
		return converterInstance;
	}

}
