package cn.hchaojie.snippets.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;

public class UrlUtils {
    public static final HashMap<String, String> sShortUrlCache = new HashMap<String, String>();

    private static final Random RANDOM = new Random();
    private static final String URL_PATTERN = "\\b(?:(?:https?)://|www\\.)[-A-Z0-9+&@#/%=~_|$?!:,.]*[A-Z0-9+&@#/%=~_|$]";

    private static String SAB_SHORT_URL_PREFIX = "http://sab.co/";
    private static final int SAB_SHORT_URL_LENGTH = 8;

    private static final int URL_SHORTEN_MIN_LENGTH = 22;
    private static final String[] EXCEPTION_DOMAIN = { "http://t.co" };

    public static String shortenUrlsInString(String s) {
        Set<String> urls = getUrlsFromString(s);
        for (String url : urls) {
            String shortUrl = shortenUrl(url);
            if (!shortUrl.equals(url)) {
                s = s.replace(url, shortUrl);
            }
        }

        return s;
    }

    private static String shortenUrl(String url) {
        StringBuilder sb = new StringBuilder(SAB_SHORT_URL_PREFIX);
        sb.append(random(SAB_SHORT_URL_LENGTH));

        String result = sb.toString();
        sShortUrlCache.put(result, url);

        return result;
    }

    private static Set<String> getUrlsFromString(String s) {
        if (s == null || s.trim().equals("")) { return new HashSet<String>(); }

        Pattern p = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        HashSet<String> result = new HashSet<String>();
        while (m.find()) {
            String url = m.group();
            if (isUrlLongEnough(url) && !isUrlInExceptionDomains(url)) {
                result.add(url);
            }
        }
        return result;
    }

    private static boolean isUrlLongEnough(String url) {
        return url.length() > URL_SHORTEN_MIN_LENGTH;
    }

    private static boolean isUrlInExceptionDomains(String url) {
        for (String domain : EXCEPTION_DOMAIN) {
            if (url.contains(domain)) { return true; }
        }

        return false;
    }

    /**
     * generate a random string with given char count, this is simple version of the RandomStringUtils in the apache common lang library
     * 
     * @param count
     *            - how many random chars we want
     * @return
     */
    private static String random(int count) {
        Random random = RANDOM;

        if (count == 0) {
            return "";
        } else if (count < 0) { throw new IllegalArgumentException("Requested random string length " + count + " is less than 0."); }

        int start = ' ';
        int end = 'z' + 1;

        char[] buffer = new char[count];
        int gap = end - start;

        while (count-- != 0) {
            char ch = (char) (random.nextInt(gap) + start);

            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                buffer[count] = ch;
            } else {
                count++;
            }
        }

        return new String(buffer);
    }

    public static Spannable getShortLinkSpannable(Context context, String content) {
        return getLinkSpannable(context, content, true);
    }

    public static Spannable getDefaultLinkSpannable(Context context, String content) {
        return getLinkSpannable(context, content, false);
    }

    private static Spannable getLinkSpannable(Context context, String content, boolean shorten) {
        Spannable sp = new SpannableString(content);
        Linkify.addLinks(sp, Linkify.WEB_URLS);
        URLSpan[] spans = sp.getSpans(0, sp.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = sp.getSpanStart(span);
            int end = sp.getSpanEnd(span);
            String url = span.getURL();
            sp.removeSpan(span);

            if (shorten) {
                sp.setSpan(new ShortenedUrlSpan(context, url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sp.setSpan(new InterceptableUrlSpan(context, url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sp;
    }
}