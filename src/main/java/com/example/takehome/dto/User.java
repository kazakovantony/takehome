package com.example.takehome.dto;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Data;

@Data
public class User {
    AtomicInteger requestCount;
    AtomicLong lastRequestTime;
}
