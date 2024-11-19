package vnskilled.edu.ecom.Service.Categories;

import org.springframework.data.domain.Pageable;
import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDescriptionsDTO;
import vnskilled.edu.ecom.Model.Request.Category.CategoryRequest;
import vnskilled.edu.ecom.Model.Request.Category.UpdateCategoryRequest;
import vnskilled.edu.ecom.Model.Response.Categories.CategoryResponse;

import java.util.List;

public interface CategoryService {
	 List<CategoryResponse> findAll(Pageable pageable);
	 List<CategoriesDescriptionsDTO> getAllDescription(Long id,Pageable pageable);
	 CategoryResponse findByCategoryId(Long id);
	 List<CategoryResponse> getAllCategories();
	 List<CategoryResponse> getCategoriesByName(String name,Pageable pageable);
	 List<CategoryResponse> findAllByParentId(Pageable pageable, Long parentId);
	 void save(CategoryRequest categoryRequest);
	 void update(UpdateCategoryRequest updateCategoryRequest, Long id);
	 void delete(Long id);
	 int totalItem();
	 void updateDescription(CategoriesDescriptionsDTO request, Long id);
	 void CreateDescription(CategoriesDescriptionsDTO request);
	 CategoriesDescriptionsDTO findDescriptionById(Long id);
	 void deleteDescriptionById(Long id);
}
