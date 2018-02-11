package com.pactera.controller.testword;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class WordAction  {
	 private String filePath; //文件路径
	 private String fileName; //文件名称
	 private String fileOnlyName; //文件唯一名称
	 
	 /**
	  * @author zhb
	  * @param req
	  * @param rsp
	  * @describtion freemarker 生成.docx
	  * @throws Exception
	  */
	@ResponseBody
	@RequestMapping("/word")
	public String createWord(HttpServletRequest req,HttpServletResponse rsp ) throws Exception {
        /** 用于组装word页面需要的数据 */
        Map<String, Object> dataMap = new HashMap<String, Object>();
        
        /** 组装数据 */
        dataMap.put("userName","小杨");
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
//        dataMap.put("currDate",sdf.format(new Date()));
//        List<Map<String, Object>> userList=new ArrayList<Map<String,Object>>();
//        for(int i=1;i<=10;i++){
//         Map<String, Object> map=new HashMap<String, Object>();
//         map.put("title", "标题"+i);
//         map.put("content", "内容"+(i*2));
//         map.put("author", "作者"+(i*3));
//         userList.add(map);
//        }
//        dataMap.put("userList",userList);
        
        /** 文件名称，唯一字符串 */
        Random r=new Random();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        StringBuffer sb=new StringBuffer();
        sb.append(sdf1.format(new Date()));
        sb.append("_");
        sb.append(r.nextInt(100));
        
        /** 生成word */
//        //文件路径
        filePath=req.getServletContext().getRealPath("/")+"upload";
//        //文件唯一名称
        fileName = "Word文档_"+sb+".docx";
//        WordUtil.createWord(dataMap, "doc2.ftl", filePath, fileOnlyName);
        /** 导出 */
        exporWord(dataMap, "test11.ftl", filePath, fileName,req,rsp);
        return "success";
    }
	    
       public  void exporWord(Map dataMap,String templateName,String filePath,
    		   String fileName,HttpServletRequest request,HttpServletResponse response){
    	   InputStream fin = null;
           ServletOutputStream out2 = null;
           File file = null;
	        try {
	        	
	        	/*
	             * (一)生成docx
	             */
		        //创建配置实例 
		        Configuration configuration = new Configuration();
	        	//设置编码
	            configuration.setDefaultEncoding("UTF-8");
	            configuration.setClassForTemplateLoading(WordUtil.class,"/com/pactera/controller/testword/");
	            Template template = configuration.getTemplate(templateName);
	            File outFile = new File(filePath+File.separator+fileName);
	            if (!outFile.getParentFile().exists()){
	                outFile.getParentFile().mkdirs();
	            }
	            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
	            //将模板和数据模型合并生成文件 
	            template.process(dataMap, out);
	            
	            /*
	             * (二)导出docx
	             */
	            request.setCharacterEncoding("UTF-8");
                // 调用工具类WordUtils的createDoc方法生成Word文档
                file = WordUtils.createDoc(dataMap, "tye");
                fin = new FileInputStream(file);
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/msword");
                // 设置浏览器以下载的方式处理该文件默认名为Exhibition.doc
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".docx", "UTF-8"));
                // response.addHeader("Content-Disposition",
                // "attachment;filename=Exhibition.doc");//用这种方式下载下来的word 文档显示不了中文
                out2 = response.getOutputStream();
                byte[] buffer = new byte[512]; // 缓冲区
                int bytesToRead = -1;
                // 通过循环将读入的Word文件的内容输出到浏览器中
                while ((bytesToRead = fin.read(buffer)) != -1) {
                	out2.write(buffer, 0, bytesToRead);
                }
            }catch(Exception e) {
	               e.printStackTrace();
            	
            }finally {
            	try {
	                if (fin != null){
						fin.close();
					} 
	                if (out2 != null){
	                    out2.close();
	                }    
	                if (file != null){
	                	file.delete();// 删除临时文件
	                }	
            	}catch (Exception e) {
					e.printStackTrace();
				}
            }
	    }   
	       
	    public String getFilePath() {
	     return filePath;
	    }

	    public void setFilePath(String filePath) {
	     this.filePath = filePath;
	    }

	    public String getFileName() {
	     return fileName;
	    }

	    public void setFileName(String fileName) {
	     this.fileName = fileName;
	    }

	    public String getFileOnlyName() {
	     return fileOnlyName;
	    }

	    public void setFileOnlyName(String fileOnlyName) {
	     this.fileOnlyName = fileOnlyName;
	    }
	    
}	    
	    