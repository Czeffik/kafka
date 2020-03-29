package com.trzewik.kafka.domain.translation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//todo domains object should have final fields for easier debugging - should be moved to DTO
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Information {
    private String name;
    private String description;
}
