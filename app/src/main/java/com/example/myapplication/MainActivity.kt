
package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val latteButton = findViewById<Button>(R.id.latteButton)
        val cappuccinoButton = findViewById<Button>(R.id.cappuccinoButton)
        val croissantButton = findViewById<Button>(R.id.croissantButton)
        val cartText = findViewById<TextView>(R.id.cartText)
        // Finds the search bar and the three product cards
        val searchBar = findViewById<EditText>(R.id.searchBar)

        val latteCard = findViewById<LinearLayout>(R.id.latteCard)
        val cappuccinoCard = findViewById<LinearLayout>(R.id.cappuccinoCard)
        val croissantCard = findViewById<LinearLayout>(R.id.croissantCard)

// Detects whenever the customer types something
//Checks whether the search matches a product
//Shows matching products
//Hides products that don't match
        searchBar.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {

                val searchText = s.toString().trim()

                latteCard.visibility =
                    if ("latte".contains(searchText, ignoreCase = true))
                        View.VISIBLE else View.GONE

                cappuccinoCard.visibility =
                    if ("cappuccino".contains(searchText, ignoreCase = true))
                        View.VISIBLE else View.GONE

                croissantCard.visibility =
                    if ("croissant".contains(searchText, ignoreCase = true))
                        View.VISIBLE else View.GONE
            }
        })


        // Opens the shopping cart
        val viewCartButton = findViewById<Button>(R.id.viewCartButton)

        viewCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        // Opens the Order History screen
        val orderHistoryButton = findViewById<Button>(R.id.orderHistoryButton)

        orderHistoryButton.setOnClickListener {
            val intent = Intent(this, OrderHistoryActivity::class.java)
            startActivity(intent)
        }

        // Adds a Latte to the cart
        latteButton.setOnClickListener {
            CartManager.addItem("Latte", 3.50)
            updateCart(cartText)
        }

        // Adds a Cappuccino to the cart
        cappuccinoButton.setOnClickListener {
            CartManager.addItem("Cappuccino", 3.20)
            updateCart(cartText)
        }

        // Adds a Croissant to the cart
        croissantButton.setOnClickListener {
            CartManager.addItem("Croissant", 2.50)
            updateCart(cartText)
        }
    }

    // Refreshes the cart when returning to the main menu
    override fun onResume() {
        super.onResume()

        val cartText = findViewById<TextView>(R.id.cartText)
        updateCart(cartText)
    }

    // Gets the latest cart count and total
    private fun updateCart(cartText: TextView) {

        val cartItems = CartManager.items.sumOf { it.quantity }

        val cartTotal = CartManager.items.sumOf {
            it.price * it.quantity
        }

        cartText.text = getString(
            R.string.cart_summary,
            cartItems,
            cartTotal
        )
    }
}
