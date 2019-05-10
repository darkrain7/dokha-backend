package com.dokhabackend.dokha.converter

import java.util.*

/**
 * @param <A> Объект который необходимо конвертировать
 * @param <B> Объект в который необходимо конвертировать
 * @author v.butuzov
 * Абстрактный класс конвертера
</B></A> */
abstract class AbstractConverter<A, B> {

    /**
     * Единичная конвертация
     *
     * @param fromObject Объект который необходимо конвертировать
     * @return Объект в который необходимо конвертировать
     */
    abstract fun convert(fromObject: A): B

    /**
     * Конвертирование коллекции
     *
     * @param input список объектов которые необходимо конвертировать
     * @return список объектов в которые необходимо конвертировать
     */
    fun convertToList(input: Collection<A>): Collection<B> {
        val result = ArrayList<B>()
        for (item in input) {
            result.add(convert(item))
        }
        return result
    }

}