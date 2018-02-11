package com.pactera.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ClientServerController {

	private static final Log log = LogFactory.getLog("ClientServerController");
	
	@ResponseBody
	@RequestMapping("/server")
	public String preService(HttpServletRequest req,HttpServletResponse rsp ) throws Exception {
		
		log.info("用户接受报文服务   start...");
//		log.info("<<<<rspMessage:::"+rspMessage);
//		Map parameterMap = req.getParameterMap();
//		String paramJson = JSONObject.fromObject(parameterMap).toString();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "success");
		log.info("响应结果："+jsonObject.toString());
		
		return jsonObject.toString();
	}

}
