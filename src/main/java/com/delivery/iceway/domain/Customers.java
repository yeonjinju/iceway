package com.delivery.iceway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customers {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
}
