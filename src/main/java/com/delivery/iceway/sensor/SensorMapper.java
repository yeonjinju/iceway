package com.delivery.iceway.sensor;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.iceway.domain.Customers;
import com.delivery.iceway.domain.Delivery;
import com.delivery.iceway.domain.Products;

@Mapper
public interface SensorMapper {
    Customers getDeliveryCoordinates(int id);
    void insertDelivery(Delivery delivery);
    void updateDelivery(Delivery delivery);
    void endDelivery(Delivery delivery);
    Optional<Boolean> getRecallStatus(int id);
    Products getProduct(int id);
    void resetDelivery();
    void resetRecall();
}
