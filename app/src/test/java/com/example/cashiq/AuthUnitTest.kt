package com.example.cashiq

import com.example.cashiq.repository.AuthRepoImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*


class AuthUnitTest {

    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var mockTask: Task<AuthResult>

    private lateinit var authRepo: AuthRepoImpl

    @Captor
    private lateinit var captor: ArgumentCaptor<OnCompleteListener<AuthResult>>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        authRepo = AuthRepoImpl(mockAuth)
    }

    @Test
    fun testLogin_Successful() {
        val email = "test@example.com"
        val password = "password"
        var resultMessage = "Initial Value"

        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(mockTask)

        val callback = { success: Boolean, message: String ->
            resultMessage = message
        }

        authRepo.login(email, password, callback)

        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)

        assertEquals("Login successful.", resultMessage)
    }

    @Test
    fun testLogin_Failure() {
        val email = "wrong@example.com"
        val password = "wrongpassword"
        var resultMessage = "Initial Value"

        `when`(mockTask.isSuccessful).thenReturn(false)
        `when`(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(mockTask)

        val callback = { success: Boolean, message: String ->
            resultMessage = message
        }

        authRepo.login(email, password, callback)

        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)

        assertEquals("Login failed.", resultMessage)
    }

    @Test
    fun testRegister_Successful() {
        val email = "newuser@example.com"
        val password = "newpassword"
        var resultMessage = "Initial Value"

        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(mockTask)

        val callback = { success: Boolean, message: String ->
            resultMessage = message
        }

        authRepo.register(email, password, callback)

        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)

        assertEquals("Registration successful.", resultMessage)
    }

    @Test
    fun testRegister_Failure() {
        val email = "invaliduser@example.com"
        val password = "weakpassword"
        var resultMessage = "Initial Value"

        `when`(mockTask.isSuccessful).thenReturn(false)
        `when`(mockAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(mockTask)

        val callback = { success: Boolean, message: String ->
            resultMessage = message
        }

        authRepo.register(email, password, callback)

        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)

        assertEquals("Registration failed.", resultMessage)
    }
}