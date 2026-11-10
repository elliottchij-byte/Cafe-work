
package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cart)

        val cartItemsText = findViewById<TextView>(R.id.cartItemsText)
        val cartTotalText = findViewById<TextView>(R.id.cartTotalText)

        if (CartManager.items.isEmpty()) {
            cartItemsText.text = getString(R.string.empty_cart)
        } else {
            cartItemsText.text = CartManager.items.joinToString("\n\n") { item ->
                getString(
                    R.string.cart_item_line,
                    item.name,
                    item.quantity,
                    item.price * item.quantity
                )
            }
        }

        val total = CartManager.items.sumOf {
            it.price * it.quantity
        }

        cartTotalText.text = getString(R.string.cart_total_amount, total)
    }
}
