package com.delivery.iceway.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    private int id;
    private String customers_name;
    private String product_name;
    private double latitude;
    private double longitude;
    private int temperature;
    private boolean delivery_status;
    private boolean recall_status;
    private Timestamp delivery_time;
    private Timestamp arrival_time;
    private Timestamp recall_time;
    private String recall_id;
    private String recall_name;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteCoordinates {
        private double arrivalLatitude;
        private double arrivalLongitude;
    }
}
