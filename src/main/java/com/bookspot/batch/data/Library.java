package com.bookspot.batch.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;

@Entity
@Getter
@ToString
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libraryCode;
    private String name;
    private String address;
    private String contactNumber;
    private Point location;
    private String homePage;
    private String closedInfo;
    private String operatingInfo;

    private LocalDate updatedAt;
    private LocalDate stockUpdatedAt;

    public Library(String libraryCode, String name, String address, String contactNumber, Point location, String homePage, String closedInfo, String operatingInfo) {
        this.libraryCode = libraryCode;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.location = location;
        this.homePage = homePage;
        this.closedInfo = closedInfo;
        this.operatingInfo = operatingInfo;
    }

    public double getLatitude() {
        return location.getY();
    }

    public double getLongitude() {
        return location.getX();
    }
}
