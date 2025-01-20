package com.example.cashiq.repository

interface Category {

    fun addCategory(categoryModel: Category )

    fun updateCategory()

    fun deleteCategory()

    fun getCategoryById()

    fun getAllProducts()
}