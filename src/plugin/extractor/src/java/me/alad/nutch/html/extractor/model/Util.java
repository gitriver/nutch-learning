package me.alad.nutch.html.extractor.model;

import org.apache.commons.lang3.StringUtils;

public class Util {

	public static String jsObjFilter(String html, String domain) {
		if (StringUtils.isNotBlank(html)) {
			html = html.replaceAll("document.location.hostname", "'" + domain + "'");
		}

		return html;
	}

	public static String jsonObjFilter(String html, String domain) {
		return html;
	}
}
