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
     *  HTTP_POST 请求
     * @param requestUrl 请求地址
     * @param jsonStr 请求参数JSON字符创
     * @return
     */
    public static String  sendPostRequest(String requestUrl, String  jsonStr){
        String result="";
        if(isEmpty(requestUrl)){
            System.out.println("com.jy.HttpUtil.sendPostRequest方法中,参数requestUrl(请求地址)为空!");
            return "请求地址为空!";
        }
        if(isEmpty(jsonStr)){
            System.out.println("com.jy.HttpUtil.sendPostRequest方法中,参数jsonStr(请求参数)为空!");
            return "请求参数为空!";
        }
        try {
            URL  url= new URL(requestUrl);
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
            conn.setRequestProperty("Content-Type","application/json");
            //连接地址
            conn.connect();
            OutputStreamWriter  writer = new OutputStreamWriter(conn.getOutputStream());
            //发送参数
            writer.write(jsonStr);
            //清理当前编辑器的左右缓冲区，并使缓冲区数据写入基础流
            writer.flush();
            //关闭输出流
            writer.close();
            //读取返回结果
            BufferedReader reader = new BufferedReader(new InputStreamReader( conn.getInputStream()));
            //读取返回结果
            String lines =reader.readLine();
            //关闭流
            reader.close();
            JSONObject jsonObject=JSONObject.parseObject(lines);
            result = jsonObject.toString();
        } catch (MalformedURLException e) {
            System.out.println("com.jy.HttpUtil,请求是失败"+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("com.jy.HttpUtil.sendPostRequest,请求失败!"+e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     *  请求参数分装
     * @param params 请求参数 Map
     * @return
     */
    public static  String getRequestParams(Map<String,Object> params){
            JSONObject head = new JSONObject();
            JSONObject body = new JSONObject();
            head.put("username",params.get("username"));//账号
            head.put("password", params.get("password"));//密码
            //设置请求编码
            head.put("requestCode", params.get("requestCode"));

            head.put("dtype", "json");//目前只支持json格式
            for (String key : params.keySet()) {
                //添加参数时忽略username  password  requestCode
                if("username".equals(key) || "password".equals(key) || "requestCode".equals(key)){

                } else {
                    body.put(key, params.get(key));
                }
            }
            head.put("data", body);
            return head.toString();
    }

    /**
     * 获取数据入口
     * @param requestUrl 请求地址
     * @param params  请求参数
     * @return
     */
    public static  String getData(String requestUrl,String requestCode,Map<String,Object> params){
        requestUrl = requestUrl + "/" + requestCode;
        params.put("requestCode",requestCode);
        String  requestParams=getRequestParams(params);
        String data="";
        if(!isEmpty(requestParams)){
        	System.out.println("请求URL---requestUrl:"+requestUrl);
            System.out.println("请求参数---requestParams:"+requestParams);
            data = sendPostRequest(requestUrl,requestParams);
        }
        return data;
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        if(isEmpty(str)){
            return false;
        }else{
            return true;
        }
    }


    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.trim().length()==0;
    }
}
