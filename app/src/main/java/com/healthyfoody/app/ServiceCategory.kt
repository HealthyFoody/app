package com.healthyfoody.app

import com.healthyfoody.app.models.Category

class ServiceCategory() {
    val categories : List<Category> = listOf(Category("1","Categoria 1","URLIMAGECAT1"),Category("1","Categoria 2","URLIMAGECAT2"),
        Category("3","Categoria 3","URLIMAGECAT3"),Category("4","Categoria 4","URLIMAGECAT4"),
        Category("5","Categoria 5","URLIMAGECAT5"),Category("6","Categoria 6","URLIMAGECAT6"),
        Category("7","Categoria 7","URLIMAGECAT7"),Category("8","Categoria 8","URLIMAGECAT8"))
    fun findAll():List<Category>{
        return categories
    }

}