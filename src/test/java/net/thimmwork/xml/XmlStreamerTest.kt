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

class XmlStreamerTest {

    @Test
    fun `XMLStreamer produces outputStream for wrapped list that can be deserialized with JAXB`() {
        //GIVEN
        val jaxbContext = JAXBContext.newInstance(WrappedList::class.java, Element::class.java)
        val wrapperName = WrappedList.XMLLISTWRAPPERNAME
        val rootName = WrappedList.XMLELEMENTNAME
        val utf8Charset = Charset.forName("UTF-8")
        val headerBytes = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><$rootName><$wrapperName>""".toByteArray(utf8Charset)
        val footerBytes = """</$wrapperName></$rootName>""".toByteArray(utf8Charset)

        val outputStream = ByteArrayOutputStream()
        val sut = XMLStreamer(jaxbContext, outputStream, Element::class.java, Element.XMLELEMENTNAME)

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
    fun `XMLStreamer produces outputStream for unwrapped list that can be deserialized with JAXB`() {
        //GIVEN
        val jaxbContext = JAXBContext.newInstance(UnwrappedList::class.java, Element::class.java)
        val rootName = UnwrappedList.XMLELEMENTNAME
        val utf8Charset = Charset.forName("UTF-8")
        val headerBytes = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><$rootName>""".toByteArray(utf8Charset)
        val footerBytes = """</$rootName>""".toByteArray(utf8Charset)

        val outputStream = ByteArrayOutputStream()
        val sut = XMLStreamer(jaxbContext, outputStream, Element::class.java, Element.XMLELEMENTNAME)

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
    fun `XMLStreamTransformer produces outputStream for unwrapped list that can be deserialized with JAXB`() {
        //GIVEN
        val jaxbContext = JAXBContext.newInstance(UnwrappedList::class.java, Element::class.java)
        val rootName = UnwrappedList.XMLELEMENTNAME
        val outputStream = ByteArrayOutputStream()
        val xmlStreamer = XMLStreamer(jaxbContext, outputStream, Element::class.java, Element.XMLELEMENTNAME)

        val list = listOf(
                Element("first"),
                Element("second"),
                Element("third")
        )
        val sut = XMLStreamTransformer(xmlStreamer, rootName, list.stream())

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