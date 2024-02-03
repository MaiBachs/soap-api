package com.viettel.msm.smartphone.utils;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.Data;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

@Data
public class Processor {
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    //Converts Object to XML file
    public void objectToXML(String fileName, Object graph) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            marshaller.marshal(graph, new StreamResult(fos));
        } finally {
            fos.close();
        }
    }
    //Converts XML to Java Object
    public Object xmlToObject(String fileName) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            return unmarshaller.unmarshal(new StreamSource(fis));
        } finally {
            fis.close();
        }
    }
}
