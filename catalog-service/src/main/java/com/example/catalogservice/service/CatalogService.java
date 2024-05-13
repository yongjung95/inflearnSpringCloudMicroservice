package com.example.catalogservice.service;

import com.example.catalogservice.repository.CatalogEntity;
import com.example.catalogservice.vo.ResponseCatalog;

import java.util.List;

public interface CatalogService {
    List<ResponseCatalog> getAllCatalogs();
}
