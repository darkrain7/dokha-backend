package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.converter.dictionary.StoreToStoreDtoConverter
import com.dokhabackend.dokha.dto.dictionary.StoreDto
import com.dokhabackend.dokha.service.StoreService
import org.springframework.beans.factory.annotation.Autowired
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
    fun getById(@RequestParam("id") id: Long): StoreDto {

        val store = storeService.findById(id)

        return storeToStoreDtoConverter.convert(store.get())
    }

    @RequestMapping(value = ["/findAll"], method = [RequestMethod.GET])
    fun getAll(): Collection<StoreDto> {

        val stores = storeService.findAll()

        return storeToStoreDtoConverter.convertToList(stores)
    }
}