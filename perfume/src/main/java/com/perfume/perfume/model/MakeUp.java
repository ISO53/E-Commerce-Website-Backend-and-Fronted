package com.perfume.perfume.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author ISO53
 */
@Entity
@Table(name = "make_up")
public class MakeUp extends Product implements Serializable {

    public MakeUp() {
        super();
    }

    public MakeUp(Integer ID, String name, String brand, Integer price, Double discount, Integer date, String information, String image, Boolean is_pubished) {
        super(ID, name, brand, price, discount, date, information, image, is_pubished);
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
}
