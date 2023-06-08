package ru.otus.kotlin.brown.mappers.exceptions

class ValueOutOfRange(clazz: Class<*>) : RuntimeException("Value of $clazz is out of range")
