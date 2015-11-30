package me.alad.nutch.html.extractor.protocol;

import java.net.URL;
import java.util.Map;

import me.alad.nutch.html.extractor.core.Content;

public interface Protocol {

	public static final String PARAM_LAST_MODIFIED = "lastModified";

	void setConf(Config conf);

	Content fetch(URL url, Map<String, Object> parameters) throws ProtocolException;

}
