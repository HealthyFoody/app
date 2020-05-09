package com.healthyfoody.app.services

import com.healthyfoody.app.models.Category

class CategoryService() {
    val categories : List<Category> = listOf(Category("1","Categoria 2","categoria_tortilla"),
        Category("3","Categoria 3","categoria_pasta"),Category("4","Categoria 4","categoria_menestra"),
        Category("5","Categoria 5","categoria_ensalada"))
    fun findAll():List<Category>{
        return categories
    }

}