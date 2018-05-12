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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name= UnwrappedList.XMLELEMENTNAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class UnwrappedList {

    public static final String XMLELEMENTNAME = "root";

    @XmlElement(name = Element.XMLELEMENTNAME, required = true)
    private List<Element> elements;

    @SuppressWarnings("unused")
    public UnwrappedList() {}

    public UnwrappedList(List<Element> elements) {
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }
    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnwrappedList)) return false;
        UnwrappedList testClass = (UnwrappedList) o;
        return Objects.equals(getElements(), testClass.getElements());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getElements());
    }
}
