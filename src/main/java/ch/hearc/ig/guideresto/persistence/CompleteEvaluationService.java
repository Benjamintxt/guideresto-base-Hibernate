package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.CompleteEvaluation;

public class CompleteEvaluationService extends TransactionService<CompleteEvaluation, Integer>{
    public CompleteEvaluationService(){
        super(CompleteEvaluation.class);
    }
}
