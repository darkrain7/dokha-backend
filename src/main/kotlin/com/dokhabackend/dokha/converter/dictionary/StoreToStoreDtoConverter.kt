package com.dokhabackend.dokha.converter.dictionary

import com.dokhabackend.dokha.converter.AbstractConverter
import com.dokhabackend.dokha.dto.dictionary.StoreDto
import com.dokhabackend.dokha.entity.dictionary.Store
import org.springframework.stereotype.Component


/**
 * Semenov A.E.
 * Created 10.05.2019
 *
 **/
@Component
class StoreToStoreDtoConverter : AbstractConverter<Store, StoreDto>() {
    override fun convert(fromObject: Store): StoreDto =
            StoreDto(id = fromObject.id!!,
                    name = fromObject.name,
                    location = fromObject.location)
}