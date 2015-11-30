package cm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import me.alad.nutch.html.extractor.core.Content;
import me.alad.nutch.html.extractor.core.ExtractEngine;
import me.alad.nutch.html.extractor.core.ExtractedDoc;
import me.alad.nutch.html.extractor.model.ExtractorConfig;
import me.alad.nutch.html.extractor.protocol.DirectHttpProtocol;
import me.alad.nutch.html.extractor.protocol.Protocol;
import me.alad.nutch.html.extractor.protocol.ProtocolFactory;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.metadata.SpellCheckedMetadata;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.parse.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 测试HTML提交信息
 *
 */
public class UrlTester {
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectHttpProtocol.class);


    public static void main(String[] args) throws Exception {
        LOGGER.info("AAA");
        String url = "http://news.ifeng.com/a/20151124/46362281_0.shtml";
//        testUrl(url, "conf/extractors.xml");
        
        testParse(url);
    }


    public static Configuration createConfiguration() {
        Configuration conf = new Configuration();
        conf.addResource("nutch-default.xml");
        conf.addResource("nutch-site.xml");
        String[] dirs = conf.getStrings("plugin.folders");
        for(String dir:dirs){
            System.out.println(dir);
        }
        return conf;
    }


    public static void testParse(String urlStr) throws Exception {
        Configuration conf = createConfiguration();
        ParseUtil parse = new ParseUtil(conf);
        URL url = new URL(urlStr);
        Protocol protocol = ProtocolFactory.getInstance().getProtocol(url);
        Content content = protocol.fetch(url, new HashMap<String, Object>());
        SpellCheckedMetadata metaData = new SpellCheckedMetadata();
        metaData.add("Host", "news.ifeng.com");
        metaData.add("Content-Type", content.getType());
        metaData.add("charset", "UTF-8");
        org.apache.nutch.protocol.Content con =
                new org.apache.nutch.protocol.Content(urlStr, urlStr, readStream(content.getData())
                    .getBytes(), "text/html", metaData, conf);
        ParseResult result = parse.parse(con);
    }


    public static String readStream(InputStream in) throws IOException {
        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = in.read(b)) != -1) {
            sb.append(new String(b, 0, len,"UTF-8"));
        }
        in.close();
        System.out.println(sb);
        return sb.toString();
    }


    /**
     * UrlTester <url> <location-of extractors.xml> e.g. http://www.google.com
     * /conf/extractors.xml file:/saved.html /conf/extractors.xml
     * 
     * @param urlStr
     * @param extracotrPath
     * @throws Exception
     */
    public static void testUrl(String urlStr, String extracotrPath) throws Exception {
        File path = new File(extracotrPath);
        System.out.println("Read config form " + path.getAbsolutePath());
        ExtractorConfig extractorConfig = null;
        Reader configReader = null;
        try {
            configReader = new BufferedReader(new FileReader(path));
            extractorConfig = ExtractorConfig.readConfig(configReader);
        }
        finally {
            if (configReader != null)
                configReader.close();
        }
        ExtractEngine engine = new ExtractEngine(extractorConfig);

        URL url = new URL(urlStr);
        System.out.println("Extracting from " + url.toString());
        Protocol protocol = ProtocolFactory.getInstance().getProtocol(url);
        Content content = protocol.fetch(url, new HashMap<String, Object>());

        List<ExtractedDoc> res = engine.extract(content);
        for (ExtractedDoc doc : res) {
            System.out.println("-------------------");
            System.out.println(doc);
        }
    }
}
