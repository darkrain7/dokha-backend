package com.dokhabackend.dokha.repository.dictionary

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import javax.sql.DataSource


/**
 * Semenov A.E.
 * Created 01.09.2019
 *
 **/

@Repository
class ImageRepository @Autowired constructor(private val dataSource: DataSource) {

    private val query: String = "SELECT image FROM dokha.s_image WHERE id = :id"

    fun save(image: ByteArray): Long =
            SimpleJdbcInsert(dataSource).executeAndReturnKey(mapOf("image" to image)) as Long

    fun findById(id: Long): ByteArray =
            NamedParameterJdbcTemplate(dataSource)
                    .query(query, mapOf("id" to id), ResultSetExtractor {
                        it.next()
                        it.getBytes("image")
                    })!!

}