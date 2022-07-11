package guru.springframework.msscbrewery.web.controller.v2;

import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import guru.springframework.msscbrewery.web.services.v2.BeerServiceV2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

//This validated annotation allows SB to validate controller methods params annotated with javax.constraints
@Validated
@RequestMapping("/api/v2/beer")
@RestController
public class BeerControllerV2 {

    private final BeerServiceV2 beerService;

    public BeerControllerV2(BeerServiceV2 beerService) {
        this.beerService = beerService;
    }

    //This @NotNull annotation validate method params
    @GetMapping({"/{beerId}"})
    public ResponseEntity<BeerDtoV2> getBeer(@NotNull @PathVariable("beerId") UUID beerId) {

        return new ResponseEntity<>(beerService.getBeerById(beerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity handlePost(@Valid @NotNull @RequestBody BeerDtoV2 beerDto) {
        BeerDtoV2 savedDto = beerService.saveNewBeer(beerDto);
        HttpHeaders headers = new HttpHeaders();
        // Usually you want to return the full URL for the recently created resource, not the relative one as such
        //TODO add hostname to url
        headers.add("Location", "/api/v1/beer/" + savedDto.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);

    }

    @PutMapping({"/{beerId}"})
    public ResponseEntity handleUpdate(@PathVariable UUID beerId, @Valid @RequestBody BeerDtoV2 beerDto) {
        beerService.updateBeer(beerId, beerDto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({"/{beerId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable UUID beerId) {
        beerService.deleteById(beerId);
    }
}
