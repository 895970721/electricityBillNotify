package org.example.service.network.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.service.network.dto.FamilyElectricityBalanceDTO;
import org.example.service.network.properties.ElectricityBillAccount;
import org.example.service.network.properties.ElectricityBillAccountProperties;
import org.example.service.network.response.ElectricityBillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NetworkService {

    @Autowired
    private ElectricityBillAccountProperties electricityBillAccountProperties;

    public List<FamilyElectricityBalanceDTO> getElectricityBill() {
        List<FamilyElectricityBalanceDTO> result = new ArrayList<>();

        List<ElectricityBillAccount> accountList = electricityBillAccountProperties.getAccountList();
        for (ElectricityBillAccount account : accountList) {
            Long supplierId = account.getSupplierId();
            String accountNo = account.getAccountNo();
            String toEmail = account.getToEmail();
            double balance = getElectricityBillBalance(supplierId, accountNo);

            FamilyElectricityBalanceDTO dto = new FamilyElectricityBalanceDTO();
            dto.setToEmail(toEmail);
            dto.setBalance(balance);
            result.add(dto);
        }
        return result;
    }

    public double getElectricityBillBalance(Long supplierId, String accountNo) {
        //创建一个HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建一个Uri对象
        URIBuilder builder = null;
        try {
            builder = new URIBuilder("https://app.innoveronline.com/innover-api/api/weixin/meter/balance/query");
            builder.addParameter("supplierId",String.valueOf(supplierId));
            builder.addParameter("accountNo",accountNo);
            HttpGet get = new HttpGet(builder.build());
            //执行请求
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity,"utf-8");
            JSONObject jsonObject = JSONObject.parseObject(content);
            ElectricityBillResponse billResponse = JSON.toJavaObject(jsonObject, ElectricityBillResponse.class);
            BigDecimal balance = (BigDecimal) billResponse.getBody().get("Balance");
            return balance.doubleValue();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
