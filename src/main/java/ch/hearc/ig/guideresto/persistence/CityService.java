package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.City;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

public class CityService extends TransactionService<City, Integer> {

    public CityService() {
        super(City.class);
    }

    // Method to find a city by its zip code
    public Optional<City> findCityByZipCode(String zipCode) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery(
                    "SELECT c FROM City c WHERE c.zipCode = :zipCode", City.class);
            query.setParameter("zipCode", zipCode);
            return Optional.ofNullable(query.uniqueResult());
        }
    }

}
