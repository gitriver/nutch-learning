package me.alad.nutch.html.extractor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.core.ExtractedDoc.LinkData;
import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.StringUtils;

@XmlRootElement
public class Link extends Function {

	@XmlElement(name = "href", required = true)
	private FunctionHolder href;

	@XmlElement(name = "anchor", required = true)
	private FunctionHolder anchor;

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		List<LinkData> res = new ArrayList<LinkData>();
		List<String> hrefs = href.extract(root, context);
		List<String> anchors = anchor == null ? Collections.EMPTY_LIST : anchor.extract(root, context);
		Iterator<String> anchorI = anchors.iterator();
		for (String href : hrefs) {
			if (!StringUtils.isBlank(href)) {
				String a = anchorI.hasNext() ? anchorI.next() : "";
				a = StringUtils.defaultIfBlank(a, href);
				LinkData link = new LinkData(href, a.trim());
				res.add(link);
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return "Link [href=" + href + ", anchor=" + anchor + "]";
	}

}
