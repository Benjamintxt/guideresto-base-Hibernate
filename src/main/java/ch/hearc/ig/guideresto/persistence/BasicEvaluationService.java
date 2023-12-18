package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import org.hibernate.Session;

public class BasicEvaluationService extends TransactionService<BasicEvaluation, Integer>{

    public BasicEvaluationService () {
        super(BasicEvaluation.class);
    }
}
