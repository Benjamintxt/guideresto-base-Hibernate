package ch.hearc.ig.guideresto.application;

import ch.hearc.ig.guideresto.persistence.*;
import ch.hearc.ig.guideresto.presentation.CLI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    var scanner = new Scanner(System.in);
    var printStream = System.out;
    var restaurantService = new RestaurantService();
    var cityService = new CityService();
    var restaurantTypeService = new RestaurantTypeService();
    var basicEvaluationService = new BasicEvaluationService();
    var completeEvaluationService = new CompleteEvaluationService();
    var evaluationCriteriaService = new EvaluationCriteriaService();
    var gradeService = new GradeService();
    var cli = new CLI(scanner, printStream, restaurantService, cityService, restaurantTypeService, basicEvaluationService, completeEvaluationService, evaluationCriteriaService, gradeService);

    EntityManagerFactory emf = null;
    EntityManager em = null;

    try {
      emf = Persistence.createEntityManagerFactory("GuideRestoPersistenceUnit");
      em = emf.createEntityManager();

      // Begin a transaction
      em.getTransaction().begin();

      cli.start();
    } finally {
      // Commit the transaction if it's still active
      if (em != null && em.getTransaction().isActive()) {
        em.getTransaction().commit();
      }

      // Close EntityManager and EntityManagerFactory
      if (em != null) {
        em.close();
      }
      if (emf != null) {
        emf.close();
      }
    }
  }
}