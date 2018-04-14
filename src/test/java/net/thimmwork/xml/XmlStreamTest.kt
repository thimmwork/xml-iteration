package net.thimmwork.xml

import org.junit.Test

import javax.xml.bind.JAXBException
import javax.xml.stream.XMLStreamException
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.Arrays
import java.util.stream.Collectors
import java.util.stream.Stream

import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat

class XmlStreamTest {

    companion object {
        // five items in XML fragments
        private val TEST1_STR = """<test key="one"></test>"""
        private val TEST2_STR = """<test key="two"></test>"""
        private val TEST3_STR = """<test key="three"></test>"""
        private val TEST4_STR = """<test key="four"></test>"""
        private val TEST5_STR = """<test key="five"></test>"""
    }

    internal var XML_WITHOUT_SPACES = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<jaxbissue>" +
            TEST1_STR + TEST2_STR + TEST3_STR + TEST4_STR + TEST5_STR +
            "</jaxbissue>"

    @Test
    fun `stream retains order`() {

        val inputStream = ByteArrayInputStream(XML_WITHOUT_SPACES.toByteArray())

        val stream = stream(inputStream, XmlTestClass::class.java, XmlTestClass.XMLELEMENTNAME)

        val list = stream.collect(Collectors.toList())
        assertThat(list, equalTo(Arrays.asList(
                XmlTestClass("one"),
                XmlTestClass("two"),
                XmlTestClass("three"),
                XmlTestClass("four"),
                XmlTestClass("five")
        )))
    }
}