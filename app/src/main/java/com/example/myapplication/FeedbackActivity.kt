
package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val categorySpinner = findViewById<Spinner>(R.id.feedbackCategory)
        val ratingBar = findViewById<RatingBar>(R.id.feedbackRating)
        val feedbackMessage = findViewById<EditText>(R.id.feedbackMessage)
        val submitButton = findViewById<Button>(R.id.submitFeedbackButton)
        val backButton = findViewById<Button>(R.id.backToMenuButton)

        val categories = arrayOf(
            "Café Service",
            "Latte",
            "Cappuccino",
            "Croissant"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        categorySpinner.adapter = adapter


        submitButton.setOnClickListener {

            val user = FirebaseAuth.getInstance().currentUser
            val category = categorySpinner.selectedItem.toString()
            val rating = ratingBar.rating.toInt()
            val message = feedbackMessage.text.toString().trim()

            if (user == null) {
                Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            } else if (rating == 0) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show()
            } else if (message.isEmpty()) {
                Toast.makeText(this, "Please write your feedback", Toast.LENGTH_SHORT).show()
            } else {

                val feedbackData = hashMapOf(
                    "userId" to user.uid,
                    "category" to category,
                    "rating" to rating,
                    "message" to message,
                    "date" to System.currentTimeMillis()
                )

                submitButton.isEnabled = false

                FirebaseFirestore.getInstance()
                    .collection("feedback")
                    .add(feedbackData)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Feedback submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    }
                    .addOnFailureListener {
                        submitButton.isEnabled = true

                        Toast.makeText(
                            this,
                            "Could not submit feedback. Please try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }


        backButton.setOnClickListener {
            finish()
        }
    }
}
