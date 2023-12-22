package com.Practica.Factura.Service

import com.Practica.Factura.Model.Client
import com.Practica.Factura.Model.Invoice
import com.Practica.Factura.Repository.ClientRepository
import com.Practica.Factura.Repository.InvoiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class InvoiceService {
    @Autowired
    lateinit var clientRepository: ClientRepository
    @Autowired
    lateinit var invoiceRepository: InvoiceRepository

    fun list ():List<Invoice>{
        return invoiceRepository.findAll()
    }

    fun listTotal(value:Double):List<Invoice>{
        return invoiceRepository.filterTotal(value)
    }

    fun save(invoice: Invoice): Invoice {
        try {
            clientRepository.findById(invoice.client_id)
                ?: throw Exception("Id del cliente no encontrados")
            return invoiceRepository.save(invoice)
        }catch (ex : Exception){
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND, ex.message, ex)
        }
    }
    fun update(invoice: Invoice): Invoice {
        try {
            invoiceRepository.findById(invoice.id)
                ?: throw Exception("ID no existe")

            return invoiceRepository.save(invoice)
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }
    }

    fun updateName(invoice: Invoice): Invoice {
        try{
            val response = invoiceRepository.findById(invoice.id)
                ?: throw Exception("ID no existe")
            response.apply {
                code=invoice.code //un atributo del modelo
            }
            return invoiceRepository.save(response)
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }
    }

    fun delete (id: Long?):Boolean?{
        try{
            val response = invoiceRepository.findById(id)
                ?: throw Exception("ID no existe")
            invoiceRepository.deleteById(id!!)
            return true
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }
    }
    fun listById (id:Long?): Invoice?{
        return invoiceRepository.findById(id)
    }

}