package vnskilled.edu.ecom.Controller.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Service.Categories.CategoryService;
import vnskilled.edu.ecom.Service.Image.ImageService;
import vnskilled.edu.ecom.Service.User.UserService;
import vnskilled.edu.ecom.Util.EndpointConstant.ImageApiPaths;

import java.util.Map;

@RestController
@RequestMapping(ImageApiPaths.BASE_PATH)
public class ImageController {
	private final UserService userService;
	private final CategoryService categoryService;
	private final ImageService imageService;
	@Autowired
	public ImageController (UserService userService, CategoryService categoryService, ImageService imageService) {
		this.userService = userService;
		this.categoryService = categoryService;
		this.imageService = imageService;
	}
	@PostMapping(ImageApiPaths.AVATAR_PATH)
	public ResponseEntity<?> upLoadAvatar(@RequestBody MultipartFile avatar) {
		try {

			String avatarUrl = imageService.uploadAvatar (avatar);
			return ResponseEntity.ok(Map.of ("message", "Tải lên avatar thành công","avatarUrl", avatarUrl));
		} catch (Exception e) {
			RequestContext  requestContext =RequestContext.get ();
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body (Map.of("message",e.getMessage(),
					"status",HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"requestUrl",requestContext.getRequestURL (),
					"requestId",requestContext.getRequestId (),
					"timestamp",requestContext.getTimestamp ()));
		}
	}


}
