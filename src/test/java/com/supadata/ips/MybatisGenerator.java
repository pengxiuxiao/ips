package com.supadata.ips;

import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MybatisGenerator {
    @Test
    public void generator() throws Exception {
        List<String> warnings = new ArrayList<>();
        File configFile = new File("src/test/resources/generatorConfig.xml");
        File deleteDirectory = new File("src/main/resources/mapper");
        File dest = new File("src/main/resources/mapper_temp");
        long start = System.currentTimeMillis();
        if (!dest.exists()) {
            dest.mkdir();
        } else {
            if (dest.listFiles().length > 0) {
                //删除temp目录文件
                for (File tempFile : dest.listFiles()) {
                    tempFile.delete();
                }
            }
        }
        if (deleteDirectory.exists()&&deleteDirectory.isDirectory()) {
            //将mapper目录文件先复制到temp目录
            for (File file : deleteDirectory.listFiles()) {
                String name = file.getName();
                File newFile = new File(dest.getAbsolutePath(), name);
                Files.copy(file.toPath(), newFile.toPath());
            }
            //mapper目录文件先删除
            for (File file : deleteDirectory.listFiles()) {
                file.delete();
            }
        } else {
            System.out.println("目录不存在...");
        }
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        long end = System.currentTimeMillis();
        System.out.println((end-start)/1000+"秒");
    }
}