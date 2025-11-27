package ee.silves.veebipood.controller;

import ee.silves.veebipood.model.ParcelMachines;
import ee.silves.veebipood.model.Supplier1;
import ee.silves.veebipood.model.Supplier2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
public class SupplierController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("supplier1")
    public List<Supplier1> supplier1(){
        // RestTemplate restTemplate = new RestTemplate();

        String url = "https://fakestoreapi.com/products";
        Supplier1[] body = restTemplate
                .exchange(url, HttpMethod.GET,null,Supplier1[].class).getBody();
        // return Arrays.asList(body);
        return Arrays.stream(body)
                .filter(e -> e.getRating().getRate() > 4.0)
                .filter(e -> e.getRating().getCount() > 100)
                // .sorted(Comparator.comparing(o -> o.getItem().getValue()))
                .toList();
    }

    @GetMapping("supplier2")
    public List<Supplier2> supplier2(){
        // RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.escuelajs.co/api/v1/products";
        Supplier2[] body = restTemplate
                .exchange(url, HttpMethod.GET,null,Supplier2[].class).getBody();
        // return Arrays.asList(body);
        return Arrays.stream(body)
                .filter(e -> e.getPrice() < 60)
                // .sorted(Comparator.comparing(o -> o.getItem().getValue()))
                .toList();
    }

    @GetMapping("parcelmachines")
    public List<ParcelMachines> parcelmachines(@RequestParam(required = false) String country){
        String url = "https://www.omniva.ee/locations.json";
        ParcelMachines[] body = restTemplate
                .exchange(url, HttpMethod.GET,null,ParcelMachines[].class).getBody();

        if (country == null) {
            return Arrays.asList(body);
        } else {
            String pmCountry = country.toUpperCase();
            return Arrays.stream(body)
                    .filter(e -> e.getA0_NAME().equals(pmCountry))
                    // .sorted(Comparator.comparing(o -> o.getItem().getValue()))
                    .toList();
        }
    }
}
