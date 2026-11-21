
package com.example.myapplication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ListenerRegistration


data class Order(
    val items: List<CartItem>,
    val total: Double,
    var status: String = "Preparing",
    var id: String = "",
    val orderDate: Long = System.currentTimeMillis()
)

object OrderManager {

    val orders = mutableListOf<Order>()

    private val auth = FirebaseAuth.getInstance()
    private val db
        get() = FirebaseFirestore.getInstance()

    fun placeOrder(onComplete: (Order?) -> Unit) {

        if (CartManager.items.isEmpty()) {
            onComplete(null)
            return
        }

        val user = auth.currentUser

        if (user == null) {
            onComplete(null)
            return
        }

        val orderItems = CartManager.items.map { it.copy() }

        val total = orderItems.sumOf {
            it.price * it.quantity
        }

        val order = Order(orderItems, total)

        val orderData = hashMapOf(
            "userId" to user.uid,
            "total" to order.total,
            "status" to order.status,
            "orderDate" to order.orderDate,
            "items" to order.items.map { item ->
                hashMapOf(
                    "name" to item.name,
                    "price" to item.price,
                    "quantity" to item.quantity
                )
            }
        )

        db.collection("orders")
            .add(orderData)
            .addOnSuccessListener { document ->

                order.id = document.id
                orders.add(order)
                CartManager.items.clear()

                onComplete(order)
            }
            .addOnFailureListener { error ->

                android.util.Log.e(
                    "OrderManager",
                    "Order could not be saved",
                    error
                )

                onComplete(null)
            }
    }

    fun loadOrders(onComplete: (List<Order>) -> Unit) {

        val user = auth.currentUser

        if (user == null) {
            onComplete(emptyList())
            return
        }

        db.collection("orders")
            .whereEqualTo("userId", user.uid)
            .get()
            .addOnSuccessListener { documents ->

                val savedOrders = mutableListOf<Order>()

                for (document in documents) {

                    val total = document.getDouble("total") ?: 0.0
                    val status = document.getString("status") ?: "Preparing"
                    val orderDate = document.getLong("orderDate") ?: 0L

                    val items = mutableListOf<CartItem>()

                    val savedItems = document.get("items") as? List<*>

                    savedItems?.forEach { savedItem ->

                        if (savedItem is Map<*, *>) {

                            val name = savedItem["name"] as? String ?: ""
                            val price = (savedItem["price"] as? Number)?.toDouble() ?: 0.0
                            val quantity = (savedItem["quantity"] as? Number)?.toInt() ?: 1

                            items.add(CartItem(name, price, quantity))
                        }
                    }

                    savedOrders.add(
                        Order(
                            items = items,
                            total = total,
                            status = status,
                            id = document.id,
                            orderDate = orderDate
                        )
                    )
                }

                savedOrders.sortByDescending { it.orderDate }

                onComplete(savedOrders)
            }
            .addOnFailureListener { error ->

                android.util.Log.e(
                    "OrderManager",
                    "Could not load orders",
                    error
                )

                onComplete(emptyList())
            }
    }


    fun listenForOrders(
        onUpdate: (List<Order>) -> Unit,
        onStatusChanged: (String) -> Unit = {}
    ): ListenerRegistration? {

        val user = auth.currentUser ?: return null

        val previousStatuses = mutableMapOf<String, String>()
        var firstUpdate = true


        return db.collection("orders")
            .whereEqualTo("userId", user.uid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    android.util.Log.e(
                        "OrderManager",
                        "Could not listen for orders",
                        error
                    )
                    return@addSnapshotListener
                }

                val savedOrders = mutableListOf<Order>()

                snapshot?.documents?.forEach { document ->

                    val items = mutableListOf<CartItem>()
                    val savedItems = document.get("items") as? List<*>

                    savedItems?.forEach { savedItem ->
                        if (savedItem is Map<*, *>) {
                            val name = savedItem["name"] as? String ?: ""
                            val price = (savedItem["price"] as? Number)?.toDouble() ?: 0.0
                            val quantity = (savedItem["quantity"] as? Number)?.toInt() ?: 1

                            items.add(CartItem(name, price, quantity))
                        }
                    }

                    savedOrders.add(
                        Order(
                            items = items,
                            total = document.getDouble("total") ?: 0.0,
                            status = document.getString("status") ?: "Preparing",
                            id = document.id,
                            orderDate = document.getLong("orderDate") ?: 0L
                        )
                    )
                }



                savedOrders.sortByDescending { it.orderDate }

                if (!firstUpdate) {
                    for (order in savedOrders) {
                        val previousStatus = previousStatuses[order.id]


                        if (previousStatus != null && previousStatus != order.status) {
                            onStatusChanged(order.status)
                        }

                    }
                }

                previousStatuses.clear()

                for (order in savedOrders) {
                    previousStatuses[order.id] = order.status
                }

                firstUpdate = false

                onUpdate(savedOrders)


            }
    }


}
