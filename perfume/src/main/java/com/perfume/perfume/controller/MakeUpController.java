package com.perfume.perfume.controller;

import com.perfume.perfume.model.MakeUp;
import com.perfume.perfume.repository.MakeUpRepository;
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
@RequestMapping("/MakeUp")
public class MakeUpController {

    @Autowired
    private MakeUpRepository makeUpRepository;

    @GetMapping("/getAll")
    public List<MakeUp> getAll() {
        List<MakeUp> makeups = makeUpRepository.findAll();
        List<MakeUp> returnList = new ArrayList<>();
        for (MakeUp makeup : makeups) {
            // Is published control
            if (makeup.getIsPublished()) {
                returnList.add(makeup);
            }
        }
        return returnList;
    }

    @GetMapping("/getById") // getByID?id=12
    public ResponseEntity<MakeUp> getById(@RequestParam("id") Integer id) {
        MakeUp makeUp = findById(id);

        if (makeUp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(makeUp, HttpStatus.FOUND);
        }
    }

    @PostMapping("/create") // getByID?MakeUp= obj???
    public ResponseEntity<MakeUp> create(@RequestBody MakeUp makeUp) {
        try {

            MakeUp _makeUp = (MakeUp) makeUpRepository
                    .save(new MakeUp(
                            makeUp.getID(),
                            makeUp.getName(),
                            makeUp.getBrand(),
                            makeUp.getPrice(),
                            makeUp.getDiscount(),
                            makeUp.getDate(),
                            makeUp.getInformation(),
                            makeUp.getImage(),
                            makeUp.getIsPublished()));
            return new ResponseEntity<>(_makeUp, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateByID") // updateByID?id=12
    public ResponseEntity<MakeUp> update(@RequestParam("id") Integer id, @RequestBody MakeUp makeUp) {
        MakeUp findedMakeUp = findById(id);

        if (findedMakeUp != null) {
            findedMakeUp.setID(makeUp.getID());
            findedMakeUp.setName(makeUp.getName());
            findedMakeUp.setBrand(makeUp.getBrand());
            findedMakeUp.setPrice(makeUp.getPrice());
            findedMakeUp.setDiscount(makeUp.getDiscount());
            findedMakeUp.setDate(makeUp.getDate());
            findedMakeUp.setInformation(makeUp.getInformation());
            findedMakeUp.setImage(makeUp.getImage());
            findedMakeUp.setIsPublished(makeUp.getIsPublished());
            return new ResponseEntity<>(makeUpRepository.save(findedMakeUp), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteById")   // deleteByID?id=12
    public ResponseEntity<HttpStatus> delete(@RequestParam("id") Integer id) {

        MakeUp makeUp = findById(id);

        if (makeUp != null) {
            makeUpRepository.delete(makeUp);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<HttpStatus> deleteAll() {
        try {
            makeUpRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public List<MakeUp> search(@RequestParam("word") String word) {

        List<MakeUp> makeups = makeUpRepository.findAll();

        if (word.length() < 3) {
            return makeups;
        }

        List<MakeUp> returnList = new ArrayList<>();

        for (MakeUp makeup : makeups) {
            if (makeup.getName().toLowerCase().contains(word.toLowerCase())) {
                if (makeup.getIsPublished()) {
                    returnList.add(makeup);
                }
            }
        }
        return returnList;

    }

    @GetMapping("/getBrands")
    public List<String> getBrands() {

        List<String> brandsList = new ArrayList<>();
        List<MakeUp> makeups = makeUpRepository.findAll();

        for (MakeUp makeup : makeups) {
            if (!brandsList.contains(makeup.getBrand())) {
                brandsList.add(makeup.getBrand());
            }
        }

        return brandsList;
    }

    @GetMapping("/getByFilters")
    public List<MakeUp> getByFilters(@RequestParam("brands") String[] brands,
            @RequestParam("price") Integer[] priceRange,
            @RequestParam("sort_by") String sortBy,
            @RequestParam("is_ascending") boolean isAscending) {

        List<MakeUp> returnList = new ArrayList<>();
        List<MakeUp> makeups = makeUpRepository.findAll();

        List<String> brandsList = Arrays.asList(brands);

        // Filter the list
        for (int i = 0; i < makeups.size(); i++) {

            // Brand filter control
            if (!brandsList.contains(makeups.get(i).getBrand())) {
                continue;
            }

            // Price range control
            if ((makeups.get(i).getPrice() * (1 - makeups.get(i).getDiscount()) < priceRange[0])
                    || (makeups.get(i).getPrice() * (1 - makeups.get(i).getDiscount()) > priceRange[1])) {
                continue;
            }
            
            // Is published control
            if (!makeups.get(i).getIsPublished()) {
                continue;
            }

            // Everything checks, add to the list
            returnList.add(makeups.get(i));
        }

        // Sort the list
        Collections.sort(returnList, (MakeUp mk1, MakeUp mk2) -> {
            // sortBy values = {Price, Discount, Last Update}
            int comparing;
            switch (sortBy) {
                case "Price":
                    comparing = (int) ((mk1.getPrice() * (1 - mk1.getDiscount())) - (mk2.getPrice() * (1 - mk2.getDiscount())));
                    return isAscending ? comparing : -comparing;
                case "Discount":
                    comparing = (int) (mk1.getDiscount() * 100) - (int) (mk2.getDiscount() * 100);
                    return isAscending ? comparing : -comparing;
                case "Last Update":
                    comparing = mk1.getDate() - mk2.getDate();
                    return isAscending ? comparing : -comparing;
                default:
                    System.out.println("Wrong sorting value!");
                    return 0;
            }
        });

        return returnList;
    }

    public MakeUp findById(Integer id) {
        List<MakeUp> makeUpData = makeUpRepository.findAll();

        for (MakeUp makeUp : makeUpData) {
            if (Objects.equals(makeUp.getID(), id)) {
                return makeUp;
            }
        }
        return null;
    }
}
