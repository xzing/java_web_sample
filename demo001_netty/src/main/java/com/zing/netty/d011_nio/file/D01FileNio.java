package com.zing.netty.d011_nio.file;

import lombok.extern.slf4j.Slf4j;
import sun.nio.ch.FileChannelImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Is Zing  Created At 2021/2/23
 */
@Slf4j
public class D01FileNio {
    public static void main(String[] args) throws IOException {
        File file = new File("./demo.log");
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                log.info("mkParent:{}", parent.mkdirs());
            }
            log.info("mkFile:{}", file.createNewFile());
        }

        // 限制时间
        try (FileOutputStream outputStream = new FileOutputStream(file, true)) {
            FileChannelImpl channel = (FileChannelImpl) outputStream.getChannel();
            ByteBuffer b;
            for (int i = 0; i < 5000; i++) {
                String data = UUID.randomUUID().toString() + "\n";
                b = UTF_8.encode(data);
                while (channel.write(b) == 0) {
                    b.flip();
                }
            }
        }


        // 限制空间(非NIO方式)
        ByteBuffer b = ByteBuffer.allocate(4096);
        try (FileOutputStream outputStream = new FileOutputStream(file, true)) {
            FileChannelImpl channel = (FileChannelImpl) outputStream.getChannel();
            for (int i = 0; i < 5000; i++) {
                String data = UUID.randomUUID().toString() + "\n";
                b.put(data.getBytes(UTF_8));
                b.flip();
                while (true) {
                    if (channel.write(b) == 0) {
                        b.clear();
                        break;
                    }
                }
            }
        }

        // 啥都不限制
        Path path = FileSystems.getDefault().getPath("./", "demo.log");
        for (int i = 0; i < 5000; i++) {
            String data = UUID.randomUUID().toString() + "\n";
            Files.write(path, data.getBytes(UTF_8), StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE, StandardOpenOption.SYNC, StandardOpenOption.WRITE);
        }

        // 大文件使用Map
        // MappedByteBuffer
    }
}
