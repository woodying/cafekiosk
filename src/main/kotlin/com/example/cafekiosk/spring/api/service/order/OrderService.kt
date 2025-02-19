package com.example.cafekiosk.spring.api.service.order

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse
import com.example.cafekiosk.spring.domain.order.Order
import com.example.cafekiosk.spring.domain.order.OrderRepository
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import com.example.cafekiosk.spring.domain.stock.StockRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val stockRepository: StockRepository
) {
    @Transactional
    fun createOrder(request: OrderCreateRequest, registeredDateTime: LocalDateTime): OrderResponse {
        val products = findProductsBy(request.productNumbers)

        deductStockQuantities(products)

        val order = Order.create(
            products = products,
            registeredDateTime = registeredDateTime
        )
        val savedOrder = orderRepository.save(order)

        return OrderResponse.of(savedOrder)
    }

    private fun deductStockQuantities(products: List<Product>) {
        val stockProductNumbers = products
            .filter { it.type in Product.Type.STOCK_TYPES }
            .map { it.productNumber }

        val stockMap = stockRepository
            .findByProductNumberIn(stockProductNumbers)
            .associateBy { it.productNumber }
        val productCountMap = stockProductNumbers.groupingBy { it }.eachCount()

        for (stockProduct in productCountMap.keys) {
            val stock = stockMap[stockProduct] ?: continue
            val quantity = productCountMap[stock.productNumber] ?: 0

            if (stock.isQuantityLessThan(quantity)) {
                throw IllegalArgumentException("재고가 부족한 상품이 있습니다.")
            }

            stockRepository.incrementQuantityById(stock.id!!, -quantity)
        }
    }

    private fun findProductsBy(productNumbers: List<String>): List<Product> {
        val productMap = productRepository
            .findByProductNumberIn(productNumbers)
            .associateBy { it.productNumber }

        return productNumbers
            .map { productId -> productMap[productId]!! }
    }
}
