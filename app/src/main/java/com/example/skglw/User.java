package com.example.skglw;
import java.util.ArrayList;

public class User {

    private static String token;
    private static String password,login;
    public static String name;

    private static ArrayList<Pay> payList= new ArrayList<Pay>();
    private static ArrayList<Check> checkList= new ArrayList<Check>();

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) { User.token = token; }

    public static void dropToken() {
        User.token = null;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        User.login = login;
    }

    public static void setPayList(ArrayList<Pay> list) {  payList = list;}

    protected static ArrayList<Pay> getPayList() { return payList;}

    protected static void setCheckList(ArrayList<Check> list) { checkList = list;}

    protected static ArrayList<Check> getCheckList() { return checkList;}

    protected static void clearCheckList() {  checkList.clear();}

    public static class Check {
        public int image;
        public String number, card, balance;
        public Boolean blocked;

        public Check(){}
        /*public Check( String number,String card, String balance) {
            this.number = number;
            this.card = card;
            this.balance = balance;
        }*/
    }

    public static class Pay {
        private String name, check;
        public Pay( String name, String check) {
            this.name = name;
            this.check =check;
        }
        public String getCheck() {
            return check;
        }
        public String getName() {
            return name;
        }
        /*public void setName(String name) {
            this.name = name;
        }*/
    }
}
