package com.demo.operator.writer;

import org.apache.flink.api.java.io.CsvOutputFormat;
import org.apache.flink.streaming.connectors.fs.StreamWriterBase;
import org.apache.flink.streaming.connectors.fs.Writer;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

public class BeanCsvWriter<T> extends StreamWriterBase<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BeanCsvWriter.class);

    private static final long serialVersionUID = 1L;
    private final String charsetName;
    private transient Charset charset;
    private String fieldDelimiter;
    private String recordDelimiter;
    private String[] fieldNames;

    public BeanCsvWriter() {
        this("UTF-8", null, CsvOutputFormat.DEFAULT_FIELD_DELIMITER, CsvOutputFormat.DEFAULT_LINE_DELIMITER);
    }

    public BeanCsvWriter(String fieldDelimiter) {
        this("UTF-8", null, fieldDelimiter, CsvOutputFormat.DEFAULT_LINE_DELIMITER);
    }

    public BeanCsvWriter(String[] fieldNames) {
        this("UTF-8", fieldNames, CsvOutputFormat.DEFAULT_FIELD_DELIMITER, CsvOutputFormat.DEFAULT_LINE_DELIMITER);
    }

    public BeanCsvWriter(String[] fieldNames, String fieldDelimiter) {
        this("UTF-8", fieldNames, fieldDelimiter, CsvOutputFormat.DEFAULT_LINE_DELIMITER);
    }

    public BeanCsvWriter(String charsetName, String[] fieldNames, String fieldDelimiter, String recordDelimiter) {
        this.charsetName = charsetName;
        this.fieldNames = fieldNames;
        this.fieldDelimiter = fieldDelimiter;
        this.recordDelimiter = recordDelimiter;
    }

    @Override
    public void open(FileSystem fs, Path path) throws IOException {
        super.open(fs, path);
        try {
            this.charset = Charset.forName(charsetName);
        } catch (IllegalCharsetNameException ex) {
            throw new IOException("The charset " + charsetName + " is not valid.", ex);
        } catch (UnsupportedCharsetException ex) {
            throw new IOException("The charset " + charsetName + " is not supported.", ex);
        }
    }

    @Override
    public void write(T element) throws IOException {
        FSDataOutputStream outputStream = getStream();
        try {
            writeRow(element, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeRow(T element, FSDataOutputStream out) throws IOException, IllegalAccessException, NoSuchFieldException {
        Class<?> clazz = element.getClass();
        String line;
        if (fieldNames != null) {
            line = fieldNamesNotNull(element, clazz);
        } else {
            line = fieldNameIsNull(element, clazz);
        }
        out.write(line.getBytes(charset));
    }

    private String fieldNamesNotNull(T element, Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        for (String fieldName : fieldNames) {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(element);
            if (obj != null) {
                sb.append(obj.toString());
            }
            sb.append(this.fieldDelimiter);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(this.recordDelimiter);
        return sb.toString();
    }

    //所有field字段全写入
    private String fieldNameIsNull(T element, Class<?> clazz) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object obj = field.get(element);
            if (obj != null) {
                sb.append(obj.toString());
            }
            sb.append(this.fieldDelimiter);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(this.recordDelimiter);
        return sb.toString();
    }

    @Override
    public Writer<T> duplicate() {
        return new BeanCsvWriter(charsetName, fieldNames, fieldDelimiter, recordDelimiter);
    }
}
