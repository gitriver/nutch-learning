package cm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Test_sunig {

	private static Document getDoc() {
		// String url = "http://product.suning.com/104668441.html";
		//String url = "http://product.suning.com/105429311.html";
		String url="http://mdskip.taobao.com/core/initItemDetail.htm?queryMemberRight=false&sellerUserTag=34672672&sellerUserTag3=70368744210560&sellerUserTag4=4297082243&sellerUserTag2=18014587488043016&isUseInventoryCenter=false&tryBeforeBuy=false&sellerPreview=false&isIFC=false&household=false&itemTags=203,331,907,1163,1478,1483,1547,1675,2049,2059,2443,2507,2635,3974,4166,4491,4614,4678,4811,5451,6411,6603,10818,13186,15554,20161,20289,20801,21442,21505,21697,21762,21826,22081,22977,24705,25025,25153,25921,25922,28802,28993,38786,53954,56130,56194,57026,59266,71682&tmallBuySupport=true&tgTag=false&addressLevel=2&isAreaSell=false&notAllowOriginPrice=false&isForbidBuyItem=false&isApparel=false&isRegionLevel=false&service3C=false&cartEnable=true&offlineShop=false&isSecKill=false&progressiveSupport=false&itemId=41847571860&showShopProm=false&callback=setMdskip&timestamp=1436405659445&ref=http%3A%2F%2Flist.tmall.hk%2Fsearch_product.htm%3Fcat%3D55934001%26sort%3Ds%26_ksTS%3D1429863500203_736%26spm%3Da2231.7718719.2014120102.8.YpzfjF%26from%3Dsn_1_rightnav%26_input_charset%3Dutf-8%26style%3Dg%26s%3D0%26search_condition%3D7%26industryCatId%3D52830004%26callback%3D__jsonp_cb%26active%3D1%26abtest%3Dnull";
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL(url), 3000);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	private static final Pattern pattern1 = Pattern.compile("(\"|'\\s+?)\\+([\\s|\\S]+?),");
	private static final Pattern pattern = Pattern.compile(",(\\s+)//([\\s|\\S]+?)");

	public static void main(String[] args) {
		Document doc = getDoc();
		String html = doc.html();
		try {

			String jsonData = "";
			Pattern pattenr = Pattern.compile("var sn = sn \\|\\|([\\s|\\S]+?});");
			Matcher matcher = pattenr.matcher(html);
			if (matcher.find()) {
				jsonData = matcher.group(1);

				Matcher matcher1 = pattern1.matcher(jsonData);
				if (matcher1.find()) {
					jsonData = matcher1.replaceAll("www.baidu.com\",");
				}

				Matcher matcher2 = pattern.matcher(jsonData);
				if (matcher2.find()) {
					jsonData = matcher2.replaceAll("");
				}
System.out.println(jsonData);
				// jsonData="{\"name\":{\"name\":\"ljw\",age:18}}";
				//jsonData = StringUtils.replace(jsonData, "+document.location.hostname", "");
				JSONObject jsonObj = JSON.parseObject(jsonData); // 反序列化
				if (jsonObj != null) {
					System.out.println(jsonObj.getString("itemDisplayName"));
					List<JSONObject> list = new ArrayList<JSONObject>();
					// list.add(jsonObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
