package com.perfume.perfume.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author ISO53
 */
@MappedSuperclass
public class Product{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "price")
    private Integer price;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "update_date")
    private Integer date;

    @Column(name = "information")
    private String information;

    @Column(name = "image")
    private String image;
    
    @Column(name = "is_published")
    private Boolean isPublished;
    
    public Product() {
    }

    public Product(Integer ID, String name, String brand, Integer price, Double discount, Integer date, String information, String image, Boolean is_published) {
        this.ID = ID;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.discount = discount;
        this.date = date;
        this.information = information;
        this.image = image;
        this.isPublished = is_published;
    }

    @Override
    public String toString() {
        return "\n ID: " + this.getID()
                + "\n Name: " + this.getName()
                + "\n Brand: " + this.getBrand()
                + "\n Price: " + this.getPrice()
                + "\n Discount: " + this.getDiscount()
                + "\n Date: " + this.getDate()
                + "\n Information: " + this.getInformation()
                + "\n Image: " + this.getImage()
                + "\n Is Published: " + this.getIsPublished();
    }

    
    /**
     * @return the ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return the price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * @return the Date
     */
    public Integer getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Integer date) {
        this.date = date;
    }

    /**
     * @return the information
     */
    public String getInformation() {
        return information;
    }

    /**
     * @param information the information to set
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the is_published
     */
    public Boolean getIsPublished() {
        return isPublished;
    }

    /**
     * @param isPublished the is_published to set
     */
    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

}

