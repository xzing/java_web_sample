package com.zing.netty.d011_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Is Zing  Created At 2021/2/22
 */
public class D02ServerSelector {

    public static final Charset charset = Charset.forName("utf-8");

    public static void main(String[] args) {
        Map<String, SocketChannel> clientMap = new ConcurrentHashMap<>();
        try {
            Selector selector = Selector.open();
            try {
                ServerSocketChannel channel = ServerSocketChannel.open();
                channel.configureBlocking(false);
                ServerSocket serverSocket = channel.socket();
                InetSocketAddress address = new InetSocketAddress(6000);
                serverSocket.bind(address);
                channel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    SocketChannel client;
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                        client = serverChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        String clientKey = UUID.randomUUID().toString();
                        clientKey = clientKey.replace("-", "");
                        clientMap.put(clientKey, client);
                        System.out.println(clientKey + " link in!");
                    } else if (selectionKey.isReadable()) {
                        // System.out.println("read client msg");
                        client = (SocketChannel) selectionKey.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int count = client.read(readBuffer);
                        if (count > 0) {
                            readBuffer.flip();
                            String resMsg = String.valueOf(charset.decode(readBuffer).array());
                            System.out.println(client + " send:" + resMsg);

                            String senderKey = null;
                            Set<Map.Entry<String, SocketChannel>> entrySet = clientMap.entrySet();
                            for (Map.Entry<String, SocketChannel> entry: entrySet){
                                if (!client.equals(entry.getValue())){
                                   ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                   writeBuffer.put((entry.getKey() + " say: " + resMsg).getBytes());
                                   writeBuffer.flip();
                                   entry.getValue().write(writeBuffer);
                                }
                            }

                        }
                    }
                }
                selectionKeys.clear();
                Thread.sleep(200);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
