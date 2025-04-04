package com.ctflearn.utils;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserLogin {

    @Getter
    private String username;
    @Getter
    private String password;

    private static final String USERS_FILE = "users.properties";
    private static Map<Integer, String[]> userMap = new HashMap<>();

    public UserLogin(int userNumber) {
        loadUsers();
        setUserInfo(userNumber);
    }

    public static void main(String[] args) {
        UserLogin userLogin = new UserLogin(1);
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            int userIndex = 1;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 2) {
                    userMap.put(userIndex++, new String[]{userData[0], userData[1]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUserInfo(int userNumber) {
        if (userMap.containsKey(userNumber)) {
            String[] userInfo = userMap.get(userNumber);
            this.username = userInfo[0];
            this.password = userInfo[1];
            System.out.println("Logging in with username: " + username + " and password: " + password);
        } else {
            System.out.println("User not found for userNumber: " + userNumber);
        }
    }

}
