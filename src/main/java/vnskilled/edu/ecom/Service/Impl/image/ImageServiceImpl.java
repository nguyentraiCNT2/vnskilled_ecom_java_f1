package vnskilled.edu.ecom.Service.Impl.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Repository.Categories.CategoryRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Service.FirebaseStorageService;
import vnskilled.edu.ecom.Service.Image.ImageService;
@Service
public class ImageServiceImpl implements ImageService {
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final FirebaseStorageService  firebaseStorageService;

	@Autowired
	public ImageServiceImpl (UserRepository userRepository, CategoryRepository categoryRepository, FirebaseStorageService firebaseStorageService) {
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.firebaseStorageService = firebaseStorageService;
	}

	@Override
	public String uploadAvatar (MultipartFile file) {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();

			if (file != null && !file.isEmpty()){
				UserEntity userEntity= userRepository.findByEmail (email);
				if (userEntity == null)
					throw new RuntimeException("User Not Found");
				String imageFileName = firebaseStorageService.uploadFile (file);
				userEntity.setAvatar (imageFileName);
				userRepository.save (userEntity);
				return imageFileName;
			}
		} catch (Exception e) {
			throw new RuntimeException (e.getMessage ());
		}
		return null;
	}

	@Override
	public String upLoadImageProduct (MultipartFile file) {
		try {
			if (file != null && !file.isEmpty()){
				String imageFileName = firebaseStorageService.uploadFile (file);
				return imageFileName;
			}
		} catch (Exception e) {
			throw new RuntimeException (e.getMessage ());
		}
		return null;
	}

	@Override
	public String upLoadImageVariant (MultipartFile file) {
		return "";
	}

	@Override
	public String upLoadImageCategory (MultipartFile file) {
		return "";
	}
}
