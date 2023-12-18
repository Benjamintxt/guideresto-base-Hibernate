package ch.hearc.ig.guideresto.business;


import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "LIKES")
public class BasicEvaluation extends Evaluation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "numero")
  private Integer id;
  @Column(name = "appreciation", nullable = false)
  private boolean likeRestaurant;

  @Column(name = "adresse_ip", nullable = false)
  private String ipAddress;

  public BasicEvaluation() {}

  public BasicEvaluation(Integer id, LocalDate visitDate, Restaurant restaurant, boolean likeRestaurant,
      String ipAddress) {
    super(id, visitDate, restaurant);
    this.likeRestaurant = likeRestaurant;
    this.ipAddress = ipAddress;
  }

  public void setLikeRestaurant(boolean likeRestaurant) {
    this.likeRestaurant = likeRestaurant;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
  public Boolean isLikeRestaurant() {
    return likeRestaurant;
  }

}