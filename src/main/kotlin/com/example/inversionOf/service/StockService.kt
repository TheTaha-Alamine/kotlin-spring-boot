package com.example.inversionOf.service

import com.example.inversionOf.domain.StockModel
import com.example.inversionOf.exception.StockNotFoundException
import com.example.inversionOf.repository.StockRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StockService(private val stockRepository: StockRepository) {

    private val log = LoggerFactory.getLogger(this::class.java.name)!!

    fun findStockById(sku: String): StockModel.Stock? {
        return try {
            stockRepository.findBySku(sku)
        } catch (e: Exception) {
            log.error("can't find stock for sku $sku")
            null
        }
    }

    fun update(stock: StockModel.Stock) {
        stockRepository.findBySku(stock.sku)
            ?: throw StockNotFoundException("stock cannot be found for this sku ${stock.sku}")

        stockRepository.update(stock)
    }

    fun listAllStock(): List<String>? {
        return stockRepository.findAll()
    }
}
