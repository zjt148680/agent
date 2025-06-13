package com.speedboot.speedbotagent.common.util;

import com.speedboot.speedbotagent.base.annotations.CsvBindingName;
import com.speedboot.speedbotagent.common.exception.SpeedBotException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CsvReadUtils {
    public static <T> List<T> parseCsv(String filePath, Class<T> clazz) {
        try {
            return readCsv(filePath, clazz, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new SpeedBotException("csv文件解析出错", e);
        }
    }

    public static <T> List<T> readCsv(String filePath, Class<T> clazz, CSVFormat format) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        InputStream inputStream = new ClassPathResource(filePath).getInputStream();
        Reader reader = new InputStreamReader(inputStream);
        CSVParser parser = format.parse(reader);
        return parseRecords(parser, clazz);
    }

    private static <T> List<T> parseRecords(CSVParser parser, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> result = new ArrayList<>();

        // 获取注解映射关系：字段 -> CSV列名
        Field[] fields = clazz.getDeclaredFields();
        String[] headers = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            CsvBindingName annotation = fields[i].getAnnotation(CsvBindingName.class);
            if (annotation != null) {
                headers[i] = annotation.column();
            }
            fields[i].setAccessible(true); // 允许访问私有字段
        }

        // 处理每条记录
        for (CSVRecord record : parser) {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (int i = 0; i < fields.length; i++) {
                if (headers[i] != null) {
                    String value = record.get(headers[i]);
                    setFieldValue(fields[i], instance, value);
                }
            }
            result.add(instance);
        }
        return result;
    }

    // 类型安全的值设置
    private static <T> void setFieldValue(Field field, T instance, String value)
            throws IllegalAccessException {
        if (value == null || value.isEmpty()) return;

        Class<?> type = field.getType();

        try {
            if (type == String.class) {
                field.set(instance, value);
            } else if (type == int.class || type == Integer.class) {
                field.set(instance, Integer.parseInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(instance, Long.parseLong(value));
            } else if (type == double.class || type == Double.class) {
                field.set(instance, Double.parseDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(instance, Float.parseFloat(value));
            } else if (type == boolean.class || type == Boolean.class) {
                field.set(instance, Boolean.parseBoolean(value));
            } else {
                // 其他类型使用字符串转换
                field.set(instance, value);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Field '%s' value '%s' cannot convert to %s",
                            field.getName(), value, type.getSimpleName()), e);
        }
    }
}
