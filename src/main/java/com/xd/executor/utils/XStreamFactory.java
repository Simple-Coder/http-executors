package com.xd.executor.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname： XStreamFactory
 * @Description：XStream工厂
 * @Author： xiedong
 * @Date： 2019/11/21 18:54
 * @Version： 1.0
 **/
public class XStreamFactory {
    private static volatile Map<String, XStream> streamMap = new HashMap<String, XStream>();
    private static volatile XStream xStream = null;
    private static volatile XStreamFactory instance = null;
    public static XStreamFactory getInstance()
    {

        if (instance == null)
        {
            synchronized (XStreamFactory.class)
            {
                if (instance == null)
                {
                    instance = new XStreamFactory();
                }
            }
        }

        return instance;
    }
    //构造方法私有化
    private XStreamFactory() {
    }

    /**
     * 注解方式的xstream
     * @param tClass
     * @return
     */
    public XStream getStream(Class tClass) {
        XStream stream = null;
        if (streamMap.containsKey(tClass.getName())) {
            stream = streamMap.get(tClass.getName());
        } else {
            stream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
            stream.alias("xml", tClass);
            stream.processAnnotations(tClass);
            streamMap.put(tClass.getName(), stream);
        }
        return stream;
    }

    /**
     * 非注解方式的xstream
     * @return
     */
    public XStream getXStream() {

        if (xStream == null) {
            xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        }
        return xStream;
    }
}
