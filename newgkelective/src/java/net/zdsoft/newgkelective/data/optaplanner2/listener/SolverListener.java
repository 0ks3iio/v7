package net.zdsoft.newgkelective.data.optaplanner2.listener;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.newgkelective.data.optaplanner2.dto.Room;

public interface SolverListener {

    /**
     * 开始排
     */
    public abstract void solveStarted();
    /**
     * 取消排
     */
    public abstract void solveCancelled();
    /**
     * 结束排且最优
     */
    public abstract void solveFinished(Map<String, List<Room>>[] bottleArray, Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap);

    /**
     * 结束排效果不好，或出其他错
     */
    public abstract void onError(Exception e);
    
    
}
