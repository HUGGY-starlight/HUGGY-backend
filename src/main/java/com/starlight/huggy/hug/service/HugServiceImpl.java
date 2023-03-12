package com.starlight.huggy.hug.service;

import com.starlight.huggy.hug.domain.HuggedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HugServiceImpl implements HugService {
    @Autowired
    private HuggedRepository huggedRepository;
}
