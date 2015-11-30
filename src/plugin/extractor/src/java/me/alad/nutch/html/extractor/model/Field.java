package me.alad.nutch.html.extractor.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

public class Field {

	@XmlAttribute
	@XmlID
	private String name;

	@XmlIDREF
	@XmlAttribute(name = "type")
	private TypeDef type;

	@XmlAttribute
	private boolean multi = false;

	@XmlAttribute
	private boolean required = false;

	@XmlAttribute
	private boolean index = true;

	@XmlAttribute
	private String indexAs;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public TypeDef getType() {
		return type;
	}

	/**
	 * @return the multi
	 */
	public boolean isMulti() {
		return multi;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @return the index
	 */
	public boolean isIndex() {
		return index;
	}

	/**
	 * @return the indexAs
	 */
	public String getIndexAs() {
		if (indexAs == null || indexAs.equals(""))
			return name;
		return indexAs;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + "]";
	}

}
