package ru.i_novus.integration.registry.backend.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.n2oapp.platform.loader.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JsonLocalLoaderRunner extends BaseLoaderRunner implements ServerLoaderRestService {

    private static final Logger logger = LoggerFactory.getLogger(JsonLocalLoaderRunner.class);

    private ObjectMapper objectMapper;

    public JsonLocalLoaderRunner(List<ServerLoader> loaders, ObjectMapper objectMapper) {
        super(loaders);
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<Object> read(InputStream body, LoaderDataInfo<?> info) {
        List<Object> data;
        try {
            CollectionType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, info.getDataType());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(body, StandardCharsets.UTF_8));
            data = objectMapper.readValue(bufferedReader, type);
            logger.info("Read data: {}", data);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Cannot read body for %s", info.getTarget()), e);
        }
        return data;
    }
}
