/**
 * Project Name:demo-rpc
 * File Name:PersonController.java
 * Package Name:com.chenxun.controller
 * Date:2016年11月29日下午4:03:50
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.chenxun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chenxun.config.RpcTemplate;

/**
 * ClassName:PersonController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月29日 下午4:03:50 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@RestController
public class PersonController {

	@Autowired
	private RpcTemplate rpcTemplate;

	@RequestMapping("/say/{message}")
	public String say(@PathVariable String message) {
		return "hello :" + message;
	}

	@RequestMapping("/test")
	public String test() {

		return rpcTemplate.get("demo", "/say/aaaaaaa");
	}

}
