
package com.example.myapplication

data class CartItem(
    val name: String,
    val price: Double,
    var quantity: Int = 1
)
// cartmanager keeps the products added to the cart.
object CartManager {
// cartitem stores a product's name, price and quantity.
    val items = mutableListOf<CartItem>()
//add item increases the quantity if the product is already there
    fun addItem(name: String, price: Double) {
        val existingItem = items.find { it.name == name }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            items.add(CartItem(name, price))
        }
    }
    fun increaseQuantity(item: CartItem) {
        item.quantity++
    }

    fun decreaseQuantity(item: CartItem) {
        if (item.quantity > 1) {
            item.quantity--
        } else {
            items.remove(item)
        }
    }

    fun removeItem(item: CartItem) {
        items.remove(item)
    }
}
