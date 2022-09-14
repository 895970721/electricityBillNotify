package org.example.service.network.response;

import lombok.Data;

import java.util.Map;

@Data
public class ElectricityBillResponse {
    private String code;
    private String msg;
    private Map body;
}
