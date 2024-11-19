package vnskilled.edu.ecom.Model.DTO.Products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDTO;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long id;
    private boolean published;
    private CategoriesDTO categoryId;
    private UserDTO createBy;
    private UserDTO updateBy;
    private Timestamp createAt;
    private Timestamp updateAt;
    private boolean hotProduct;
    private boolean newProduct;
    private String image;
    private String video;
	private Timestamp deletedAt;
    private double rating;

}
