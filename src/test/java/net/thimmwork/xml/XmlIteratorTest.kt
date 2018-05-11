package net.thimmwork.xml

import net.thimmwork.xml.jaxb.XmlTestClass
import org.junit.Test

import javax.xml.bind.JAXBException
import javax.xml.stream.XMLStreamException
import java.io.ByteArrayInputStream

import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat

class XmlIteratorTest {

    companion object {
        // five items in XML fragments
        private val TEST1_STR = """<test key="one"></test>"""
        private val TEST2_STR = """<test key="two"></test>"""
        private val TEST3_STR = """<test key="three"></test>"""
        private val TEST4_STR = """<test key="four"></test>"""
        private val TEST5_STR = """<test key="five"></test>"""
    }

    @Test
    @Throws(JAXBException::class, XMLStreamException::class)
    fun `iteration over xml without spaces`() {
        val XML_WITHOUT_SPACES = """<?xml version="1.0" encoding="UTF-8"?>
            |<jaxbissue>$TEST1_STR$TEST2_STR$TEST3_STR$TEST4_STR$TEST5_STR
            |</jaxbissue>""".trimMargin()

        val inputStream = ByteArrayInputStream(XML_WITHOUT_SPACES.toByteArray())

        val iterator = XmlIterator(inputStream, XmlTestClass::class.java, XmlTestClass.XMLELEMENTNAME)

        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("one")))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("two")))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("three")))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("four")))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("five")))
        assertThat(iterator.hasNext(), equalTo(false))
        assertThat(iterator.hasNext(), equalTo(false))
    }

    @Test
    @Throws(JAXBException::class, XMLStreamException::class)
    fun `iteration over xml with spaces and line breaks`() {
        val XML_WITH_SPACES = """<?xml version="1.0" encoding="UTF-8"?><jaxbissue>
            $TEST1_STR $TEST2_STR $TEST3_STR $TEST4_STR $TEST5_STR
            </jaxbissue>"""

        val inputStream = ByteArrayInputStream(XML_WITH_SPACES.toByteArray())

        val iterator = XmlIterator(inputStream, XmlTestClass::class.java, XmlTestClass.XMLELEMENTNAME)

        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("one")))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("two")))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("three")))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("four")))
        assertThat(iterator.hasNext(), equalTo(true))
        assertThat(iterator.next(), equalTo(XmlTestClass("five")))
        assertThat(iterator.hasNext(), equalTo(false))
        assertThat(iterator.hasNext(), equalTo(false))
    }
}
