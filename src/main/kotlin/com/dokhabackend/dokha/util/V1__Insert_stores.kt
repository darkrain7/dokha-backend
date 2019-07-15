package com.dokhabackend.dokha.util

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import java.io.File


/**
 * Created by SemenovAE on 15.07.2019

 */

class V1__Insert_stores : BaseJavaMigration() {

    override fun migrate(context: Context) {

        val photo = File("photo/test.jpg")
        val jdbcInsert = SimpleJdbcInsert(context.configuration.dataSource).withTableName("s_store")

        val params = HashMap<String, Any>()
        params["id"] = -1
        params["name"] = "Dokha 1"
        params["location"] = "пр. Ленина 34"
        params["photo"] = photo.readBytes()

        jdbcInsert.execute(params)
    }
}