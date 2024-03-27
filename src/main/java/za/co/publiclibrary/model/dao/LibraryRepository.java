package za.co.publiclibrary.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.publiclibrary.model.entity.LibraryEntity;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 * */

@Repository
public interface LibraryRepository extends JpaRepository<LibraryEntity, Long> {}