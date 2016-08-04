package com.truthso.ip360.utils;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.net.Proxy;
import android.util.Log;

public class HttpUtil
{
  private static HttpClient httpClient;

  public static synchronized HttpClient getHttpClient()
  {
    if (httpClient == null) {
      HttpParams params = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(params, 30000);

      HttpConnectionParams.setSoTimeout(params, Configuration.SO_TIMEOUT_INT);

      String proxyHost = Proxy.getDefaultHost();

      SchemeRegistry schReg = new SchemeRegistry();
      schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

      ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
      httpClient = new DefaultHttpClient(conMgr, params);

      if (proxyHost != null) {
        HttpHost proxy = new HttpHost(proxyHost, Proxy.getDefaultPort());
        httpClient.getParams().setParameter("http.route.default-proxy", proxy);
      }
    }
    return httpClient;
  }

  public static synchronized HttpClient getDefaultHttpClient()
  {
    BasicHttpParams httpParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams,
				Configuration.SO_TIMEOUT_INT);
    HttpClient client = new DefaultHttpClient(httpParams);
    return client;
  }

  public static HttpPost getPost(String uri, List<BasicNameValuePair> params, String strParams)
  {
    HttpPost post = new HttpPost(uri);
    try {
      if (strParams != null) {
        StringEntity se = new StringEntity(strParams, "UTF-8");
        se.setContentType("text/xml");
        post.setEntity(se);
        if (params != null)
        {
          post.setHeader("Content-Type", "text/xml;charset=utf-8");
          post.getParams().setParameter("http.socket.timeout", Integer.valueOf(6000));

          for (BasicNameValuePair pair : params) {
            post.addHeader(pair.getName(), pair.getValue());
          }
        }
      }
      else if (params != null)
      {
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
      }

    }
    catch (UnsupportedEncodingException e)
    {
      Log.e("HttpUtil", "获取HttpPost时异常", e);
    }
    return post;
  }

//  public static HttpPost getStream(String uri, List<BasicNameValuePair> valuePair, byte[] stream, boolean inZip, ProgressEntity.ProgressListener progressListener)
//    throws Exception
//  {
//    String paramStr = "";
//    if (valuePair != null) {
//      for (BasicNameValuePair pair : valuePair) {
//        paramStr = paramStr + (paramStr = "&" + pair.getName() + "=" + pair.getValue());
//      }
//    }
//
//    if (!paramStr.equals("")) {
//      paramStr = paramStr.replaceFirst("&", "?");
//      uri = uri + paramStr;
//    }
//
//    if (inZip) {
//      stream = gZIPData(stream);
//    }
//
//    ByteArrayEntity arrayEntity = new ProgressEntity(stream, progressListener);
//    arrayEntity.setContentType("application/octet-stream");
//
//    HttpPost post = new HttpPost(uri);
//    try {
//      if (stream != null) {
//        post.setEntity(arrayEntity);
//      }
//
//    }
//    catch (Exception e)
//    {
//      Log.e("HttpUtil", "获取HttpPost时异常", e);
//    }
//    return post;
//  }

  public static byte[] gZIPData(byte[] inBytes) throws Exception {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    GZIPOutputStream gzipos = new GZIPOutputStream(bout);
    gzipos.write(inBytes);
    gzipos.flush();
    gzipos.close();
    byte[] bOutArray = bout.toByteArray();
    bout.close();
    return bOutArray;
  }

//  public static HttpResult getString(HttpPost post, int requestLimit)
//  {
//    if (requestLimit < 1) {
//      return null;
//    }
//
//    int currCount = 0;
//    HttpResult result = new HttpResult();
//    while (currCount < requestLimit)
//    {
//      HttpClient httpClient = getHttpClient();
//      currCount++;
//      try {
//        HttpResponse response = httpClient.execute(post);
//        int statusCode = response.getStatusLine().getStatusCode();
//        result.setNetCode(statusCode);
//        if (statusCode == 200)
//        {
//          result.setCode(200);
//          if (response.getEntity() != null) {
//            result.setResult(EntityUtils.toString(response.getEntity(), "UTF-8"));
//          }
//          return result;
//        }
//        post.abort();
//        result.setCode(201);
//      }
//      catch (ClientProtocolException e) {
//        result.setException(e);
//        result.setCode(202);
//        Log.e("HttpUtil", "请求失败", e);
//        if (currCount <= requestLimit)
//          continue;
//        break;
//      }
//      catch (IOException e) {
//        result.setException(e);
//        e.printStackTrace();
//        if ((e instanceof ConnectTimeoutException))
//        {
//          result.setCode(203);
//        } else if ((e instanceof SocketTimeoutException))
//        {
//          result.setCode(203);
//        }
//        else result.setCode(204);
//
//        if (currCount <= requestLimit) continue;
//        break;
//      }
//      catch (Exception e) {
//        result.setException(e);
//        if (currCount <= requestLimit) continue; 
//      }break;
//    }
//
//    return result;
//  }
//
//  public static HttpResult getBytes(HttpPost post, int requestLimit)
//  {
//    if (requestLimit < 1) {
//      return null;
//    }
//
//    int currCount = 0;
//    HttpResult result = new HttpResult();
//    while (currCount < requestLimit)
//    {
//      HttpClient httpClient = getHttpClient();
//      currCount++;
//      try {
//        HttpResponse response = httpClient.execute(post);
//        int statusCode = response.getStatusLine().getStatusCode();
//        result.setNetCode(statusCode);
//        if (statusCode == 200)
//        {
//          result.setCode(200);
//          result.setBytes(EntityUtils.toByteArray(response.getEntity()));
//          return result;
//        }
//        post.abort();
//        result.setCode(201);
//      }
//      catch (ClientProtocolException e) {
//        e.printStackTrace();
//        result.setException(e);
//        result.setCode(202);
//        if (currCount <= requestLimit)
//          continue;
//        break;
//      }
//      catch (IOException e) {
//        e.printStackTrace();
//        result.setException(e);
//        if ((e instanceof ConnectTimeoutException))
//        {
//          result.setCode(203);
//        }
//        else {
//          result.setCode(204);
//        }
//        if (currCount <= requestLimit) continue;
//        break;
//      }
//      catch (Exception e) {
//        result.setException(e);
//        if (currCount <= requestLimit) continue; 
//      }break;
//    }
//
//    return result;
//  }
//
//  public static void shutdown()
//  {
//    httpClient.getConnectionManager().shutdown();
//    httpClient = null;
//  }
}