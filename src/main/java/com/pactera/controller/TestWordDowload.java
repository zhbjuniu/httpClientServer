package com.pactera.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestWordDowload {
	
	@ResponseBody
	@RequestMapping("/download")
	public void download(HttpServletRequest request,HttpServletResponse response ) throws Exception {
//        try {  
        	
//        	String contextPath = req.getContextPath();
        	String fileName = "测试.docx";
        	String filePath=request.getRealPath(fileName);
        	System.out.println("<<<<<<<<<<<<<:"+filePath);
        	
            
        	
            //设置向浏览器端传送的文件格式  
            response.setContentType("application/x-download");  
  
            fileName = URLEncoder.encode(fileName, "UTF-8");  
            response.addHeader("Content-Disposition", "attachment;filename="  + fileName);  
            FileInputStream fis = null;  
            OutputStream os = null;  
            try { 
                os = response.getOutputStream();  
                fis = new FileInputStream(filePath);  
                byte[] b = new byte[1024 * 10];  
                int i = 0;  
                while ((i = fis.read(b)) > 0) {  
                    os.write(b, 0, i);  
                }  
                os.flush();  
                os.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                } finally {  
                    if (fis != null) {  
                        try {  
                            fis.close();  
                        } catch (IOException e) {  
                            e.printStackTrace();  
                        }  
                    }  
                    if (os != null) {  
                        try {  
                            os.close();  
                        } catch (IOException e) {  
                            e.printStackTrace();  
                        }  
                    }  
                }  
        
	}

}
