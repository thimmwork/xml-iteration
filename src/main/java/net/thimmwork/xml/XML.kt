package net.thimmwork.xml

import java.io.InputStream
import java.util.*
import java.util.stream.StreamSupport

fun <T> iterator(inputStream: InputStream, clazz: Class<T>, xmlElementName: String): XmlIterator<T>
        = XmlIterator(inputStream, clazz, xmlElementName)

fun <T> stream(inputStream: InputStream, clazz: Class<T>, xmlElementName: String) =
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator(inputStream, clazz, xmlElementName), Spliterator.ORDERED),
                false
        )
