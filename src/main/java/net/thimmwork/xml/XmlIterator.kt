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

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException
import javax.xml.bind.Unmarshaller
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader
import java.io.InputStream
import java.util.NoSuchElementException

/**
 * @author dalelane, notsaltfish, thimmwork
 * @param T class to deserialize
 */
class XmlIterator<T> @Throws(JAXBException::class, XMLStreamException::class)
constructor(inputStream: InputStream,
            private val tClazz: Class<T>,
            private val xmlElementName: String
) : Iterator<T> {
    private val unmarshaller: Unmarshaller
    private val reader: XMLStreamReader
    private var next: T? = null
    private var readerEvent: Int = 0
    private var closed: Boolean = false

    init {
        // prepare an unmarshaller to turn the XML into objects
        val context = JAXBContext.newInstance(tClazz)
        unmarshaller = context.createUnmarshaller()
        // prepare a stream to read the XML document
        reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream)

        // get first event for the XML parsing
        readerEvent = reader.next()
    }

    override fun hasNext(): Boolean {
        return if (next != null) {
            true
        } else loadNext()
    }

    private fun loadNext(): Boolean {
        try {
            // keep going until we reach the end of the document
            while (readerEvent != XMLStreamConstants.END_DOCUMENT) {

                // keep unmarshalling for every element of the expected type
                if (readerEvent == XMLStreamConstants.START_ELEMENT && reader.localName == xmlElementName) {
                    val unmarshalledObj = unmarshaller.unmarshal(reader, tClazz)
                    next = unmarshalledObj.value

                    // The unmarshaller will have moved the pointer in the
                    //  stream reader - we should now be pointing at the
                    //  next event immediately after the unmarshalled
                    //  element.
                    // This will either the start of the next element, or
                    //  CHARACTERS if there is whitespace in between them,
                    //  or something else like a comment.
                    // We need to check this to decide whether we can
                    //  unmarshall again, or if we need to move the
                    //  stream reader on to get to the next element.
                    readerEvent = reader.eventType
                    return true
                }

                // move the stream reader on to the next element
                readerEvent = reader.next()
            }
            // reached the end of the document - close the reader
            reader.close()
            closed = true
            return false
        } catch (e: XMLStreamException) {
            throw RuntimeException("unable to parse stream", e)
        } catch (e: JAXBException) {
            throw RuntimeException("unable to parse stream", e)
        }
    }

    override fun next(): T {
        if (closed || next == null && !hasNext()) {
            throw NoSuchElementException("stream closed. no next element exists.")
        }
        val result = next!!
        next = null
        return result
    }
}