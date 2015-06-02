/**
 * Copyright(c)2012 Beijing PeaceMap Co. Ltd.
 * All right reserved. 
 */
package com.bjcathay.qt.uptutil;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * 网络操作类
 */
public class HttpRequestUtil {
    private Context context;


    /**
     * 获取远程数据
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String getSourceResult(String url, Map<String, String> paramMap, Context mContext) {
        try {
            String url_ = "https://api.weibo.com/2/statuses/friends_timeline.json?source=4191347294";
            HttpGet httpGetRequest = new HttpGet(url_);
            List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
            /*if(paramMap!=null && !paramMap.isEmpty()){
                for (Iterator<Map.Entry<String,String>> it = paramMap.entrySet().iterator(); it.hasNext();) {
                    String key = it.next().getKey();
                    String value = it.next().getValue();
                    httpParams.add(new BasicNameValuePair(key,value));
                }
               // httpPostRequest.setEntity(new UrlEncodedFormEntity(httpParams,HTTP.UTF_8));
            }*/
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory
                    .getSocketFactory(), 443));
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                    null, schReg);
            // SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
            /*Scheme sch = new Scheme("https", socketFactory, 443);
            HttpClient mHttpClient = new DefaultHttpClient();
            mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);*/


            //sClient = new DefaultHttpClient(conMgr, params);
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            // defaultHttpClient.getConnectionManager().getSchemeRegistry().getSchemeRegistry().register(schReg);
            /*if(IntentUtil.getNetType(mContext).equals(IntentUtil.Type._CMWAP)){
                HttpHost proxy = new HttpHost("10.0.0.172", 80);
                defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            }*/
            defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
            defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
            HttpResponse httpResponse = defaultHttpClient.execute(httpGetRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void paramsSetting() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            // 获取当前正在使用的APN接入点
            Uri uri = Uri.parse("content://telephony/carriers/preferapn");
            Cursor mCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (mCursor != null && mCursor.moveToFirst()) {
                // 游标移至第一条记录，当然也只有一条
                String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
                if (proxyStr != null && proxyStr.trim().length() > 0) {
                    HttpHost proxy = new HttpHost(proxyStr, 80);
                    //client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
                }
                mCursor.close();
            }
        }
    }

    /**
     * Post请求连接Https服务
     *
     * @param serverURL 请求地址
     * @param jsonStr   请求报文
     * @return
     * @throws Exception
     */

    public static synchronized String doHttpsPost(String serverURL, String jsonStr) throws Exception {
        String url_ = "https://api.weibo.com/2/statuses/friends_timeline.json?source=4191347294";
        // 参数
        HttpParams httpParameters = new BasicHttpParams();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        // 设置socket超时
        HttpConnectionParams.setSoTimeout(httpParameters, 3000);
        // 获取HttpClient对象 （认证）
        HttpClient hc = initHttpClient(httpParameters);
        HttpPost post = new HttpPost(url_);
        // 发送数据类型
        post.addHeader("Content-Type", "application/json;charset=utf-8");
        // 接受数据类型
        post.addHeader("Accept", "application/json");
        // 请求报文
        StringEntity entity = new StringEntity(jsonStr, "UTF-8");
        post.setEntity(entity);
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    /**
     * Get请求连接Https服务
     *
     * @param serverURL 请求地址
     * @param jsonStr   请求报文
     * @return
     * @throws Exception
     */
    public static synchronized String doHttpsGet(String serverURL, String jsonStr) throws Exception {
        String url_ = "https://api.weibo.com/2/statuses/public_timeline.json?source=4191347294";
        // 参数
        HttpParams httpParameters = new BasicHttpParams();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        // 设置socket超时
        HttpConnectionParams.setSoTimeout(httpParameters, 3000);
        // 获取HttpClient对象 （认证）
        HttpClient hc = initHttpClient(httpParameters);
        HttpGet get = new HttpGet(url_);
        // 发送数据类型
       /* get.addHeader("Content-Type", "application/json;charset=utf-8");
        // 接受数据类型
        get.addHeader("Accept", "application/json");*/
        // 请求报文
        //StringEntity entity = new StringEntity(jsonStr, "UTF-8");
        //get.setEntity(entity);
        get.setParams(httpParameters);
        //client=initHttpClient(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(get);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }



    private static HttpClient client = null;

    /**
     * 初始化HttpClient对象
     *
     * @param params
     * @return
     */
    public static synchronized HttpClient initHttpClient(HttpParams params) {
        if (client == null) {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
                //允许所有主机的验证
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
                // 设置http和https支持
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                e.printStackTrace();
                return new DefaultHttpClient(params);
            }
        }
        return client;
    }

    public static class SSLSocketFactoryImp extends SSLSocketFactory {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryImp(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        X509Certificate[] chain,
                        String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        X509Certificate[] chain,
                        String authType)
                        throws CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    /**
     * 1.浏览器访问https地址，保存提示的证书到本地，放到android项目中的assets目录。
     * 2.导入证书，代码如下。
     * 3.把证书添加为信任
     */
    public static String requestHTTPSPage(Context context, String mUrl) {
        InputStream ins = null;
        String result = "";
        try {
            ins = context.getAssets().open("my.key"); // 下载的证书放到项目中的assets目录中
            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            Certificate cer = cerFactory.generateCertificate(ins);
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
            keyStore.load(null, null);
            keyStore.setCertificateEntry("trust", cer);

            SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
            Scheme sch = new Scheme("https", socketFactory, 443);
            HttpClient mHttpClient = new DefaultHttpClient();
            mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);

            BufferedReader reader = null;
            try {
                HttpGet request = new HttpGet();
                request.setURI(new URI(mUrl));
                HttpResponse response = mHttpClient.execute(request);
                if (response.getStatusLine().getStatusCode() != 200) {
                    request.abort();
                    return result;
                }

                reader = new BufferedReader(new InputStreamReader(response
                        .getEntity().getContent()));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                result = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ins != null)
                    ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}