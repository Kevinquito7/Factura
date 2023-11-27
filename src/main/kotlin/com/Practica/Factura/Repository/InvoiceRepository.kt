package com.Practica.Factura.Repository

import com.Practica.Factura.Model.Invoice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface InvoiceRepository : JpaRepository<Invoice, Long?> {

    fun findById (id: Long?): Invoice?

    @Query(nativeQuery =true)
    fun filterTotal(value: Double): List <Invoice>

}