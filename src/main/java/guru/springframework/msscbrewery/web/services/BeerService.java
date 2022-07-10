package guru.springframework.msscbrewery.web.services;

import guru.springframework.msscbrewery.web.model.BeerDto;

import java.util.UUID;

public interface BeerService {
    BeerDto getBeerById(UUID beerId);
    void updateBeer(UUID beerId, BeerDto beerDto);
    BeerDto save(BeerDto beerDto);
    void deleteById(UUID beerId);
}
