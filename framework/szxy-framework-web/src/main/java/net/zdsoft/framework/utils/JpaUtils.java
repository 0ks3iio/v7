package net.zdsoft.framework.utils;

import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @date 2019-08-21 09:56
 */
public class JpaUtils {

    /**
     * oracle in参数的最大值
     */
    private static final int ORACLE_MAX_IN = 1000;


    public static <T> Predicate processIn(T[] targets, CriteriaBuilder criteriaBuilder, Expression expression) {
        //Assert.hasElements(targets, "targets can't empty");
        if (ArrayUtils.isEmpty(targets)) {
            throw new IllegalArgumentException("targets must has elements");
        }
        if (targets.length <= ORACLE_MAX_IN) {
            return expression.in(targets);
        }
        int max = targets.length;

        int loop = max / ORACLE_MAX_IN;
        if (max % ORACLE_MAX_IN > 0) {
            ++loop;
        }

        List<Predicate> ps = new ArrayList<>(loop);
        for (int i = 0; i < loop; i++) {
            int start = i * ORACLE_MAX_IN;
            int end = (i + 1) * ORACLE_MAX_IN;
            if (end > max) {
                end = max;
            }

            Object[] sub = new Object[end - start];
            System.arraycopy(targets, start, sub, 0, end - start);
            ps.add(expression.in(sub));
        }
        return criteriaBuilder.or(ps.toArray(new Predicate[0]));
    }
}
