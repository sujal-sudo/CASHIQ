package com.example.cashiq.repository

import com.example.cashiq.model.CategoryModel
import com.google.firebase.database.*

class CategoryRepositoryImpl : CategoryRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("categories")

    // Add a new category
    override fun addCategory(category: CategoryModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString() // Generate unique ID
        val newCategory = category.copy(categoryId = id)

        ref.child(id).setValue(newCategory).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Category added successfully")
            } else {
                callback(false, it.exception?.message ?: "Error adding category")
            }
        }
    }

    // Update an existing category
    override fun updateCategory(categoryId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        ref.child(categoryId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Category updated successfully")
            } else {
                callback(false, it.exception?.message ?: "Error updating category")
            }
        }
    }

    // Delete a category
    override fun deleteCategory(categoryId: String, callback: (Boolean, String) -> Unit) {
        ref.child(categoryId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Category deleted successfully")
            } else {
                callback(false, it.exception?.message ?: "Error deleting category")
            }
        }
    }

    // Get a category by ID
    override fun getCategoryById(categoryId: String, callback: (CategoryModel?, Boolean, String) -> Unit) {
        ref.child(categoryId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val category = snapshot.getValue(CategoryModel::class.java)
                    callback(category, true, "Category fetched successfully")
                } else {
                    callback(null, false, "Category not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    // Get all categories
    override fun getAllCategories(callback: (List<CategoryModel>?, Boolean, String) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val categories = mutableListOf<CategoryModel>()
                    for (eachData in snapshot.children) {
                        val category = eachData.getValue(CategoryModel::class.java)
                        if (category != null) {
                            categories.add(category)
                        }
                    }
                    callback(categories, true, "Categories fetched successfully")
                } else {
                    callback(emptyList(), false, "No categories found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
}
