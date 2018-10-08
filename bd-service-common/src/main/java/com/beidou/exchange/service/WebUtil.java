package com.beidou.exchange.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class WebUtil {

    private static final Logger LOGGER = Logger.getLogger(WebUtil.class);

    public static final String RESULT = "result";

    public static final String SUCCESS = "success";

    public static final String FAILURE = "failure";

    /**
     * input String [] {1,2,3} output "'1','2','3'"
     */
    public static String strToSplit(String arrIds[]) {
        StringBuffer str = new StringBuffer();
        for (String c : arrIds) {
            str.append("'").append(c).append("',");
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 向web页面write数据
     */
    public static void writeToWeb(HttpServletResponse resp, String content) {
        resp.setStatus(200);
        resp.setContentType("text/html;charset=UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*"); //解决jsonp跨域问题.added by zhuhaitao 2016-3-25
        PrintWriter pw = null;
        try {
            pw = resp.getWriter();
            content = content.replaceAll("\n", "").replace("\r", ""); // 过滤new
            // line
            pw.write(content);
            pw.flush();
        } catch (IOException e) {
            LOGGER.error("IOException!", e);
        } finally {
            if (null != pw) {
                pw.close();
            }
        }
    }

    /**
     * 向web页面write数据
     */
    public static void writeToWeb(HttpServletResponse resp, int content) {
        resp.setContentType("text/html;charset=UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*"); //解决jsonp跨域问题.added by zhuhaitao 2016-3-25
        PrintWriter pw = null;
        try {
            pw = resp.getWriter();
            pw.write(content);
            pw.flush();
        } catch (IOException e) {
            LOGGER.error("IOException!", e);
        } finally {
            if (null != pw) {
                pw.close();
            }
        }
    }

    public static void writeToWeb(HttpServletResponse resp, JSONObject jsonObject) {
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*"); //解决jsonp跨域问题.added by zhuhaitao 2016-3-25
        try {
            resp.getWriter().print(jsonObject);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * gzip压缩数据
     */
    public static void writeToWebByGzip(HttpServletResponse resp, String content) {
        resp.setContentType("text/html;charset=UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*"); //解决jsonp跨域问题.added by zhuhaitao 2016-3-25
        OutputStream os = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gout = new GZIPOutputStream(out);
            gout.write(content.getBytes("UTF-8"));
            gout.flush();
            gout.close();
            byte[] buf = out.toByteArray();
            resp.setHeader("content-encoding", "gzip");
            resp.setHeader("content-length", buf.length + "");
            os = resp.getOutputStream();
            os.write(buf);
            os.flush();
        } catch (Exception e) {
            LOGGER.error("压缩异常！", e);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOGGER.error("流关闭异常！", e);
                }
            }
        }
    }

    /**
     * 根据请求判断是否压缩
     */
    public static void writeToWeb(HttpServletRequest req, HttpServletResponse resp, String content) {
        String contentEncoding = req.getHeader("Accept-Encoding");
        if (StringUtils.isNotEmpty(contentEncoding) && contentEncoding.indexOf("gzip") != -1) {
            writeToWebByGzip(resp, content);
        } else {
            writeToWeb(resp, content);
        }
    }

    /**
     * 字符串的压缩
     *
     * @param str 待压缩的字符串
     * @return 返回压缩后的字符串
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输出流
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        // 将 b.length 个字节写入此输出流
        gzip.write(str.getBytes());
        gzip.close();
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("ISO-8859-1");
    }

    /**
     * 字符串的解压
     *
     * @param str 对字符串解压
     * @return 返回解压缩后的字符串
     * @throws IOException
     */
    public static String unCompress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        ByteArrayInputStream in = new ByteArrayInputStream(
                str.getBytes("utf-8"));
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("utf-8");
    }

    public static String unCompress(InputStream is) throws IOException {
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(is);
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("utf-8");
    }

    public static String read(InputStream is) throws IOException {
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输入流
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = is.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("utf-8");
    }


    public static void writePictureToMobile(HttpServletResponse resp, File picture) throws FileNotFoundException, IOException {
        try {
            LOGGER.info("Write picture "
                    + ImageIO.write(ImageIO.read(picture), "jpg",
                    resp.getOutputStream()));
        } catch (Exception e) {
            LOGGER.error("Write picture to mobile error!", e);
        }
    }

    public static void writeGzipPictureToMobile(HttpServletResponse resp,
                                                File picture) {
        resp.setContentType("image/jpeg");
    }

    private static final int DEEP = 3;

    public static void printStackTrace() {
        printStackTraceInner(DEEP);
    }

    public static void printStackTrace(int deep) {
        printStackTraceInner(deep);
    }

    private static void printStackTraceInner(int deep) {
        deep += 2;
        StackTraceElement[] stackElements = new Throwable().getStackTrace();
        StringBuilder sb = new StringBuilder();
        if (stackElements != null) {
            for (int i = 2; i < (stackElements.length > deep ? deep : stackElements.length); i++) {
                sb.append("\r\n--->").append(i - 1).append(": ").append(stackElements[i]);
            }
            LOGGER.info(sb.toString());
        }
    }

    /**
     * 或者完整的服务器url，用于单点登陆
     */
    public static StringBuilder getSiteString(HttpServletRequest request) {
        StringBuilder site = new StringBuilder();
        site.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).append("/");
        return site;
    }


    /**
     * 把web的请求参数封装成map的形式<br/>
     * 注意，暂时不支持checkbox的形式的参数
     */
    public static Map<String, String> getRequetParamMap(HttpServletRequest request) {
        Enumeration paramNames = request.getParameterNames();
        Map<String, String> params = new HashMap<String, String>();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String value = StringUtils.trim(request.getParameter(paramName));
            params.put(paramName, value);
        }
        return params;
    }

    /**
     * 转换成String
     */
    public static String objectToString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 转换成String
     */
    public static String objectToString(Map<String, Object> objMap, String key, String def) {
        String result = objectToString(objMap.get(key));
        if (StringUtils.isEmpty(result)) {
            result = def;
        }
        return result;
    }

    /**
     * 转换成INT
     */
    public static Integer objectToInt(Object obj) {
        return objectToInt(obj, 0);
    }

    /**
     * 转换成INT
     */
    public static Integer objectToInt(Object obj, Integer defaultValue) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
        }

        return defaultValue;
    }

    /**
     * 转换成Long
     */
    public static Long objectToLong(Object obj) {
        return objectToLong(obj, 0L);
    }

    /**
     * 转换成Long
     */
    public static Long objectToLong(Object obj, Long defaultValue) {
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
        }

        return defaultValue;
    }

    /**
     * 转换成Float
     */
    public static Float objectToFloat(Object obj) {
        return objectToFloat(obj, 0F);
    }

    /**
     * 转换成Float
     */
    public static Float objectToFloat(Object obj, Float defaultValue) {
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
        }

        return defaultValue;
    }

    /**
     * 转换成Double
     */
    public static Double objectToDouble(Object obj) {
        return objectToDouble(obj, 0.0);
    }

    /**
     * 转换成Double
     */
    public static Double objectToDouble(Object obj, Double defaultValue) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
        }

        return defaultValue;
    }

    /**
     * 从请求对象中解析【必填参数】，参数不存在则抛出异常
     *
     * @param request    HttpServletRequest对象
     * @param paramName  参数名称
     * @param encodeName 解码方式
     * @return
     * @throws Exception
     */
    public static String decodeParameter(HttpServletRequest request, String paramName, String encodeName) throws Exception {
        String value = request.getParameter(paramName);
        if (value != null) {
            return URLDecoder.decode(value, encodeName);
        }
        return null;
    }

    /**
     * 从请求对象中解析【必填参数】，参数不存在则抛出异常
     *
     * @param request   HttpServletRequest对象
     * @param paramName 参数名称
     * @return
     * @throws Exception
     */
    public static String decodeParameter(HttpServletRequest request, String paramName) throws Exception {
        return decodeParameter(request, paramName, "UTF-8");
    }


    /**
     * 获取远程访问的IP
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 从request从获取请求头信息
     *
     * @param request
     * @return 注意：返回的参数全部是小写的
     */
    public static Map<String, String> getHeaderParamsMap(HttpServletRequest request) {
        //获取header中的通用参数
        Enumeration<String> headers = request.getHeaderNames();
        Map<String, String> headMap = new HashMap<String, String>(); //key：参数名，value：参数值
        while (headers.hasMoreElements()) {
            String name = headers.nextElement().toLowerCase(); //转换成小写
            headMap.put(name, request.getHeader(name));
        }
        return headMap;
    }
}
