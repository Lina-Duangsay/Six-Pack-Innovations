package com.nashss.se.musicplaylistservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.musicplaylistservice.dynamodb.models.Order;
import com.nashss.se.musicplaylistservice.exceptions.OrderNotFoundException;
import com.nashss.se.musicplaylistservice.metrics.MetricsConstants;
import com.nashss.se.musicplaylistservice.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrderDaoTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;

    private OrderDao orderDao;


    @BeforeEach
    public void setup() {
        initMocks(this);
        orderDao = new OrderDao(dynamoDBMapper, metricsPublisher);
    }

    @Test
    public void getOrder_withOrderId_callsMapperWithPartitionKey() {
        // GIVEN
        String orderId = "orderId";
        when(dynamoDBMapper.load(Order.class, orderId)).thenReturn(new Order("4321", "troys toddlers tower", List.of("apple juice", "water"), BigDecimal.valueOf(50)));

        // WHEN
        Order order = orderDao.getOrder(orderId);

        // THEN
        assertNotNull(order);
        verify(dynamoDBMapper).load(Order.class, orderId);
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETPLAYLIST_PLAYLISTNOTFOUND_COUNT), anyDouble());
    }

    @Test
    public void getOrder_badOrderId_throwsOrderNotFoundException() {
        // GIVEN
        String badOrderId = "NotReal";
        when(dynamoDBMapper.load(Order.class, badOrderId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(OrderNotFoundException.class, () -> orderDao.getOrder(badOrderId));
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETPLAYLIST_PLAYLISTNOTFOUND_COUNT), anyDouble());
    }

    @Test
    public void saveOrder_callsMapperWithPlaylist() {
        // GIVEN
        Order order = new Order("1234", "linas legume bar", List.of("meow", "bark"), BigDecimal.valueOf(50));

        // WHEN
        Order result = orderDao.saveOrder(order);

        // THEN
        verify(dynamoDBMapper).save(order);
        assertEquals(order, result);
    }

}
