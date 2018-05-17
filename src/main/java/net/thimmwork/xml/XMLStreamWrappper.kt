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
