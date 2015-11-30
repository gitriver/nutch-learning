package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.nutch.crawl.CrawlDBTestUtil;
import org.apache.nutch.crawl.Generator;
import org.apache.nutch.crawl.Injector;
import org.apache.nutch.fetcher.Fetcher;
import org.apache.nutch.segment.SegmentReader;


/**
 * DateTime: 2015年7月2日 上午11:30:58
 *
 */
public class CustomPluginTest {
    public final static Path testdir = new Path("D:/gitwork/nutch-release-1.10/build/test/customplugin");
    public static Configuration conf;
    public static FileSystem fs;
    public static Path crawldbPath;
    public static Path segmentsPath;
    public static Path urlPath;


    public static void main(String[] args) throws IOException {
        init();
        fetch();
    }


    public static void fetch() throws IOException {
        if (fs.exists(testdir)) {
            fs.delete(testdir, true);
        }
        fs.mkdirs(testdir);

        ArrayList<String> urls = new ArrayList<String>();   
//        urls.add("http://www.oschina.net/question/2006379_167113?fromerr=jssNMBx2");
//        urls.add("http://baike.baidu.com/view/2955.htm");
//        urls.add("http://baike.baidu.com/view/347473.htm");
        urls.add("http://news.ifeng.com/a/20151124/46362281_0.shtml");

        CrawlDBTestUtil.generateSeedList(fs, urlPath, urls);

        System.out.println(segmentsPath.toString());

        String[] injectArgs = { crawldbPath.toString(), urlPath.toString() };
        String[] generatorArgs = { crawldbPath.toString(), segmentsPath.toString(), "-topN","1","-noFilter" };
        String[] fetchArgs = { segmentsPath.toString() + "/","-threads","2" };
        String[] readsegArgs =
                { "-dump", segmentsPath.toString() + "/", testdir.toString() + "/readseg", "-noparsetext",
                 "-noparse", "-noparsedata" };

        try {
            ToolRunner.run(conf, new Injector(), injectArgs);
            ToolRunner.run(conf, new Generator(), generatorArgs);
            File segPath = new File(segmentsPath.toString());
            String[] list = segPath.list();
            print(Arrays.asList(list));
            fetchArgs[0] = fetchArgs[0] + list[0];
            ToolRunner.run(conf, new Fetcher(), fetchArgs);

            readsegArgs[1] += list[0];
            SegmentReader.main(readsegArgs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Configuration createConfiguration() {
        Configuration conf = new Configuration();
        conf.addResource("nutch-default.xml");
        conf.addResource("nutch-site.xml");
//        conf.addResource("crawl-tests.xml");
        return conf;
    }


    public static void init() throws IOException {
        conf = createConfiguration();
        fs = FileSystem.get(conf);
        fs.delete(testdir, true);
        urlPath = new Path(testdir, "urls");
        crawldbPath = new Path(testdir, "crawldb");
        segmentsPath = new Path(testdir, "segments");
    }


    public static void destroy() throws IOException {
        fs.delete(testdir, true);
    }


    public static final void print(Object text) {
        System.out.println(text);
    }
}
