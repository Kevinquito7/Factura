package com.Practica.Factura.Controller

import com.Practica.Factura.Model.Client
import com.Practica.Factura.Model.Invoice
import com.Practica.Factura.Service.ClientService
import com.Practica.Factura.Service.InvoiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/invoice")   //endpoint
class InvoiceController {
    @Autowired
    lateinit var invoiceService: InvoiceService

    @GetMapping
    fun list ():List <Invoice>{
        return invoiceService.list()
    }

}