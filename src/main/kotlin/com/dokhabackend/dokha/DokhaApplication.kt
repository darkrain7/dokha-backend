package com.dokhabackend.dokha

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DokhaApplication

fun main(args: Array<String>) {
    runApplication<DokhaApplication>(*args)
}
