package com.dokhabackend.dokha.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * Semenov A.E.
 * Created 08.06.2019
 *
 **/

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}