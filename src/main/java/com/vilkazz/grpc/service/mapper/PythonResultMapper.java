package com.vilkazz.grpc.service.mapper;

import com.vilkazz.grpc.domain.*;
import com.vilkazz.grpc.service.dto.PythonResultDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PythonResult and its DTO PythonResultDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PythonResultMapper extends EntityMapper<PythonResultDTO, PythonResult> {



    default PythonResult fromId(Long id) {
        if (id == null) {
            return null;
        }
        PythonResult pythonResult = new PythonResult();
        pythonResult.setId(id);
        return pythonResult;
    }
}
