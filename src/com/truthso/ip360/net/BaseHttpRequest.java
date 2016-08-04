package com.truthso.ip360.net;

import java.lang.reflect.Type;

import com.loopj.android.http.RequestHandle;


public class BaseHttpRequest <T extends BaseHttpResponse>{
	private T responseData;
//	private BaseHttpRequestCallback resultCallback;
//	private ServiceRequestParams params;
//	protected final String baseUri = URLConstant.SERVER_IP;
	protected final String prefix = "";
	private String path = "";
	private String method = "";
	private RequestHandle handle;
//	protected Gson gson = GsonUtil.getGson();
	private int bufferSize = 8192;
	private Type classType;
//	private ABApiCallback apiCallback;//WeakReference<ABApiCallback> apiCallback;
	private String tokenId;

	public void setPath(String path) {
		this.path = path;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public RequestHandle getHandle() {
		return handle;
	}

//	public ServiceRequestParams params() {
//		if (params == null) {
//			this.initialiseRequestParams();
//		}
//
//		return params;
//	}
	


	public BaseHttpResponse responseData() {
		return responseData;
	}


	
//	BaseHttpRequest(Class<T> classType, BaseHttpRequestCallback callback,String method) {
//		this.classType = classType;
//		this.resultCallback = callback;
//		this.method = method ;
//		initialiseRequestParams();
//	}

	
	/**
	 * 公共参数
	 */
//	void initialiseRequestParams() {
//		String sn = UUID.randomUUID().toString();
//		String hashcode = CryptoUtils.generateSNHash(sn, this.method,
//				App.platformId);
//		String devicemodel = android.os.Build.MODEL;
//		String osversion = android.os.Build.VERSION.RELEASE;
//		tokenId = (String) SharePreferenceUtil.getAttributeByKey(App.getInstance(), KeyConstant.SP_USER, KeyConstant.tokenId, SharePreferenceUtil.VALUE_IS_STRING);
//		String channel_id = (String) SharePreferenceUtil.getAttributeByKey(App.getInstance(), KeyConstant.SP_USER, KeyConstant.channelId, SharePreferenceUtil.VALUE_IS_STRING);
//		String user_id = (String) SharePreferenceUtil.getAttributeByKey(App.getInstance(), KeyConstant.SP_USER, KeyConstant.userId, SharePreferenceUtil.VALUE_IS_STRING);
//		String appTokenId = "";
//		if(!CheckUtil.isEmpty(channel_id) && !CheckUtil.isEmpty(user_id)){
//			appTokenId = channel_id + "|" + user_id;
//		}else if(!CheckUtil.isEmpty(channel_id) && CheckUtil.isEmpty(user_id)){
//			appTokenId = channel_id ;
//		}
//			
//		params = new ServiceRequestParams();
//		params.add("method", method);
//		params.add("sn", sn);
//		params.add("msg_id", sn);
//		params.add("hashcode", hashcode);
//		params.add("platformId", App.platformId);
//		params.add("mode", "jsonios");
//		params.add("tokenId", tokenId);
//		params.add("deviceType", "Android");
//		params.add("appVersion", App.getInstance().getVersionName());
//		params.add("deviceAppId", App.getInstance().getDeviceAppId());
//		params.add("appChannel", App.getInstance().getAppChannel());
//		params.add("platformSecret", CryptoUtils.getOauthKey());
//		
//		//  使用百度云push消息, 使用channel_id+|（分隔符）+ user_id 标示客户
//		params.put("appTokenId", appTokenId);
//		params.put("deviceImei", App.IMEI);
////		params.put("deviceImei", "357458044240280");
//		params.put("deviceModel", devicemodel);
//		params.put("osVersion", osversion);
//		
//	}
	
//	public void removeParameters(String... keys){
//		for(String key: keys){
//			this.params.remove(key);
//		}
//	}

	protected String configureUri(String uri) {
		if ((this.prefix == null) || (uri.startsWith(this.prefix))) {
			return uri;
		}
		return this.prefix + uri;
	}

//	protected String createUri(String path) {
//		return this.baseUri + configureUri(path);
//	}

//	public String createGetUri() {
//		return this.createUri(path);
//	}
//
//	public String createPostUri() {
//		return this.createUri(path) ;
//	}

//	public RequestHandle post() {
//		
//		this.handle = AsyncHttpClient.post(this.createPostUri(), params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] responseBody) {
//						BaseHttpRequest.this.onSuccess(statusCode, headers,
//								responseBody);
//
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] responseBody, Throwable error) {
//						BaseHttpRequest.this.onFailure(statusCode, headers,
//								responseBody, error);
//					}
//				});
//
//		return handle;
//	}

//	public RequestHandle get() {
//		this.handle = AsyncHttpClient.get(this.createGetUri(), params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] responseBody) {
//						BaseHttpRequest.this.onSuccess(statusCode, headers,
//								responseBody);
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] responseBody, Throwable error) {
//						BaseHttpRequest.this.onFailure(statusCode, headers,
//								responseBody, error);
//					}
//				});
//		return handle;
//	}

//	public RequestHandle getFile(File file) {
//		this.handle = AsyncHttpClient.get(this.createGetUri(), params,
//				new FileAsyncHttpResponseHandler(file) {
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							Throwable throwable, File file) {
//						BaseHttpRequest.this.onFailure(statusCode, headers,
//								throwable, file);
//					}
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							File file) {
//						BaseHttpRequest.this.onSuccess(statusCode, headers,
//								file);
//					}
//				});
//		return handle;
//	}

//	public RequestHandle uploadFile(String path) {
//		File file = new File(path);
//		try {
//			this.params().put("file", file, "application/octet-stream");
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		return this.post();
//	}
//
//	protected <V> V parseJson(byte[] responseBody, Type type, Type listType)
//			throws IOException {
//		ByteArrayInputStream bis = new ByteArrayInputStream(responseBody);
//		return parseJson(bis, type, listType);
//	}
//
//	protected <V> V parseJson(byte[] responseBody, Type type)
//			throws IOException {
//		return parseJson(responseBody, type, null);
//	}
//
//	protected <V> V parseJson(InputStream stream, Type type) throws IOException {
//		return parseJson(stream, type, null);
//	}
//
//	protected <V> V parseJson(InputStream stream, Type type, Type listType)
//			throws IOException {
//		BufferedReader reader = new BufferedReader(new InputStreamReader(
//				stream, "UTF-8"), this.bufferSize);
//		IOException ioe;
//		if (listType == null)
//			try {
//				StringBuilder b = new StringBuilder();
//				String line;
//				while((line=reader.readLine())!=null) {
//				b.append(line);
//				
//				}
//				b.toString();
//				Log.i("jsonString", b.toString());
//				return JSON.parseObject(b.toString(), type);
//			} catch (JsonParseException jpe) {
//				ioe = new IOException(
//						"Parse exception converting JSON to object");
//				ioe.initCause(jpe);
//				throw ioe;
//			} finally {
//				try {
//					reader.close();
//				} catch (IOException localIOException2) {
//				}
//			}
//		JsonReader jsonReader = new JsonReader(reader);
//		try {
//			if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
//				return this.gson.fromJson(jsonReader, listType);
//			}
//			return this.gson.fromJson(jsonReader, type);
//		} catch (JsonParseException jpe) {
//			IOException ioe2 = new IOException(
//					"Parse exception converting JSON to object");
//			ioe2.initCause(jpe);
//			throw ioe2;
//		} finally {
//			try {
//				jsonReader.close();
//			} catch (IOException localIOException5) {
//			}
//		}
//	}

//	protected String toJson(Object object) throws IOException {
//		try {
////			return this.gson.toJson(object);
//		} catch (JsonParseException jpe) {
//			IOException ioe = new IOException(
//					"Parse exception converting object to JSON");
//			ioe.initCause(jpe);
//			throw ioe;
//		}
//	}

//	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//		try {
//			this.responseData = parseJson(responseBody, this.classType);
//
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		if (this.resultCallback != null) {
//			this.resultCallback.onRequestResult(this.handle, statusCode, "",
//					this.responseData);
//
//		}
//		// TODO: 15/12/19 callback
//	}
//
//	public void onSuccess(int statusCode, Header[] headers, File file) {
//		// TODO: 15/12/19 callback
//	}
//
//	public void onFailure(int statusCode, Header[] headers,
//			Throwable throwable, File file) {
//		// TODO: 15/12/19 callback
//	}
//
//	public void onFailure(int statusCode, Header[] headers,
//			byte[] responseBody, Throwable error) {
//		// TODO: 15/12/19 callback
//	}
	
}
