package vnskilled.edu.ecom.Repository.Favorites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Favorites.FavoritesEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoritesEntity, Long> {
	List<FavoritesEntity> findByUserId(UserEntity user);
}
