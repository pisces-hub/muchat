package io.pisceshub.muchat.server.util;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    /**
     * 获取文件后缀
     *
     * @param fileName  文件名
     * @return boolean
     */
    public static String getFileExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension;
    }

    /**
     * 判断文件是否图片类型
     *
     * @param fileName  文件名
     * @return  boolean
     */
    public static boolean isImage(String fileName) {
        String extension = getFileExtension(fileName);
        String[] imageExtension = new String[]{"jpeg", "jpg", "bmp", "png","webp","gif"};
        for (String e : imageExtension){
            if (extension.toLowerCase().equals(e)) {
                return true;
            }
        }

        return false;
    }

    public static byte[] readToByte(InputStream inputStream) throws IOException {
        if(inputStream==null){
            throw new IOException("空");
        }
        try {
            return inputStream.readAllBytes();
        }finally {
            inputStream.close();
        }

    }
}
