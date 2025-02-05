package com.example.cashiq.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.CategoryModel
import com.example.cashiq.repository.CategoryRepository

class CategoryViewModel(private val repo: CategoryRepository) : ViewModel() {

    // LiveData for a single category
    private var _category = MutableLiveData<CategoryModel?>()
    val category: MutableLiveData<CategoryModel?> get() = _category

    // LiveData for all categories
    private var _allCategories = MutableLiveData<List<CategoryModel>>()
    val allCategories: MutableLiveData<List<CategoryModel>> get() = _allCategories

    // LiveData for loading state
    private var _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    // Add a new category
    fun addCategory(categoryModel: CategoryModel, callback: (Boolean, String) -> Unit) {
        repo.addCategory(categoryModel, callback)
    }

    // Update a category
    fun updateCategory(categoryId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.updateCategory(categoryId, data, callback)
    }

    // Delete a category
    fun deleteCategory(categoryId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteCategory(categoryId, callback)
    }

    // Fetch a single category
    fun getCategoryById(categoryId: String) {
        repo.getCategoryById(categoryId) { categoryModel, success, message ->
            if (success) {
                _category.value = categoryModel
            }
        }
    }

    // Fetch all categories
    fun getAllCategories() {
        _loading.value = true
        repo.getAllCategories { categories, success, message ->
            if (success) {
                _allCategories.value = categories ?: emptyList()
            } else {
                _allCategories.value = emptyList()
            }
            _loading.value = false
        }
    }
}
