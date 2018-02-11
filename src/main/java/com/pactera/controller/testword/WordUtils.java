package com.pactera.controller.testword;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Encoder;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class WordUtils {
	
    private static Configuration configuration = null;
    private static Map<String, Template> allTemplates = null;
    static {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        try {
        	
            //configuration.setDirectoryForTemplateLoading(new File(SendMailUtil.getFilePath()));
            configuration.setClassForTemplateLoading(WordUtils.class,"/com/pactera/controller/testword/");
            allTemplates = new HashMap<>(); // Java 7 钻石语法
            allTemplates.put("tye", configuration.getTemplate("test11.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private WordUtils() {
        throw new AssertionError();
    }

    public static File createDoc(Map<?, ?> dataMap, String type) {
        String name = "temp" + (int) (Math.random() * 100000) + ".docx";
        File f = new File(name);
        Template t = allTemplates.get(type);
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }
    /**
     * 把图片转成base64
     * @param filename
     * @return
     * @throws Exception
     */
    public static String getImageString(String filename) throws Exception {
        URL url = new URL(filename);
        URLConnection openConnection = url.openConnection();
        InputStream in = openConnection.getInputStream();
        try {
            byte[] bytes = null;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = in.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            bytes = output.toByteArray();
            
            String strBase64 = new BASE64Encoder().encode(bytes); // 将字节流数组转换为字符串
            System.out.println(strBase64);
            return strBase64;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            in.close();
        }
    }

    /**
     * 方法一错误
     * 将图片转成BASE64
     * 
     * @param filename
     * @return
     * @throws IOException
     */
    /*public static String getImageString(String filename) throws Exception {
        URL url = new URL(filename);
        URLConnection openConnection = url.openConnection();
        InputStream in = openConnection.getInputStream();
        try {
            byte[] bytes = new byte[in.available()];
            String strBase64 = new BASE64Encoder().encode(bytes); // 将字节流数组转换为字符串
            System.out.println(strBase64);
            return strBase64;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            in.close();
        }
    }*/
    
    /**
     * 方法二
     * 根据传入的图片文件把图片转成BASE64
     * @param file
     * @return
     */
    public static String getImageStr(File file) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }
}