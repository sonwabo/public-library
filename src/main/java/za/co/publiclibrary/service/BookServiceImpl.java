package za.co.publiclibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.publiclibrary.model.dao.BookRepository;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
 */

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository repository;

}
