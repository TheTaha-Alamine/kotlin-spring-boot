package com.example.inversionOf.domain

object StockModel {
    data class Stock(
        val sku: String,
        val baseProductNo: String? = null,
        val amount: Int = 0
    )
}
