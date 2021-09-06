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
public class BioEchoServer {
    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9999));
        System.out.println("socket连接创建,等待连接请求....");
        final Socket socket = serverSocket.accept();
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

    }
}
