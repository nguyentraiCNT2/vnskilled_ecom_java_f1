package vnskilled.edu.ecom.Controller.AdminController.Categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDTO;
import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDescriptionsDTO;
import vnskilled.edu.ecom.Model.Request.Category.CategoryByParentRequest;
import vnskilled.edu.ecom.Model.Request.Category.CategoryRequest;
import vnskilled.edu.ecom.Model.Request.Category.UpdateCategoryRequest;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Model.Response.Categories.CategoryOutPut;
import vnskilled.edu.ecom.Model.Response.Categories.CategoryResponse;
import vnskilled.edu.ecom.Service.Categories.CategoryService;
import vnskilled.edu.ecom.Util.EndpointConstant.CategoryApiPaths;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin"+CategoryApiPaths.BASE_PATHS)
public class AdminCategoryController {
	private final CategoryService categoryService;

	@Autowired
	public AdminCategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	@GetMapping
	public String getAllCategories(
			@RequestParam(value = "page",defaultValue = "1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			Model model) {
		try {
			// Lấy danh sách danh mục với phân trang
			Pageable pageable = PageRequest.of(page - 1, limit);
			List<CategoryResponse> categories = categoryService.findAll(pageable);

			// Tính toán tổng số trang
			int totalItems = categoryService.totalItem();
			int totalPages = (int) Math.ceil((double) totalItems / limit);

			// Truyền dữ liệu vào model để hiển thị trên view
			model.addAttribute("categories", categories);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("limit", limit);

			// Trả về tên view để hiển thị dữ liệu
			return "Category/listCategories"; // Thay đổi đường dẫn đến view phù hợp
		} catch (Exception e) {
			// Xử lý lỗi và có thể chuyển đến trang lỗi hoặc trang có thông báo
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/listCategories"; // Thay đổi đường dẫn đến view lỗi phù hợp
		}
	}
	@GetMapping("/desription/{id}")
	public String getAllCategoriesDescription(@PathVariable Long id,
			@RequestParam(value = "page",defaultValue = "1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			Model model) {
		try {
			// Lấy danh sách danh mục với phân trang
			Pageable pageable = PageRequest.of(page - 1, limit);
			List<CategoriesDescriptionsDTO> categoriesDescriptionsDTOS = categoryService.getAllDescription(id,pageable);

			// Tính toán tổng số trang
			int totalItems = categoryService.totalItem();
			int totalPages = (int) Math.ceil((double) totalItems / limit);

			// Truyền dữ liệu vào model để hiển thị trên view
			model.addAttribute("categoriesDescriptionsDTOS", categoriesDescriptionsDTOS);
			model.addAttribute("categoryId", id);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("limit", limit);

			// Trả về tên view để hiển thị dữ liệu
			return "Category/ListDescription"; // Thay đổi đường dẫn đến view phù hợp
		} catch (Exception e) {
			// Xử lý lỗi và có thể chuyển đến trang lỗi hoặc trang có thông báo
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/ListDescription"; // Thay đổi đường dẫn đến view lỗi phù hợp
		}
	}

	@GetMapping("/search")
	public String getCategoriesByName(@RequestParam("name") String name,
			@RequestParam(value = "page",defaultValue = "1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			Model model) {
		try {
			// Lấy danh sách danh mục với phân trang
			Pageable pageable = PageRequest.of(page - 1, limit);
			List<CategoryResponse> categories = categoryService.getCategoriesByName(name,pageable);

			// Tính toán tổng số trang
			int totalItems = categoryService.totalItem();
			int totalPages = (int) Math.ceil((double) totalItems / limit);

			// Truyền dữ liệu vào model để hiển thị trên view
			model.addAttribute("categories", categories);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("limit", limit);

			// Trả về tên view để hiển thị dữ liệu
			return "Category/searchCategories"; // Thay đổi đường dẫn đến view phù hợp
		} catch (Exception e) {
			// Xử lý lỗi và có thể chuyển đến trang lỗi hoặc trang có thông báo
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/searchCategories"; // Thay đổi đường dẫn đến view lỗi phù hợp
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
	@GetMapping("/create")
	public String createCategory(Model model){
		try {
			// Lấy danh sách danh mục với phân trang
			List<CategoryResponse> categories = categoryService.getAllCategories();
			// Truyền dữ liệu vào model để hiển thị trên view
			model.addAttribute("categories", categories);
			// Trả về tên view để hiển thị dữ liệu
			return "Category/CategoryCreate"; // Thay đổi đường dẫn đến view phù hợp
		} catch (Exception e) {
			// Xử lý lỗi và có thể chuyển đến trang lỗi hoặc trang có thông báo
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/CategoryCreate"; // Thay đổi đường dẫn đến view lỗi phù hợp
		}
	}
	@PostMapping(CategoryApiPaths.CREATE_PATHS)
	public String createCategory(@ModelAttribute  CategoryRequest categoryRequest, Model model) {
		try {
			categoryService.save(categoryRequest);
			model.addAttribute("message", "Category created successfully");
			return "redirect:/admin/categories/create";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "Category/CategoryCreate";
		}
	}

	@GetMapping(CategoryApiPaths.EDIT_PATHS)
	public String editCategory(@PathVariable Long id, Model model) {
		try {
			CategoryResponse categoryResponse = categoryService.findByCategoryId(id);
			// Lấy danh sách danh mục với phân trang
			List<CategoryResponse> categories = categoryService.getAllCategories();
			// Truyền dữ liệu vào model để hiển thị trên view
			model.addAttribute("categories", categories);
			model.addAttribute("categoryResponse", categoryResponse);
			return "Category/CategoryUpdate";
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/CategoryUpdate";
		}
	}

	@PutMapping(CategoryApiPaths.EDIT_PATHS)
	public String updateCategory(@ModelAttribute UpdateCategoryRequest updateCategoryRequest, @PathVariable Long id, Model model) {
		try {

			categoryService.update(updateCategoryRequest, id);
			model.addAttribute("message", "Category updated successfully");
			return "redirect:/admin/categories/edit/"+id;

		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/CategoryUpdate";
		}
	}

	@GetMapping("/description/create/{id}")
	public String createDescription(@PathVariable Long id, Model model) {
		try {
			model.addAttribute("categoryId", id);
			return "Category/CreateDescription";
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/CreateDescription";
		}
	}

	@PostMapping("/description/create/{id}")
	public String createDescription(
			@PathVariable Long id,
			@RequestParam String name,
			@RequestParam String description,
			@RequestParam String language,
			@RequestParam Long categoryId,
			Model model) {
		try {
			CategoriesDescriptionsDTO dto = new CategoriesDescriptionsDTO();
			dto.setName(name);
			dto.setDescription(description);
			dto.setLanguage(language);
			dto.setCategoryId(new CategoriesDTO(categoryId, null, null, null, null, null)); // Tạo CategoriesDTO từ categoryId

			categoryService.CreateDescription(dto);
			model.addAttribute("message", "Description created successfully");
			return "redirect:/admin/categories/description/create/"+id; // Redirect sau khi thành công

		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/CreateDescription"; // Trả về trang tạo mô tả với thông báo lỗi
		}
	}

	@GetMapping("/description/edit/{id}")
	public String updateDescription(@PathVariable Long id, Model model) {
		try {
			CategoriesDescriptionsDTO categoriesDescriptionsDTO = categoryService.findDescriptionById(id);
			model.addAttribute("categoriesDescription", categoriesDescriptionsDTO);
			return "Category/EditDescription";
		}catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/EditDescription";
		}
	}
	@PutMapping(CategoryApiPaths.EDIT_PATHS_DESCRIPTION)
	public String updateCategory(@PathVariable Long id,
								 @RequestParam String name,
								 @RequestParam String description,
								 @RequestParam String language,
								 Model model) {
		try {
			CategoriesDescriptionsDTO dto = new CategoriesDescriptionsDTO();
			dto.setName(name);
			dto.setDescription(description);
			dto.setLanguage(language);

			categoryService.updateDescription(dto, id);
			model.addAttribute("message", "Description updated successfully");
			return "redirect:/admin/categories/description/edit/"+id;

		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/EditDescription";
		}
	}


	@DeleteMapping(CategoryApiPaths.DELETE_PATHS)
	public String deleteCategory(@PathVariable Long id, Model model) {
		try {
			categoryService.delete(id);
			model.addAttribute("message", "Category deleted successfully");
			return "redirect:/admin/categories";

		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/CategoryDelete";
		}
	}

	@DeleteMapping("/delete/description/{id}")
	public String deleteDescription(@PathVariable Long id, Model model) {
		try {
			categoryService.deleteDescriptionById(id);
			model.addAttribute("message", "Description deleted successfully");
			return "redirect:/admin/categories";
		}catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Category/CategoryDelete";
		}
	}


}
