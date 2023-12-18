package com.Practica.Factura.Repository

import com.Practica.Factura.Model.Detail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DetailRepository : JpaRepository<Detail, Long?> {

    fun findById (id: Long?): Detail?
    @Query(nativeQuery = true)
    fun filterPrice(value:Double):List<Detail>
    fun findByInvoiceId(invoiceId: Long?): List<Detail>
}