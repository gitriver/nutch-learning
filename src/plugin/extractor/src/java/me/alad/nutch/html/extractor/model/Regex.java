package me.alad.nutch.html.extractor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class Regex extends Function {

	private static final Logger LOGGER = LoggerFactory.getLogger(Regex.class);

	@XmlAttribute
	private String value;

	@XmlAttribute
	private String group;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		Element rootEle = (Element) root;
		String html = rootEle.html();

		Pattern pattern = Pattern.compile(value);
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			List<String> list = new ArrayList<String>();
			if (StringUtils.isNotBlank(group)) {
				int groupCount = matcher.groupCount();
				String[] groupIdx = StringUtils.split(group, ",");
				for (String idx : groupIdx) {
					try {
						int i = Integer.valueOf(idx);
						if (i > groupCount) {
							LOGGER.warn("regex group index outof!");
							continue;
						}
						list.add(matcher.group(Integer.valueOf(idx)));
					} catch (Exception e) {
						LOGGER.warn("get regex value failure!" + e);
					}
				}
			} else {
				list.add(matcher.group());
			}
			return list;
		} else {
			LOGGER.warn("regex matcher failure!");
		}

		return null;
	}

	@Override
	public String toString() {
		return "Regex [value=" + value + "]";
	}

}
