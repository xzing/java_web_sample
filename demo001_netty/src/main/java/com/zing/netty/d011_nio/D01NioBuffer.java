package com.zing.netty.d011_nio;

import sun.nio.ch.DefaultSelectorProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Is Zing  Created At 2021/2/18
 */
public class D01NioBuffer {
    public static void main(String[] args) throws IOException, InterruptedException {

        Selector selector = Selector.open();
        System.out.println(selector.getClass());
        System.out.println(SelectorProvider.provider().getClass());
        System.out.println(DefaultSelectorProvider.create().getClass());

        int[] ports = IntStream.range(5000, 5005).toArray();
        for (int i = 0; i < ports.length; ++i) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 非阻塞方式运行
            serverSocketChannel.configureBlocking(false);

            // 创建ServerSocket
            ServerSocket serverSocket = serverSocketChannel.socket();

            // 绑定地址
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            serverSocket.bind(address);

            System.out.println("监听端口:" + ports[i]);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }

        while (true) {
            int connect = selector.select();
            System.out.println("connected num:" + connect);
            Set<SelectionKey> selectionKeys = selector.keys();
            System.out.println("selectionKeys:" + selectionKeys);
            Iterator<SelectionKey> keys = selectionKeys.iterator();
            while (keys.hasNext()) {
                SelectionKey selectionKey = keys.next();
                if (selectionKey.isAcceptable()) {
                    // 获取激活的Channel
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    keys.remove();
                    System.out.println("获得客户端连接: " + socketChannel);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int byteRead = 0;
                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);
                        if (read <= 0) {
                            break;
                        }

                        byteBuffer.flip();

                        socketChannel.write(byteBuffer);

                        byteRead += read;
                    }
                    System.out.println("read:" + byteRead + ", from:" + socketChannel);
                    keys.remove();

                }
            }

            Thread.sleep(20);
        }
    }
}
