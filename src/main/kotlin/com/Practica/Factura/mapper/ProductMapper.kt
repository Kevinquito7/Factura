package com.Practica.Factura.mapper

import com.Practica.Factura.Model.Product
import com.Practica.Factura.dto.ProductDto

object ProductMapper{
    fun mapToDto(product: Product):ProductDto{

        return ProductDto(
            product.id,
            "${product.description} ${product.brand}"
        )

    }
}