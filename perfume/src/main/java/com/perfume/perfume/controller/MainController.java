package com.perfume.perfume.controller;

import com.perfume.perfume.model.MakeUp;
import com.perfume.perfume.model.Perfume;
import com.perfume.perfume.model.Product;
import com.perfume.perfume.repository.MakeUpRepository;
import com.perfume.perfume.repository.PerfumeRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ISO53
 */
@CrossOrigin
@RestController
@RequestMapping("/Main")
public class MainController {

    @Autowired
    private MakeUpRepository makeUpRepository;

    @Autowired
    private PerfumeRepository perfumeRepository;

    @GetMapping("/getAll")
    public List<Product> getAll() {

        List<MakeUp> makeUpList = makeUpRepository.findAll();
        List<Perfume> perfumeList = perfumeRepository.findAll();

        List<Product> finalList = new ArrayList<>();
        finalList.addAll(makeUpList);
        finalList.addAll(perfumeList);

        return finalList;
    }

    @GetMapping("/getById")
    public Product getById(@RequestParam("id") Integer id) {

        List<MakeUp> makeUpList = makeUpRepository.findAll();
        List<Perfume> perfumeList = perfumeRepository.findAll();

        List<Product> finalList = new ArrayList<>();
        finalList.addAll(makeUpList);
        finalList.addAll(perfumeList);

        for (Product product : finalList) {
            if (Objects.equals(product.getID(), id)) {
                return product;
            }
        }
        return null;
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<HttpStatus> deleteAll() {
        try {
            makeUpRepository.deleteAll();
            perfumeRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam("word") String word) {

        List<Product> products = getAll();

        if (word.length() < 3) {
            return products;
        }

        List<Product> returnList = new ArrayList<>();

        for (Product product : products) {
            if (product.getName().toLowerCase().contains(word.toLowerCase())) {
                returnList.add(product);
            }
        }
        return returnList;
    }

    @GetMapping("/getByFilters")
    public List<Product> getByFilters(@RequestParam("brands") String[] brands,
            @RequestParam("price") Integer[] priceRange,
            @RequestParam("sort_by") String sortBy,
            @RequestParam("is_ascending") boolean isAscending) {

        List<Product> returnList = new ArrayList<>();
        List<Product> products = getAll();

        List<String> brandsList = Arrays.asList(brands);

        // Filter the list
        for (int i = 0; i < products.size(); i++) {

            // Brand filter control
            if (!brandsList.contains(products.get(i).getBrand())) {
                continue;
            }

            // Price range control
            if ((products.get(i).getPrice() * (1 - products.get(i).getDiscount()) < priceRange[0])
                    || (products.get(i).getPrice() * (1 - products.get(i).getDiscount()) > priceRange[1])) {
                continue;
            }

            // Everything checks, add to the list
            returnList.add(products.get(i));
        }

        // Sort the list
        Collections.sort(returnList, (Product o1, Product o2) -> {
            // sortBy values = {Price, Discount, Last Update}
            int comparing;

            switch (sortBy) {
                case "Price":
                    comparing = (int) (o1.getPrice() * (1 - o1.getDiscount()) - o2.getPrice() * (1 - o2.getDiscount()));
                    return isAscending ? comparing : -comparing;
                case "Discount":
                    comparing = (int) (o1.getDiscount() * 100) - (int) (o2.getDiscount() * 100);
                    return isAscending ? comparing : -comparing;
                case "Last Update":
                    comparing = o1.getDate() - o2.getDate();
                    return isAscending ? comparing : -comparing;
                default:
                    System.out.println("Wrong sorting value!");
                    return 0;
            }

        });

        return returnList;
    }

    @GetMapping("/setPublished")
    public ResponseEntity<Product> setPublished(@RequestParam("productId") Integer productId,
            @RequestParam("isPublished") Boolean isPublished) {

        Product product = getById(productId);

        if (product != null) {
            product.setIsPublished(isPublished);

            if (product instanceof MakeUp) {
                return new ResponseEntity<>(makeUpRepository.save((MakeUp) product), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(perfumeRepository.save((Perfume) product), HttpStatus.OK);

            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getOpportunities")
    public List<Product> getOpportunities() {
        // Returns top 9 most discounted products

        List<Product> products = getAll();

        // Sort the list
        Collections.sort(products, (Product o1, Product o2) -> {
            // sortBy values = {Price, Discount, Last Update}
            return (int) (o2.getDiscount() * 100) - (int) (o1.getDiscount() * 100);
        });

        // return first 9 elements
        return products.stream().limit(9).collect(Collectors.toList());
    }
    
    @GetMapping("/getRandom")
    public List<Product> getRandomProducts(@RequestParam("count") Integer count) {

        List<MakeUp> makeUpList = makeUpRepository.findAll();
        List<Perfume> perfumeList = perfumeRepository.findAll();

        List<Product> finalList = new ArrayList<>();
        finalList.addAll(makeUpList);
        finalList.addAll(perfumeList);
        
        Collections.shuffle(finalList);

        return finalList.stream().limit(count).collect(Collectors.toList());
    }

    @GetMapping("/getBrands")
    public List<String> getBrands() {

        List<String> brandsList = new ArrayList<>();
        List<Product> products = getAll();

        for (Product product : products) {
            if (!brandsList.contains(product.getBrand())) {
                brandsList.add(product.getBrand());
            }
        }
        return brandsList;
    }

}
