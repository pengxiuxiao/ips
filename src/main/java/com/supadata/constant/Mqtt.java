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

    private String topic;

    private String clientId;

    private String userName;

    private String passWord;

    public Mqtt() {
    }

    public Mqtt(String host, String topic, String clientId, String userName, String passWord) {
        this.host = host;
        this.topic = topic;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
