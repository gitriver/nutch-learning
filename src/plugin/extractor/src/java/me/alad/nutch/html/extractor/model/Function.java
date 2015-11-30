package me.alad.nutch.html.extractor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class Function implements Extractor {

	@XmlElementRef
	protected List<Function> args;

	/**
	 * @return the args
	 */
	public List<Function> getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(List<Function> args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return "args=" + args;
	}

}
