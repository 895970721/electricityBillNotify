package org.example.service.network.properties;

import lombok.Data;

@Data
public class ElectricityBillAccount {

    private Long supplierId;

    private String accountNo;

    private String toEmail;
}
