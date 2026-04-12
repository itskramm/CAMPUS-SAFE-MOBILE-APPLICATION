package com.example.campussafeapplication.repository

import com.example.campussafeapplication.models.User
import com.example.campussafeapplication.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class AuthRepository {
    
    private val client = SupabaseClient.client
    
    /**
     * Sign up a new user with email and password
     */
    suspend fun signUp(email: String, password: String, fullName: String): Result<User> {
        return try {
            // Create auth user
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            
            // Get the created user
            val authUser = client.auth.currentUserOrNull()
            
            if (authUser != null) {
                // Create user profile in users table
                val user = User(
                    id = authUser.id,
                    email = email,
                    fullName = fullName
                )
                
                client.from("users").insert(user)
                
                Result.success(user)
            } else {
                Result.failure(Exception("Failed to create user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Sign in with email and password
     */
    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            
            val authUser = client.auth.currentUserOrNull()
            
            if (authUser != null) {
                // Fetch user profile from users table
                val users = client.from("users")
                    .select {
                        filter {
                            eq("id", authUser.id)
                        }
                    }
                    .decodeList<User>()
                
                if (users.isNotEmpty()) {
                    Result.success(users.first())
                } else {
                    Result.failure(Exception("User profile not found"))
                }
            } else {
                Result.failure(Exception("Authentication failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Sign out current user
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            client.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get current authenticated user
     */
    suspend fun getCurrentUser(): User? {
        return try {
            val authUser = client.auth.currentUserOrNull() ?: return null
            
            val users = client.from("users")
                .select {
                    filter {
                        eq("id", authUser.id)
                    }
                }
                .decodeList<User>()
            
            users.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return client.auth.currentUserOrNull() != null
    }
    
    /**
     * Reset password
     */
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            client.auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
