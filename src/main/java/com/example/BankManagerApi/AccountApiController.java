package com.example.BankManagerApi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountApiController {
    private static Map<String, Account> accountRepo = new HashMap<>();
//    @RequestMapping("/")
//    String home() {
//        return "Hello World!";
//    }

//    @RequestMapping(value = "/account/{accountID}")
//    public ResponseEntity<Account> getAccount(@PathVariable("accountID") String accountID) {
//        Account account = new DBAccounts().getAccount(accountID);
//        return new ResponseEntity<>(account, HttpStatus.OK);
//    }
//

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody String accountID) {
        String password = new DBAccounts().getPassword(accountID);
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    @RequestMapping(value = "/loginsuccess", method = RequestMethod.POST)
    public ResponseEntity<String> loginSuccess(@RequestBody ArrayList<String> accountStrArrList) {
        System.out.println(accountStrArrList);
        String accountID = accountStrArrList.get(0);
        String uuid = accountStrArrList.get(1);
        String uuidDB = new DBAccounts().getUUID(uuid);
        //Compare uuid with uuidDB. if uuid exist in DB then delete and new insert, otherwise insert
        if(uuidDB.equals(uuid)) {
            //Delete old uuid  in DB
            new DBAccounts().deleteUUID(uuid);
            //Insert new uuid and account
            new DBAccounts().insertUUID(accountID, uuid);
        }else{
            new DBAccounts().insertUUID(accountID, uuid);
        }
        return new ResponseEntity<>("Autologin is created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/autologin", method = RequestMethod.POST)
    public ResponseEntity<String> autologin(@RequestBody ArrayList<String> accountStrArrList) {
        System.out.println(accountStrArrList);
        String accountID = accountStrArrList.get(0);
        String uuid = accountStrArrList.get(1);
        String uuidDB = new DBAccounts().getUUID(uuid);
        String accountIDDB = new DBAccounts().getAccountIDFromUUIDs(accountID);
        //Compare uuid with uuidDB. if uuid exist in DB then delete and new insert, otherwise insert
        if(uuidDB.equals(uuid) && accountIDDB.equals(accountID)) {
            //Delete old uuid  in DB
            new DBAccounts().deleteUUID(uuid);
            //Insert new uuid and account
            new DBAccounts().insertUUID(accountID, uuid);
        }else{
            return new ResponseEntity<>("AutoLoginFailed", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("AutoLoginSuccess", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public ResponseEntity<Account> getAccount(@RequestBody String accountID) {
        Account account = new DBAccounts().getAccount(accountID);
        System.out.println(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/editprofile", method = RequestMethod.POST)
    public ResponseEntity<String> editProfile(@RequestBody ArrayList<String> accountStrArrList) {
        String accountID = accountStrArrList.get(0);
        String query = accountStrArrList.get(1);
        boolean b = new DBAccounts().updateAccountInfomation(accountID, query);
        if(b){
            return new ResponseEntity<>("EditProfileSuccess", HttpStatus.OK);
        }
        return new ResponseEntity<>("EditProfileFailed", HttpStatus.OK);
    }

    @RequestMapping(value = "/transfermoney", method = RequestMethod.POST)
    public ResponseEntity<Account> transferMoney(@RequestBody ArrayList<String> accountStrArrList) {
        String accountID = accountStrArrList.get(0);
        String accountID2 = accountStrArrList.get(1);
        String moneyTransfer = accountStrArrList.get(2);
        if(accountID2.equals(new DBAccounts().getAccountID(accountID2))){
            boolean b = new DBAccounts().updateTransferAccountBalance(accountID,accountID2,moneyTransfer);
            if (b) {
                Account account = new DBAccounts().getAccount(accountID);
                System.out.println(account);
                return new ResponseEntity<>(account, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null ,HttpStatus.FAILED_DEPENDENCY);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
