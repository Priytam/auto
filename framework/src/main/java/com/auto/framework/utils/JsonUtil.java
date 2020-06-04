package com.auto.framework.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.InputStream;

public final class JsonUtil {

    private static final ObjectMapper mapper = getNewObjectMapper();

    private JsonUtil() {
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    private static ObjectMapper getNewObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.setVisibility(mapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper;
    }

    public static <T> String serialize(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Exception serialising json", e);
        }
    }

    public static <T> T deSerialize(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (Exception e) {
            throw new RuntimeException("Exception deSerialize json " + json, e);
        }
    }

    public static <T> T deSerialize(InputStream in, Class<T> tClass) {
        try {
            return mapper.readValue(in, tClass);
        } catch (Exception e) {
            throw new RuntimeException("Exception deSerialize json", e);
        }
    }
}
