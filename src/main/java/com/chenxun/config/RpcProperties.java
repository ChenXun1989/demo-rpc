/**
 * Project Name:demo-rpc
 * File Name:RpcProperties.java
 * Package Name:com.chenxun.config
 * Date:2016年11月29日下午4:39:32
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.chenxun.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * ClassName:RpcProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月29日 下午4:39:32 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
@ConfigurationProperties(prefix=RpcProperties.PREFIX)
public class RpcProperties {
	
	public static final String PREFIX="rpc";
	
	private String application;
	
	private String zookeeper;
   
}

