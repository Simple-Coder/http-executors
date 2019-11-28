package com.xd.executor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @Classname： testXml2Bean
 * @Description：
 * @Author： xiedong
 * @Date： 2019/11/21 20:50
 * @Version： 1.0
 **/
@XStreamAlias("fathor")
public class Fathor {
    @XStreamAsAttribute
    private String id;
    @XStreamAlias("personAge")
    private Integer age;
    @XStreamImplicit
    private List<Son> sons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Son> getSons() {
        return sons;
    }

    public void setSons(List<Son> sons) {
        this.sons = sons;
    }

    @Override
    public String toString() {
        return "Fathor{" +
                "id='" + id + '\'' +
                ", age=" + age +
                ", sons=" + sons +
                '}';
    }
}
