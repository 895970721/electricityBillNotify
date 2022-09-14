package org.example.controller;

import org.example.service.mail.MailService;
import org.example.service.network.impl.NetworkService;
import org.example.service.network.dto.FamilyElectricityBalanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BillController {

    @Autowired
    NetworkService networkService;

    @Autowired
    private MailService mailService;

    @Value("${mail.from}")
    private String mailFrom;

    @RequestMapping(value = "/queryBill")
    public String queryBill() {
        List<FamilyElectricityBalanceDTO> electricityBill = networkService.getElectricityBill();
        for (FamilyElectricityBalanceDTO dto : electricityBill) {
            String subject = "煤气费低于阈值提醒";
            String content = "剩余煤气费【${balance}】,请知晓";
            content = content.replace("${balance}", String.valueOf(dto.getBalance()));
            mailService.sendSimpleMessage(subject, content, mailFrom, new String[]{dto.getToEmail()});
        }
        return "ok";
    }
}
