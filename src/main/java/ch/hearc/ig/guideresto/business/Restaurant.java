package ch.hearc.ig.guideresto.business;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "RESTAURANTS")
public class Restaurant {
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "SEQ_RESTAURANTS")
    @SequenceGenerator(name = "SEQ_RESTAURANTS", sequenceName = "SEQ_RESTAURANTS", initialValue = 1, allocationSize = 1)
    @Column(name = "NUMERO")
    private Integer id;
    @Column(name = "NOM")
    private String name;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SITE_WEB")
    private String website;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<BasicEvaluation> basicEvaluations = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CompleteEvaluation> completeEvaluations = new HashSet<>();
    @Embedded
    private Localisation address;
    @ManyToOne
    @JoinColumn(name = "fk_type")
    private RestaurantType type;
    public Restaurant() {}
    public Restaurant(Integer id, String name, String description, String website, Localisation address, RestaurantType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.address = address;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getZipCode() {
        return address.getCity().getZipCode();
    }

    public String getStreet() {
        return address.getStreet();
    }

    public String getCityName() {
        return address.getCity().getCityName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<Evaluation> getEvaluations() {
        Set<Evaluation> evaluations = new HashSet<>();
        evaluations.addAll(basicEvaluations);
        evaluations.addAll(completeEvaluations);
        return evaluations;
    }

    public Localisation getAddress() {
        return address;
    }

    public RestaurantType getType() {
        return type;
    }

    public void setType(RestaurantType type) {
        this.type = type;
    }
}