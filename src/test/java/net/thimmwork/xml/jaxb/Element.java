package net.thimmwork.xml.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

import static net.thimmwork.xml.jaxb.Element.XMLELEMENTNAME;

@XmlRootElement(name = XMLELEMENTNAME)
public class Element {

    public static final String XMLELEMENTNAME = "element";

    protected String key;

    @SuppressWarnings("unused")
    public Element() {}

    public Element(String key) {
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
        if (!(o instanceof Element)) return false;
        Element testClass = (Element) o;
        return Objects.equals(getKey(), testClass.getKey());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getKey());
    }
}
