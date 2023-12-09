package ch.hearc.ig.guideresto.business;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "VILLES")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "your_sequence_generator")
    @SequenceGenerator(name = "your_sequence_generator", sequenceName = "your_sequence_name", allocationSize = 1)
    @Column(name = "numero")
    private Integer id;
    @Column(name = "code_postal", nullable = false)
    private String zipCode;
    @Column(name = "nom_ville", nullable = false)
    private String cityName;
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Restaurant> restaurants;

    public City() {
    }
    public City(Integer id, String zipCode, String cityName) {
        this.id = id;
        this.zipCode = zipCode;
        this.cityName = cityName;
        this.restaurants = new HashSet<>();
    }
    
    public String getZipCode() {
        return zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }
}