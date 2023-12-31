package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.RestaurantType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.Optional;

public class RestaurantTypeService extends TransactionService<RestaurantType, Integer>{
    public RestaurantTypeService(){
        super(RestaurantType.class);
    }

    public Optional<RestaurantType> findRestaurantTypeByType(String label) {
        try (Session session = sessionFactory.openSession()) {
            Query<RestaurantType> query = session.createQuery(
                    "SELECT rt FROM RestaurantType rt WHERE lower(rt.label) = :label",
                    RestaurantType.class);
            query.setParameter("label", label.toLowerCase());
            return Optional.ofNullable(query.uniqueResult());
        }
    }
}
