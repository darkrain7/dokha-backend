package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.converter.dictionary.StoreToStoreDtoConverter
import com.dokhabackend.dokha.core.RestResponse
import com.dokhabackend.dokha.dto.dictionary.StoreDto
import com.dokhabackend.dokha.service.dictionary.StoreService
import org.springframework.beans.factory.annotation.Autowired
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

    @GetMapping
    fun getById(@RequestParam("id") id: Long): RestResponse<StoreDto> {

        val store = storeService.findById(id)

        return RestResponse(storeToStoreDtoConverter.convert(store.get()))
    }

    @GetMapping(value = ["/findAll"])
    fun getAll(): RestResponse<Collection<StoreDto>> {

        val stores = storeService.findAll()

        return RestResponse(storeToStoreDtoConverter.convertToList(stores))
    }

}