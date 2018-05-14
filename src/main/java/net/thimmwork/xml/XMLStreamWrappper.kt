package net.thimmwork.xml

import java.nio.charset.Charset
import java.util.stream.Stream

val UTF8 = Charset.forName("UTF-8")

/**
 * wraps an XMLStreamMarshaller
 */
class XMLStreamWrapper<T>(val xmlStreamMarshaller: XMLStreamMarshaller<T>,
                          private val elementStream: Stream<T>,
                          wrapperName: String) {

    private val headerBytes = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><$wrapperName>""".toByteArray(UTF8)
    private val footerBytes = """</$wrapperName>""".toByteArray(UTF8)

    fun copy() {
        xmlStreamMarshaller.outputStream.write(headerBytes)
        elementStream.forEach(xmlStreamMarshaller)
        xmlStreamMarshaller.outputStream.write(footerBytes)
    }
}
