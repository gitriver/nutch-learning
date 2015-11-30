package com.ju51.nutch.plugin.protocol.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.protocol.Protocol;
import org.apache.nutch.protocol.ProtocolOutput;
import org.apache.nutch.protocol.RobotRulesParser;

import crawlercommons.robots.BaseRobotRules;


/**
 * DateTime: 2015年7月2日 上午11:17:19
 *
 */
public class TestProtocol implements Protocol {
    private Configuration conf = null;


    @Override
    public Configuration getConf() {
        return conf;
    }


    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }


    @Override
    public ProtocolOutput getProtocolOutput(Text url, CrawlDatum datum) {
        Content c =
                new Content(url.toString(), url.toString(), "hello world".getBytes(), "text/html",
                    new Metadata(), this.conf); // 返回的网页内容为"hello world"
        return new ProtocolOutput(c);
    }


    @Override
    public BaseRobotRules getRobotRules(Text url, CrawlDatum datum) {
        return RobotRulesParser.EMPTY_RULES; // 没有robot规则
    }

}
