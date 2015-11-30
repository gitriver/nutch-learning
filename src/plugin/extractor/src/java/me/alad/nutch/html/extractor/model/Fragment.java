package me.alad.nutch.html.extractor.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;

import me.alad.nutch.html.extractor.core.ExtractedDoc;
import me.alad.nutch.html.extractor.core.ExtractedDoc.LinkData;
import me.alad.nutch.html.extractor.evaluation.EvaluationContext;

import org.apache.commons.lang.StringUtils;
import org.apache.nutch.util.MyConstant;
import org.mozilla.javascript.NativeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class Fragment extends Rooted {

	private static final Logger LOGGER = LoggerFactory.getLogger(Fragment.class);

	public static final String TEXT_FIELD = "content";
	public static final String TITLE_FIELD = "title";
	public static final String URL_FIELD = "url";

	@XmlElement(name = "extract-to")
	private List<ExtractTo> extractTos = new ArrayList<ExtractTo>();

	@XmlElement(name = "script")
	private List<Script> scripts = new ArrayList<Script>();

	@XmlElement(name = "json")
	private List<Json> jsons = new ArrayList<Json>();
	protected static final String ExtractToSplit = ",";

	@XmlElementWrapper(name = "outlinks")
	@XmlElementRef
	private List<Function> outlinks;

	public List<ExtractTo> getExtractTos() {
		return extractTos;
	}

	@Override
	public List<ExtractedDoc> extract(Object root, EvaluationContext context) throws Exception {
		List<ExtractedDoc> docs = new ArrayList<ExtractedDoc>();

		for (Object subRoot : getRoots(root, context)) {
			ExtractedDoc extractedDoc = new ExtractedDoc();
			context.setCurrentDoc(extractedDoc);

			extractFields(subRoot, context, extractedDoc);
			insertSpecialFields(context, extractedDoc);
			extractOutlinks(subRoot, context, extractedDoc);

			docs.add(extractedDoc);
		}

		return docs;
	}

	@SuppressWarnings("unchecked")
	protected void extractFields(Object subRoot, EvaluationContext context, ExtractedDoc extractedDoc) throws Exception {
		for (ExtractTo extractTo : extractTos) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(extractTo.toString());
			List<String> res = extractTo.extract(subRoot, context);
			if (res != null) {
				Field field = extractTo.getField();
				if (field.isMulti()) {
					Object values = extractedDoc.getField(field.getName());
					List<String> fieldValues = res;
					if (values != null) {
						((List<String>) values).addAll(fieldValues);
					} else {
						List<String> fieldValueList = new ArrayList<String>();
						fieldValueList.addAll(fieldValues);
						extractedDoc.addField(field.getName(), fieldValueList);
					}
				} else {
					StringBuilder fieldValue = new StringBuilder();
					join(fieldValue, res);
					extractedDoc.addField(field.getName(), fieldValue.toString());
				}
			}
		}
	}

	protected void extractJsonAttrs(Object subRoot, EvaluationContext context, ExtractedDoc extractedDoc, List<ExtractedDoc> extractedDocs) throws Exception {
		for (Json jsonObj : jsons) {
			// 解析json字符串
			List<JSON> jsonRes = jsonObj.extract(subRoot, context);
			if (jsonRes == null || jsonRes.isEmpty()) {
				continue;
			}
			JSON jsonData = jsonRes.get(0);
			List<LinkData> linkList = null;
			boolean used = false;
			if (StringUtils.isNotBlank(jsonObj.getPage())) {
				boolean hasCondition = false;
				JsonAttr pageAttr = new JsonAttr();
				if (StringUtils.isNotBlank(jsonObj.getCondition())) {
					String[] con = jsonObj.getCondition().split("==");
					if (con.length == 2) {
						pageAttr.setKey(con[0]);
						List<String> resultList = pageAttr.extract(jsonData, context);
						if (resultList != null && con[1].equals(resultList.get(0))) {
							hasCondition = true;
						}
					}
				}
				if (hasCondition) {
					pageAttr.setKey(jsonObj.getTotalPage());
					List<String> resultList = pageAttr.extract(jsonData, context);
					if (resultList != null && resultList.size() > 0) {
						int totalPage = Integer.valueOf(resultList.get(0));
						linkList = new ArrayList<LinkData>(totalPage);

						for (int i = 1; i < totalPage; i++) {
							String myUrl = context.getContent().getUrl() + String.format(jsonObj.getPage(), (i * jsonObj.getPageSize()));
							LinkData linkData = new LinkData(myUrl, myUrl);
							linkList.add(linkData);
							used=true;
						}

					}
				}
			}

			List<JsonList> jsonList = jsonObj.getJsonList();
			List<ExtractedDoc> docList = new ArrayList<ExtractedDoc>();
			if (jsonList != null) {// jlist解析
				for (JsonList jList : jsonList) {
					List<JSONArray> jListData = jList.extract(jsonData, context);// 解析jlist
																					// key列表
					if (jListData != null) {
						JSONArray array = jListData.get(0);// 取得json array数据
						for (int i = 0; i < array.size(); i++) {
							List<JsonAttr> attrList = jList.getJsonAttrs();
							if (attrList == null || attrList.isEmpty()) {
								break;
							}

							ExtractedDoc myDoc = new ExtractedDoc();
							for (Iterator<String> iter = extractedDoc.getFields().keySet().iterator(); iter.hasNext();) {
								String key = iter.next();
								if (URL_FIELD.equalsIgnoreCase(key) || TITLE_FIELD.equalsIgnoreCase(key)) {
									continue;
								}
								myDoc.addField(key, extractedDoc.getField(key));
							}
							myDoc.setSubDoc(true);
							// mainDoc.setUrl(extractedDoc.getUrl());
							myDoc.setText("");
							if (linkList != null && used) {								
								myDoc.setOutlinks(linkList);
								used = false;
							}
							myDoc.setUpdate(false);
							myDoc.addField(MyConstant.NUTCH_TIME, System.currentTimeMillis());

							for (JsonAttr attr : attrList) {
								List<String> tmpList = attr.extract(array.get(i), context);
								if (tmpList == null || tmpList.isEmpty()) {
									continue;
								}
								if (URL_FIELD.equalsIgnoreCase(attr.getExtractTo())) {
									StringBuilder fieldValue = new StringBuilder();
									join(fieldValue, tmpList);
									if (fieldValue.length() > 0) {
										myDoc.setUrl(fieldValue.toString());
									} else {
										LOGGER.debug("the url data is required!");
										break;
									}
								} else if (TITLE_FIELD.equalsIgnoreCase(attr.getExtractTo())) {
									StringBuilder fieldValue = new StringBuilder();
									join(fieldValue, tmpList);
									if (fieldValue.length() > 0) {
										myDoc.setTitle(fieldValue.toString());
									} else {
										LOGGER.debug("the title data is required!");
										break;
									}
								} else {
									extractTo(attr.getExtractTo(), tmpList, myDoc);
								}
							}

							docList.add(myDoc);
						}
					}
				}
			}

			List<JsonAttr> jsonAttrList = jsonObj.getJsonAttrs();
			if (jsonAttrList != null) {// 解析jattr
				for (JsonAttr attr : jsonAttrList) {
					List<String> tmpList = attr.extract(jsonData, context);
					if (tmpList == null || tmpList.isEmpty()) {
						continue;
					}
					if (!docList.isEmpty()) {
						for (ExtractedDoc doc : docList) {
							extractTo(attr.getExtractTo(), tmpList, doc);
						}
					} else {
						extractTo(attr.getExtractTo(), tmpList, extractedDoc);
					}
				}
			}

			if (!docList.isEmpty()) {
				extractedDocs.addAll(docList);
			}

		}
	}

	private void extractTo(String extractTo, List<String> jsval, ExtractedDoc extractedDoc) {
		if (extractTo.contains(ExtractToSplit)) {
			String[] extractTos = StringUtils.split(extractTo, ExtractToSplit);
			for (int i = 0; i < extractTos.length; i++) {
				if (i > jsval.size()) {
					break;
				}
				String eTo = extractTos[i];
				String eVal = jsval.get(i);
				if (StringUtils.isBlank(eTo) || StringUtils.isBlank(eVal)) {
					continue;
				}
				extractedDoc.addField(eTo, eVal);
			}
		} else {
			StringBuilder fieldValue = new StringBuilder();
			join(fieldValue, jsval);
			extractedDoc.addField(extractTo, fieldValue.toString());
		}
	}

	protected void extractScriptFields(Object subRoot, EvaluationContext context, ExtractedDoc extractedDoc) throws Exception {
		for (Script script : scripts) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(script.toString());
			List<NativeObject> res = script.extract(subRoot, context);
			if (res == null) {
				continue;
			}

			List<ScriptField> scriptFields = script.getScriptFields();
			if (scriptFields == null) {
				continue;
			}

			for (ScriptField sfield : scriptFields) {
				List<String> jsval = sfield.extract(res.get(0), context);
				if (jsval!=null && !jsval.isEmpty()) {
					extractTo(sfield.getExtractTo(), jsval, extractedDoc);
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void extractOutlinks(Object root, EvaluationContext context, ExtractedDoc extractedDoc) throws Exception {
		if (outlinks != null) {
			for (Function linkFunction : outlinks) {
				extractedDoc.getOutlinks().addAll((List) linkFunction.extract(root, context));
			}
		}
	}

	protected void insertSpecialFields(EvaluationContext context, ExtractedDoc extractedDoc) {
		Object url = extractedDoc.getFields().get(URL_FIELD);
		if (url != null)
			extractedDoc.setUrl(url.toString());
		else
			throw new RuntimeException("Url field for a document or fragment can not be null. Current url: " + context.getContent().getUrl());

		Object title = extractedDoc.getFields().get(TITLE_FIELD);
		if (title != null)
			extractedDoc.setTitle(title.toString());
		else
			extractedDoc.setTitle(url.toString());

		Object text = extractedDoc.getFields().get(TEXT_FIELD);
		if (text != null) {
			extractedDoc.setText(text.toString());
		} else
			extractedDoc.setText("");
	}

	public static void join(StringBuilder res, List<?> items) {
		for (int i = 0; i < items.size(); i++) {
			Object item = items.get(i);
			if (item instanceof List) {
				join(res, (List<?>) item);
			} else
				res.append(item);
			if (i < items.size() - 1)
				res.append(' ');
		}
	}

	/**
	 * @return the outlinks
	 */
	public List<Function> getOutlinks() {
		return outlinks;
	}

}
