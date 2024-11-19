package vnskilled.edu.ecom.Repository.Categories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Categories.CategoriesDescriptionsEntity;
import vnskilled.edu.ecom.Entity.Categories.CategoriesEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesDescriptionRepository extends JpaRepository<CategoriesDescriptionsEntity, Long> {
	List<CategoriesDescriptionsEntity> findByCategoryId(CategoriesEntity categoriesEntity);
	Optional<CategoriesDescriptionsEntity> findByIdAndLanguage(Long id, String language);
	@Query("select cd from CategoriesDescriptionsEntity cd JOIN CategoriesEntity  c on c.id = cd.categoryId.id WHERE c.id = :id")
	List<CategoriesDescriptionsEntity> findCategoriesDescriptionsEntitiesByCategoryId(@Param("id") Long id, Pageable pageable);
}
