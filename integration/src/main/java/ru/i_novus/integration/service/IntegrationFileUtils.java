package ru.i_novus.integration.service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.*;
import java.util.*;

/**
 * File utilities.
 *
 * @author asamoilov
 */
public class IntegrationFileUtils {

    // buffer size
    private final static Integer BUF_SIZE = 1024 * 1024;

    /**
     * Соединение массива файлов в один файл
     */
    static void mergeFile(File outFile, File[] files) throws IOException {
        if (files == null)
            return;
        try (FileOutputStream out = new FileOutputStream(outFile)) {
            for (File file : files) {
                try (FileInputStream in = new FileInputStream(file)) {
                    if (in.available() != 0) {
                        byte[] buffer = new byte[BUF_SIZE];
                        int sz = in.read(buffer);
                        out.write(buffer, 0, sz);
                    }
                }
            }
        }
    }

    /**
     * Разделение файла на части по BUF_SIZE
     */
    static void splitFile(File toSplit, File dir) throws IOException {
        int counter = 1;

        byte[] buffer = new byte[BUF_SIZE];

        try (FileInputStream fis = new FileInputStream(toSplit); BufferedInputStream bis = new BufferedInputStream(fis)) {
            int amount;
            if (fis.available() != 0) {
                while ((amount = bis.read(buffer)) > 0) {
                    File partFile = new File(dir + "/" + counter++);

                    try (FileOutputStream out = new FileOutputStream(partFile)) {
                        out.write(buffer, 0, amount);
                    }
                }
            } else {
                new File(dir + "/" + counter).createNewFile();
            }
        }
    }

    /**
     * Сортировка массива файлов по имени
     */
    public static void sortedFilesByName(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int n1 = extractNumber(o1.getName());
                int n2 = extractNumber(o2.getName());
                return n1 - n2;
            }

            private int extractNumber(String name) {
                return Integer.parseInt(name);
            }
        });
    }

    /**
     * Сформировать DataHandler из файла
     */
    public static DataHandler prepareDataHandler(String filePath) {
        return new DataHandler(new FileDataSource(new File(filePath)));
    }

}