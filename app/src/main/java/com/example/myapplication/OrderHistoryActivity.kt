
package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ListenerRegistration
import android.widget.Toast

class OrderHistoryActivity : AppCompatActivity() {

    private var orderListener: ListenerRegistration? = null

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

        val loadingText = TextView(this)
        loadingText.text = "Loading your orders..."
        loadingText.textSize = 16f
        orderHistoryContainer.addView(loadingText)

        orderListener = OrderManager.listenForOrders(
            onUpdate = { orders ->

                orderHistoryContainer.removeAllViews()

                if (orders.isEmpty()) {

                    val emptyMessage = TextView(this)
                    emptyMessage.text = "No orders found."
                    emptyMessage.textSize = 16f

                    orderHistoryContainer.addView(emptyMessage)

                } else {

                    orders.forEachIndexed { index, order ->

                        val orderText = TextView(this)

                        val products = order.items.joinToString("\n") {
                            "${it.name} x${it.quantity}"
                        }

                        val date = java.text.SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date(order.orderDate))

                        orderText.text =
                            "Order ${index + 1}\n" +
                                    "Date: $date\n" +
                                    "$products\n" +
                                    "Total: £${"%.2f".format(order.total)}\n" +
                                    "Status: ${order.status}"

                        orderText.textSize = 16f
                        orderText.setTextColor(getColor(R.color.dark_brown))
                        orderText.setPadding(12, 20, 12, 20)

                        orderHistoryContainer.addView(orderText)
                    }
                }
            },


            onStatusChanged = { newStatus ->



                NotificationHelper.showNotification(this, newStatus)
            }

        )
    }

    override fun onDestroy() {
        super.onDestroy()
        orderListener?.remove()
    }
}
