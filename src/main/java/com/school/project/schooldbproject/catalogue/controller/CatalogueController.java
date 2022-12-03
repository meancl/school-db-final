package com.school.project.schooldbproject.catalogue.controller;

import com.school.project.schooldbproject.catalogue.dto.CreateCatalogueDto;
import com.school.project.schooldbproject.catalogue.entity.Catalogue;
import com.school.project.schooldbproject.catalogue.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CatalogueController {
    private final CatalogueService catalogueService;

    @Autowired
    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    @ResponseBody
    @PostMapping("catalogue")
    public Catalogue createCatalogue(@Valid @RequestBody CreateCatalogueDto createCatalogueDto) {
        System.out.println("Call controller");
        return catalogueService.createCatalogue(createCatalogueDto);
    }

    @ResponseBody
    @GetMapping("catalogue")
    public Catalogue findByCatalogueName(@RequestParam(name = "name") String name) {
        return catalogueService.findCatalogueByName(name);
    }


}