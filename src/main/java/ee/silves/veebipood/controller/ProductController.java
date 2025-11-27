package ee.silves.veebipood.controller;

import ee.silves.veebipood.entity.Product;
import ee.silves.veebipood.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    /* Dependency injection */
    @Autowired
    ProductRepository productRepository;

    // http://localhost:8080/products unless changed
    @GetMapping("admin-products")
    public List<Product> getAdminProducts(){
        return productRepository.findAllByOrderByIdAsc(); // SELECT * FROM products;
    }

    // http://localhost:8080/public-products?page=0&size=2
    @GetMapping("public-products")
    public Page<Product> getPublicProducts(Pageable pageable){
        return productRepository.findByActiveTrue(pageable); // SELECT * FROM products;
    }

    // http://localhost:8080/category-products?categoryId=1 unless changed
    @GetMapping("category-products")
    public List<Product> getCategoryProducts(@RequestParam Long categoryId){
        return productRepository.findByCategoryId(categoryId); // SELECT * FROM products;
    }

    // http://localhost:8080/products?id=1
    @DeleteMapping("products")
    public List<Product> deleteProduct(@RequestParam Long id){
        productRepository.deleteById(id);
        return productRepository.findAllByOrderByIdAsc(); // SELECT * FROM products;
    }

    // http://localhost:8080/products
    @PostMapping("products")
    public List<Product> addProduct(@RequestBody Product product){
        if (product.getId() != null){
            throw new RuntimeException("Cannot add with ID");
        }
        if (product.getPrice() <= 0){
            throw new RuntimeException("Cannot add with negative price");
        }
        productRepository.save(product);
        return productRepository.findAllByOrderByIdAsc(); // SELECT * FROM products;
    }

    // http://localhost:8080/products/1 unless changed
    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable Long id){
        return productRepository.findById(id).orElseThrow();
    }

    // http://localhost:8080/products
    @PutMapping("products")
    public List<Product> editProduct(@RequestBody Product product){
        if (product.getId() == null || product.getId() <= 0){
            throw new RuntimeException("Cannot edit without ID");
        }
        if (productRepository.findById(product.getId()).isEmpty()){
            throw new RuntimeException("No product with this ID - " + product.getId());
        }
        productRepository.save(product);
        return productRepository.findAllByOrderByIdAsc(); // SELECT * FROM products;
    }
}
