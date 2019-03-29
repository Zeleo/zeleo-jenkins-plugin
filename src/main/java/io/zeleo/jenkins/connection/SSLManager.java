package io.zeleo.jenkins.connection;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

public class SSLManager {
	private static final SSLContext trustZeleoSslContext;
	
	/**
	 * We need to make a post to the Zeleo server. Rather than require the Jenkins operator to add our
	 * wildcard to their truststore, we're going to make an exception for our own domain. 
	 */
	private static final TrustManager[] trustZeleoCerts = new TrustManager[] {
		new X509TrustManager() {
			@Override
		    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
		    }

		    @Override
		    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
		    }

		    @Override
		    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		      return new java.security.cert.X509Certificate[]{};
		    }
		}
	};
	
	static {
		try {
	        trustZeleoSslContext = SSLContext.getInstance("SSL");
	        trustZeleoSslContext.init(null, trustZeleoCerts, new java.security.SecureRandom());
	    } catch (NoSuchAlgorithmException | KeyManagementException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	private static final SSLSocketFactory trustZeleoSslSocketFactory = trustZeleoSslContext.getSocketFactory();
	
	/**
	 * We own the server we're connecting to- we're going to trust the connection so long as it is our domain
	 * of origin.
	 * 
	 * @param client The OkHttpClient we're using.
	 * @return Whether we are connecting to our Zeleo app domain.
	 */
	public static OkHttpClient trustZeleoSslClient(OkHttpClient client) {
		Builder builder = client.newBuilder();
		builder.sslSocketFactory(trustZeleoSslSocketFactory, (X509TrustManager)trustZeleoCerts[0]);
		builder.hostnameVerifier(new HostnameVerifier() {
			@Override
		    public boolean verify(String hostname, SSLSession session) {
				if(hostname.equals("applications.zeleo.io")) {
					return true;
				} else {
					return false;
				}
		    }
		});
		return builder.build();
	}
}
