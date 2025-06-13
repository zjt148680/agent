package com.speedboot.speedbotagent.service;

import com.speedboot.speedbotagent.dto.t2vservice.BatchVectorRequestDTO;
import com.speedboot.speedbotagent.dto.t2vservice.VectorRequestDTO;
import com.speedboot.speedbotagent.dto.t2vservice.VectorResponseDTO;

import java.util.List;

public interface IGetVectorService {
    VectorResponseDTO getVector(VectorRequestDTO vectorRequestDTO);

    List<VectorResponseDTO> batchGetVector(BatchVectorRequestDTO vectorRequestDTO);
}
