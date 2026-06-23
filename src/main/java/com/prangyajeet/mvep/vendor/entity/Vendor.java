package com.prangyajeet.mvep.vendor.entity;

import com.prangyajeet.mvep.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String businessName;

    private String businessAddress;

    private String gstNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Vendor() {
    }

    public Vendor(Long id, String businessName,
                  String businessAddress,
                  String gstNumber,
                  User user) {
        this.id = id;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.gstNumber = gstNumber;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}