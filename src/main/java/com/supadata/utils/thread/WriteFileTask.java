package com.supadata.utils.thread;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @ClassName: WriteFileTask
 * @Description:
 * @Auther: pxx
 * @Date: 2018/6/21 16:23
 * @Description:
 */
public class WriteFileTask implements Runnable {

    private String filePath;

    private String fileName;

    private ResponseEntity<byte[]> response;

    public WriteFileTask(String path, String name, ResponseEntity<byte[]> response){
        this.filePath = path;
        this.fileName = name;
        this.response = response;
    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println(fileName + "文件写出开始...");
        InputStream in = null;//将该文件加入到输入流之中
        try {
            in = new FileInputStream(new File(filePath));
            byte[] body=null;
            body=new byte[in.available()];// 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
            in.read(body);//读入到输入流里面

            fileName=new String(fileName.getBytes("gbk"),"iso8859-1");//防止中文乱码
            HttpHeaders headers=new HttpHeaders();//设置响应头
            headers.add("Content-Disposition", "attachment;filename="+fileName);
            HttpStatus statusCode = HttpStatus.OK;//设置响应吗
            response = new ResponseEntity<byte[]>(body, headers, statusCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(fileName + "文件写出完成!");
    }
}
