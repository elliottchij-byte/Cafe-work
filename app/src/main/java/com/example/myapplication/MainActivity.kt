package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var cartItems = 0
    private var cartTotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val latteButton = findViewById<Button>(R.id.latteButton)
        val cappuccinoButton = findViewById<Button>(R.id.cappuccinoButton)
        val croissantButton = findViewById<Button>(R.id.croissantButton)
        val cartText = findViewById<TextView>(R.id.cartText)

        latteButton.setOnClickListener {
            cartItems++
            cartTotal += 3.50
            updateCart(cartText)
            Toast.makeText(this, "Latte added to cart!", Toast.LENGTH_SHORT).show()
        }

        cappuccinoButton.setOnClickListener {
            cartItems++
            cartTotal += 3.20
            updateCart(cartText)
            Toast.makeText(this, "Cappuccino added to cart!", Toast.LENGTH_SHORT).show()
        }

        croissantButton.setOnClickListener {
            cartItems++
            cartTotal += 2.50
            updateCart(cartText)
            Toast.makeText(this, "Croissant added to cart!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateCart(cartText: TextView) {
        cartText.text = "Cart: $cartItems items | Total: £${"%.2f".format(cartTotal)}"
    }
}