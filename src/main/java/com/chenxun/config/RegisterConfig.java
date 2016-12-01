/**
 * Project Name:demo-rpc
 * File Name:RegisterConfig.java
 * Package Name:com.chenxun.config
 * Date:2016年11月29日下午4:01:30
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.chenxun.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.chainsaw.Main;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * ClassName:RegisterConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月29日 下午4:01:30 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@EnableConfigurationProperties(RpcProperties.class)
@ConditionalOnBean(value=RpcTemplate.class)
@Configuration
public class RegisterConfig implements CommandLineRunner, ApplicationContextAware,
		ApplicationListener<EmbeddedServletContainerInitializedEvent> {

	private ApplicationContext applicationContext;
	
	private String  application ;
	
	private final String root ="/rpc";

	private int serverPort;
	
	private String serverHost;
	
	@Autowired
	private RpcProperties rpcProperties;
	@Autowired
	private RpcTemplate rpcTemplate;
	
	private  ZkClient zkClient;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void run(String... args) throws Exception {

		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
		
		application=root+"/"+rpcProperties.getApplication();
		zkClient.createPersistent(application,true);
		
		String tmp=serverHost+":"+serverPort;
		zkClient.createEphemeral(application+"/"+tmp);
		rpcTemplate.putRoute(application, tmp);
		for (RequestMappingInfo info : map.keySet()) {
			// 注册http接口
			}
		// 改方法会阻赛
		zkClient.subscribeChildChanges(application, rpcTemplate);
		
	}
	
	
	@PreDestroy
	public void destory(){
		zkClient.close();
	}
	

	@Override
	public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
		  this.serverPort = event.getEmbeddedServletContainer().getPort();
	}
	 
	@Bean
	public RpcTemplate rpcTemplate(){
		RpcTemplate rpcTemplate=new RpcTemplate();		
		return rpcTemplate;
	}
	
	@PostConstruct
	private void init(){
		zkClient=new ZkClient(rpcProperties.getZookeeper());
		getHost();
	}
	
	private void  getHost(){
        try {
        	 this.serverHost= InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
           
        }
	}
	
	public static class Test{
		public static void main(String[] args) {
			System.out.println("aaa,,aaa,a,b".replaceAll(",{1,}", ","));
		}
	}

}
