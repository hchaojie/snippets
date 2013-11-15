package cn.hchaojie.snippets.java.fishdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Collector {
    private static Map<String, String> EXCHANGE_TABLE = new HashMap<String, String>();
    static {
        EXCHANGE_TABLE.put("鱼兆", "鮡");  // zhao
        EXCHANGE_TABLE.put("鱼丕", "魾");  // pi
        EXCHANGE_TABLE.put("鱼平", "鲆");  // ping
        EXCHANGE_TABLE.put("鱼周", "鲷");  // diao
        EXCHANGE_TABLE.put("鱼央", "鱼央");  //
        EXCHANGE_TABLE.put("鱼危", "鮠");  // wei
        EXCHANGE_TABLE.put("鱼尝", "鲿");  // chang
        EXCHANGE_TABLE.put("鱼屯", "鲀");  // tun
        EXCHANGE_TABLE.put("鱼旨", "鮨");  // yi
        EXCHANGE_TABLE.put("鱼是", "鳀");  // ti
        EXCHANGE_TABLE.put("鱼将", "鳉");  // jiang
        EXCHANGE_TABLE.put("鱼齐", "鲚");  // ji
        EXCHANGE_TABLE.put("鱼甾", "鲻");  // zi
        EXCHANGE_TABLE.put("鱼箴", "鱵");  // zhen
        EXCHANGE_TABLE.put("鱼幼", "䱂");  // you
        EXCHANGE_TABLE.put("鱼宗", "鯮");  // zong
        EXCHANGE_TABLE.put("鱼感", "鳡");  // gan
        EXCHANGE_TABLE.put("鱼管", "鳤");  // guan
        EXCHANGE_TABLE.put("鱼旁", "鳑");  // pang
        EXCHANGE_TABLE.put("鱼皮", "鲏");  // pi
        EXCHANGE_TABLE.put("鱼巴", "鲃");  // ba
        EXCHANGE_TABLE.put("鱼句", "鮈");  // ju
        EXCHANGE_TABLE.put("鱼骨", "鱼骨");
        EXCHANGE_TABLE.put("鱼泉", "鳈");  // quan
        EXCHANGE_TABLE.put("鱼它", "鮀");  // tuo
        EXCHANGE_TABLE.put("鱼白", "鲌");  // bo
        EXCHANGE_TABLE.put("鱼康", "鱇");  // kang
        EXCHANGE_TABLE.put("鱼鼠", "鱼鼠");
        EXCHANGE_TABLE.put("鱼乔", "鱼乔");
        EXCHANGE_TABLE.put("鱼丹", "鱼丹");
        EXCHANGE_TABLE.put("鱼岁", "鱼岁");
        EXCHANGE_TABLE.put("鱼祭", "鰶");  // ji
    }

    private static final String FILE_PATH = "data/";

    private static final String DIRECTORY_1 = FILE_PATH + "director.htm";
    private static final String DIRECTORY_2 = FILE_PATH + "director-1.htm";
    private static final String DIRECTORY_3 = FILE_PATH + "director-2.htm";

    private static final String HREF_PATTERN = "\\d{1,3}(-[1-2]){0,1}\\.htm";

    private static final String EXCHANGE_PATTERN = "\\(.{2}\\)|\\[.{2}\\]";
    private static final String ENCLOSING_SIGN_PATTERN = "\\(|\\)|\\[|\\]";

    private static List<Family> families = new ArrayList<Family>();
    private static List<Fish> fishes = new ArrayList<Fish>();

    public static void main(String[] args) {
        parseFamilyFile(DIRECTORY_1);
        parseFamilyFile(DIRECTORY_2);
        parseFamilyFile(DIRECTORY_3);

        // printData();
        saveData();
    }

    private static void saveData() {
        DatabaseHelper db = new DatabaseHelper();
        db.setup();

        for (Family f : families) {
            db.addFamily(f);
        }

        for (Fish f : fishes) {
            db.addFish(f);
        }

        db.shutDown();
    }

    private static void parseFamilyFile(String file) {
        File input = new File(file);
        Document doc;
        try {
            doc = Jsoup.parse(input, "gbk");

            Elements tds = doc.getElementsByTag("td");

            Family currentFamily = null;
            int i = 0;
            for (Element e : tds) {  // skip the first two
                i++;
                if (i == 1 || i == 2) continue;

                String text = e.text().trim();
                if (text.length() == 0) continue;

                String num = text.replaceAll("\\D", ""); // extract digital
                String str = text.replaceAll("[\\d|\\s|\u3000]", ""); // extract text
                str = exchange(str);

                if (num.length() == 0) { // categary
                    currentFamily = new Family(str);
                    families.add(currentFamily);
                } else {
                    String href = e.select("a").first().attr("href");
                    if (href == null || href.isEmpty() || !href.matches(HREF_PATTERN)) {
                        href = "";
                    }

                    Fish fish = new Fish();
                    fish.setName(str);
                    fish.setHref(href);
                    readDescription(fish, href);
                    fish.setFamily(currentFamily);
                    fishes.add(fish);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readDescription(Fish fish, String href) {
        if (href == null || href.isEmpty()) return;

        File input = new File(FILE_PATH + href);
        Document doc;
        try {
            doc = Jsoup.parse(input, "gbk");
            Elements ps = doc.getElementsByTag("p");

            if (ps.size() < 3) return;
            fish.setLatinName(ps.get(1).text());

            String p2 = ps.get(2).text();
            if (p2.matches("\\(.+\\)")) {
                p2 = exchange(p2);
                fish.setCommonName(p2);
            }

            Elements images = doc.select("p img");
            if (images.size() == 1) {
                fish.setImageFile(FILE_PATH + images.get(0).attr("src"));
            }

            Elements descs = doc.select("body > p[align=justify], body > p:has(font[size=+0]):not(p[align=center])");
            if (descs.size() != 0) {
                StringBuffer sb = new StringBuffer();
                for (Element e : descs) {
                    sb.append(e.text());
                    sb.append(System.getProperty("line.separator"));
                }

                fish.setDesc(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String exchange(String src) {
        Pattern p = Pattern.compile(EXCHANGE_PATTERN);
        Matcher m = p.matcher(src);

        while (m.find()) {
            String find = m.group();
            String word = find.replaceAll(ENCLOSING_SIGN_PATTERN, "");
            if (EXCHANGE_TABLE.containsKey(word)) {
                src = src.replace(find, EXCHANGE_TABLE.get(word));
            }
        }

        return src;
    }

    private static void sortFish() {
        Collections.sort(fishes, new Comparator<Fish>() {
            @Override
            public int compare(Fish o1, Fish o2) {
                if (o1 == null) return -1;
                if (o2 == null) return 1;

                Family f1 = o1.getFamily();
                Family f2 = o2.getFamily();

                if (f1 == null) return -1;
                if (f2 == null) return 1;

                if (!f1.equals(f2)) {
                    return f1.getName().compareTo(f2.getName());
                } else {
                    String n1 = o1.getName();
                    String n2 = o2.getName();
                    if (n1 == null) return -1;
                    if (n2 == null) return 1;

                    return n1.compareTo(n2);
                }
            }
        });
    }

    private static void printData() {
        StringBuffer output = new StringBuffer();

        for (Fish fish : fishes) {
            output.append(fish.getFamily().getName()).append(',').append(fish.getName()).append(',').append(fish.getHref()).append(',')
                    .append(System.getProperty("line.separator"));
        }

        System.out.println(output);
    }
}

class Family {
    private String name;

    public Family(String str) {
        this.name = str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Fish {
    private String name;
    private Family family;
    private String href;
    private String desc;
    private String latinName;
    private String commonName;
    private String imageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
