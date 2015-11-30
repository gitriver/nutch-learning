package cm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.sourceforge.htmlunit.corejs.javascript.NativeObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Test_ymatou {

    public static void main(String[] args) {
        Document doc = getDoc("http://baike.baidu.com/item/%E5%8F%A4%E8%A7%82%E9%9F%B3%E7%A6%85%E5%AF%BA");
        Elements res = doc.select("meta[name=description]");
        
        System.out.println("head meta" + res.size()+"ss"+res.attr("content") );
    }


    private static Document getDoc(String url) {
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(url), 30000);
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc;
    }


    private static void getDynamicData(String url) {
        // 使用json來parse html
        // http://www.ymatou.com/product/37606c439e954c2cba9f2ce5de2c47ec.html
        // http://www.ymatou.com/product/7f9e480e5f694b5681c468bcabca0ce9.html
        try {
            Document doc = getDoc(url);

            String js = null;
            Pattern pattenr = Pattern.compile("var _goodsData =([\\S|\\s]+?)\\};");
            System.out.println(doc.html());
            Matcher matcher = pattenr.matcher(doc.html());
            if (matcher.find()) {
                System.out.println(matcher.group());
                js = matcher.group();

                // 使用ScriptEngine來parse
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
                Object aaa = engine.eval(js);

                // 取得你要的變數
                NativeObject obj = (NativeObject) engine.get("_goodsData");
                Object aa = obj.get("name");
                System.out.println("_goodsData = " + aa);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
