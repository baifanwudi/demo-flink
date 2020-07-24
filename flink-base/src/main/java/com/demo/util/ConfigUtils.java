package com.demo.util;

import java.io.IOException;
import java.util.Properties;

/**
 * @author allen.bai
 */
public class ConfigUtils {

    private static final Properties props;
    /**
     *获取配置文件
     */
    static {
        props = new Properties();
        try {
            props.load(ConfigUtils.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取key的value
     */
    public static String getPros(String key) {
        return props.getProperty(key);
    }
}