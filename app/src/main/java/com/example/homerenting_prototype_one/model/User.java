package com.example.homerenting_prototype_one.model;

public class User {
    private String id;
    private String company_id;
    private String name;
    private String email;
    private String phone;
    private String token;
    private String title;
    private String verifyCode;

//    public User(String id, String company_id, String name, String phone){
//        setName(name);
//        setGender(gender);
//        setPhone(phone);
//        setAddress(address);
//    }

    public String getId(){ return id;}
    public void setId(String id){ this.id = id;}

    public String getCompany_id() { return company_id;}
    public void setCompany_id(String company_id){ this.company_id = company_id;}

    public String getName(){ return name;}
    public void setName(String name){ this.name = name;}

    public String getEmail(){ return email;}
    public void setEmail(String email){ this.email = email;}

    public String getPhone(){ return phone;}
    public void setPhone(String phone){ this.phone = phone;}

    public String getToken(){ return token;}
    public void setToken(String token){ this.token = token;}

    public String getVerifyCode(){return verifyCode; }
    public void setVerifyCode(String code){this.verifyCode = code;}

    public String getTitle(){return title;}
    public void setTitle(String title){this.title = title;}
}