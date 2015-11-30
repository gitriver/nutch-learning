package me.alad.nutch.html.extractor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.jsoup.nodes.Element;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class Script extends Function {

	private static final Logger LOGGER = LoggerFactory.getLogger(Script.class);

	@XmlAttribute(required = true)
	@XmlID
	private String var;

	@XmlAttribute
	private String regex;

	// @XmlIDREF
	@XmlElement(name = "sfield")
	private List<ScriptField> scriptFields;

	public String getVar() {
		return var;
	}

	public String getRegex() {
		return regex;
	}

	public List<ScriptField> getScriptFields() {
		return scriptFields;
	}

	@Override
	public List<NativeObject> extract(Object root, EvaluationContext context) throws Exception {
		// Validate.isTrue(args != null && args.size() == 1,
		// "Only one arg should be specified");
		NativeObject nativeObject = null;

		Element rootEle = (Element) context.getMainRoot();
		if (regex != null && !"".equals(regex.trim())) {
			String myScript = "";
			Pattern pattenr = Pattern.compile(regex);
			Matcher matcher = pattenr.matcher(rootEle.html());
			if (matcher.find()) {
				myScript = matcher.group();
			}
			myScript = Util.jsObjFilter(myScript, rootEle.baseUri());
			Context cx = Context.enter();
			Scriptable scope = cx.initStandardObjects();
			cx.evaluateString(scope, myScript, "extractor_script", 1, null);

			Object obj = scope.get(getVar(), scope);
			if (obj == Scriptable.NOT_FOUND) {
				LOGGER.info(getVar() + " is not defined.");
			} else if (obj instanceof NativeObject) {
				nativeObject = (NativeObject) obj;
			}
		}

		if (nativeObject != null) {
			List<NativeObject> list = new ArrayList<NativeObject>();
			list.add(nativeObject);
			return list;
		}
		return null;
	}

	@Override
	public String toString() {
		return "Script [" + super.toString() + "]";
	}
}
