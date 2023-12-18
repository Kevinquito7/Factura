package com.Practica.Factura.Service


import com.Practica.Factura.Model.Detail
import com.Practica.Factura.Repository.DetailRepository
import com.Practica.Factura.Repository.InvoiceRepository
import com.Practica.Factura.Repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class DetailService {
    @Autowired
    lateinit var detailRepository: DetailRepository

    @Autowired
    lateinit var invoiceRepository: InvoiceRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    fun list(): List<Detail> {
        return detailRepository.findAll()
    }
    fun listById (id:Long?):Detail?{
        return detailRepository.findById(id)
    }

    fun listByPrice(value: Double): List<Detail> {
        return detailRepository.filterPrice(value)
    }


    fun save(detail: Detail): Detail {
        try {
            val product = productRepository.findById(detail.productId)
                ?: throw Exception("Id del producto no encontrada")

            val invoicetoVP = invoiceRepository.findById(detail.invoiceId)

            invoiceRepository.findById(detail.invoiceId)
                ?: throw Exception("Id invoice no encontrada")

            detailRepository.findById(detail.id)
                ?: throw Exception("Id detail no encontrada")

            val savedDetail = detailRepository.save(detail)
            var sum = 0.0
            val listinvoice = detailRepository.findByInvoiceId(detail.invoiceId)
            listinvoice.map {items ->
                sum += (detail.price!!.times(detail.quantity!!))

            }


            product.apply {
                stock = stock?.minus(detail.quantity!!)
            }
            invoicetoVP?.apply {
                total = sum
            }
            invoiceRepository.save(invoicetoVP!!)

            productRepository.save(product)

            return savedDetail
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, ex.message)
        }

    }

    fun update(detail: Detail): Detail {
        try {
            detailRepository.findById(detail.id)
                ?: throw Exception("ID no existe")

            return detailRepository.save(detail)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, ex.message)
        }
    }

    fun updateName(detail: Detail): Detail {
        try {
            val response = detailRepository.findById(detail.id)
                ?: throw Exception("ID no existe")
            response.apply {
                quantity = detail.quantity //un atributo del modelo
            }
            return detailRepository.save(response)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, ex.message)
        }
    }

    fun delete(id: Long?): Boolean {
        try {
            val detail = detailRepository.findById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "ID no existe")

            val product = productRepository.findById(detail.productId)
            product?.apply {
                stock = stock?.plus(detail.quantity!!)
            }
            productRepository.save(product!!)

            detailRepository.deleteById(id!!)

            return true
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el detalle", ex)
        }


    }
}
