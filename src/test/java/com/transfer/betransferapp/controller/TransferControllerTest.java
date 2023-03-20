package com.transfer.betransferapp.controller;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transfer.betransferapp.BeTransferAppApplication;
import com.transfer.betransferapp.dto.ResultingAccount;
import com.transfer.betransferapp.dto.TransferDto;
import com.transfer.betransferapp.entity.AllowanceAccount;
import com.transfer.betransferapp.entity.RestaurantAccount;
import com.transfer.betransferapp.repository.AllowanceAccountRepository;
import com.transfer.betransferapp.repository.RestaurantAccountRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BeTransferAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantAccountRepository restaurantAccountRepository;

    @Autowired
    private AllowanceAccountRepository allowanceAccountRepository;

    @Test
    void test_transfer_to_restaurant_successfully() {

        AllowanceAccount allowanceAccount = createAllowanceAccount("1", new BigDecimal("10"));
        RestaurantAccount restaurantAccount = createRestaurantAccount("1", new BigDecimal("5"));

        HttpEntity<String> httpEntity = new HttpEntity<>(getTransferDtoBody(allowanceAccount.getAccountNumber(), restaurantAccount.getAccountNumber(), BigDecimal.TEN), getHeaders());
        String url = "http://localhost:" + port + "/transfer";
        var responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
            ResultingAccount.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResultingAccount resultingAccount = responseEntity.getBody();
        assertThat(resultingAccount).isNotNull();
        assertThat(resultingAccount.getRestaurantAccountDto().getAmount()).isEqualTo(new BigDecimal("15.00"));
        assertThat(resultingAccount.getAllowanceAccountDto().getAmount()).isEqualTo(new BigDecimal("0.00"));
        assertThat(resultingAccount.getTransferredAmount()).isEqualTo(new BigDecimal("10"));
    }

    @Test
    void test_transfer_to_restaurant_insufficient_funds() {

        AllowanceAccount allowanceAccount = createAllowanceAccount("3", new BigDecimal("10"));
        RestaurantAccount restaurantAccount = createRestaurantAccount("3", new BigDecimal("5"));

        HttpEntity<String> httpEntity = new HttpEntity<>(getTransferDtoBody(allowanceAccount.getAccountNumber(), restaurantAccount.getAccountNumber(), new BigDecimal("8")), getHeaders());
        String url = "http://localhost:" + port + "/transfer";
        var responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
            ResultingAccount.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResultingAccount resultingAccount = responseEntity.getBody();
        assertThat(resultingAccount).isNotNull();
        assertThat(resultingAccount.getRestaurantAccountDto().getAmount()).isEqualTo(new BigDecimal("13.00"));
        assertThat(resultingAccount.getAllowanceAccountDto().getAmount()).isEqualTo(new BigDecimal("2.00"));
        assertThat(resultingAccount.getTransferredAmount()).isEqualTo(new BigDecimal("8"));

        var insufficientResponseEntity = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
            String.class);
        assertThat(insufficientResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertThat(insufficientResponseEntity.getBody()).isEqualTo("Insufficient Funds");
    }


    @Test
    void test_transfer_to_allowance_not_found() {

        HttpEntity<String> httpEntity = new HttpEntity<>(getTransferDtoBody("100", "100", BigDecimal.TEN), getHeaders());
        String url = "http://localhost:" + port + "/transfer";
        var responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
            String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Allowance Account not found");
    }

    @Test
    void test_transfer_to_restaurant_not_found() {

        AllowanceAccount allowanceAccount = createAllowanceAccount("2", new BigDecimal("10"));

        HttpEntity<String> httpEntity = new HttpEntity<>(getTransferDtoBody(allowanceAccount.getAccountNumber(), "100", BigDecimal.TEN), getHeaders());
        String url = "http://localhost:" + port + "/transfer";
        var responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
            String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Restaurant Account not found");
    }

    private AllowanceAccount createAllowanceAccount(String accountNumber, BigDecimal amount) {
        AllowanceAccount allowanceAccount = new AllowanceAccount();
        allowanceAccount.setAccountNumber(accountNumber);
        allowanceAccount.setAmount(amount);
        return allowanceAccountRepository.save(allowanceAccount);
    }

    private RestaurantAccount createRestaurantAccount(String accountNumber, BigDecimal amount) {
        RestaurantAccount restaurantAccount = new RestaurantAccount();
        restaurantAccount.setAccountNumber(accountNumber);
        restaurantAccount.setAmount(amount);
        return restaurantAccountRepository.save(restaurantAccount);
    }

    private String getTransferDtoBody(String allowanceAccountNumber,String restaurantAccountNumber, BigDecimal amount) {
        final TransferDto transferDto = new TransferDto();
        transferDto.setAllowanceAccountNumber(allowanceAccountNumber);
        transferDto.setRestaurantAccountNumber(restaurantAccountNumber);
        transferDto.setAmount(amount);
        try {
            return objectMapper.writeValueAsString(transferDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

}
