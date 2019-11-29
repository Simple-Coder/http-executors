package com.xd.executor.utils;

import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Classname： XStreamUtil
 * @Description：xml与bean相互转换工具类
 * @Author： xiedong
 * @Date： 2019/11/21 20:11
 * @Version： 1.0
 **/
public class XStreamUtil {
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * bean转xml
     * @param obj   bean对象
     * @return
     */
    public static String bean2Xml(Object obj) {
        if (obj == null) {
            return "";
        }
//        String top = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n";
        XStream stream = XStreamFactory.getInstance().getStream(obj.getClass());
        return stream.toXML(obj);
    }

    /**
     * xml转bean方法
     * @param xmlStr        xml报文
     * @param reClass       bean
     * @param <T>
     * @return
     */
    public static <T> T xml2Bean(String xmlStr,Class reClass) throws Exception {
        try {
            XStream stream = XStreamFactory.getInstance().getStream(reClass);
            return (T) stream.fromXML(xmlStr);
        } catch (Exception e) {
            throw new Exception(xmlStr+">>>类型转换错误>>>"+reClass);
        }
    }
}
