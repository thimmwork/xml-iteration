package net.thimmwork.xml

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import java.util.function.Consumer
import java.util.stream.Stream
import java.util.stream.StreamSupport
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.Marshaller
import javax.xml.namespace.QName

fun <T> iterator(inputStream: InputStream, clazz: Class<T>, xmlElementName: String): XmlIterator<T>
        = XmlIterator(inputStream, clazz, xmlElementName)

fun <T> stream(inputStream: InputStream, clazz: Class<T>, xmlElementName: String) =
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator(inputStream, clazz, xmlElementName), Spliterator.ORDERED),
                false
        )

val UTF8 = Charset.forName("UTF-8")

class XMLStreamTransformer<T>(val xmlStreamer: XMLStreamer<T>, wrapperName: String, val elementStream: Stream<T>) {

    private val headerBytes = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><$wrapperName>""".toByteArray(UTF8)
    private val footerBytes = """</$wrapperName>""".toByteArray(UTF8)

    fun copy() {
        xmlStreamer.outputStream.write(headerBytes)
        elementStream.forEach(xmlStreamer)
        xmlStreamer.outputStream.write(footerBytes)
    }
}

class XMLStreamer<T>(jaxbContext: JAXBContext,
                     val outputStream: OutputStream,
                     val clazz: Class<T>,
                     xmlElementName: String?
) : Consumer<T> {
    private val marshaller: Marshaller
    private val qName: QName

    init {
        marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true)
        qName = QName(xmlElementName)
    }

    override fun accept(item: T) {
        val byteBuffer = ByteArrayOutputStream()
        marshaller.marshal(JAXBElement(qName, clazz, item), byteBuffer)
        outputStream.write(byteBuffer.toByteArray())
    }
}
