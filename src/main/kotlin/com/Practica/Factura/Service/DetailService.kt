package com.Practica.Factura.Service

import com.Practica.Factura.Model.Client
import com.Practica.Factura.Model.Detail
import com.Practica.Factura.Model.Product
import com.Practica.Factura.Repository.ClientRepository
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
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var invoiceRepository: InvoiceRepository
    fun list ():List<Detail>{
        return detailRepository.findAll()
    }

    fun save(detail: Detail): Detail {
        try {
            val product = productRepository.findById(detail.product_id)
                ?: throw Exception("Id del producto no encontrada")

            val invoicetoVP =invoiceRepository.findById(detail.invoice_id)

            invoiceRepository.findById(detail.invoice_id)
                ?: throw Exception("Id invoice no encontrada")

            detailRepository.findById(detail.id)
                ?: throw Exception("Id detail no encontrada")

            val savedDetail = detailRepository.save(detail)
            var sum = 0.0
            val listinvoice = detailRepository.findByInvoiceId(detail.invoice_id)
            listinvoice.map { items ->
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


        /*fun save(detail: Detail): Detail{
                val response= detailRepository.save(detail)
                val product= productRepository.findById(detail.product_id)
            product?.apply {
                stock = stock?.minus(detail.quantity!!)
            }
                productRepository.save(product!!)

            return  detailRepository.save(detail)
            }*/

    /*val response= detailRepo.save (detail)
    Disminuir stock
    val product= productRepo.findId(product_id)
    //Esto es del ejericio anterior
   productRepository.findById(detail.product_id)
                ?: throw Exception ("Id del producto no encontrada")
            return detailRepository.save(detail)
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }
     */
    fun update(detail: Detail): Detail {
        try {
            val existingDetail = detailRepository.findById(detail.id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "ID no existe")

            val originalQuantity = existingDetail.quantity!!
            val updatedQuantity = detail.quantity!!

            val quantityDifference = updatedQuantity - originalQuantity

            val product = productRepository.findById(existingDetail.product_id)
            product?.apply {
                stock = stock?.plus(quantityDifference)
            }
            productRepository.save(product!!)

            existingDetail.quantity = updatedQuantity
            return detailRepository.save(existingDetail)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el detalle", ex)
        }
    }

    fun updateName(detail: Detail): Detail{
        try{
            val response = detailRepository.findById(detail.id)
                ?: throw Exception("ID no existe")
            response.apply {
                quantity=detail.quantity //un atributo del model
            }
            return detailRepository.save(response)
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }
    }

    fun listById (id:Long?):Detail?{
        return detailRepository.findById(id)
    }

    fun delete(id: Long?): Boolean {
        try {
            val detail = detailRepository.findById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "ID no existe")

            val product = productRepository.findById(detail.product_id)
            product?.apply {
                stock = stock?.plus(detail.quantity!!)
            }
            productRepository.save(product!!)

            detailRepository.deleteById(id!!)

            return true // Puedes ajustar el tipo de retorno según tus necesidades
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el detalle", ex)
        }
    }


        }
