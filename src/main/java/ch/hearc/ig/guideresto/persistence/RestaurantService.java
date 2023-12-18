package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.business.RestaurantType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class RestaurantService extends TransactionService<Restaurant, Integer>{
    public RestaurantService(){
        super(Restaurant.class);
    }
    public List<Restaurant> findRestaurantByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Restaurant> query = session.createQuery(
                    "SELECT r FROM Restaurant r WHERE lower(r.name) LIKE :name", Restaurant.class);
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            return query.list();
        }
    }

    public List<Restaurant> findRestaurantsByCityName(String partialCityName) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "SELECT r FROM Restaurant r WHERE lower(r.address.city.cityName) LIKE :partialCityName";
            Query<Restaurant> query = session.createQuery(queryStr, Restaurant.class);
            query.setParameter("partialCityName", "%" + partialCityName.toLowerCase() + "%");
            return query.list();
        }
    }

    public List<Restaurant> findRestaurantByType(RestaurantType type) {
        try (Session session = sessionFactory.openSession()) {
            Query<Restaurant> query = session.createQuery(
                    "SELECT r FROM Restaurant r WHERE r.type = :type", Restaurant.class);
            query.setParameter("type", type);
            return query.list();
        }
    }
}
