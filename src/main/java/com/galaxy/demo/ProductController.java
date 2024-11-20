package com.galaxy.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        int id = productService.addProduct(product);
        return id > 0
                ? ResponseEntity.ok("Product created with ID: " + id)
                : ResponseEntity.badRequest().body("Failed to create product.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
      boolean isUpdated = productService.updateProduct(id, updatedProduct);
      return isUpdated
              ? ResponseEntity.ok("Product updated with ID: " + id)
              : ResponseEntity.badRequest().body("Failed to update product");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        boolean isDeleted = productService.deleteProduct(id);
        return isDeleted
                ? ResponseEntity.ok("Product deleted with ID: " + id)
                : ResponseEntity.badRequest().body("Failed to delete product");
    }
}
