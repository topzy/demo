
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * 作者：张正平
 * 时间：2017-02-15  09:54
 * 作用：http请求工具类
 */
public class HttpUtil {
    /**
     * HTTP_POST 请求
     *
     * @param requestUrl 请求地址
     * @param jsonStr    请求参数JSON字符创
     * @return
     */
    public static String sendPostRequest(String requestUrl, String jsonStr) {
        String result = "";
        if (isEmpty(requestUrl)) {
            System.out.println("com.jy.HttpUtil.sendPostRequest方法中,参数requestUrl(请求地址)为空!");
            return "请求地址为空!";
        }
        if (isEmpty(jsonStr)) {
            System.out.println("com.jy.HttpUtil.sendPostRequest方法中,参数jsonStr(请求参数)为空!");
            return "请求参数为空!";
        }
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//提交模式
            //是否允许输入输出
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //是否允许缓存
            conn.setUseCaches(false);
            //设置超时时间（毫秒）
            conn.setConnectTimeout(30000);
            //设置请求头信息
            conn.setRequestProperty("Content-Type", "application/json");
            //连接地址
            conn.connect();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            //发送参数
            writer.write(jsonStr);
            //清理当前编辑器的左右缓冲区，并使缓冲区数据写入基础流
            writer.flush();
            //关闭输出流
            writer.close();
            //读取返回结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //读取返回结果
            String lines = reader.readLine();
            //关闭流
            reader.close();
            JSONObject jsonObject = JSONObject.parseObject(lines);
            result = jsonObject.toString();
        } catch (MalformedURLException e) {
            System.out.println("com.jy.HttpUtil,请求是失败" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("com.jy.HttpUtil.sendPostRequest,请求失败!" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     *  HTTP_POST 请求
     * @param requestUrl 请求地址
     * @param jsonStr 请求参数JSON字符创
     * @return
     */
    public static JSONObject sendImagePostRequest(String requestUrl, String jsonStr, HttpServletResponse response) {
        JSONObject jsonObject = null;
        BufferedReader reader = null;
        OutputStreamWriter writer = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        OutputStream stream = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//提交模式
            //是否允许输入输出
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //是否允许缓存
            conn.setUseCaches(false);
            //设置超时时间（毫秒）
            conn.setConnectTimeout(30000);
            //设置请求头信息
            conn.setRequestProperty("Content-Type", "application/json");
            //连接地址
            conn.connect();
            writer = new OutputStreamWriter(conn.getOutputStream());
            //发送参数
            writer.write(jsonStr);
            //清理当前编辑器的左右缓冲区，并使缓冲区数据写入基础流
            writer.flush();

            //读取图片流
            stream = response.getOutputStream();
            //读取返回结果
            byte[] data = new byte[1024];
            int len = 0;
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                inputStream = conn.getInputStream();
                while ((len = inputStream.read(data)) != -1) {
                    stream.write(data, 0, len);
                }
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //读取返回结果
            String lines = reader.readLine();
            jsonObject = JSONObject.parseObject(lines);
        } catch (MalformedURLException e) {
            logger.error("HttpUtil.sendPostRequest,请求失败!" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("HttpUtil.sendPostRequest,请求失败!" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    //关闭流
                    reader.close();
                }
                if (writer != null) {
                    //关闭输出流
                    writer.close();
                }
                if (conn != null) {
                    //释放链接
                    conn.disconnect();
                }
                if (inputStream != null) {
                    //释放链接
                    inputStream.close();
                }
                if (stream != null) {
                    //释放链接
                    stream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
    /**
     * 请求参数分装
     *
     * @param  head 请求头信息
     * @param  body 请求参数
     * @return
     */
    public static String getRequestParams(Map<String, String> head, Map<String, String> body) {
        JSONObject _head = new JSONObject();
        JSONObject _body = new JSONObject();
        _head.put("username", head.get("username"));//账号
        _head.put("password", head.get("password"));//密码

        for (String key : body.keySet()) {
           _body.put(key, body.get(key));
        }

        _head.put("data", _body);
        return _head.toString();
    }

    /**
     * 获取数据入口
     *
     * @param requestUrl 请求地址
     * @param  head 请求头信息
     * @param  body 请求参数
     * @return
     */
    public static String getData(String requestUrl, Map<String, String> head, Map<String, String> body) {
        String requestParams = getRequestParams(head, body);
        String data = "";
        if (!isEmpty(requestParams)) {
            System.out.println("请求URL---requestUrl:" + requestUrl);
            System.out.println("请求参数---requestParams:" + requestParams);
            data = sendPostRequest(requestUrl, requestParams);
        }
        return data;
    }
    /**
     * 获取图片入口
     * @param requestUrl 请求地址
     * @return
     */
    public static String getImg(String requestUrl, Map<String, String> head, Map<String, String> body, HttpServletResponse response) {
        String requestParams = getRequestParams(head, body);
        String data = "";
        if (!isEmpty(requestParams)) {
            System.out.println("请求URL---requestUrl:" + requestUrl);
            System.out.println("请求参数---requestParams:" + requestParams);
            data = sendImagePostRequest(requestUrl, requestParams, response).toJSONString();
        }
        return data;
    }
    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.trim().length() == 0;
    }
}
