package com.model;



/**
 * Created by bill on 2/12/18.
 */
public class UserInfo {

    public String userId;
    public String emailId;
    public String cartId;
    public Customer customer;

    public String customerId;
    public String firstName;
    public String lastName;
    public String customerPhone;
    public ShippingAddress shippingAddress;
    public BillingAddress billingAddress;

    public String getUsername() {
        return emailId;
    }
    public UserInfo() {}
    public UserInfo(String userId, String emailId) {
        this.userId = userId;
        this.emailId = emailId;
    }
}
