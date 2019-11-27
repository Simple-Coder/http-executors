package com.xd.executor.utils;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: PropUtil
 * @Description:
 * @Author: xiedong
 * @Date: 2019/11/27 17:47
 */
public class PropUtil {
    private static ConcurrentHashMap<String, Properties> propsMap = new ConcurrentHashMap();

    public PropUtil() {
    }

    public static String getProperty(String key, String propertyFilePath) {
        Properties prop = getProperties(propertyFilePath);
        return prop == null ? null : prop.getProperty(key);
    }

    public static Properties getProperties(String propertyFilePath) {
        if (propertyFilePath == null) {
            return null;
        } else {
            Properties ppts = (Properties)propsMap.get(propertyFilePath);
            if (ppts == null) {
                ppts = loadPropertyFile(propertyFilePath);
                if (ppts != null) {
                    propsMap.put(propertyFilePath, ppts);
                }
            }

            return ppts;
        }
    }

    private static Properties loadPropertyFile(String propertyFilePath) {
        Properties prop = new Properties();

        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFilePath);
            prop.load(in);
            return prop;
        } catch (FileNotFoundException var3) {
            throw new RuntimeException("配置文件不存在", var3);
        } catch (IOException var4) {
            throw new RuntimeException("配置文件加载异常", var4);
        }
    }

    public static Properties getPropertiesByAbsPath(String propertyFilePath) {
        if (propertyFilePath == null) {
            return null;
        } else {
            Properties ppts = (Properties)propsMap.get(propertyFilePath);
            if (ppts == null) {
                ppts = loadPropFileByAbsPath(propertyFilePath);
                if (ppts != null) {
                    propsMap.put(propertyFilePath, ppts);
                }
            }

            return ppts;
        }
    }

    private static Properties loadPropFileByAbsPath(String filePath) {
        Properties prop = new Properties();
        Object in = null;

        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            if (in == null) {
                File file = new File(filePath);
                in = new FileInputStream(file);
            }

            prop.load((InputStream)in);
        } catch (FileNotFoundException var12) {
            throw new RuntimeException("配置文件不存在", var12);
        } catch (IOException var13) {
            throw new RuntimeException("配置文件加载异常", var13);
        } finally {
            if (in != null) {
                try {
                    ((InputStream)in).close();
                } catch (IOException var11) {
                    throw new RuntimeException("配置文件加载异常", var11);
                }
            }

        }

        return prop;
    }
}
