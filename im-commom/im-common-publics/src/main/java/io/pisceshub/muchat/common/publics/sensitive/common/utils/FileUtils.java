package io.pisceshub.muchat.common.publics.sensitive.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.*;

/**
 * @author xiaochangbai
 */
public class FileUtils {

    /**
     * 压缩zip
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String compressFileByZIP(String filePath, String outPath) throws Exception {
        File file = new File(filePath);
        String outputFIleName = file.getName() + ".zip";
        ArrayList<File> fileList = new ArrayList<>();
        if (file.isDirectory()) {
            fileList.addAll(Arrays.asList(file.listFiles()));
        } else {
            fileList.add(file);
        }
        FileInputStream fileInputStream = null;
        if (outPath == null) {
            outPath = file.getParentFile().getPath() + File.separator + outputFIleName;
        }
        CheckedOutputStream checkedOutputStream = new CheckedOutputStream(new FileOutputStream(outPath), new Adler32());
        ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream);
        for (File f : fileList) {
            if (f.isDirectory()) {
                continue;
            }
            zipOutputStream.putNextEntry(new ZipEntry(f.getName()));
            fileInputStream = new FileInputStream(f);
            byte[] bytes = new byte[1024];
            int read;
            while ((read = fileInputStream.read(bytes)) != -1) {
                zipOutputStream.write(bytes);
            }
        }
        byte[] bytes = new byte[1024];
        int read;
        while ((read = fileInputStream.read(bytes)) != -1) {
            zipOutputStream.write(bytes);
        }
        fileInputStream.close();
        zipOutputStream.close();

        return outputFIleName;
    }

    /**
     * 读出每一行的内容
     * 
     * @return
     */
    public static List<String> readAllLines(InputStream io) {
        if (io == null) {
            return Collections.emptyList();
        }
        BufferedReader e = new BufferedReader(new InputStreamReader(io, StandardCharsets.UTF_8));
        try {
            List<String> lines = new ArrayList();
            while (true) {
                String entry;
                do {
                    if (!e.ready()) {
                        return lines;
                    }

                    entry = e.readLine();
                } while (StringUtil.isEmpty(entry));

                lines.add(entry);
            }
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        } finally {
            if (e != null) {
                try {
                    e.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * 从压缩文件中读取每一行内容
     * 
     * @return
     */
    public static List<String> readAllLinesForZip(InputStream inputStream) {
        if (inputStream == null) {
            return Collections.emptyList();
        }
        ZipInputStream zipInputStream = new ZipInputStream(new CheckedInputStream(inputStream, new Adler32()));
        BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(zipInputStream, StandardCharsets.UTF_8));
        try {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                List<String> datas = new ArrayList<>();
                while (true) {
                    String entry;
                    do {
                        if (!bufferedReader.ready()) {
                            return datas;
                        }
                        entry = bufferedReader.readLine();
                        datas.add(entry);
                    } while (StringUtil.isEmpty(entry));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                zipInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return Collections.emptyList();
    }

    public static boolean isImage(String string) {
        if (StringUtil.isEmpty(string)) {
            return false;
        } else {
            return string.endsWith(".png") || string.endsWith(".jpeg") || string.endsWith(".jpg")
                   || string.endsWith(".gif");
        }
    }
}
