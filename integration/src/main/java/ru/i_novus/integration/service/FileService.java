package ru.i_novus.integration.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.IntegrationProperties;
import ru.i_novus.integration.ws.internal.api.DocumentData;
import ru.i_novus.integration.ws.internal.api.IntegrationMessage;
import ru.i_novus.integration.ws.internal.api.MessageData;
import ru.i_novus.integration.ws.internal.api.SplitDocumentModel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private static final String TEMP_PATH = "tmp";
    private static final String MERGE_FILE_PATH = "merge";
    private static final char URL_SPLIT = '/';
    private final IntegrationProperties property;

    @Autowired
    public FileService(IntegrationProperties property) {
        this.property = property;
    }

    public void saveDocumentInStorage(String message) throws IOException, JAXBException {
        MessageData messageData = stringToJaxb(message).getMessage();
        DocumentData data = messageData.getAppData();

        File tmpDirectory = new File(property.getTempPath() + URL_SPLIT + TEMP_PATH + URL_SPLIT + messageData.getGroupUid());
        if (!tmpDirectory.exists()) {
            tmpDirectory.mkdirs();
        }
        File[] filesCount = tmpDirectory.listFiles();
        int count = 1;
        if (filesCount != null) {
            count = count + filesCount.length;
        }
        File tempFile = new File(tmpDirectory.getPath() + URL_SPLIT + count);

        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            IOUtils.copy(data.getSplitDocument().getBinaryData().getInputStream(), outputStream);
            logger.info("file path {} name {} count {} file size {}", tmpDirectory.getPath(), data.getDocName(), count, tempFile.length());
        }
        if (data.getSplitDocument().getCount() == tmpDirectory.listFiles().length) {
            File margeDir = new File(property.getTempPath() + URL_SPLIT + MERGE_FILE_PATH);
            margeDir.mkdirs();
            File concatFile = new File(margeDir.getPath() + URL_SPLIT + data.getDocName());
            List<File> fileList = Arrays.asList(new File(tmpDirectory.getPath()).listFiles());
            if (!fileList.isEmpty()) {
                IntegrationFileUtils.sortedFilesByName(fileList);
            }
            logger.info("merge path {} dir size{}", tmpDirectory.getPath(), fileList.size());
            IntegrationFileUtils.mergeFile(concatFile, fileList);

            logger.info("concat file size{}", concatFile.length());

            Files.move(concatFile.toPath(), new File(property.getTempPath() + URL_SPLIT + data.getDocName()).toPath(),
                    StandardCopyOption.ATOMIC_MOVE);
            logger.info("file {} put to {} file size {}", data.getDocName(), property.getTempPath(),
                    new File(property.getTempPath() + URL_SPLIT + data.getDocName()).length());
            FileUtils.deleteDirectory(tmpDirectory);
            Files.deleteIfExists(Paths.get(concatFile.getPath()));
        }
    }

    SplitDocumentModel prepareSplitModel(String filePath, String uid) throws IOException {
        File file = new File(filePath);
        File splitDir = new File(getTempPath() + uid + "_");
        logger.info("prepare split file {} file size {}", filePath, file.length());
        splitDir.mkdirs();
        SplitDocumentModel splitModel = new SplitDocumentModel();
        splitModel.setTemporaryPath(splitDir.getPath());
        IntegrationFileUtils.splitFile(file, new File(splitModel.getTemporaryPath()));
        splitModel.setCount(splitDir.list().length);

        return splitModel;
    }

    private String getTempPath() {
        return property.getTempPath() + URL_SPLIT + TEMP_PATH +
                URL_SPLIT + LocalDate.now().getYear() +
                URL_SPLIT + LocalDate.now().getMonth() +
                URL_SPLIT + LocalDate.now().getDayOfMonth() +
                URL_SPLIT + UUID.randomUUID() +
                URL_SPLIT;
    }

    private IntegrationMessage stringToJaxb(String message) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(IntegrationMessage.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(message);

        return (IntegrationMessage) unmarshaller.unmarshal(reader);
    }
}
