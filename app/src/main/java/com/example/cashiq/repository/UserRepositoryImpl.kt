package com.example.cashiq.repository

import com.example.cashiq.model.UserModel
import com.google.firebase.database.*

class UserRepositoryImpl : UserRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("users")

    override fun addUser(user: UserModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        val userWithId = user.copy(id = id)
        ref.child(id).setValue(userWithId).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User added successfully.")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateUser(
        userId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User updated successfully.")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun deleteUser(userId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User deleted successfully.")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getUserById(
        userId: String,
        callback: (UserModel?, Boolean, String) -> Unit
    ) {
        ref.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserModel::class.java)
                    callback(user, true, "User fetched successfully.")
                } else {
                    callback(null, false, "User not found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun getAllUsers(callback: (List<UserModel>?, Boolean, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val users = mutableListOf<UserModel>()
                    for (eachData in snapshot.children) {
                        val user = eachData.getValue(UserModel::class.java)
                        user?.let { users.add(it) }
                    }
                    callback(users, true, "Users fetched successfully.")
                } else {
                    callback(null, false, "No users found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
}
