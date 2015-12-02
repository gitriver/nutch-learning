package test;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.parse.ParseData;
import org.apache.nutch.parse.ParseText;
import org.apache.nutch.protocol.Content;


/**
 * DateTime: 2015年7月1日 上午10:33:38
 *
 */
public class ReadResult {
    public static Configuration conf;
    public static FileSystem fs;
    public static String root = "D:/gitwork/nutch-learning/build/test/customplugin/";
    public static String dir = root + "segments/20151202103518/";
    public static String dbDir = root + "crawldb/";


    public static void main(String[] args) throws IOException {
        conf = SimpleDemo.createConfiguration();
        fs = FileSystem.get(conf);

        // 读取content内容
        // 每个下载页面的内容
        readMapFile(dir + Content.DIR_NAME, new Text(), new Content());
        System.out.println("============================================");

        // 读取crawl_fetch内容
        // 每个下载url的状态
         readMapFile(dir + "crawl_fetch", new Text(), new CrawlDatum());
         System.out.println("============================================");

        // 读取parse_text内容
        // 包含每个解析过的url文本内容
         readMapFile(dir + "parse_text", new Text(), new ParseText());
         System.out.println("============================================");

        // 读取parse_data内容
        // 每个url解析出的外部链接和元数据
         readMapFile(dir +"parse_data", new Text(), new ParseData());
         System.out.println("============================================");

        // 读取crawldb的内容
         readMapFile(dbDir + "current", new Text(), new CrawlDatum());

    }


    public static void readSequenceFile(String dataDir, Writable key, Writable value) throws IOException {
        // verify content
        Path content = new Path(new Path(dataDir), "part-00000/data");
        @SuppressWarnings("resource")
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, content, conf);

        READ_CONTENT: do {
            if (!reader.next(key, value))
                break READ_CONTENT;
            // String contentString = new String(value.getContent());
            System.out.println(String.format("key:%s \n ---- \n content:%s \n", key, value));
        } while (true);

        reader.close();
    }


    public static void readMapFile(String dataDir, WritableComparable key, Writable value) throws IOException {
        Path content = new Path(new Path(dataDir), "part-00000");
        MapFile.Reader reader = new MapFile.Reader(fs, content.toString(), conf);

        while (reader.next(key, value)) {
            System.out.printf("key:%s \n ---- \n content:%s \n", key, value);
        }
        reader.close();
    }

}
