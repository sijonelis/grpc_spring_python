package com.vilkazz.grpc.service;

import com.vilkazz.grpc.domain.PythonResult;
import com.vilkazz.grpc.repository.PythonResultRepository;
import com.vilkazz.grpc.service.dto.PythonResultDTO;
import com.vilkazz.grpc.service.mapper.PythonResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PythonResult.
 */
@Service
@Transactional
public class PythonResultService {

    private final Logger log = LoggerFactory.getLogger(PythonResultService.class);

    private PythonResultRepository pythonResultRepository;

    private PythonResultMapper pythonResultMapper;

    public PythonResultService(PythonResultRepository pythonResultRepository, PythonResultMapper pythonResultMapper) {
        this.pythonResultRepository = pythonResultRepository;
        this.pythonResultMapper = pythonResultMapper;
    }

    /**
     * Save a pythonResult.
     *
     * @param pythonResultDTO the entity to save
     * @return the persisted entity
     */
    public PythonResultDTO save(PythonResultDTO pythonResultDTO) {
        log.debug("Request to save PythonResult : {}", pythonResultDTO);

        PythonResult pythonResult = pythonResultMapper.toEntity(pythonResultDTO);
        pythonResult = pythonResultRepository.save(pythonResult);
        return pythonResultMapper.toDto(pythonResult);
    }

    /**
     * Get all the pythonResults.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PythonResultDTO> findAll() {
        log.debug("Request to get all PythonResults");
        return pythonResultRepository.findAll().stream()
            .map(pythonResultMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one pythonResult by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PythonResultDTO> findOne(Long id) {
        log.debug("Request to get PythonResult : {}", id);
        return pythonResultRepository.findById(id)
            .map(pythonResultMapper::toDto);
    }

    /**
     * Delete the pythonResult by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PythonResult : {}", id);
        pythonResultRepository.deleteById(id);
    }
}
