package com.mycompany.generalproject;

import java.time.LocalDate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

 public class Sales implements SalesInterface {
        private final IntegerProperty quarter;
        private final StringProperty region;
        private final ObjectProperty<LocalDate> date;
        private final DoubleProperty amount;

        public Sales(LocalDate date, int quarter, String region, double amount) {
            this.quarter = new SimpleIntegerProperty(quarter);
            this.region = new SimpleStringProperty(region);
            this.date = new SimpleObjectProperty<>(date);
            this.amount = new SimpleDoubleProperty(amount);
        }

        // Getters and setters
        
        @Override
        public int getQuarter() {
            return quarter.get();
        }
        
        @Override
        public IntegerProperty quarterProperty() {
            return quarter;
        }
        
        @Override
        public void setQuarter(int quarter) {
            this.quarter.set(quarter);
        }
        
        @Override
        public String getRegion() {
            return region.get();
        }
        
        @Override
        public StringProperty regionProperty() {
            return region;
        }
        
        @Override
        public void setRegion(String region) {
            this.region.set(region);
        }
        
        @Override
        public LocalDate getDate() {
            return date.get();
        }
        
        @Override
        public ObjectProperty<LocalDate> dateProperty() {
            return date;
        }
        
        @Override
        public void setDate(LocalDate dateOfBirth) {
            this.date.set(dateOfBirth);
        }
        
        @Override
        public double getAmount() {
            return amount.get();
        }
        
        @Override
        public DoubleProperty amountProperty() {
            return amount;
        }
        
        @Override
        public void setAmount(double amount) {
            this.amount.set(amount);
        }
}