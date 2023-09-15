package com.delivery.iceway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    private int id;
    private String name;
    private int price;
    private int temperature;
}
