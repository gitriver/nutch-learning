package cm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test_Tmall {

	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();

		builder.append("<script>\n");
		builder.append("(function(w,d){\n");
		builder.append("try{\n");
		builder.append("var l,url='//mdskip.taobao.com/core/initItemDetail.htm?tryBeforeBuy=false&isIFC=false&sellerUserTag=34672672&notAllowOriginPrice=false&tgTag=false&queryMemberRight=false&sellerUserTag2=18014535948435456&sellerUserTag3=70368744210560&sellerUserTag4=4297082243&progressiveSupport=false&service3C=true&addressLevel=2&isUseInventoryCenter=false&itemId=44201796923&showShopProm=false&isRegionLevel=false&offlineShop=false&isForbidBuyItem=false&itemTags=843,1035,1163,1227,1291,1478,1483,1611,1867,2049,2059,2187,2443,2507,2635,3787,3974,4166,4491,4555,4811,5323,5451,6603,12097,12161,13186,17665,17793,18497,20161,20289,20801,21762,23041,24705,25282,25921,28866,28993,57026,71682&household=false&tmallBuySupport=true&isSecKill=false&sellerPreview=false&isApparel=false&cartEnable=true&isAreaSell=false';\n");
		builder.append("if(!url){return;}\n");
		builder.append("var arr=[\"callback=setMdskip\",\"timestamp=\"+(+new Date())],reg=/[?&^](ip|campaignId|key|abt|cat_id|q|u_channel|areaId)=([^&]+)/g,params=w.location.search;\n");
		builder.append("while(r=reg.exec(params)){arr.push(r[1]+\"=\"+r[2]);}\n");
		builder.append("d.referrer && (arr.push(\"ref=\"+encodeURIComponent(d.referrer)));\n");
		builder.append("w.onMdskip=function(c){l=l?c(l):c}\n");
		builder.append("w.setMdskip=function(v){l=l?l(v):v;}\n");
		builder.append("try{\n");
		builder.append("var head=d.head || d.getElementsByTagName(\"head\")[0];\n");
		builder.append("var script=d.createElement(\"script\");\n");
		builder.append("head.insertBefore(script,head.firstChild);\n");
		builder.append("script.src=url+'&'+arr.join(\"&\");\n");
		builder.append("}\n");
		builder.append("catch(err){\n");
		builder.append("d.write('<script src=\"'+url+'&'+arr.join(\"&\")+'\" async=\"async\"></'+'script>');\n");
		builder.append("}\n");
		builder.append("}catch(e){\n");
		builder.append("w.onMdskip=null;\n");
		builder.append("setTimeout(function(){throw err;},0);\n");
		builder.append("}\n");
		builder.append("}(window,document))\n");
		builder.append("</script>\n");

		Pattern pattern = Pattern.compile("var l,url='([\\S|\\s]+?)';");
		Matcher matcher = pattern.matcher(builder.toString());
		if (matcher.find()) {
			String g = matcher.group(1);
			System.out.println(g);
			
			
		}

	}

}
