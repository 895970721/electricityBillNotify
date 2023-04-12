package org.example.schedule;

import lombok.extern.slf4j.Slf4j;
import org.example.service.mail.MailService;
import org.example.service.network.impl.NetworkService;
import org.example.service.network.dto.FamilyElectricityBalanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class BillSchedule {

    @Autowired
    NetworkService networkService;

    @Autowired
    private MailService mailService;

    @Value("${mail.from}")
    private String mailFrom;

    /**
     * 快到家提醒
     */
    @Scheduled(cron = "0 0 20 * * ?")
    public void billCompute() {
        System.out.println(3);
        List<FamilyElectricityBalanceDTO> electricityBill = networkService.getElectricityBill();
        for (FamilyElectricityBalanceDTO dto : electricityBill) {
            String subject = "煤气费低于阈值提醒";
            String content = "剩余煤气费【${balance}】,请知晓";
            content = content.replace("${balance}", String.valueOf(dto.getBalance()));
            mailService.sendSimpleMessage(subject, content, mailFrom, new String[]{dto.getToEmail()});
        }
    }
}
