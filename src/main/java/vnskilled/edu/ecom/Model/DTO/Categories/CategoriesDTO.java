package vnskilled.edu.ecom.Model.DTO.Categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriesDTO {
    private Long id;
    private Long parentId;
    private String image;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

}
