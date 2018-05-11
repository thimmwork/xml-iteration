package net.thimmwork.xml

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.function.Consumer
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
