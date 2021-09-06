package com.self.echo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author shaojieyue
 * @date 2021/09/06
 */
public class NioEchoServer {
    public static void main(String[] args) throws IOException {
        List<SocketChannel> connectList = new CopyOnWriteArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    for (SocketChannel socket : connectList) {
                        final ByteBuffer allocate = ByteBuffer.allocate(1024);
                        try {
                            final int count = socket.read(allocate);

                            if (count > 0) {
                                byte[] arr = new byte[count];
                                allocate.get(arr, 0, arr.length);
                                for (int i = 0; i < count; i++) {
                                    arr[i] = allocate.get(i);
                                }
                                final String line = new String(arr, Charset.forName("UTF-8"));
                                System.out.println("接收到消息:"+line);
                                socket.write(StandardCharsets.UTF_8.encode("服务端发送内容:" + line + "\n"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.socket().bind(new InetSocketAddress(9999));
        for (; ; ) {
            final SocketChannel socket = socketChannel.accept();
            socket.configureBlocking(false);
            connectList.add(socket);
        }
    }
}
