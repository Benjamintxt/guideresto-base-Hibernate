package ch.hearc.ig.guideresto.business;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
@Entity
@Table(name = "TYPES_GASTRONOMIQUES")
public class RestaurantType {
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "SEQ_TYPES_GASTRONOMIQUES")
    @SequenceGenerator(name = "SEQ_TYPES_GASTRONOMIQUES", sequenceName = "SEQ_TYPES_GASTRONOMIQUES", initialValue = 1, allocationSize = 1)
    @Column(name = "NUMERO")
    private Integer id;
    @Column(name = "libelle")
    private String label;
    @Lob
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Restaurant> restaurants;

    public RestaurantType(Integer id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.restaurants = new HashSet<>();
    }

    public RestaurantType() {}

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getLabel() {return label;}

    public void setLabel(String label) {this.label = label;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public Set<Restaurant> getRestaurants() {return restaurants;}

    public void setRestaurants(Set<Restaurant> restaurants) {this.restaurants = restaurants;}
}