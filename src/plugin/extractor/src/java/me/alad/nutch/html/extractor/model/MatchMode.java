package me.alad.nutch.html.extractor.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum MatchMode {

	@XmlEnumValue("single")
	SINGLE, @XmlEnumValue("multiple")
	MULTIPLE;

}
