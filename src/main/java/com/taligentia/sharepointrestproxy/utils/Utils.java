package com.taligentia.sharepointrestproxy.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

    public static String prettyPrintJsonString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(json));
        } catch (JsonProcessingException e) {
            return json;
        }
    }

    public static void dumpToTextFile(String dumpDirectory, String dumpFileName, String dumpValue) throws IOException {
        File f = new File(dumpDirectory);
        if (!f.exists() || !f.isDirectory())
            return;
        BufferedWriter writer = new BufferedWriter(new FileWriter(dumpDirectory + "/" + dumpFileName));
        writer.write(dumpValue);
        writer.close();
    }



}
