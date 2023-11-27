package com.Practica.Factura.Controller

import com.Practica.Factura.Model.Client
import com.Practica.Factura.Model.Product
import com.Practica.Factura.Service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/product")   //endpoint
class ProductController {
    @Autowired
    lateinit var productService: ProductService

    @GetMapping
    fun list ():List <Product>{
        return productService.list()
    }
    @PostMapping
    fun save (@RequestBody product: Product): ResponseEntity<Product> {
        return ResponseEntity(productService.save(product), HttpStatus.OK)
    }

    @PutMapping
    fun update (@RequestBody product: Product): ResponseEntity<Product> {
        return ResponseEntity(productService.update(product), HttpStatus.OK)
    }

    @PatchMapping
    fun updateName (@RequestBody product: Product): ResponseEntity<Product> {
        return ResponseEntity(productService.updateName(product), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun listById (@PathVariable("id") id: Long): ResponseEntity<*> {
        return ResponseEntity(productService.listById (id), HttpStatus.OK)

    }

    @DeleteMapping("/delete/{id}")
    fun delete (@PathVariable("id") id: Long):Boolean?{
        return productService.delete(id)
    }

}