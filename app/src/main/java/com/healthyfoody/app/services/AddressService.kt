package com.healthyfoody.app.services

import com.healthyfoody.app.models.Address

class AddressService {
    val addresses : List<Address> = listOf(Address(12.00f,13.0f),Address(16.00f,14.0f),Address(11.00f,15.0f))
    fun findAll():List<Address>{
        return addresses
    }

}