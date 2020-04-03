package com.supadata;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: pxx
 * @Date: 2020/4/3 13:07
 * @Version 1.0
 */
public class TestPost {
    public static void main(String[] args) {

        String url = "http://pengxiuxiao.55555.io/card/synchci";
        String encoding = "utf-8";

        List<UserBean> users = new ArrayList<>();
        Map<String,List> map = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            String name = "张三" + i;
            UserBean userBean = new UserBean(name, "1529888000", "2020-04-01", "2020-04-30");
            users.add(userBean);
        }
        map.put("datas",users);

        JSONObject jsonObject = JSONObject.fromObject(map);

        try {
            String send = send(url, jsonObject, encoding);

            System.out.println(send);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String send(String url, JSONObject jsonObject,String encoding)throws ParseException, IOException {
        String body = "";

        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        JSONObject param = new JSONObject();
        param.put("datas",jsonObject);
        //装填参数
        StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");

        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));
        //设置参数到请求对象中
        httpPost.setEntity(s);
        System.out.println("请求地址："+url);
//        System.out.println("请求参数："+nvps.toString());

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
//        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }
}
