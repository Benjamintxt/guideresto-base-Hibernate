package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.EvaluationCriteria;

public class EvaluationCriteriaService extends TransactionService<EvaluationCriteria, Integer>{
    public EvaluationCriteriaService(){
        super(EvaluationCriteria.class);
    }
}
