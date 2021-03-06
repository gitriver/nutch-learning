package me.alad.nutch.html.extractor.nutch;

import me.alad.nutch.html.extractor.core.ExtractEngine;
import me.alad.nutch.html.extractor.model.Document;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.AdaptiveFetchSchedule;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.metadata.Nutch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A fetch schedule that determines revisit schedules based on the values provided in extractors.xml file per document
 * using every/adaptive attributes.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorFetchSchedule extends AdaptiveFetchSchedule {

	public static final Text WRITABLE_SET_INTERVAL = new Text("setExtractorInterval");

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorFetchSchedule.class);

	@Override
	public void setConf(Configuration conf) {
		super.setConf(conf);
		try {
			ExtractEngine.getInstance().setConf(NutchUtils.config(conf));
		} catch (Exception e) {
			LOGGER.error("Exception occured", e);
		}
	}

	@Override
	public CrawlDatum setFetchSchedule(Text url, CrawlDatum datum, long prevFetchTime, long prevModifiedTime,
			long fetchTime, long modifiedTime, int state) {
		// Check whether it previously being set
		if (!datum.getMetaData().containsKey(WRITABLE_SET_INTERVAL)) {
			List<Document> docs = ExtractEngine.getInstance().findMatchingDocs(url.toString(), null);
			if (docs != null && !docs.isEmpty()) {
				Document doc = docs.get(0);
				if (doc != null && doc.getEveryInMiliSecond() != null) {
					datum.setFetchInterval(doc.getEveryInMiliSecond());
					if (!doc.isAdaptive())
						datum.getMetaData().put(Nutch.WRITABLE_FIXED_INTERVAL_KEY,
								new FloatWritable(doc.getEveryInMiliSecond()));
				}
				// set the flag
				datum.getMetaData().put(WRITABLE_SET_INTERVAL, new BooleanWritable(true));
			}
		}
		return super.setFetchSchedule(url, datum, prevFetchTime, prevModifiedTime, fetchTime, modifiedTime, state);
	}

}
