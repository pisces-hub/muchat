package io.pisceshub.muchat.server.util;

import cn.hutool.core.util.StrUtil;

import java.io.*;
import java.util.function.Function;

public class FileUtil {

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return boolean
     */
    public static String getFileExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension;
    }

    /**
     * 判断文件是否图片类型
     *
     * @param fileName 文件名
     * @return boolean
     */
    public static boolean isImage(String fileName) {
        String extension = getFileExtension(fileName);
        String[] imageExtension = new String[] { "jpeg", "jpg", "bmp", "png", "webp", "gif" };
        for (String e : imageExtension) {
            if (extension.toLowerCase().equals(e)) {
                return true;
            }
        }

        return false;
    }

    public static byte[] readToByte(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("空");
        }
        try {
            return inputStream.readAllBytes();
        } finally {
            inputStream.close();
        }
    }

    public static void lineHandler(String path, Function<String, String> function) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));

        String outPath = path + ".bak";

        checkeFileExist(outPath);

        BufferedWriter bufferedWriter = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(outPath), "utf-8"));
        ;
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (StrUtil.isEmpty(line)) {
                continue;
            }
            line = function.apply(line);
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();

    }

    public static void main(String[] args) throws Exception {
        String path = FileUtil.class.getClassLoader().getResource("base.txt").getPath();

        lineHandler(path, new Function<String, String>() {

            @Override
            public String apply(String s) {
                if (s.substring(s.length() - 1).equals(",")) {
                    return s.substring(0, s.length() - 1);
                }
                return s;
            }
        });

        String s = "aaa,";
        System.out.println(path);
        System.out.println(s.substring(s.length() - 1));
        System.out.println(s.substring(0, s.length() - 1));
    }

    private static void checkeFileExist(String outPath) {
        File file = new File(outPath);
        if (!file.getParentFile().exists()) {
            file.mkdir();
        }
    }
}
