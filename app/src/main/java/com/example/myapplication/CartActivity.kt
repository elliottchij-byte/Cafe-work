
package com.example.myapplication

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cart)

        showCart()

        val placeOrderButton = findViewById<Button>(R.id.placeOrderButton)

        placeOrderButton.setOnClickListener {

            placeOrderButton.isEnabled = false

            OrderManager.placeOrder { order ->

                placeOrderButton.isEnabled = true

                if (order == null) {
                    Toast.makeText(
                        this,
                        "Order could not be placed. Check your cart, login and internet connection.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Order Confirmed!")
                        .setMessage(
                            "Your order has been placed successfully.\nTotal: £${"%.2f".format(order.total)}"
                        )
                        .setPositiveButton("OK", null)
                        .show()

                    showCart()
                }
            }
        }

        val backToMenuButton = findViewById<Button>(R.id.backToMenuButton)

        backToMenuButton.setOnClickListener {
            finish()
        }
    }

    private fun showCart() {

        val container = findViewById<LinearLayout>(R.id.cartItemsContainer)
        val totalText = findViewById<TextView>(R.id.cartTotalText)

        container.removeAllViews()

        if (CartManager.items.isEmpty()) {
            val emptyText = TextView(this)
            emptyText.text = getString(R.string.empty_cart)
            container.addView(emptyText)
        }

        for (item in CartManager.items.toList()) {

            val itemLayout = LinearLayout(this)
            itemLayout.orientation = LinearLayout.VERTICAL
            itemLayout.setPadding(0, 15, 0, 15)

            val nameText = TextView(this)
            nameText.text = getString(
                R.string.cart_item_name_price,
                item.name,
                item.price
            )
            nameText.textSize = 18f

            val quantityText = TextView(this)
            quantityText.text = getString(
                R.string.quantity_text,
                item.quantity
            )

            val buttonLayout = LinearLayout(this)
            buttonLayout.orientation = LinearLayout.HORIZONTAL
            buttonLayout.gravity = Gravity.CENTER_VERTICAL

            val minusButton = Button(this)
            minusButton.text = getString(R.string.minus)

            val plusButton = Button(this)
            plusButton.text = getString(R.string.plus)

            val removeButton = Button(this)
            removeButton.text = getString(R.string.remove)

            minusButton.setOnClickListener {
                CartManager.decreaseQuantity(item)
                showCart()
            }

            plusButton.setOnClickListener {
                CartManager.increaseQuantity(item)
                showCart()
            }

            removeButton.setOnClickListener {
                CartManager.removeItem(item)
                showCart()
            }

            buttonLayout.addView(minusButton)
            buttonLayout.addView(plusButton)
            buttonLayout.addView(removeButton)

            itemLayout.addView(nameText)
            itemLayout.addView(quantityText)
            itemLayout.addView(buttonLayout)

            container.addView(itemLayout)
        }

        val total = CartManager.items.sumOf {
            it.price * it.quantity
        }

        totalText.text = getString(R.string.cart_total_amount, total)
    }
}
