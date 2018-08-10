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

package net.thimmwork.xml

import net.thimmwork.xml.jaxb.Element
import net.thimmwork.xml.jaxb.UnwrappedList
import net.thimmwork.xml.jaxb.WrappedList
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import javax.xml.bind.JAXBContext

class XmlStreamMarshallerTest {

    @Test
    fun `XMLStreamMarshaller produces outputStream for wrapped list that can be deserialized with JAXB`() {
        //GIVEN
        val jaxbContext = JAXBContext.newInstance(WrappedList::class.java, Element::class.java)
        val wrapperName = WrappedList.XMLLISTWRAPPERNAME
        val rootName = WrappedList.XMLELEMENTNAME
        val utf8Charset = Charset.forName("UTF-8")
        val headerBytes = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><$rootName><$wrapperName>""".toByteArray(utf8Charset)
        val footerBytes = """</$wrapperName></$rootName>""".toByteArray(utf8Charset)

        val outputStream = ByteArrayOutputStream()
        val sut = XMLStreamMarshaller(jaxbContext, outputStream, Element::class.java, Element.XMLELEMENTNAME)

        val list = listOf(
                Element("first"),
                Element("second"),
                Element("third")
        )

        //WHEN
        outputStream.write(headerBytes)
        list.stream().forEach(sut)
        outputStream.write(footerBytes)

        //THEN
        val xmlBytes = outputStream.toByteArray()
        println(String(xmlBytes))
        val actualList = jaxbContext.createUnmarshaller().unmarshal(ByteArrayInputStream(xmlBytes)) as WrappedList
        assertThat(actualList, CoreMatchers.isA(WrappedList::class.java))
        assertThat(actualList.elements, equalTo(list))
    }

    @Test
    fun `XMLStreamMarshaller produces outputStream for unwrapped list that can be deserialized with JAXB`() {
        //GIVEN
        val jaxbContext = JAXBContext.newInstance(UnwrappedList::class.java, Element::class.java)
        val rootName = UnwrappedList.XMLELEMENTNAME
        val utf8Charset = Charset.forName("UTF-8")
        val headerBytes = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><$rootName>""".toByteArray(utf8Charset)
        val footerBytes = """</$rootName>""".toByteArray(utf8Charset)

        val outputStream = ByteArrayOutputStream()
        val sut = XMLStreamMarshaller(jaxbContext, outputStream, Element::class.java, Element.XMLELEMENTNAME)

        val list = listOf(
                Element("first"),
                Element("second"),
                Element("third")
        )

        //WHEN
        outputStream.write(headerBytes)
        list.stream().forEach(sut)
        outputStream.write(footerBytes)

        //THEN
        val xmlBytes = outputStream.toByteArray()
        println(String(xmlBytes))
        val actualList = jaxbContext.createUnmarshaller().unmarshal(ByteArrayInputStream(xmlBytes)) as UnwrappedList
        assertThat(actualList, CoreMatchers.isA(UnwrappedList::class.java))
        assertThat(actualList.elements, equalTo(list))
    }

    @Test
    fun `XMLStreamWrapper produces outputStream for unwrapped list that can be deserialized with JAXB`() {
        //GIVEN
        val jaxbContext = JAXBContext.newInstance(UnwrappedList::class.java, Element::class.java)
        val rootName = UnwrappedList.XMLELEMENTNAME
        val outputStream = ByteArrayOutputStream()
        val xmlStreamer = XMLStreamMarshaller(jaxbContext, outputStream, Element::class.java, Element.XMLELEMENTNAME)

        val list = listOf(
                Element("first"),
                Element("second"),
                Element("third")
        )
        val sut = XMLStreamWrapper(xmlStreamer, list.stream(), rootName)

        //WHEN
        sut.copy()

        //THEN
        val xmlBytes = outputStream.toByteArray()
        println(String(xmlBytes))
        val actualList = jaxbContext.createUnmarshaller().unmarshal(ByteArrayInputStream(xmlBytes)) as UnwrappedList
        assertThat(actualList, CoreMatchers.isA(UnwrappedList::class.java))
        assertThat(actualList.elements, equalTo(list))
    }
}