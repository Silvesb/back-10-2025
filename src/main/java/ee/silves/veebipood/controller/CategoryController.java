package ee.silves.veebipood.controller;

import ee.silves.veebipood.entity.Category;
import ee.silves.veebipood.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    /* Dependency injection */
    @Autowired
    CategoryRepository categoryRepository;

    // http://localhost:8080/category unless changed
    @GetMapping("categories")
    public ResponseEntity<List<Category>> getAdminCategory(){
        return ResponseEntity.ok().body(categoryRepository.findAll()); // SELECT * FROM category;
    }

    // http://localhost:8080/category?id=1
    @DeleteMapping("categories")
    public ResponseEntity<List<Category>> deleteCategory(@RequestParam Long id){
        categoryRepository.deleteById(id);
        return ResponseEntity.ok().body(categoryRepository.findAll()); // SELECT * FROM category;
    }

    // http://localhost:8080/category
    @PostMapping("categories")
    public ResponseEntity<List<Category>> addCategory(@RequestBody Category category){
        if (category.getId() != null){
            throw new RuntimeException("Cannot add with ID");
        }
        categoryRepository.save(category);
        return ResponseEntity.status(201).body(categoryRepository.findAll()); // SELECT * FROM category;
    }
}
