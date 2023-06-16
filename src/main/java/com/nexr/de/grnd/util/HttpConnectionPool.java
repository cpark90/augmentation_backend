package com.nexr.de.grnd.util;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;

public class HttpConnectionPool {
	private static ConnectionPool connectionPool;
	
	private HttpConnectionPool(){
		connectionPool = new ConnectionPool(500, 10000, TimeUnit.MILLISECONDS);
	}
	
	private static class LazyHolder {
		public static final HttpConnectionPool INSTANCE = new HttpConnectionPool();
	}
	
	private static HttpConnectionPool getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	public static ConnectionPool getPool() {
		if(connectionPool == null) {
			getInstance();
		}
		
		return connectionPool;
	}
}
