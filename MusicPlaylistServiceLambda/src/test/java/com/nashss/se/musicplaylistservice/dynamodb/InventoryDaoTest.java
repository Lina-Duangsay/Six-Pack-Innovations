package com.nashss.se.musicplaylistservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.musicplaylistservice.dynamodb.models.AlbumTrack;
import com.nashss.se.musicplaylistservice.exceptions.AlbumTrackNotFoundException;
import com.nashss.se.musicplaylistservice.exceptions.BeerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class InventoryDaoTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private InventoryDao inventoryDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
        inventoryDao = new InventoryDao(dynamoDBMapper);
    }

    @Test
    void getBeer_withExistentbeerIdAndPackageType_loadsAlbumTrackByPartitionAndSortKeys() {
        // GIVEN
        String beerId = "beer123";
        String packageType = "KEG";
        Beer beer = new Beer();
        beer.setBeerId(beerId);
        beer.setPackageType(packageType);
        when(dynamoDBMapper.load(Beer.class, beerId, packageType)).thenReturn(beer);

        // WHEN
        Beer result = inventoryDao.getBeer(beerId, packageType);

        // THEN
        verify(dynamoDBMapper).load(Beer.class, beerId, packageType);
        assertEquals(beer, result,
                String.format("Expected to receive Beer returned by DDB (%s), but received %s",
                        beer.getBeerId, result.getBeerId));
    }

    @Test
    void getBeer_withNonexistentBeerIdOrPackageType_throwsException() {
        // GIVEN
        String beerId = "NONbeerId";
        String packageType = "NONpackage";

        // also part of THEN - expect this mock call
        when(dynamoDBMapper.load(Beer.class, beerId, packageType)).thenReturn(null);

        // WHEN + THEN
        assertThrows(BeerNotFoundException.class, () -> inventoryDao.getBeer(beerId, packageType));
    }
}
