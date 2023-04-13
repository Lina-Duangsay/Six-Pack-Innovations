package com.nashss.se.musicplaylistservice.converters;

import com.nashss.se.musicplaylistservice.dynamodb.models.AlbumTrack;
import com.nashss.se.musicplaylistservice.dynamodb.models.Beer;
import com.nashss.se.musicplaylistservice.models.BeerModel;
import com.nashss.se.musicplaylistservice.models.SongModel;

import java.util.ArrayList;
import java.util.List;

public class ModelConverterSPI {

    public List<BeerModel> toBeerModelList(List<Beer> beerList) {
        List<BeerModel> beerModels = new ArrayList<>();

        for (Beer beerington : beerList) {
            beerModels.add(toBeerModel(beerington));
        }

        return beerModels;
    }

    public BeerModel toBeerModel(Beer beer) {
        return BeerModel.builder()
                .withName(beer.getName())
                .withPackagingType(beer.getPackagingType())
                .withUnitPrice(beer.getUnitPrice())
                .withReservedUnits(beer.getReservedUnits())
                .withAvailableUnits(beer.getAvailableUnits())
                .withBeerType(beer.getBeerType())
                .withBeerId(beer.getBeerId())
                .build();
    }
}