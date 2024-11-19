package vnskilled.edu.ecom.Model.DTO.Categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriesDescriptionsDTO {
    private Long id;
    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserDTO createdBy;
    private String language;
    private CategoriesDTO categoryId;

}
