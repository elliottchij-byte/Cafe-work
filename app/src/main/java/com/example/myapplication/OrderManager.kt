
package com.example.myapplication

data class Order(
    val items: List<CartItem>,
    val total: Double,
    var status: String = "Preparing"
)

object OrderManager {

    val orders = mutableListOf<Order>()

    fun placeOrder(): Order? {

        if (CartManager.items.isEmpty()) {
            return null
        }

        val orderItems = CartManager.items.map { it.copy() }

        val total = orderItems.sumOf {
            it.price * it.quantity
        }

        val order = Order(orderItems, total)

        orders.add(order)

        CartManager.items.clear()

        return order
    }
}
