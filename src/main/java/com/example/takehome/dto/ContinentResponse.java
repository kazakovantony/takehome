package com.example.takehome.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ContinentResponse {
    List<ContinentWrapper> continent;
    String msg;
}
