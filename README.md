## XML Iteration
This library provides classes for de/serializing collections/streams of objects to XML. It relies heavily on JAXB.

[![Build Status](https://travis-ci.org/thimmwork/xml-iteration.svg?branch=master)](https://travis-ci.org/thimmwork/xml-iteration)

### Deserialization
#### XMLIterator
Given an `InputStream` containing the XML
```xml
<fruits>
  <fruit name="orange"/>
  <fruit name="lemon"/>
</fruits>
```
* the XML element name `fruit` and
* a class `Fruit` with suitable JAXB annotations,

the XMLIterator streams the `InputStream` until it finds a closing tag of `fruit` and deserializes it to an instance of `Fruit`:
```
XMLIterator<Fruit> iterator = new XMLIterator<>(inputStream, Fruit.class, "fruit");
while (iterator.hasNext()) {
    System.out.println(iterator.next().getName());
}
```
```
for (Fruit fruit : new XMLIterator<>(inputStream, Fruit.class, "fruit")) {
    System.out.println(fruit.getName());
}
```
will result in the output
```text
orange
lemon
```
Iteration will occur on calls of `hasNext()` and continue until the end of the stream is reached. 

Beware that if the stream is infinite and does not contain any matching element, the stream will iterate indefinitely!

