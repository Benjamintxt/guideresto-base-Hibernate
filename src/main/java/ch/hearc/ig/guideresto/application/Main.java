package ch.hearc.ig.guideresto.application;

import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.persistence.FakeItems;
import ch.hearc.ig.guideresto.presentation.CLI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class Main {

  /*public static void main(String[] args) {
    var scanner = new Scanner(System.in);
    var fakeItems = new FakeItems();
    var printStream = System.out;
    var cli = new CLI(scanner, printStream, fakeItems);

    cli.start();

   */
  public static void main(String[] args) {
    // Create EntityManagerFactory using persistence unit name from persistence.xml
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("GuideRestoPersistenceUnit");

    // Create EntityManager
    EntityManager em = emf.createEntityManager();

    // Begin a transaction
    em.getTransaction().begin();

    try {
      // Create and execute a JPA query to select all cities
      TypedQuery<City> query = em.createQuery("SELECT c FROM City c", City.class);
      List<City> cities = query.getResultList();

      // Print the results to the console
      for (City city : cities) {
        System.out.println("City ID: " + city.getId());
        System.out.println("Zip Code: " + city.getZipCode());
        System.out.println("City Name: " + city.getCityName());
        System.out.println("-------------");
      }

      // Commit the transaction
      em.getTransaction().commit();
    } finally {
      // Close EntityManager and EntityManagerFactory
      em.close();
      emf.close();
    }
  }
}

