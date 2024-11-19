package vnskilled.edu.ecom.Model.Request.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCategoryRequest {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String language;
    private Long categoryId;
    private Long parentId;

}
