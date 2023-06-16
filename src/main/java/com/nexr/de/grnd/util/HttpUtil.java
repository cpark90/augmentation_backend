package com.nexr.de.grnd.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class HttpUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
	
	private Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
	private ConnectionPool connectionPool;
	
	@PostConstruct
	public void init() {
		connectionPool = HttpConnectionPool.getPool();
	}
	
	public Object[] sync(String restURL, Object msg) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder().connectionPool(connectionPool).build();
		
		RequestBody reqBody = RequestBody.create(gson.toJson(msg), MediaType.parse("application/json; charset=UTF-8"));
		Request request = new Builder().url(restURL).post(reqBody).build();
		Response response;
		
		// 실행
		response = httpClient.newCall(request).execute();
		String rspStatusCode = String.valueOf(response.code());
		JsonElement rspBodyJson = null;
		if(response.body() != null) {
			String mediateBuf = response.body().string();
			
			if(!mediateBuf.isEmpty()) {
				rspBodyJson = new JsonParser().parse(mediateBuf);
				LOGGER.info("responsebody to json : " + rspBodyJson.toString());
			} else {
				LOGGER.info("responseBody is empty."); // responsebody는 있으나 내용없는 응답 ex: []
			}
		} else {
			LOGGER.info("responseBody is null"); // case : status 코드만 있는 경우.
		}
		
		return new Object[] {rspStatusCode, rspBodyJson};
	}
	
	public Object[] syncGet(String restURL) throws IOException {
		
		OkHttpClient httpClient = new OkHttpClient.Builder().connectionPool(connectionPool).build();
				
		Request request = new Builder().url(restURL).get().build();
		Response response;
		
		// 실행
		response = httpClient.newCall(request).execute();
		String rspStatusCode = String.valueOf(response.code());
		JsonElement rspBodyJson = null;
		
		if(response.body() != null) {
			String mediateBuf = response.body().string();
			
			if(!mediateBuf.isEmpty()) {
				rspBodyJson = new JsonParser().parse(mediateBuf);
				LOGGER.info("responsebody to json : " + rspBodyJson.toString());
			} else {
				LOGGER.info("responseBody is empty."); // responsebody는 있으나 내용없는 응답 ex: []
			}
		} else {
			LOGGER.info("responseBody is null"); // case : status 코드만 있는 경우.
		}
		
		return new Object[] {rspStatusCode, rspBodyJson};
	}
	
	/**
	 * GET
	 * 
	 * @param url
	 * @param async
	 * @return
	 */
	public String get(String url, Map<String, String> header, boolean async) {
		String resultJson = "";

		OkHttpClient okHttpClient = new OkHttpClient.Builder().connectionPool(connectionPool).build();
		LOGGER.debug(String.format(">> Connection Count : %s, Idle Connection Count : %s", connectionPool.connectionCount(), connectionPool.idleConnectionCount()));
		Builder builder = new Request.Builder();
		Response response = null;
		
		if (header != null) {
			Iterator<String> keyIter = header.keySet().iterator();
			while (keyIter != null && keyIter.hasNext()) {
				String key = keyIter.next();
				String value = header.get(key);

				if (value != null && !"".equals(value)) {
					builder.addHeader(key, value);
				}
			}
		}
				
		try {
			Request request = builder.url(url).build();

			// 비동기
			if (async) {
				okHttpClient.newCall(request).enqueue(new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {
						LOGGER.info(String.format("HTTP GET Method Async is failure. Exception message : %s", e.getMessage()));
					}

					@Override
					public void onResponse(Call call, Response resp) throws IOException {
						LOGGER.info("HTTP GET Method Async is success");
						// callback 호출
					}
				});
			}
			// 동기
			else {
				response = okHttpClient.newCall(request).execute();
				resultJson = response.body().string();
			}

		} catch (Exception e) {
			LOGGER.error(String.format("Exception message %s", e.getMessage()));
			Map<String, String> result = new HashMap<String, String>();
			result.put("errorMessage", e.getMessage());
			resultJson = gson.toJson(result);
		}

		return resultJson;
	}




}
