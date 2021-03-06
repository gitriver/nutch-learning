package me.alad.nutch.html.extractor.evaluation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

public class MapNamespaceContext implements NamespaceContext {

	private final Map<String, String> namespaces = new HashMap<String, String>();

	public void addNamespace(String prefix, String uri) {
		namespaces.put(prefix, uri);
	}

	/**
	 * @return the namespaces
	 */
	public Map<String, String> getNamespaces() {
		return namespaces;
	}

	@Override
	public String getNamespaceURI(String prefix) {
		return namespaces.get(prefix);
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return null;
	}

	@Override
	public Iterator<?> getPrefixes(String namespaceURI) {
		return null;
	}

}
