package net.thimmwork.xml.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name= XmlTestClass.XMLELEMENTNAME)
public class XmlTestClass {

    public static final String XMLELEMENTNAME = "test";

    protected String key;

    @SuppressWarnings("unused")
    public XmlTestClass() {}

    public XmlTestClass(String key) {
        this.key = key;
    }

    @XmlAttribute
    public String getKey() {
        return key;
    }
    public void setKey(String value) {
        this.key = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlTestClass)) return false;
        XmlTestClass testClass = (XmlTestClass) o;
        return Objects.equals(getKey(), testClass.getKey());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getKey());
    }
}
