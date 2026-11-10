package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

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
        // this finds the View Shopping Cart button, waits for the customer to press it and then opens CartActivity.
        val viewCartButton = findViewById<Button>(R.id.viewCartButton)

        viewCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
// allows it to save the actual product and quantity in cart manager
        latteButton.setOnClickListener {
            CartManager.addItem("Latte", 3.50)
            cartItems++
            cartTotal += 3.50
            updateCart(cartText)
        }

        cappuccinoButton.setOnClickListener {
            CartManager.addItem("Cappuccino", 3.20)
            cartItems++
            cartTotal += 3.20
            updateCart(cartText)
        }

        croissantButton.setOnClickListener {
            CartManager.addItem("Croissant", 2.50)
            cartItems++
            cartTotal += 2.50
            updateCart(cartText)
        }
    }

    private fun updateCart(cartText: TextView) {
        cartText.text = getString(R.string.cart_summary, cartItems, cartTotal)
    }
}