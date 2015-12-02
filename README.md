##学习nutch, 通过配置采集模板、存储模板，实现互联网数据的自动采集

##修改内容如下
#http.content.limit 				
设置为页面内容的大小				nutch-site.xml
从-1修改为5M 5*1024*1024
	<property>
		<name>http.content.limit</name>
		<value>5242880</value>
		<description>The length limit for downloaded content using the file://
			protocol, in bytes. If this value is nonnegative (>=0), content
			longer
			than it will be truncated; otherwise, no truncation at all. Do
			not
			confuse this setting with the http.content.limit setting.
		</description>
	</property>

#parser.character.encoding.default	
设置为正确的编码（针对反爬虫才有效)	nutch-default.xml
从windows-1252修改为utf-8
	<property>
  		<name>parser.character.encoding.default</name>
  		<value>utf-8</value>
  		<description>The character encoding to fall back to when no other information
  		is available</description>
	</property>

#增加是否跳过获取robot的配置
在nutch-site.xml增加如下配置，修改了org.apache.nutch.fetcher.Fetcher.java,org.apache.nutch.parse.ParseSegment.java类
	<property>
		<name>parser.skip.robot</name>
		<value>true</value>
	</property>

#增大EXPECTED_COMPRESSION_RATIO
修改org.apache.nutch.util.DeflateUtils的EXPECTED_COMPRESSION_RATIO，从5调大至于20

#增加根据配置提取网页内容的插件
extractor