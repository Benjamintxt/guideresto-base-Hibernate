package ch.hearc.ig.guideresto.business;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "COMMENTAIRES")
public class CompleteEvaluation extends Evaluation {
  @Lob
  @Column(name = "COMMENTAIRE")
  private String comment;
  @Column(name = "nom_utilisateur")
  private String username;
  @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Grade> grades;

  public CompleteEvaluation(){}

  public CompleteEvaluation(Integer id, LocalDate visitDate, Restaurant restaurant, String comment,
      String username) {
    super(id, visitDate, restaurant);
    this.comment = comment;
    this.username = username;
    this.grades = new HashSet<>();
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {this.comment = comment;}

  public String getUsername() {return username;}

  public void setUsername(String username) {this.username = username;}

  public Set<Grade> getGrades() {return grades;}

  public void setGrades(Set<Grade> grades) {this.grades = grades;}

}