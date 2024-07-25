package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Integer> {
}
