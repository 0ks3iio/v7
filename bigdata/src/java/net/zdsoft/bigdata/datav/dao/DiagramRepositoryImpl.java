package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.dto.SimpleDiagram;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramContrast;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.model.QDiagram;
import net.zdsoft.bigdata.datav.model.QDiagramContrast;
import net.zdsoft.bigdata.datav.model.QDiagramParameter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

/**
 * @author shenke
 * @since 2019/5/20 下午5:43
 */
public class DiagramRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<SimpleDiagram> getSimpleDiagramByScreenId(String screenId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<SimpleDiagram> query = criteriaBuilder.createQuery(SimpleDiagram.class);
        Root<Diagram> root = query.from(Diagram.class);

        //自查询 name
        Subquery<String> contrastSubquery = query.subquery(String.class);
        Root<DiagramContrast> contrastRoot = contrastSubquery.from(DiagramContrast.class);
        contrastSubquery.select(contrastRoot.get(QDiagramContrast.name));
        contrastSubquery.where(criteriaBuilder.equal(contrastRoot.get(QDiagramContrast.type), root.get(QDiagram.diagramType)));


        Join<Diagram, DiagramParameter> join = root.join(QDiagram.parameters, JoinType.LEFT);

        query.multiselect(contrastSubquery.getSelection(), root.get(QDiagram.id),
                join.get(QDiagramParameter.value).as(Integer.class), /*root.get(QDiagram.layerGroupId),*/
                root.get(QDiagram.diagramType));

        query.where(
                criteriaBuilder.equal(root.get(QDiagram.screenId), screenId),
                criteriaBuilder.equal(join.get(QDiagramParameter.key), "level")
        ).orderBy(criteriaBuilder.desc(join.get(QDiagramParameter.key)));

        return entityManager.createQuery(query).getResultList();
    }
}
