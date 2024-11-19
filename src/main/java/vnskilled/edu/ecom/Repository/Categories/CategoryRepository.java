package vnskilled.edu.ecom.Repository.Categories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Categories.CategoriesEntity;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoriesEntity, Long> {
    List<CategoriesEntity> findByParentId(Long id, Pageable pageable);
    @Query("SELECT c FROM CategoriesEntity c " +
            "LEFT JOIN  CategoriesDescriptionsEntity cd on  cd.categoryId.id = c.id " +
            "where cd.name like %:name%")
    List<CategoriesEntity> findCategoriesByName(@Param("name") String name, Pageable pageable);
}
