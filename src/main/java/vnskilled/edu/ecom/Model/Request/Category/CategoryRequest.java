package vnskilled.edu.ecom.Model.Request.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryRequest {
	private String name;
	private String description;
	private MultipartFile file;
	private Long parentId;

}
