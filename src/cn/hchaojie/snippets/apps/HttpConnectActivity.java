package cn.hchaojie.snippets.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.hchaojie.snippets.R;

public class HttpConnectActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.http_test);
		trustAllHosts();
		
		Log.v("SH", "run http test....");
		Button b = (Button) findViewById(R.id.update_button);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				int[] sizes = new int[2];
				for (int i = 0; i < 2; i++) {
					sizes[i] = do_request();
				}
				Toast.makeText(getApplication(),
						"Requests  returned " + sizes[0] + " and " + sizes[1],
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public int do_request() {
		String str = "";
		try {
			URL url = new URL("https://www.bankofshanghai.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String aux;
			while ((aux = in.readLine()) != null) {
				str += aux;
			}
			in.close();
			Log.w("Test", "Request size " + str.length());
		} catch (Exception e) {
			Log.e("exception", e.getMessage());
			e.printStackTrace();
			return -1;
		}
		return str.length();
	}

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				Log.w("Test", "getAcceptedIssuers");
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				Log.w("Test", "checkClientTrusted");
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				Log.w("Test", "checkServerTrusted");
			}
		} };
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
