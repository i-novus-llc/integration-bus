package ru.i_novus.integration.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.ws.internal.DocumentData;
import ru.i_novus.integration.ws.internal.MessageData;
import ru.i_novus.integration.ws.internal.model.ObjectFactory;
import ru.i_novus.integration.ws.internal.model.SplitDocumentModel;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileStorageService {
    private static final String TEMP_PATH = "tmp";
    private static final String MERGE_FILE_PATH = "merge";
    private static final String URL_SPLIT = "/";
    @Autowired
    private PlaceholdersProperty property;

    public DataHandler prepareDataHandler(String filePath) {
        return new DataHandler(new FileDataSource(new File(filePath)));
    }

    public void saveDocumentInStorage(MessageData messageData) throws IOException {
        DocumentData data = messageData.getAppData();

        File tmpDirectory = new File(property.getTempPath() + URL_SPLIT + TEMP_PATH + URL_SPLIT + messageData.getGroupUid());
        if (!tmpDirectory.exists()) {
            tmpDirectory.mkdirs();
        }
        File tempFile = new File(tmpDirectory.getPath() + data.getSplitDocument().getCount());

        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            IOUtils.copy(data.getSplitDocument().getBinaryData().getInputStream(), outputStream);
        }
        if (data.getSplitDocument().isIsLast()) {
            mergeFiles(tmpDirectory.getPath(), data.getDocName());
        }
    }

    public SplitDocumentModel prepareSplitModel(String filePath, String uid) throws IOException {
        ObjectFactory objectFactory = new ObjectFactory();
        File splitDir = new File(getTempPath() + uid);
        splitDir.mkdirs();
        SplitDocumentModel splitModel = objectFactory.createSplitDocumentModel();
        splitModel.setTemporaryPath(splitDir.getPath());
        splitFile(new File(filePath), splitModel);

        return splitModel;
    }

    private void mergeFiles(String outPath, String docName) throws IOException {
        List<File> files = Arrays.stream((new File(outPath).listFiles())).collect(Collectors.toList());
        File tempFile = new File(property.getTempPath() + URL_SPLIT + MERGE_FILE_PATH + URL_SPLIT + docName);

        try (FileOutputStream fos = new FileOutputStream(tempFile);
             BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
            files.forEach(f -> {
                try {
                    Files.copy(f.toPath(), mergingStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        try (InputStream in = new FileInputStream(tempFile)) {
            IOUtils.copy(in, new FileOutputStream(new File(property.getTempPath() + URL_SPLIT + docName)));
            files.forEach(file -> {
                try {
                    Files.deleteIfExists(Paths.get(file.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        Files.deleteIfExists(Paths.get(tempFile.getPath()));
    }

    private void splitFile(File f, SplitDocumentModel splitModel) throws IOException {
        int partCounter = 1;

        int sizeOfFiles = 1024 * 1024;
        byte[] buffer = new byte[sizeOfFiles];

        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                File newFile = new File(splitModel.getTemporaryPath(), Integer.toString(partCounter++));
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
        }
    }

    private String getTempPath() {
        return property.getTempPath() + URL_SPLIT + TEMP_PATH +
                URL_SPLIT + LocalDate.now().getYear() +
                URL_SPLIT + LocalDate.now().getMonth() +
                URL_SPLIT + LocalDate.now().getDayOfMonth() +
                URL_SPLIT;

    }
}
