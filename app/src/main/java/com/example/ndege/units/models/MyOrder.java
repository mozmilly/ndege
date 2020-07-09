package com.example.ndege.units.models;

public class MyOrder implements java.io.Serializable {
    private static final long serialVersionUID = -7693966022475247322L;
    private MyOrderUnit unit;
    private double price;
    private double service_fee;
    private String ref_code;
    private String time;
    private double transportation_fee;
    private String order_name;
    private String status;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyOrderUnit getUnit() {
        return this.unit;
    }

    public void setUnit(MyOrderUnit unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getService_fee() {
        return this.service_fee;
    }

    public void setService_fee(double service_fee) {
        this.service_fee = service_fee;
    }

    public String getRef_code() {
        return this.ref_code;
    }

    public void setRef_code(String ref_code) {
        this.ref_code = ref_code;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTransportation_fee() {
        return this.transportation_fee;
    }

    public void setTransportation_fee(double transportation_fee) {
        this.transportation_fee = transportation_fee;
    }

    public String getOrder_name() {
        return this.order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MyOrder{" +
                "unit=" + unit +
                ", price=" + price +
                ", service_fee=" + service_fee +
                ", ref_code='" + ref_code + '\'' +
                ", time='" + time + '\'' +
                ", transportation_fee=" + transportation_fee +
                ", order_name='" + order_name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
