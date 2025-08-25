package com.example.bjjm.dto.response.map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private String menuName;
    private String price;
}