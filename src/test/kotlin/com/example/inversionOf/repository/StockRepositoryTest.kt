package com.example.inversionOf.repository

import com.example.inversionOf.domain.StockModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.junit.jupiter.Testcontainers

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class StockRepositoryTest : PostgresTestContainer() {

    private val stockRepository = StockRepository(namedParameterJdbcTemplate)

    @BeforeAll
    fun initiate() {
        assertThat(stockRepository).isNotNull.withFailMessage("Instancing StockRepository failed")
    }

    @Test
    fun `should return null given empty sku or that's not exist`() {
        listOf("", " ", "1234567")
            .forEach { assertThat(stockRepository.findBySku(it)).isNull() }
    }

    @Test
    fun `should create new stock given sku, amount and base product no`() {
        assertThat(stockRepository.findBySku("123456")).isNull()
        stockRepository.create(StockModel.Stock("123456", "01", 19))
        val stock = stockRepository.findBySku("123456")
        assertThat(stock).isNotNull
        assertThat(stock!!.amount).isEqualTo(19)
    }

    @Test
    fun `should update existing stock given new amount`() {
        val baseProductNo = "222222"
        stockRepository.create(StockModel.Stock(baseProductNo, baseProductNo, 20))
        val stock = stockRepository.findBySku(baseProductNo)!!
        stockRepository.update(stock.copy(amount = 80))
        assertThat(stockRepository.findBySku(baseProductNo)!!.amount).isEqualTo(80)
    }
}
