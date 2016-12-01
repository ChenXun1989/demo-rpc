/**
 * Project Name:demo-rpc
 * File Name:RpcTemplate.java
 * Package Name:com.chenxun.config
 * Date:2016年11月29日下午4:08:57
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.chenxun.config;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * ClassName:RpcTemplate <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月29日 下午4:08:57 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Component
public class RpcTemplate implements IZkChildListener{
	
	
	private ConcurrentHashMap<String, CopyOnWriteArraySet<String>> routeMap=new ConcurrentHashMap<>();
	
	public String  get(String serviceName,String path){
		
		//
		String url="http://"+route(serviceName)+path;
		
		 CloseableHttpClient httpclient = HttpClients.createDefault();  
	        try {  
	            // 创建httpget.    
	        	
	            HttpGet httpget = new HttpGet(url);  
	            // 执行get请求.    
	            CloseableHttpResponse response = httpclient.execute(httpget);  
	            try {  
	                // 获取响应实体    
	                HttpEntity entity = response.getEntity();  
	                if (entity != null) {  
	                   return EntityUtils.toString(entity);
	                }  
	            } finally {  
	                response.close();  
	            }  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            // 关闭连接,释放资源    
	            try {  
	                httpclient.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }
			return url;  
		
	}
	/**
	 * 
	 * route:(随机算法路由). <br/>
	 *
	 * @author 陈勋
	 * @param serviceName
	 * @return
	 * @since JDK 1.7
	 */
	private String route(String serviceName){
		Random r=new Random();	
		CopyOnWriteArraySet<String> set=routeMap.get("/rpc/"+serviceName);
        if(set==null||set.size()==0){
        	throw  new RuntimeException("该服务没有提供者");
        }    
		return set.toArray(new String[set.size()])[r.nextInt(set.size())];
	}




	
	public void putRoute(String application, String address){
		CopyOnWriteArraySet<String> set=routeMap.get(application);
		synchronized (routeMap) {
			if(set==null){
				set=new CopyOnWriteArraySet<>();
			}
		}
		set.add(address);
		routeMap.putIfAbsent(application, set);
	}
	
	
	@Override
	public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
		
		CopyOnWriteArraySet<String> set=new CopyOnWriteArraySet<>(currentChilds);
		routeMap.putIfAbsent(parentPath, set);
		
	}

}

