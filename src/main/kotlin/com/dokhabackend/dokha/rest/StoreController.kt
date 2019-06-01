package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.converter.dictionary.StoreToStoreDtoConverter
import com.dokhabackend.dokha.dto.dictionary.StoreDto
import com.dokhabackend.dokha.service.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


/**
 * Semenov A.E.
 * Created 10.05.2019
 *
 **/

@RestController
@RequestMapping(value = ["/store"])
class StoreController @Autowired constructor(val storeToStoreDtoConverter: StoreToStoreDtoConverter,
                                             val storeService: StoreService) {

    @RequestMapping(method = [RequestMethod.GET])
    fun getById(@RequestParam("id") id: Long): ResponseEntity<StoreDto> {

        val store = storeService.findById(id)

        return ResponseEntity(storeToStoreDtoConverter.convert(store.get()), HttpStatus.OK)
    }

    @RequestMapping(value = ["/findAll"], method = [RequestMethod.GET])
    fun getAll(): ResponseEntity<Collection<StoreDto>> {

        val authentication = SecurityContextHolder.getContext().authentication
        val stores = storeService.findAll()

        return ResponseEntity(storeToStoreDtoConverter.convertToList(stores), HttpStatus.OK)
    }
}