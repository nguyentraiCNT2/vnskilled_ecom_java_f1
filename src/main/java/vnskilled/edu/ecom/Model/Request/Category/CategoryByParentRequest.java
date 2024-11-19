package vnskilled.edu.ecom.Model.Request.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryByParentRequest {
    private Long parentId;
    private int page;
    private Integer limit;

}
