package me.alad.nutch.html.extractor.nutch;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;

import me.alad.nutch.html.extractor.core.ExtractedDoc.LinkData;
import me.alad.nutch.html.extractor.model.ExtractorConfig;

import org.apache.commons.lang3.Validate;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.parse.Outlink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:taha.ghasemi@gmail.com">Taha Ghasemi</a>
 */
public class NutchUtils {

    private static final String FILE_CONFIG_KEY = "extractor.file";
    private static final String DEFATUL_CONFIG_FILE = "extractors.xml";
    private static final Logger LOG = LoggerFactory.getLogger(NutchUtils.class);
    /**
     * Returns extractor config from the given nutch config.
     */
    public static ExtractorConfig config(Configuration configuration) throws Exception {
        Validate.notNull(configuration);

        String configFileName = configuration.get(FILE_CONFIG_KEY, DEFATUL_CONFIG_FILE);
        LOG.info("读取Extractor配置：" + configFileName + "......");
       
        InputStreamReader configReader = new InputStreamReader(
                configuration.getConfResourceAsInputStream(configFileName), "UTF-8");
        return ExtractorConfig.readConfig(configReader);
    }

    public static Outlink[] getOutlinksAsArray(List<LinkData> outlinks) throws MalformedURLException {
        Outlink[] res;
        if (outlinks == null)
            res = new Outlink[0];
        else {
            res = new Outlink[outlinks.size()];
            for (int i = 0; i < outlinks.size(); i++) {
                LinkData linkData = outlinks.get(i);
                res[i] = new Outlink(linkData.getUrl(), linkData.getAnchor());
            }
        }
        return res;
    }
}
