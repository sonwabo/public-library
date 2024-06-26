package za.co.publiclibrary.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import za.co.publiclibrary.dto.LibraryDTO;
import za.co.publiclibrary.exceptions.LibraryNotFoundException;
import za.co.publiclibrary.mapper.LibraryMapper;
import za.co.publiclibrary.model.dao.LibraryRepository;
import za.co.publiclibrary.model.entity.LibraryEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryServiceImpl implements LibraryService
{
    private final static String CACHE_NAME = "libaries";

    private final LibraryRepository libraryRepository;

    @Cacheable(value = CACHE_NAME)
    public List<LibraryDTO> getAllLibraries()
    {
        return libraryRepository
                .findAll()
                .stream()
                .map(LibraryMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Cacheable(value = CACHE_NAME, key = "#id")
    public LibraryDTO findLibraryById(final Long id)
    {
        return libraryRepository
                .findById(id)
                .map(LibraryMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found for provided id: " + id ));
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public LibraryDTO createLibrary(final LibraryDTO libraryDTO)
    {
        LibraryEntity entity = LibraryMapper.INSTANCE.toEntity(libraryDTO);
        LibraryMapper.INSTANCE.mapLibraryOnDependencies(entity);

        return LibraryMapper.INSTANCE.toDTO(libraryRepository.save(entity));
    }

    @SneakyThrows
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public LibraryDTO updateLibrary(final LibraryDTO libraryDTO)
    {
        if (!this.libraryRepository.existsById(libraryDTO.id())) {
            throw new LibraryNotFoundException("Library not found with id: " + libraryDTO.id());
        }

        final var entity = LibraryMapper.INSTANCE.toEntity(libraryDTO);
        LibraryMapper.INSTANCE.mapLibraryOnDependencies(entity);

        return  LibraryMapper.INSTANCE.toDTO(libraryRepository.save(entity));
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deleteLibraryById(final Long id)
    {
        libraryRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Refresh cache daily at midnight
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void evictCache() { /* Cache will be refreshed automatically */ }

}