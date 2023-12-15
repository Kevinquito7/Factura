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

    fun save(detail: Detail):Detail{
        try {
            // Verification logic for invoice and product existence
            detail.invoice_id?.let { invoiceId ->
                if (!invoiceRepository.existsById(invoiceId)) {
                    throw ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found for id: $invoiceId")
                }
            }

            detail.product_id?.let { productId ->
                if (!productRepository.existsById(productId)) {
                    throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for id: $productId")
                }
            }
            val response = detailRepository.save(detail)
            //logica disminuir detail
            val product = productRepository.findById(detail.product_id)
            product?.apply {
                stock = stock?.minus(detail.quantity!!)
            }
            productRepository.save(product!!)
            return response

            // Save the detail
            return detailRepository.save(detail)
        } catch (ex: Exception) {
            // Handle exceptions by wrapping them in a ResponseStatusException
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request", ex)


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
    