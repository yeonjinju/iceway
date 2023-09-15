package com.delivery.iceway.status;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.iceway.domain.Delivery;
import com.delivery.iceway.domain.Recall;

@Mapper
public interface StatusMapper {
    Delivery getDeliveryById(int orderId);
    List<Delivery> getAllDelivery();
    void recallUpdate(int delivery_id, Timestamp recall_time);
    void insertRecall(Recall recall);
    List<Recall> getRecalls();
    void deleteRecall(int delivery_id);
}
