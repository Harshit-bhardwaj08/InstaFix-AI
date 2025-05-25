package project.hackmol.hackmolinstafix.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import project.hackmol.hackmolinstafix.model.User
import project.hackmol.hackmolinstafix.util.Resource

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    // Check if user is logged in
    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    // Get current user
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // Register new user
    suspend fun registerUser(name: String, email: String, password: String): Resource<FirebaseUser> {
        return withContext(Dispatchers.IO) {
            try {
                // Create authentication account
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    // Create user document in Firestore
                    val user = User(
                        id = firebaseUser.uid,
                        name = name,
                        email = email
                    )

                    usersCollection.document(firebaseUser.uid).set(user).await()
                    Resource.Success(firebaseUser)
                } else {
                    Resource.Error("User registration failed")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Login user
    suspend fun loginUser(email: String, password: String): Resource<FirebaseUser> {
        return withContext(Dispatchers.IO) {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                authResult.user?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Login failed")
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Reset password
    suspend fun resetPassword(email: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to send reset email")
            }
        }
    }

    // Logout user
    fun logout() {
        firebaseAuth.signOut()
    }
}