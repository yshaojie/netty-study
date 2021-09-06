package com.self.echo.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author shaojieyue
 * @date 2021/09/06
 */

public class BioEchoServerV2 {
    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(9999));
        System.out.println("socket连接创建,等待连接请求....");
        for (; ; ) {
            final Socket socket = serverSocket.accept();
            //为每个socket连接都创建一个线程来处理读写请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                        final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
                        while (true) {
                            final String line = bufferedReader.readLine();
                            System.out.println("接收到消息:"+line);
                            writer.write("服务端发送内容:"+line+"\n");
                            writer.flush();
                            if (line.equals("bye")) {
                                break;
                            }
                        }

                        bufferedReader.close();
                        writer.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}
