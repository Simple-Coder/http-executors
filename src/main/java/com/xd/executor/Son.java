package com.xd.executor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @Classname： Son
 * @Description：
 * @Author： xiedong
 * @Date： 2019/11/21 20:52
 * @Version： 1.0
 **/
@XStreamAlias("son")
public class Son {
    @XStreamAsAttribute
    private String sname;

    @XStreamAsAttribute
    @XStreamAlias("class")
    private String clazz;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Son(String sname, String clazz) {
        this.sname = sname;
        this.clazz = clazz;
    }
}
