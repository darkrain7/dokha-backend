package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.converter.dictionary.StoreToStoreDtoConverter
import com.dokhabackend.dokha.dto.dictionary.StoreDto
import com.dokhabackend.dokha.entity.dictionary.Store
import com.dokhabackend.dokha.service.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


/**
 * Semenov A.E.
 * Created 10.05.2019
 *
 **/

@CrossOrigin
@RestController
@RequestMapping(value = ["/store"])
class StoreController @Autowired constructor(val storeToStoreDtoConverter: StoreToStoreDtoConverter,
                                             val storeService: StoreService) {

    @RequestMapping(method = [RequestMethod.GET])
    fun getById(@RequestParam("id") id: Long): ResponseEntity<StoreDto> {

        val store = storeService.findById(id)

        return ResponseEntity.ok(storeToStoreDtoConverter.convert(store.get()))
    }

    @RequestMapping(value = ["/findAll"], method = [RequestMethod.GET])
    fun getAll(): ResponseEntity<Collection<StoreDto>> {

        val stores = storeService.findAll()

        return ResponseEntity(storeToStoreDtoConverter.convertToList(stores), HttpStatus.OK)
    }

    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    fun create() {

        val store = Store(
                null,
                "test",
                "test")

        storeService.create(store)
    }
}