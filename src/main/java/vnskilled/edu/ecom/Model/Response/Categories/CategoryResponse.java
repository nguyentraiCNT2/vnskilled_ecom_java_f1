package vnskilled.edu.ecom.Model.Response.Categories;

import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDTO;
import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDescriptionsDTO;

import java.util.List;

public class CategoryResponse {
	private CategoriesDTO categoriesDTO;
	private List<CategoriesDescriptionsDTO>  categoriesDescriptionsDTOList;

	public CategoriesDTO getCategoriesDTO () {
		return categoriesDTO;
	}

	public void setCategoriesDTO (CategoriesDTO categoriesDTO) {
		this.categoriesDTO = categoriesDTO;
	}

	public List<CategoriesDescriptionsDTO> getCategoriesDescriptionsDTOList () {
		return categoriesDescriptionsDTOList;
	}

	public void setCategoriesDescriptionsDTOList (List<CategoriesDescriptionsDTO> categoriesDescriptionsDTOList) {
		this.categoriesDescriptionsDTOList = categoriesDescriptionsDTOList;
	}
}
