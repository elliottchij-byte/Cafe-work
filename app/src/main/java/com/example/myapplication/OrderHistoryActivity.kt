
package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OrderHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_order_history)

        val orderHistoryContainer =
            findViewById<LinearLayout>(R.id.orderHistoryContainer)

        val backToMenuButton =
            findViewById<Button>(R.id.backToMenuButton)

        backToMenuButton.setOnClickListener {
            finish()
        }

        if (OrderManager.orders.isEmpty()) {

            val emptyMessage = TextView(this)
            emptyMessage.text = "You haven't placed any orders yet."
            emptyMessage.textSize = 16f

            orderHistoryContainer.addView(emptyMessage)

        } else {

            OrderManager.orders.forEachIndexed { index, order ->

                val orderText = TextView(this)

                val products = order.items.joinToString("\n") {
                    "${it.name} x${it.quantity}"
                }

                orderText.text =
                    "Order ${index + 1}\n" +
                            "$products\n" +
                            "Total: £${"%.2f".format(order.total)}\n" +
                            "Status: ${order.status}"

                orderText.textSize = 16f
                orderText.setTextColor(
                    getColor(R.color.dark_brown)
                )

                orderText.setPadding(12, 12, 12, 12)

                orderHistoryContainer.addView(orderText)
            }
           }
           }
        }
