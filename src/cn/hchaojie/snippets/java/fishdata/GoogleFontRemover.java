package cn.hchaojie.snippets.java.fishdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleFontRemover {
    private static final String BASE_URL = "/home/ciro/dev/android-sdk-linux/docs/guide/";
    private static final String BASE_URL_2 = "/home/ciro/dev/android-sdk-linux/docs/reference/";
    private static final String BASE_URL_3 = "/home/ciro/dev/android-sdk-linux/docs/training/";
    private static List<File> mFiles = new ArrayList<File>();

    public static void main(String[] args) {
        System.out.println((int) ((float) 50000 / 100000 * 100));
        // tranverseDir(new File(BASE_URL));
        // tranverseDir(new File(BASE_URL_2));
        // tranverseDir(new File(BASE_URL_3));

        // parseFile();
    }

    private static void tranverseDir(File baseDir) {
        File[] files = baseDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File path) {
                String name = path.getName();
                return path.isDirectory() || name.endsWith("html") || name.endsWith("htm");
            }
        });

        for (File file : files) {
            if (file.isDirectory()) {
                tranverseDir(file);
            } else {
                mFiles.add(file);
            }
        }
    }

    private static void parseFile() {
        for (File f : mFiles) {
            santinize(f);
        }
    }

    private static void santinize(File f) {
        Document doc;
        try {
            doc = Jsoup.parse(f, "UTF-8");
            Elements links = doc.select("link[title=roboto], script[src=http://www.google.com/jsapi]");
            for (Element e : links) {
                e.remove();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(doc.html());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
