package me.alad.nutch.html.extractor.model;

import org.apache.commons.lang3.Validate;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@XmlRootElement(name = "config")
@XmlType(propOrder = { "types", "processors", "fields", "documents" })
public class ExtractorConfig {

	@XmlAttribute
	private boolean omitNonMatching = false;

	@XmlAttribute
	private boolean filterNonMatching = false;

	@XmlAttribute
	private String defaultEngine = "css";

	@XmlAttribute
	private MatchMode matchMode = MatchMode.SINGLE;

	@XmlElementWrapper(name = "types")
	@XmlElement(name = "type")
	private List<TypeDef> types;

	@XmlElementWrapper(name = "processors")
	@XmlElement(name = "processor")
	private List<ProcessorDef> processors;

	@XmlElementWrapper(name = "fields")
	@XmlElement(name = "field")
	private List<Field> fields;

	@XmlElementWrapper(name = "documents")
	@XmlElement(name = "document")
	private List<Document> documents;

	/**
	 * @return the list
	 */
	public List<Document> getDocuments() {
		return documents;
	}

	public List<TypeDef> getTypes() {
		return types;
	}

	public List<Field> getFields() {
		return fields;
	}

	public boolean isOmitNonMatching() {
		return omitNonMatching;
	}

	/**
	 * @return the filterNonMatching
	 */
	public boolean isFilterNonMatching() {
		return filterNonMatching;
	}

	/**
	 * @return the defaultEngine
	 */
	public String getDefaultEngine() {
		return defaultEngine;
	}

	/**
	 * @return the matchMode
	 */
	public MatchMode getMatchMode() {
		return matchMode;
	}

	public static ExtractorConfig readConfig(Reader configReader) throws Exception {
		Validate.notNull(configReader);

		JAXBContext context = JAXBContext.newInstance(ExtractorConfig.class, Document.class, ExtractTo.class, Filter.class, Fragment.class, Field.class,
				Function.class, Constant.class, TypeDef.class, Text.class, Attribute.class, Concat.class, Expr.class, Replace.class, Truncate.class,
				Trim.class, Url.class, First.class, Last.class, Size.class, Matches.class, Link.class, FunctionHolder.class, Resolve.class, ForEach.class,
				Fetch.class, FieldValue.class, Decode.class, Default.class, ProcessorDef.class, Process.class, Raw.class, Script.class, ScriptField.class,
				Regex.class, Json.class, JsonAttr.class, JsonList.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(ExtractorConfig.class.getResource("/extractors.xsd"));
		unmarshaller.setSchema(schema);

		return (ExtractorConfig) unmarshaller.unmarshal(configReader);
	}

	public static void main(String[] args) {
		try {
			InputStreamReader configReader = new InputStreamReader(new FileInputStream("/opt/zhouhb/workspace_nutch/nutch1-10/conf/extractors.xml"), "UTF-8");
			ExtractorConfig config = ExtractorConfig.readConfig(configReader);
			System.out.println(config.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
