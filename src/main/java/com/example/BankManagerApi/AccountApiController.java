package com.example.BankManagerApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountApiController {
    private static Map<String, Account> accountRepo = new HashMap<>();
//    @RequestMapping("/")
//    String home() {
//        return "Hello World!";
//    }
    //asdasdsd
    //asdsads
    @RequestMapping(value = "/account/{accountID}")
    public ResponseEntity<Account> getProduct(@PathVariable("accountID") String accountID) {
        Account account = new DBAccounts().getAccount(accountID);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}
