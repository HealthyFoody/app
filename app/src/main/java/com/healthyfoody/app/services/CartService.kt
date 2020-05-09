package com.healthyfoody.app.services

import com.healthyfoody.app.models.Cart

class CartService {
    val cart : List<Cart> = listOf(Cart("1","Carrito Item 1"),Cart("2","Carrito Item 2"))
    fun findAll():List<Cart>{
        return cart
    }
}