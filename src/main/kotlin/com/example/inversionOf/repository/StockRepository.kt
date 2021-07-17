package com.example.inversionOf.repository

import arrow.core.Try
import com.example.inversionOf.domain.StockModel
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class StockRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    private val log = LoggerFactory.getLogger(this::class.java.name)!!

    fun findBySku(sku: String): StockModel.Stock? {
        if (sku.isBlank()) return null

        return Try {
            jdbcTemplate.queryForObject(
                """SELECT sku, base_product_no, amount FROM stock WHERE sku = :sku""".trimIndent(),
                mapOf("sku" to sku)
            ) { rs, _ ->
                StockModel.Stock(
                    rs.getString("sku"),
                    rs.getString("base_product_no"),
                    rs.getInt("amount")
                )
            }
            }.fold(
            ifFailure = {
                log.warn("No stock found by sku=$sku")
                null
            },
            ifSuccess = { it }
        )
    }

    fun create(stock: StockModel.Stock) {
        if (stock.baseProductNo.isNullOrBlank()) return

        val updatedRows = jdbcTemplate.update("""INSERT INTO stock (sku, base_product_no, amount) 
            VALUES (:sku, :baseProductNo, :amount)""".trimIndent(),
            mapOf(
                "sku" to stock.sku,
                "baseProductNo" to stock.baseProductNo,
                "amount" to stock.amount
                )
            )
        if (updatedRows < 1) {
            throw RuntimeException(
                "DB Error: could not create stock for baseProductNo=${stock.baseProductNo}"
            )
        }
    }

    fun update(stock: StockModel.Stock) {
        if (stock.baseProductNo.isNullOrEmpty()) return

        val params = mapOf(
            "sku" to stock.sku,
            "base_product_no" to stock.baseProductNo,
            "amount" to stock.amount
        )

        val updatedRows = jdbcTemplate.update("""
                UPDATE stock
                SET sku = :sku,
                    base_product_no = :baseProductNo,
                    amount = :amount
                WHERE sku = :sku
            """.trimIndent(),
            params
        )
        if (updatedRows < 1) {
            throw RuntimeException(
                "DB Error: could not update product stock for sku=${stock.sku}"
            )
        }
    }
}
