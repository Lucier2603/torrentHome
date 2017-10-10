package com.Lucifer2603.torrentHome.crawler;

import com.Lucifer2603.torrentHome.common.entity.MagnetEntity;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.util.MathUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Random;

/**
 * @author zhangchen20
 */
public class TorrentKittyCrawler extends BaseCrawler {

    private static final String baseUrl = "http://bt.askyaya.com";

    String[] kwPrefixes = {"SNIS"};


    public void crawl() {
        for (String prefix : kwPrefixes) {

            for (int i = 1; i < 1000; i++) {
                try {

                    int currentPage = 1;
                    int maxPage = 1;

                    while (currentPage <= maxPage) {
                        String kw = String.format("%s-%03d", prefix, i);
                        String url = baseUrl + "/index.php?r=files/index&kw=" + kw + "&page=" + currentPage;

                        String result = get(url);

//                        System.out.println(result);

                        Document doc = Jsoup.parse(result);
                        Element ul = doc.select("ul").get(2);
                        Elements lis = ul.select("li");

                        for (Element li : lis) {
                            String link = li.getElementsByTag("a").attr("href");

                            // 进入magent页面
                            String magnetUrl = baseUrl + link;
                            processMagnet(magnetUrl);

                        }

                        Element pagination = doc.select("ul.pagination").get(0);
                        Elements lis2 = pagination.select("li");

                        for (Element li2 : lis2) {
//                            System.out.println("$$$ " + li2.text());
                            if (NumberUtils.isNumber(li2.text())) {
                                Integer a = Integer.parseInt(li2.text());
                                maxPage = maxPage > a ? maxPage : a;
                            }
                        }

                        currentPage++;
//                    System.out.println("###" + eles.get(2).text());

                        Thread.sleep(new Random().nextInt(31) * 97 + new Random().nextInt(13) * 79);


                    }

                    // todo
                    if (i == 1) break;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private MagnetEntity processMagnet(String url) {
        try {
            MagnetEntity m = new MagnetEntity();

            Document doc = Jsoup.parse(get(url));

            m.title = doc.select("h3").get(0).text();
            m.date = doc.select("h4").get(0).select("span").get(0).text();
            m.size = doc.select("h4").get(0).select("span").get(1).text();
            m.url = doc.select("h4").get(1).text();

            int ind = m.url.indexOf("magnet:?");
            m.url = m.url.substring(ind);

            System.out.println(JSON.toJSONString(m));
            Thread.sleep(new Random().nextInt(31) * 97 + new Random().nextInt(13) * 79);

            return m;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
