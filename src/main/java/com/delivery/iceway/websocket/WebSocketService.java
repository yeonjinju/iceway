package com.delivery.iceway.websocket;

import java.util.List;

import com.delivery.iceway.domain.Delivery;
import com.delivery.iceway.domain.Recall;

public interface WebSocketService {
    void notifyDashboardUpdate(List<Delivery> deliveryList);
    void recallsUpdate(List<Recall> recalls);
}
