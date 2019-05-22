package com.supadata.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: Mqtt
 * @Description:
 * @Author: pxx
 * @Date: 2019/4/3 16:44
 * @Description:
 */

@Component
@ConfigurationProperties(prefix = "mqtt")
public class Mqtt {

    private String host;

    //主机发送消息的主题
    private String pubTopic;

    //主机订阅消息的主题
    private String subTopic;

    private String clientId;

    private String userName;

    private String passWord;

    public Mqtt() {
    }

    public Mqtt(String host, String pubTopic, String subTopic, String clientId, String userName, String passWord) {
        this.host = host;
        this.pubTopic = pubTopic;
        this.subTopic = subTopic;
        this.clientId = clientId;
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPubTopic() {
        return pubTopic;
    }

    public void setPubTopic(String pubTopic) {
        this.pubTopic = pubTopic;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
