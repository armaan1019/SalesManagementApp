/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.generalproject;

import java.time.LocalDate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author armaansharma
 */
public interface SalesInterface {
    public int getQuarter();
    public IntegerProperty quarterProperty();
    public void setQuarter(int quarter);
    public String getRegion();
    public StringProperty regionProperty();
    public void setRegion(String region);
    public LocalDate getDate();
    public ObjectProperty<LocalDate> dateProperty();
    public void setDate(LocalDate dateOfBirth);
    public double getAmount();
    public DoubleProperty amountProperty();
    public void setAmount(double amount);
}
