/**
 * Copyright 2018 thimmwork
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
