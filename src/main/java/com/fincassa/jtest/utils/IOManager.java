package com.fincassa.jtest.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.Part;
/**
 * Created by vyn on 12.11.2016.
 */
public class IOManager {
    private static final String _fsep= File.separator,
            _uploadDir= System.getProperty("user.home") + _fsep
            +"AppData"+_fsep
            +"Roaming"+_fsep
            +"CassaCompany"+_fsep
            +"jTest"+_fsep
            +"upload";

    private static final int _bufferBytesSize= 1024*1024;

    public static void init() throws IOException {
        Path uploadDir= Paths.get(_uploadDir);

        if (!Files.exists(uploadDir)) { Files.createDirectories(uploadDir); }
    }

    public static long writeFileFromStorageToStream(String pFilepath, final OutputStream pOutcomeStream) throws IOException {
        long filesize= 0;

        RandomAccessFile fromFile= new RandomAccessFile(pFilepath, "rw");
        FileChannel fromFileChannel= fromFile.getChannel();

        WritableByteChannel toChannel= Channels.newChannel(pOutcomeStream);

        try {
            filesize= fromFile.length();

            ByteBuffer buf= ByteBuffer.allocate(_bufferBytesSize);

            int bytesRead= fromFileChannel.read(buf);
            while (bytesRead != -1) {
                buf.flip();
                toChannel.write(buf);
                buf.clear(); //make buffer ready for writing
                bytesRead= fromFileChannel.read(buf);
            }
        }
        finally {
            if (fromFileChannel != null) {fromFileChannel.close();}
            if (toChannel != null) {toChannel.close();}
        }

        return filesize;
    }

    public static String saveFileToStorageDirectory(Part pIncomePart) throws IOException {
        String uploadedFilename= UUID.randomUUID().toString(),
                tmpFilepath= _uploadDir+_fsep+uploadedFilename,
                result= null;

        RandomAccessFile toFile= new RandomAccessFile(tmpFilepath, "rw");
        FileChannel toFileChannel= toFile.getChannel();

        ReadableByteChannel fromChannel= Channels.newChannel(pIncomePart.getInputStream());

        try {
            ByteBuffer buf= ByteBuffer.allocate(_bufferBytesSize);

            int bytesRead= fromChannel.read(buf);
            while (bytesRead != -1) {
                buf.flip();
                toFileChannel.write(buf);
                buf.clear(); //make buffer ready for writing
                bytesRead= fromChannel.read(buf);
            }

            result= tmpFilepath;
        }
        finally {
            if (toFileChannel != null) {toFileChannel.close();}
            if (fromChannel != null) {fromChannel.close();}
        }

        return result;
    }
}
