package com.example.cashiq.repository

import com.example.cashiq.model.CategoryModel

interface CategoryRepository {

    fun addCategory(category: CategoryModel, callback: (Boolean, String) -> Unit)
    fun updateCategory(categoryId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteCategory(categoryId: String, callback: (Boolean, String) -> Unit)
    fun getCategoryById(categoryId: String, callback: (CategoryModel?, Boolean, String) -> Unit)
    fun getAllCategories(callback: (List<CategoryModel>?, Boolean, String) -> Unit)
}
