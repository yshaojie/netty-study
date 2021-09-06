package com.self.echo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author shaojieyue
 * @date 2021/09/06
 */
public class NioSelectorEchoServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        socketChannel.bind(new InetSocketAddress(9999));
        for (; ; ) {
            final int readyChannels = selector.select();
            if (readyChannels < 1) {
                continue;
            }

            for (SelectionKey selectedKey : selector.selectedKeys()) {

                if (selectedKey.isAcceptable()) {
                    final SocketChannel clientSocket = socketChannel.accept();
                    if (clientSocket == null) {
                        continue;
                    }
                    System.out.println("clientSocket="+clientSocket);
                    clientSocket.configureBlocking(false);
                    clientSocket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    System.out.println("isAcceptable");
                }else if (selectedKey.isReadable()) {
                    System.out.println("isReadable");
                    final SocketChannel channel = (SocketChannel)selectedKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    final int read = channel.read(buffer);
                    System.out.println("readSize="+read);
                }else if (selectedKey.isValid()) {
                    System.out.println("isValid");
                }else if (selectedKey.isConnectable()) {
                    System.out.println("isConnectable");
                }else if (selectedKey.isWritable()) {
                    System.out.println("isWritable");
                }
            }
        }

    }
}
