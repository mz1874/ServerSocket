package com.candy.mz;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author by wangchong
 * @Description
 * @Date 2020/7/16 16:55
 */
public class BIOServer {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(8763);

            while (true) {
                final Socket accept = serverSocket.accept();
                System.out.println("客户端连接");
                executorService.execute(()->{
                    handler(accept);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handler(Socket socket) {
        try {
            byte [] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true){
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0,read));
                    System.out.println(Thread.currentThread().getName());
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                System.out.println("关闭连接 --- ");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
