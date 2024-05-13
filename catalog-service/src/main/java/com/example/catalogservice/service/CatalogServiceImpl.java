package com.example.catalogservice.service;

import com.example.catalogservice.repository.CatalogEntity;
import com.example.catalogservice.repository.CatalogRepository;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public List<ResponseCatalog> getAllCatalogs() {
        List<CatalogEntity> catalogList = catalogRepository.findAll();

        List<ResponseCatalog> result = new ArrayList<>();

        catalogList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseCatalog.class));
        });

        return result;
    }
}
