package me.alad.nutch.html.extractor.protocol;

import org.apache.commons.lang3.Validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProtocolFactory {

	private static ProtocolFactory instance;

	/**
	 * @return the instance
	 */
	public static ProtocolFactory getInstance() {
		if (instance == null)
			instance = new ProtocolFactory();
		return instance;
	}

	private Map<String, Protocol> protocolsByName = new HashMap<String, Protocol>();

	public ProtocolFactory() {
		addProtocol("file", new FileProtocol());
		addProtocol("http", new DirectHttpProtocol());
	}

	public Protocol getProtocol(String url) throws MalformedURLException {
		return getProtocol(new URL(url));
	}

	public void addProtocol(String name, Protocol protocol) {
		Validate.notNull(name);
		Validate.notNull(protocol);

		protocolsByName.put(name, protocol);
	}

	public Protocol getProtocol(URL url) {
		Validate.notNull(url);

		Protocol protocol = protocolsByName.get(url.getProtocol());
		Validate.notNull(protocol, "Unknow protocol for " + url);
		return protocol;
	}

	public void setConf(Config conf) {
		for (Protocol protocol : protocolsByName.values()) {
			protocol.setConf(conf);
		}
	}

}
