package com.perfume.perfume.controller;

import com.perfume.perfume.model.Perfume;
import com.perfume.perfume.repository.PerfumeRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ISO53
 */
@CrossOrigin
@RestController
@RequestMapping("/Perfume")
public class PerfumeController {

    @Autowired
    private PerfumeRepository perfumeRepository;

    @GetMapping("/getAll")
    public List<Perfume> getAll() {
        List<Perfume> perfumes = perfumeRepository.findAll();
        List<Perfume> returnList = new ArrayList<>();
        for (Perfume perfume : perfumes) {
            // Is published control
            if (perfume.getIsPublished()) {
                returnList.add(perfume);
            }
        }
        return returnList;
    }

    @GetMapping("/getById") // getByID?id=12
    public ResponseEntity<Perfume> getById(@RequestParam("id") Integer id) {
        Perfume perfume = findById(id);

        if (perfume == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(perfume, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create") // getByID?Perfume= obj???
    public ResponseEntity<Perfume> create(@RequestBody Perfume perfume) {
        try {
            Perfume _perfume = perfumeRepository
                    .save(new Perfume(
                            perfume.getID(),
                            perfume.getName(),
                            perfume.getBrand(),
                            perfume.getPrice(),
                            perfume.getDiscount(),
                            perfume.getDate(),
                            perfume.getInformation(),
                            perfume.getImage(),
                            perfume.getIsPublished()));
            return new ResponseEntity<>(_perfume, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateByID") // updateByID?id=12
    public ResponseEntity<Perfume> update(@RequestParam("id") Integer id, @RequestBody Perfume perfume) {
        Perfume findedPerfume = findById(id);

        if (findedPerfume != null) {
            findedPerfume.setID(perfume.getID());
            findedPerfume.setName(perfume.getName());
            findedPerfume.setBrand(perfume.getBrand());
            findedPerfume.setPrice(perfume.getPrice());
            findedPerfume.setDiscount(perfume.getDiscount());
            findedPerfume.setDate(perfume.getDate());
            findedPerfume.setInformation(perfume.getInformation());
            findedPerfume.setImage(perfume.getImage());
            findedPerfume.setIsPublished(perfume.getIsPublished());
            return new ResponseEntity<>(perfumeRepository.save(findedPerfume), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteById")   // deleteByID?id=12
    public ResponseEntity<HttpStatus> delete(@RequestParam("id") Integer id) {

        Perfume perfume = findById(id);

        if (perfume != null) {
            perfumeRepository.delete(perfume);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<HttpStatus> deleteAll() {
        try {
            perfumeRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public List<Perfume> search(@RequestParam("word") String word) {

        List<Perfume> perfumes = perfumeRepository.findAll();

        if (word.length() < 3) {
            return perfumes;
        }

        List<Perfume> returnList = new ArrayList<>();

        for (Perfume perfume : perfumes) {
            if (perfume.getName().toLowerCase().contains(word.toLowerCase())) {
                if (perfume.getIsPublished()) {
                    returnList.add(perfume);
                }
            }
        }
        return returnList;

    }

    @GetMapping("/getBrands")
    public List<String> getBrands() {

        List<String> brandsList = new ArrayList<>();
        List<Perfume> perfumes = perfumeRepository.findAll();

        for (Perfume perfume : perfumes) {
            if (!brandsList.contains(perfume.getBrand())) {
                brandsList.add(perfume.getBrand());
            }
        }

        return brandsList;
    }

    @GetMapping("/getByFilters")
    public List<Perfume> getByFilters(@RequestParam("brands") String[] brands,
            @RequestParam("price") Integer[] priceRange,
            @RequestParam("sort_by") String sortBy,
            @RequestParam("is_ascending") boolean isAscending) {

        List<Perfume> returnList = new ArrayList<>();
        List<Perfume> perfumes = perfumeRepository.findAll();

        List<String> brandsList = Arrays.asList(brands);

        // Filter the list
        for (int i = 0; i < perfumes.size(); i++) {

            // Brand filter control
            if (!brandsList.contains(perfumes.get(i).getBrand())) {
                continue;
            }

            // Price range control
            if ((perfumes.get(i).getPrice() * (1 - perfumes.get(i).getDiscount()) < priceRange[0])
                    || (perfumes.get(i).getPrice() * (1 - perfumes.get(i).getDiscount()) > priceRange[1])) {
                continue;
            }

            // Is published control
            if (!perfumes.get(i).getIsPublished()) {
                continue;
            }

            // Everything checks, add to the list
            returnList.add(perfumes.get(i));
        }

        // Sort the list
        Collections.sort(returnList, (Perfume p1, Perfume p2) -> {
            // sortBy values = {Price, Discount, Last Update}
            int comparing;
            switch (sortBy) {
                case "Price":
                    comparing = (int) ((p1.getPrice() * (1 - p1.getDiscount())) - (p2.getPrice() * (1 - p2.getDiscount())));
                    return isAscending ? comparing : -comparing;
                case "Discount":
                    comparing = (int) (p1.getDiscount() * 100) - (int) (p2.getDiscount() * 100);
                    return isAscending ? comparing : -comparing;
                case "Last Update":
                    comparing = p1.getDate() - p2.getDate();
                    return isAscending ? comparing : -comparing;
                default:
                    System.out.println("Wrong sorting value!");
                    return 0;
            }
        });
        return returnList;
    }
    
    public Perfume findById(Integer id) {
        List<Perfume> perfumeData = perfumeRepository.findAll();

        for (Perfume perfume : perfumeData) {
            if (Objects.equals(perfume.getID(), id)) {
                return perfume;
            }
        }
        return null;
    }
}
