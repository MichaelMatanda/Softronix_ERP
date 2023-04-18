package com.ERP.Softronix_ERP.model;

import javax.persistence.*;
import java.util.Date;
@Entity
public class Receipt {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;
   private String receiptNumber;
   private Date date;
   private double amount;
   private String payMethod;
   private String customerInformation;
   private String serviceDetails;
   private double tax;
   private double change;

   public Receipt() {
   }

   public Receipt(Long id, String receiptNumber,
                  Date date,
                  double amount,
                  String payMethod,
                  String customerInformation,
                  String serviceDetails, double tax, double change) {
      this.id = id;
      this.receiptNumber = receiptNumber;
      this.date = date;
      this.amount = amount;
      this.payMethod = payMethod;
      this.customerInformation = customerInformation;
      this.serviceDetails = serviceDetails;
      this.tax = tax;
      this.change = change;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getReceiptNumber() {
      return receiptNumber;
   }

   public void setReceiptNumber(String receiptNumber) {
      this.receiptNumber = receiptNumber;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public double getAmount() {
      return amount;
   }

   public void setAmount(double amount) {
      this.amount = amount;
   }

   public String getPayMethod() {
      return payMethod;
   }

   public void setPayMethod(String payMethod) {
      this.payMethod = payMethod;
   }

   public String getCustomerInformation() {
      return customerInformation;
   }

   public void setCustomerInformation(String customerInformation) {
      this.customerInformation = customerInformation;
   }

   public String getServiceDetails() {
      return serviceDetails;
   }

   public void setServiceDetails(String serviceDetails) {
      this.serviceDetails = serviceDetails;
   }

   public double getTax() {
      return tax;
   }

   public void setTax(double tax) {
      this.tax = tax;
   }

   public double getChange() {
      return change;
   }

   public void setChange(double change) {
      this.change = change;
   }

   @Override
   public String toString() {
      return "Receipt{" +
              "id=" + id +
              ", receiptNumber='" + receiptNumber + '\'' +
              ", date=" + date +
              ", amount=" + amount +
              ", payMethod='" + payMethod + '\'' +
              ", customerInformation='" + customerInformation + '\'' +
              ", serviceDetails='" + serviceDetails + '\'' +
              ", tax=" + tax +
              ", change=" + change +
              '}';
   }
}
