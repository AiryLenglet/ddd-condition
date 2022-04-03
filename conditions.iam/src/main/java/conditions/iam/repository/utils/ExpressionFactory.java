package conditions.iam.repository.utils;

import conditions.core.model.Condition;
import conditions.core.model.Pid;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

public class ExpressionFactory {

    private ExpressionFactory() {
    }

    public static Expression<Boolean> isInvolvedInCondition(CriteriaBuilder cb, Root<Condition> root, Pid user) {
        return cb.equal(root.get("owner"), user);
    }
}
