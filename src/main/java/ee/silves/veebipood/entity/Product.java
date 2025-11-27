package ee.silves.veebipood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private String image;
    private boolean active;
    private int stock;

    // Parem pool tähistab kas List või üksik
    // @OneToOne
    // @ManyToOne
    // @OneToMany
    // @ManyToMany
    @ManyToOne
    private Category category;
}
