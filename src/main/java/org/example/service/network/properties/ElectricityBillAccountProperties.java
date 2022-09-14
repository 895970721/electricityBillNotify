package org.example.service.network.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "bill.electricity")
public class ElectricityBillAccountProperties {
    List<ElectricityBillAccount> accountList;
}
