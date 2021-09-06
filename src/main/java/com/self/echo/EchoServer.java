package com.self.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author shaojieyue
 * @date 2021/08/30
 */
public class EchoServer {
    public static int DEFAULT_PORT = 7;

    /**
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (RuntimeException ex) {
            port = DEFAULT_PORT;
        }

        // 多线程事件循环器
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // boss
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // worker

        try {
            // 启动NIO服务的引导程序类
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup) // 设置EventLoopGroup
                    .channel(NioServerSocketChannel.class) // 指明新的Channel的类型
                    .childHandler(new ChannelInitializer<SocketChannel>(){

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    }) // 指定ChannelHandler
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置的ServerChannel的一些选项
                    .option(ChannelOption.TCP_NODELAY,false)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // 设置的ServerChannel的子Channel的选项

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync();

            System.out.println("EchoServer已启动，端口：" + port);

            // 等待服务器 socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();
        } finally {

            // 优雅的关闭
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    static class EchoServerHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(ctx.channel().remoteAddress() + " -> Server :" + msg);
            ctx.writeAndFlush(msg);
        }
    }
}
