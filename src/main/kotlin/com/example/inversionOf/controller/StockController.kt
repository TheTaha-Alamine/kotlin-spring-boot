package com.example.inversionOf.controller

import com.example.inversionOf.domain.StockModel
import com.example.inversionOf.service.StockService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock")
class StockController(private val stockService: StockService) {

    private val log = LoggerFactory.getLogger(this::class.java.name)!!

    @GetMapping("/{sku}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getStockBySku(@PathVariable("sku") sku: String): ResponseEntity<StockModel.Stock?> {

        log.info("request to find stock for sku $sku")

        val stock = stockService.findStockById(sku)
        return ResponseEntity(stock, HttpStatus.OK)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<String>?> {

        log.info("getting all stocks")

        val skus = stockService.listAllStock()
        return ResponseEntity(skus, HttpStatus.OK)
    }
}
