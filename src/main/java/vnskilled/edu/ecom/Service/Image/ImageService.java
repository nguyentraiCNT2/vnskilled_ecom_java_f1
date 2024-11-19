package vnskilled.edu.ecom.Service.Image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	String uploadAvatar(MultipartFile file);
	String upLoadImageProduct(MultipartFile file);
	String upLoadImageVariant(MultipartFile file);
	String upLoadImageCategory(MultipartFile file);
}
