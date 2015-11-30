package org.apache.nutch.indexwriter.txt;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobConf;
import org.apache.nutch.indexer.IndexWriter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.indexer.NutchField;
import org.apache.nutch.util.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class TxtIndexWriter implements IndexWriter {

	public static final Logger LOG = LoggerFactory.getLogger(TxtIndexWriter.class);
	private static final char fieldDelimiter = 239;
	private TxtMappingReader txtMapping;
	private Configuration config;
	private int batchSize;
	//private int writeSize = 0;

	private String dataDir = null;
	private String fileName = null;
	private StringBuilder txtBuilder = new StringBuilder(10240);
	private int count = 0;
	//private int writeCount = 0;
	private Map<String, String> requiredFieldMap = new HashMap<String, String>();

	public void open(JobConf job, String name) throws IOException {
		init(job);
	}

	// package protected for tests
	void init(JobConf job) throws IOException {
		batchSize = job.getInt(TxtConstants.BATCH_SIZE, 2000);
		//int maxLine = job.getInt(TxtConstants.MAX_LINE, 10);
		//writeSize = batchSize / maxLine;

		txtMapping = TxtMappingReader.getInstance(job);

		String requiredField = txtMapping.getRequiredSourceField();
		LOG.info("required source field config:" + requiredField);
		if (StringUtils.isNotBlank(requiredField)) {
			String[] reqFields = requiredField.split(",");
			for (String f : reqFields) {
				if (StringUtils.isNotBlank(f)) {
					requiredFieldMap.put(StringUtils.trim(f), null);
				}
			}
		}
	}

	@Override
	public void update(NutchDocument doc) throws IOException {
		write(doc);
	}

	@Override
	public void write(NutchDocument doc) throws IOException {
		if (!requiredFieldMap.isEmpty()) {
			Collection<String> fieldNames = doc.getFieldNames();
			for (Iterator<String> iter = requiredFieldMap.keySet().iterator(); iter.hasNext();) {
				if (!fieldNames.contains(iter.next())) {
					LOG.warn("not containe required source field,Ignore this data");
					return;
				}
			}
		}

		if (fileName == null) {
			try {
				NutchField nutchField = doc.getField(MyConstant.NUTCH_TIME);
				String val = "" + nutchField.getValues().get(0);
				fileName = TxtUtils.getStringFromDate("yyyyMMdd", Long.valueOf(val)) + "_" + System.currentTimeMillis() + ".txt";
			} catch (Exception e) {
				fileName = TxtUtils.getStringFromDate("yyyyMMdd", new Date()) + "_" + System.currentTimeMillis() + ".txt";
			}
		}
		Map<String, TxtConfig> configMap = txtMapping.getFieldConfigMap();
		if (!configMap.isEmpty()) {
			StringBuilder builderTmp = new StringBuilder(1024);
			for (Iterator<String> iter = configMap.keySet().iterator(); iter.hasNext();) {
				String dest = iter.next();
				TxtConfig config = configMap.get(dest);

				String val = getValue(doc, config);
				if (Strings.isNullOrEmpty(val = config.getConstant())) {
					val = getValue(doc, config);
				}
				if (config.isRequire() && (val == null || "".equals(val.trim()))) {
					builderTmp.delete(0, builderTmp.length());
					return;
				}

				if (!Strings.isNullOrEmpty(config.getConcat())) {
					TxtConfig concatConfig = configMap.get(config.getConcat());
					String concatVal = null;
					if (Strings.isNullOrEmpty(concatVal = concatConfig.getConstant())) {
						concatVal = getValue(doc, concatConfig);
					}
					if (concatVal != null) {
						val = val + config.getJoinsym() + concatVal;
					}
				}
				builderTmp.append(val).append(fieldDelimiter);
			}

			if (builderTmp.length() > 0) {
				txtBuilder.append(builderTmp).append('\n');
				count++;
			}
		}

		if (count == batchSize) {
			LOG.info("txt indexer writer:" + dataDir + "/" + fileName);
			count = 0;
			//writeCount++;
			TxtUtils.write(dataDir, fileName, txtBuilder.toString());
			txtBuilder.delete(0, txtBuilder.length());
		}

		/*
		 * if (writeCount == writeSize) { fileName =
		 * TxtUtils.getStringFromDate("yyyyMMdd", new Date()) +
		 * TxtUtils.getIndex() + ".txt"; writeCount = 0; }
		 */
	}

	private String getValue(NutchDocument doc, TxtConfig config) {
		NutchField field = doc.getField(config.getSource());
		if (field == null) {
			return "";
		}

		String val = "";
		if (!Strings.isNullOrEmpty(config.getConstant())) {
			return config.getConstant();
		} else {
			val = "" + field.getValues().get(0);
		}

		if ("float".equalsIgnoreCase(config.getType())) {
			val = (val + "").replaceAll(",", "");
		} else if ("double".equalsIgnoreCase(config.getType())) {

		} else if ("long".equalsIgnoreCase(config.getType())) {

		} else if ("date".equalsIgnoreCase(config.getType())) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(config.getFormat() != null ? config.getFormat() : "yyyyMMdd");
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.valueOf(val));
				val = dateFormat.format(cal.getTime());
			} catch (Exception e) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(config.getFormat() != null ? config.getFormat() : "yyyyMMdd");
				val = dateFormat.format(new Date());
			}
		}
		return val;
	}

	@Override
	public Configuration getConf() {
		return config;
	}

	@Override
	public void setConf(Configuration conf) {
		config = conf;
		dataDir = conf.get(TxtConstants.TXT_DATA_DIR);
		if (dataDir == null) {
			String message = "Missing Txt Result Data Directory. Should be set via -D " + TxtConstants.TXT_DATA_DIR;
			message += "\n" + describe();
			LOG.error(message);
			throw new RuntimeException(message);
		}
	}

	public String describe() {
		StringBuffer sb = new StringBuffer("TxtIndexWriter\n");
		sb.append("\t").append(TxtConstants.BATCH_SIZE).append(" : buffer size when write to Txt(default 50000)\n");
		sb.append("\t").append(TxtConstants.MAPPING_FILE).append(" : name of the mapping file for fields (default txtindex-mapping.xml)\n");
		return sb.toString();
	}

	@Override
	public void delete(String key) throws IOException {

	}

	@Override
	public void close() throws IOException {
		TxtUtils.write(dataDir, fileName, txtBuilder.toString());
		txtBuilder.delete(0, txtBuilder.length());
	}

	@Override
	public void commit() throws IOException {
		TxtUtils.write(dataDir, fileName, txtBuilder.toString());
		txtBuilder.delete(0, txtBuilder.length());
	}

}
