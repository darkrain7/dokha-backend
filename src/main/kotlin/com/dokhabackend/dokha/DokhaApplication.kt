package com.dokhabackend.dokha

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
class DokhaApplication

fun main(args: Array<String>) {
    runApplication<DokhaApplication>(*args)
}