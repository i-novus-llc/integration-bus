package ru.i_novus.integration.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
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
import java.time.LocalDate;

@Component
public class FileService {
    private static final String TEMP_PATH = "tmp";
    private static final String MERGE_FILE_PATH = "merge";
    private static final String URL_SPLIT = "\\";
    @Autowired
    private PlaceholdersProperty property;

    public void saveDocumentInStorage(String message) throws IOException, JAXBException {
        MessageData messageData = stringToJaxb(message).getMessage();
        DocumentData data = messageData.getAppData();

        File tmpDirectory = new File(property.getTempPath() + URL_SPLIT + TEMP_PATH + URL_SPLIT + messageData.getGroupUid());
        if (!tmpDirectory.exists()) {
            tmpDirectory.mkdirs();
        }
        File tempFile = new File(tmpDirectory.getPath() + URL_SPLIT + data.getSplitDocument().getCount());

        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            IOUtils.copy(data.getSplitDocument().getBinaryData().getInputStream(), outputStream);
        }
        if (data.getSplitDocument().isIsLast() != null && data.getSplitDocument().isIsLast()) {
            File concatFile = new File(property.getTempPath() + URL_SPLIT + MERGE_FILE_PATH + URL_SPLIT + data.getDocName());
            File[] fileList = new File(tmpDirectory.getPath()).listFiles();
            IntegrationFileUtils.sortedFilesByName(fileList);
            IntegrationFileUtils.mergeFile(concatFile, fileList);

            try (InputStream in = new FileInputStream(concatFile);
                 FileOutputStream out = new FileOutputStream(new File(property.getTempPath() + URL_SPLIT + data.getDocName()))) {
                IOUtils.copy(in, out);
            }
            FileUtils.deleteDirectory(tmpDirectory);
            Files.deleteIfExists(Paths.get(concatFile.getPath()));
        }
    }

    SplitDocumentModel prepareSplitModel(String filePath, String uid) throws IOException {
        File splitDir = new File(getTempPath() + uid);
        splitDir.mkdirs();
        SplitDocumentModel splitModel = new SplitDocumentModel();
        splitModel.setTemporaryPath(splitDir.getPath());
        IntegrationFileUtils.splitFile(new File(filePath), new File(splitModel.getTemporaryPath()));

        return splitModel;
    }

    private String getTempPath() {
        return property.getTempPath() + URL_SPLIT + TEMP_PATH +
                URL_SPLIT + LocalDate.now().getYear() +
                URL_SPLIT + LocalDate.now().getMonth() +
                URL_SPLIT + LocalDate.now().getDayOfMonth() +
                URL_SPLIT;

    }

    private IntegrationMessage stringToJaxb(String message) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(IntegrationMessage.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(message);

        return (IntegrationMessage) unmarshaller.unmarshal(reader);
    }
}