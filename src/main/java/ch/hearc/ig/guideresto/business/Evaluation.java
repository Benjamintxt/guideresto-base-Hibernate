package ch.hearc.ig.guideresto.business;

import javax.persistence.*;
import java.time.LocalDate;
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
public abstract class Evaluation {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EVAL")
  @SequenceGenerator(name = "SEQ_EVAL", sequenceName = "SEQ_EVAL", allocationSize = 1)
  @Column(name = "NUMERO")
  private Integer id;
  @Column(name = "DATE_EVAL")
  private LocalDate visitDate;
  @ManyToOne
  @JoinColumn(name = "fk_rest")
  private Restaurant restaurant;
  public Evaluation(){}
  public Evaluation(Integer id, LocalDate visitDate, Restaurant restaurant) {
    this.id = id;
    this.visitDate = visitDate;
    this.restaurant = restaurant;
  }
  public Integer getId() {return id;}


  public LocalDate getVisitDate() {return visitDate;}

  public Restaurant getRestaurant() {return restaurant;}

  public void setVisitDate(LocalDate visitDate) {this.visitDate = visitDate;}

  public void setRestaurant(Restaurant restaurant) {this.restaurant = restaurant;}
}