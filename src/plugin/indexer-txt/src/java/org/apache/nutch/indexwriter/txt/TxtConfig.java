package org.apache.nutch.indexwriter.txt;

public class TxtConfig {
	private boolean require = false;// 不能为空
	private String dest = null;// 目标
	private String source = null;// 来源
	private String constant = null;// 常量数据
	private String type = null;// 数据类型
	private String format = null;// 格式化
	private String concat = null;// 连接
	private String joinsym = null;// 连接符

	public TxtConfig(String dest, String source, String constant, String type, String format, String concat, String joinsym, boolean require) {
		super();
		this.dest = dest;
		this.source = source;
		this.constant = constant;
		this.type = type;
		this.format = format;
		this.concat = concat;
		this.joinsym = joinsym;
		this.require = require;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getConstant() {
		return constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getConcat() {
		return concat;
	}

	public void setConcat(String concat) {
		this.concat = concat;
	}

	public String getJoinsym() {
		return joinsym;
	}

	public void setJoinsym(String joinsym) {
		this.joinsym = joinsym;
	}

	public boolean isRequire() {
		return require;
	}

	public void setRequire(boolean require) {
		this.require = require;
	}

}
