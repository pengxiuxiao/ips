package com.supadata;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @ClassName: SpringbootStartApplication
 * @Description:
 * @Auther: pxx
 * @Date: 2018/8/24 10:07
 * @Description:
 */
public class SpringbootStartApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IpsApplication.class);
    }
}
