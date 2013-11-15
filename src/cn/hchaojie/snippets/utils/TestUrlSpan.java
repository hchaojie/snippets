package cn.hchaojie.snippets.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import cn.hchaojie.snippets.R;

public class TestUrlSpan extends Activity {
    private static final String TEST_STRING1 = "aaaaaaaaaaaa http://twitter.com/aabbdd/ddessf.html is good twitt";
    private static final String TEST_STRING2 = "http://t.co/aabbdd/ddessf.html is exception";
    private static final String TEST_STRING3 = "http://s.co";

    private static final String TEST_STRING4 = "aaaaaaaaaaaa http://s.co is a short, http://baidu.com/aaa/dk.ht is long, and "
            + "http://t.co/xJJessk is an exception domain";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.url_span);

        String shortenedContent = UrlUtils.shortenUrlsInString(TEST_STRING1);
        Spannable sp = UrlUtils.getShortLinkSpannable(this, shortenedContent);
        TextView contentText = (TextView) findViewById(R.id.post_body1);
        contentText.setText(sp);
        contentText.setMovementMethod(LinkMovementMethod.getInstance());

        String shortenedContent2 = UrlUtils.shortenUrlsInString(TEST_STRING2);
        Spannable sp2 = UrlUtils.getShortLinkSpannable(this, shortenedContent2);
        TextView contentText2 = (TextView) findViewById(R.id.post_body2);
        contentText2.setText(sp2);
        contentText2.setMovementMethod(LinkMovementMethod.getInstance());

        String shortenedContent3 = UrlUtils.shortenUrlsInString(TEST_STRING3);
        Spannable sp3 = UrlUtils.getShortLinkSpannable(this, shortenedContent3);
        TextView contentText3 = (TextView) findViewById(R.id.post_body3);
        contentText3.setText(sp3);
        contentText3.setMovementMethod(LinkMovementMethod.getInstance());

        String shortenedContent4 = UrlUtils.shortenUrlsInString(TEST_STRING4);
        Spannable sp4 = UrlUtils.getShortLinkSpannable(this, shortenedContent4);
        TextView contentText4 = (TextView) findViewById(R.id.post_body4);
        contentText4.setText(sp4);
        contentText4.setMovementMethod(LinkMovementMethod.getInstance());
    }
}