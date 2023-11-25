package com.Practica.Factura.Repository

import com.Practica.Factura.Model.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client, Long?> {

    fun findById (id: Long?): Client?

}

