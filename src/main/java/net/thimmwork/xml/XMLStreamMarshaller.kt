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

/**
 * a Consumer that marshalls consumed objects into an XML OutputStream
 * using the given Mashaller or a marshaller created from the given JAXBContext.
 */
class XMLStreamMarshaller<T>(private val marshaller: Marshaller,
                             val outputStream: OutputStream,
                             val clazz: Class<T>,
                             xmlElementName: String?
) : Consumer<T> {
    constructor(jaxbContext: JAXBContext, outputStream: OutputStream, clazz: Class<T>, xmlElementName: String?)
            : this(jaxbContext.createMarshaller(), outputStream, clazz, xmlElementName)

    private val qName: QName

    init {
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true)
        qName = QName(xmlElementName)
    }

    override fun accept(item: T) {
        val byteBuffer = ByteArrayOutputStream()
        marshaller.marshal(JAXBElement(qName, clazz, item), byteBuffer)
        outputStream.write(byteBuffer.toByteArray())
    }
}
