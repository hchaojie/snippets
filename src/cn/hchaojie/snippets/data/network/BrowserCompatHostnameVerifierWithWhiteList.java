/*
 * Copyright Critical Path 2012
 */
package cn.hchaojie.snippets.data.network;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Locale;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class BrowserCompatHostnameVerifierWithWhiteList implements X509HostnameVerifier {

    HashSet<String> mHostsWhiteList = new HashSet<String>();
    private BrowserCompatHostnameVerifier impl;

    public BrowserCompatHostnameVerifierWithWhiteList(String[] hostsWhiteList) {
        impl = new BrowserCompatHostnameVerifier();
        if (hostsWhiteList != null) {
            for (String host : hostsWhiteList) {
                if (host != null && host.length() > 0) {
                    mHostsWhiteList.add(host);
                }
            }
        }
    }

    public boolean verify(String host, SSLSession session) {
        System.out.println("verify 1 !!!!!!!!!");

        try {
            javax.security.cert.X509Certificate[] chain = session.getPeerCertificateChain();
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }

        // common name is whitelisted so don't perform the check
        if (mHostsWhiteList.contains(host)) return true;

        return impl.verify(host, session);

    }

    public void verify(String host, SSLSocket arg1) throws IOException {
        System.out.println("verify 2 !!!!!!!!!" + " host is" + host);
        if (mHostsWhiteList.contains(host)) return;

        impl.verify(host, arg1);

    }

    public void verify(String host, X509Certificate arg1) throws SSLException {
        System.out.println("verify 3 !!!!!!!!!");
        
        // common name is whitelisted so don't perform the check
        if (mHostsWhiteList.contains(host)) return;

        impl.verify(host, arg1);
    }

    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
        System.out.println("verify 4 !!!!!!!!!");
        
        for (int i = 0; i < cns.length; ++i) {
            // common name is whitelisted so don't perform the check
            if (mHostsWhiteList.contains(cns[i])) return;
        }

        // TODO add wildcard support

        impl.verify(host, cns, subjectAlts, false);
    }

    // @Override
    // public final void verify(final String host, final String[] cns, final String[] subjectAlts) throws SSLException {
    //
    // for (int i = 0; i < cns.length; ++i) {
    // // common name is whitelisted so don't perform the check
    // if (mHostsWhiteList.contains(cns[i])) return;
    // }
    //
    // // TODO add wildcard support
    //
    // impl.verify(host, cns, subjectAlts, false);
    // }
    //
    // @Override
    // public String toString() {
    // return "BROWSER_COMPAT_WITH_WHITELIST";
    // }
    //
    // @Override
    // public boolean verify(String host, SSLSession session) {
    //
    // try {
    // javax.security.cert.X509Certificate[] chain = session.getPeerCertificateChain();
    // } catch (SSLPeerUnverifiedException e) {
    // e.printStackTrace();
    // }
    //
    // // common name is whitelisted so don't perform the check
    // if (mHostsWhiteList.contains(host)) return true;
    //
    // return impl.verify(host, session);
    // }
    //
    // @Override
    // public void verify(String host, SSLSocket arg1) throws IOException {
    // // common name is whitelisted so don't perform the check
    // if (mHostsWhiteList.contains(host)) return;
    //
    // impl.verify(host, arg1);
    //
    // }
    //
    // @Override
    // public void verify(String host, X509Certificate arg1) throws SSLException {
    // // common name is whitelisted so don't perform the check
    // if (mHostsWhiteList.contains(host)) return;
    //
    // impl.verify(host, arg1);
    //
    // }
    
    
    private static String[] WHITE = {
        "*.akamai.net",
        "*.akamaihd.net",
        "a248.e.akamai.net",
        "cpth.ie",
        "devmozufe01.cpth.ie"
    };
    
    public static void main(String[] args) {
        System.out.println(verify("akamai.net", WHITE));
        System.out.println(verify("a248.e.akamai.net", WHITE));
        System.out.println(verify("fbexternal-a.akamaihd.net", WHITE));
    }
    
    private static boolean verify(String host, String[] patterns) {
        String hostName = host.trim().toLowerCase(Locale.US);
        boolean match = false;
        
        // the pattern should be *.co.uk or twitter.co.uk but not twitt*.co.uk
        for(String pattern : patterns) {
            if (pattern.startsWith("*")) {
                match = hostName.endsWith(pattern.substring(1));
            } else {
                match = hostName.equals(pattern);
            }
            
            if (match) {
                break;
            }
        }
        
        return match;
    }
}
