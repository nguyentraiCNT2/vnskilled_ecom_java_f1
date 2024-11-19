package vnskilled.edu.ecom.Controller.Categories;

import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDescriptionsDTO;
import vnskilled.edu.ecom.Model.Request.Category.CategoryByParentRequest;
import vnskilled.edu.ecom.Model.Request.Category.CategoryRequest;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Model.Request.Category.UpdateCategoryRequest;
import vnskilled.edu.ecom.Model.Response.Categories.CategoryOutPut;
import vnskilled.edu.ecom.Service.Categories.CategoryService;
import vnskilled.edu.ecom.Util.EndpointConstant.CategoryApiPaths;

import java.util.Map;

@RestController
@RequestMapping(CategoryApiPaths.BASE_PATHS)
public class CategoryController {
	private final CategoryService categoryService;

	@Autowired
	public CategoryController (CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	@GetMapping
	public ResponseEntity<?> getAllCategories(
			@RequestParam("page") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			Model model) {
		try {
			CategoryOutPut result = new CategoryOutPut();

			result.setPage(page);
			Pageable pageable = PageRequest.of(page - 1, limit);
			result.setListResult(categoryService.findAll(pageable));
			result.setTotalPage((int) Math.ceil((double) categoryService.totalItem() / limit));

			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					Map.of("message", e.getMessage(),
							"status", HttpStatus.INTERNAL_SERVER_ERROR.value())
			);
		}
	}

	@PostMapping(CategoryApiPaths.CATEGORIES_BY_PARENT_ID)
	public ResponseEntity<?> getAllCategoriesByParentId(
			@RequestBody CategoryByParentRequest request,
			Model model) {
		try {
			CategoryOutPut result = new CategoryOutPut();
			int page = request.getPage();
			Integer limit = request.getLimit() != null ? request.getLimit() : 10;

			result.setPage(page);
			Pageable pageable = PageRequest.of(page - 1, limit);
			result.setListResult(categoryService.findAllByParentId(pageable, request.getParentId()));
			result.setTotalPage((int) Math.ceil((double) categoryService.totalItem() / limit));

			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
		RequestContext requestContext =RequestContext.get ();
		return  ResponseEntity.status(status).body (Map.of("message",message,
				"status",status.value(),
				"requestUrl",requestContext.getRequestURL (),
				"requestId",requestContext.getRequestId (),
				"timestamp",requestContext.getTimestamp ()));
	}

	@PostMapping(CategoryApiPaths.CREATE_PATHS)
	public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
		try {
			categoryService.save(categoryRequest);

			return ResponseEntity.ok("Category created successfully");

		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PutMapping(CategoryApiPaths.EDIT_PATHS)
	public ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryRequest updateCategoryRequest, @PathVariable Long id) {
		try {
			categoryService.update(updateCategoryRequest, id);

			return ResponseEntity.ok("Category updated successfully");

		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PutMapping(CategoryApiPaths.EDIT_PATHS_DESCRIPTION)
	public ResponseEntity<?> updateCategory(@RequestBody CategoriesDescriptionsDTO updateCategoryRequest, @PathVariable Long id) {
		try {
			categoryService.updateDescription(updateCategoryRequest, id);

			return ResponseEntity.ok("Category updated successfully");

		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@DeleteMapping(CategoryApiPaths.DELETE_PATHS)
	public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
		try {
			categoryService.delete(id);

			return ResponseEntity.ok("Category updated successfully");

		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
