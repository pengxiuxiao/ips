package com.supadata.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: IpsConfig
 * @Description:
 * @Author: pxx
 * @Date: 2019/3/26 14:57
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "config")
public class Config {

    private String WINPATH;

    private String LINUXDOCPATH;

    private String SERVICEURL;

    private String DESKEY;

    public Config() {
    }

    public Config(String WINPATH, String LINUXDOCPATH, String SERVICEURL, String DESKEY) {
        this.WINPATH = WINPATH;
        this.LINUXDOCPATH = LINUXDOCPATH;
        this.SERVICEURL = SERVICEURL;
        this.DESKEY = DESKEY;
    }

    public String getWINPATH() {
        return WINPATH;
    }

    public void setWINPATH(String WINPATH) {
        this.WINPATH = WINPATH;
    }

    public String getLINUXDOCPATH() {
        return LINUXDOCPATH;
    }

    public void setLINUXDOCPATH(String LINUXDOCPATH) {
        this.LINUXDOCPATH = LINUXDOCPATH;
    }

    public String getSERVICEURL() {
        return SERVICEURL;
    }

    public void setSERVICEURL(String SERVICEURL) {
        this.SERVICEURL = SERVICEURL;
    }

    public String getDESKEY() {
        return DESKEY;
    }

    public void setDESKEY(String DESKEY) {
        this.DESKEY = DESKEY;
    }

    @Override
    public String toString() {
        return "Config{" +
                "WINPATH='" + WINPATH + '\'' +
                ", LINUXDOCPATH='" + LINUXDOCPATH + '\'' +
                ", SERVICEURL='" + SERVICEURL + '\'' +
                ", DESKEY='" + DESKEY + '\'' +
                '}';
    }
}
