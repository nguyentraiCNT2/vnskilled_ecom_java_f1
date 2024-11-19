package vnskilled.edu.ecom.Service.Impl.Categories;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Categories.CategoriesDescriptionsEntity;
import vnskilled.edu.ecom.Entity.Categories.CategoriesEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDTO;
import vnskilled.edu.ecom.Model.DTO.Categories.CategoriesDescriptionsDTO;
import vnskilled.edu.ecom.Model.Request.Category.CategoryRequest;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Model.Request.Category.UpdateCategoryRequest;
import vnskilled.edu.ecom.Model.Response.Categories.CategoryResponse;
import vnskilled.edu.ecom.Repository.Categories.CategoriesDescriptionRepository;
import vnskilled.edu.ecom.Repository.Categories.CategoryRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Service.Categories.CategoryService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoriesDescriptionRepository categoriesDescriptionRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoriesDescriptionRepository categoriesDescriptionRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.categoriesDescriptionRepository = categoriesDescriptionRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<CategoryResponse> findAll(Pageable pageable) {
        try {
            return categoryRepository.findAll(pageable).getContent().stream()
                    .filter(category -> category.getDeletedAt() == null)
                    .map(category -> {
                        List<CategoriesDescriptionsDTO> descriptionsDTOList = categoriesDescriptionRepository.findByCategoryId(category).stream()
                                .map(description -> modelMapper.map(description, CategoriesDescriptionsDTO.class))
                                .collect(Collectors.toList());

                        CategoryResponse response = new CategoryResponse();
                        response.setCategoriesDTO(modelMapper.map(category, CategoriesDTO.class));
                        response.setCategoriesDescriptionsDTOList(descriptionsDTOList);

                        return response;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<CategoriesDescriptionsDTO> getAllDescription(Long id,Pageable pageable) {
        try {
            List<CategoriesDescriptionsDTO> descriptionsDTOList = new ArrayList<>();
            List<CategoriesDescriptionsEntity> categoriesDescriptionsEntities = categoriesDescriptionRepository.findCategoriesDescriptionsEntitiesByCategoryId(id, pageable);
            categoriesDescriptionsEntities.stream()
                    .forEach(description -> descriptionsDTOList.add( modelMapper.map(description, CategoriesDescriptionsDTO.class)));
        return descriptionsDTOList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CategoryResponse findByCategoryId(Long id) {
        try {
            CategoriesEntity categoriesEntity = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
            List<CategoriesDescriptionsDTO> descriptionsDTOList = categoriesDescriptionRepository.findByCategoryId(categoriesEntity).stream()
                    .map(description -> modelMapper.map(description, CategoriesDescriptionsDTO.class))
                    .collect(Collectors.toList());
            CategoryResponse response = new CategoryResponse();
            response.setCategoriesDTO(modelMapper.map(categoriesEntity, CategoriesDTO.class));
            response.setCategoriesDescriptionsDTOList(descriptionsDTOList);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        try {
            return categoryRepository.findAll().stream()
                    .filter(category -> category.getDeletedAt() == null)
                    .map(category -> {
                        List<CategoriesDescriptionsDTO> descriptionsDTOList = categoriesDescriptionRepository.findByCategoryId(category).stream()
                                .map(description -> modelMapper.map(description, CategoriesDescriptionsDTO.class))
                                .collect(Collectors.toList());

                        CategoryResponse response = new CategoryResponse();
                        response.setCategoriesDTO(modelMapper.map(category, CategoriesDTO.class));
                        response.setCategoriesDescriptionsDTOList(descriptionsDTOList);

                        return response;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<CategoryResponse> getCategoriesByName(String name, Pageable pageable) {
        try {
            return categoryRepository.findCategoriesByName(name,pageable).stream()
                    .filter(category -> category.getDeletedAt() == null)
                    .map(category -> {
                        List<CategoriesDescriptionsDTO> descriptionsDTOList = categoriesDescriptionRepository.findByCategoryId(category).stream()
                                .map(description -> modelMapper.map(description, CategoriesDescriptionsDTO.class))
                                .collect(Collectors.toList());

                        CategoryResponse response = new CategoryResponse();
                        response.setCategoriesDTO(modelMapper.map(category, CategoriesDTO.class));
                        response.setCategoriesDescriptionsDTOList(descriptionsDTOList);

                        return response;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<CategoryResponse> findAllByParentId(Pageable pageable, Long parentId) {
        try {
            return categoryRepository.findByParentId(parentId,pageable).stream()
                    .filter(category -> category.getDeletedAt() == null)
                    .map(category -> {
                        List<CategoriesDescriptionsDTO> descriptionsDTOList = categoriesDescriptionRepository.findByCategoryId(category).stream()
                                .map(description -> modelMapper.map(description, CategoriesDescriptionsDTO.class))
                                .collect(Collectors.toList());

                        CategoryResponse response = new CategoryResponse();
                        response.setCategoriesDTO(modelMapper.map(category, CategoriesDTO.class));
                        response.setCategoriesDescriptionsDTOList(descriptionsDTOList);

                        return response;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(CategoryRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(email);

            // Kiểm tra điều kiện đầu vào
            if (request.getName() == null || request.getName().isEmpty())
                throw new RuntimeException("Tên chủ đề không thể để trống");
            if (request.getDescription() == null || request.getDescription().isEmpty())
                throw new RuntimeException("Mô tả chủ đề không thể để trống");
            if (request.getFile() == null || request.getFile().isEmpty())
                throw new RuntimeException("Ảnh của chủ đề không thể để trống");

            // Kiểm tra vòng lặp danh mục
            if (request.getParentId() != null && isCircularReference(request.getParentId(), request.getParentId())) {
                throw new RuntimeException("Danh mục không thể tham chiếu đến danh mục con của chính nó.");
            }

            // Tạo mới CategoriesEntity
            CategoriesEntity categoriesEntity = new CategoriesEntity();
            categoriesEntity.setParentId(request.getParentId());
            categoriesEntity.setCreatedAt(Timestamp.from(Instant.now()));
            categoriesEntity.setDeletedAt(null);

            CategoriesEntity categories = categoryRepository.save(categoriesEntity);

            // Tạo mới CategoriesDescriptionsEntity
            CategoriesDescriptionsEntity categoriesDescriptions = new CategoriesDescriptionsEntity();
            categoriesDescriptions.setCategoryId(categories);
            categoriesDescriptions.setDescription(request.getDescription());
            categoriesDescriptions.setName(request.getName());
            categoriesDescriptions.setCreatedBy(user);
            categoriesDescriptions.setCreatedAt(Timestamp.from(Instant.now()));
            categoriesDescriptions.setLanguage("vn");

            categoriesDescriptionRepository.saveAndFlush(categoriesDescriptions);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Phương thức đệ quy kiểm tra vòng lặp danh mục
    private boolean isCircularReference(Long parentId, Long categoryId) {
        // Tìm danh mục cha của `parentId`
        Optional<CategoriesEntity> parentCategory = categoryRepository.findById(parentId);
        if (parentCategory.isPresent()) {
            // Nếu danh mục cha trỏ về chính danh mục đó, đây là vòng lặp
            if (parentCategory.get().getId().equals(categoryId)) {
                return true;
            }
            // Đệ quy kiểm tra tiếp danh mục cha của danh mục cha hiện tại
            return isCircularReference(parentCategory.get().getParentId(), categoryId);
        }
        return false;
    }

    @Override
    public void update(UpdateCategoryRequest request, Long id) {
        try {
            // Tìm kiếm CategoriesDescriptionsEntity theo id và ngôn ngữ
            CategoriesDescriptionsEntity categoriesDescriptionsEntity = categoriesDescriptionRepository
                    .findByIdAndLanguage(id, "vi")
                    .orElseThrow(() -> new RuntimeException("Category Not Found for language: " + request.getLanguage()));

            // Cập nhật thông tin mô tả
            categoriesDescriptionsEntity.setName(request.getName());
            categoriesDescriptionsEntity.setDescription(request.getDescription());
            categoriesDescriptionsEntity.setUpdatedAt(Timestamp.from(Instant.now()));
            categoriesDescriptionRepository.save(categoriesDescriptionsEntity);

            // Tìm kiếm CategoriesEntity theo categoryId
            CategoriesEntity categoriesEntity = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent Category Not Found"));

            // Kiểm tra vòng lặp danh mục khi cập nhật
            if (request.getParentId() != null && isCircularReference(request.getParentId(), request.getCategoryId())) {
                throw new RuntimeException("Danh mục không thể tham chiếu đến danh mục con của chính nó.");
            }

            // Cập nhật thông tin của Category
            categoriesEntity.setParentId(request.getParentId());
            categoriesEntity.setUpdatedAt(Timestamp.from(Instant.now()));
            categoriesEntity.setImage(request.getImage());
            categoryRepository.save(categoriesEntity);

        } catch (Exception e) {
            throw new RuntimeException("Update failed: " + e.getMessage());
        }
    }

    @Override
    public void updateDescription(CategoriesDescriptionsDTO request, Long id) {
        try {
            // Tìm kiếm CategoriesDescriptionsEntity theo id và ngôn ngữ
            CategoriesDescriptionsEntity categoriesDescriptionsEntity = categoriesDescriptionRepository
                    .findByIdAndLanguage(id, request.getLanguage())
                    .orElseThrow(() -> new RuntimeException("Category Not Found for language: " + request.getLanguage()));

            // Cập nhật thông tin mô tả
            categoriesDescriptionsEntity.setName(request.getName());
            categoriesDescriptionsEntity.setDescription(request.getDescription());
            categoriesDescriptionsEntity.setUpdatedAt(Timestamp.from(Instant.now()));
            categoriesDescriptionRepository.save(categoriesDescriptionsEntity);

        } catch (Exception e) {
            throw new RuntimeException("Update failed: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            CategoriesEntity categoriesEntity = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category Not Found"));
            categoriesEntity.setDeletedAt(Timestamp.from(Instant.now()));
            categoryRepository.save(categoriesEntity);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int totalItem() {
        return (int) categoryRepository.count();
    }

    @Override
    public void CreateDescription(CategoriesDescriptionsDTO request) {
        try {
            // Tìm kiếm CategoriesDescriptionsEntity theo id và ngôn ngữ
            CategoriesEntity categoriesEntity = categoryRepository.findById(request.getCategoryId().getId())
                    .orElseThrow(() -> new RuntimeException("Category Not Found"));
            CategoriesDescriptionsEntity categoriesDescriptionsEntity = new CategoriesDescriptionsEntity();

            // Cập nhật thông tin mô tả
            categoriesDescriptionsEntity.setName(request.getName());
            categoriesDescriptionsEntity.setDescription(request.getDescription());
            categoriesDescriptionsEntity.setUpdatedAt(Timestamp.from(Instant.now()));
            categoriesDescriptionsEntity.setCategoryId(categoriesEntity);
            categoriesDescriptionsEntity.setLanguage(request.getLanguage());
            categoriesDescriptionRepository.save(categoriesDescriptionsEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CategoriesDescriptionsDTO findDescriptionById(Long id) {
        try {
            CategoriesDescriptionsEntity categoriesDescriptionsEntity = categoriesDescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not Found"));
            return modelMapper.map(categoriesDescriptionsEntity, CategoriesDescriptionsDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteDescriptionById(Long id) {
        try {
            CategoriesDescriptionsEntity categoriesDescriptionsEntity = categoriesDescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not Found"));
            categoriesDescriptionRepository.delete(categoriesDescriptionsEntity);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
