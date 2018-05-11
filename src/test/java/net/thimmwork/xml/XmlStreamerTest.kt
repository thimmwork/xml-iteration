package net.thimmwork.xml

import net.thimmwork.xml.jaxb.Element
import net.thimmwork.xml.jaxb.Elements
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import javax.xml.bind.JAXBContext

class XmlStreamerTest {
    val jaxbContext = JAXBContext.newInstance(Elements::class.java, Element::class.java)
    val wrapperName = Elements.XMLLISTWRAPPERNAME
    val rootName = Elements.XMLELEMENTNAME

    private val utf8Charset = Charset.forName("UTF-8")
    private val headerBytes = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><$rootName><$wrapperName>""".toByteArray(utf8Charset)
    private val footerBytes = """</$wrapperName></$rootName>""".toByteArray(utf8Charset)

    @Test
    fun `XMLStreamer produces outputStream that can be deserialized with JAXB`() {
        //GIVEN
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
        val actualList = jaxbContext.createUnmarshaller().unmarshal(ByteArrayInputStream(xmlBytes)) as Elements
        assertThat(actualList, CoreMatchers.isA(Elements::class.java))
        assertThat(actualList.elements, equalTo(list))
    }
}