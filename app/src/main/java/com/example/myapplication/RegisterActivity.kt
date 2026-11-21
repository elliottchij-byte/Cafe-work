
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput =
            findViewById<EditText>(R.id.confirmPasswordInput)

        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        registerButton.setOnClickListener {

            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (name.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {

                Toast.makeText(this, "Please fill in all fields",
                    Toast.LENGTH_SHORT).show()

            } else if (password != confirmPassword) {

                Toast.makeText(this, "Passwords do not match",
                    Toast.LENGTH_SHORT).show()

            } else if (password.length < 6) {

                Toast.makeText(this, "Password must be at least 6 characters",
                    Toast.LENGTH_SHORT).show()

            } else {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()

                            auth.currentUser?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener {

                                    Toast.makeText(this,
                                        "Account created successfully!",
                                        Toast.LENGTH_SHORT).show()

                                    val intent = Intent(
                                        this,
                                        MainActivity::class.java
                                    )

                                    startActivity(intent)
                                    finish()
                                }

                        } else {

                            Toast.makeText(this,
                                task.exception?.message ?: "Registration failed",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        loginLink.setOnClickListener {
            // We will connect this to LoginActivity next
        }
    }
}
