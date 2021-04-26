package com.huali.utils;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Yang_my
 * @since 2020/12/21
 */
public class FileUtil {
    /**
     * 通过文件路径来获得文件的 byte 数据
     *
     * @param filePath 文件路径
     * @return 文件的byte 数据
     * @throws IOException .
     */
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * the traditional io way
     *
     * @param filename .
     * @return .
     * @throws IOException .
     */
    public static byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length())) {
            BufferedInputStream in = null;
            in = new BufferedInputStream(new FileInputStream(f));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * NIO way
     *
     * @param filename .
     * @return .
     * @throws IOException .
     */
    public static byte[] toByteArray2(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                assert channel != null;
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
     *
     * @param filename .
     * @return .
     * @throws IOException .
     */
    public static byte[] toByteArray3(String filename) throws IOException {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
                    fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                assert fc != null;
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将 来源地址中 当前目录下所有的文件coyp 的目标地址下。
     *
     * @param fromPath 来源地址的根目录
     * @param aimsPath 目标地址的根目录
     */
    public static void copyAll(String fromPath, String aimsPath, boolean del) {
        try {
            if (!aimsPath.endsWith("\\")) {
                aimsPath += "\\";
            }
            File temp = new File(fromPath);
            if (!temp.exists() || !temp.isDirectory()) {
                throw new RuntimeException("起始路径必须为文件夹,且保证它已经存在了");
            }
            copyFile(temp, "", aimsPath, del);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归调用的方法
     *
     * @param temp     当前文件(可能是文件夹或者文件)
     * @param path     当前文件的新增路径
     * @param aimsPath 目标地址
     * @throws IOException .
     */
    private static void copyFile(File temp, String path, String aimsPath, boolean del) throws IOException {
        File[] files = temp.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            String newFileName = aimsPath + path + fileName;
            if (file.isDirectory()) {
                File copy = new File(newFileName);
                if (!copy.exists()) {
                    copy.mkdirs();
                }
                copyFile(file, path + fileName + "\\", aimsPath, del);
            } else {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                FileOutputStream fos = new FileOutputStream(newFileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int len;
                while ((len = bis.read()) != -1) {
                    bos.write(len);
                    bos.flush();
                }
                bis.close();
                fos.close();
                if (del) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 像某个文件中写入信息
     * 不会写入最后
     *
     * @param info     写入的信息
     * @param filePath 文件路径
     */
    @SneakyThrows
    public static void writer(List<String> info, String filePath) {
        FileWriter writer = new FileWriter(filePath);
        for (String message : info) {
            writer.write(message + "\n");
        }
        writer.flush();
        writer.close();
    }

    /**
     * 获得项目根目录
     *
     * @return 项目根目录
     */
    @SneakyThrows
    public static String getProjectRootDirectory() {
        return new File("").getCanonicalPath();
    }

}