package com.starlight.huggy.store.controller;

import com.starlight.huggy.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreApiController {
    @Autowired
    StoreService storeService;
}
