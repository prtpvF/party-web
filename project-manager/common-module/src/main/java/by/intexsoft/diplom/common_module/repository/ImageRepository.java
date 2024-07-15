package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
