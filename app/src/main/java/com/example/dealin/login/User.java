package com.example.dealin.login;

public class User {

 private int userid;
 private int usertype;
 private int collegeid;



 private int isActive;
 private long mobile;
 private String name,email,password;

   public  User()
    {

    }

    public User(int userid,String name,String password,String email, int collegeid,long mobile,int usertype)
    {
        this.userid=userid;
        this.name=name;
        this.password=password;
        this.email=email;
        this.collegeid=collegeid;
        this.mobile=mobile;
        this.usertype=usertype;
    }

 public int getUserid() {
  return userid;
 }

 public void setUserid(int userid) {
  this.userid = userid;
 }

 public int getUsertype() {
  return usertype;
 }

 public void setUsertype(int usertype) {
  this.usertype = usertype;
 }

 public int getCollegeid() {
  return collegeid;
 }

 public void setCollegeid(int collegeid) {
  this.collegeid = collegeid;
 }

 public long getMobile() {
  return mobile;
 }

 public void setMobile(long mobile) {
  this.mobile = mobile;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }
 public int getIsActive() {
  return isActive;
 }

 public void setIsActive(int isActive) {
  this.isActive = isActive;
 }
}
