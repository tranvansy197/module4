package com.codegym.service.contract.impl;

import com.codegym.dto.contract.IAttachFacilityDto;
import com.codegym.model.contract.AttachFacility;
import com.codegym.repository.contract.IAttachFacilityRepository;
import com.codegym.service.contract.IAttachFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AttachFacilityService implements IAttachFacilityService {
    @Autowired
    private IAttachFacilityRepository attachFacilityRepository;

    @Override
    public List<AttachFacility> findAll() {
        return attachFacilityRepository.findAll();
    }

    @Override
    public List<AttachFacility> findByContractId(int id) {
        return attachFacilityRepository.findByContractId(id);
    }

    @Override
    public List<IAttachFacilityDto> findByContractIdNew(int id) {
        return attachFacilityRepository.findByContractIdNew(id);
    }

    @Override
    public AttachFacility findById(int id) {
        return attachFacilityRepository.findById(id).orElse(null);
    }


}
