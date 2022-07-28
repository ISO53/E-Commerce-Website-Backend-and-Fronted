package com.perfume.perfume.model;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 *
 * @author ISO53
 */
@Entity
@TypeDef(
    name = "list-array",
    typeClass = ListArrayType.class
)
@Table(name = "u_user")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    private Integer ID;

    @Column(name = "name_surname")
    private String name_surname;

    @Column(name = "location")
    private String location;

    @Column(name = "password")
    private String password;

    @Column(name = "mail")
    private String mail;

    @Column(name = "phone_number")
    private String phone_number;

    @Type(type = "list-array")
    @Column(name = "favorites")
    private List<Integer> favorites; // String

    @Type(type = "list-array")
    @Column(name = "shopping_cart")
    private List<Integer> shopping_cart; // String

    public User() {
    }

    public User(Integer ID, String name_surname, String location, String password, String mail, String phone_number, List<Integer> favorites, List<Integer> shopping_cart) {
        this.ID = ID;
        this.name_surname = name_surname;
        this.location = location;
        this.password = password;
        this.mail = mail;
        this.phone_number = phone_number;
        this.favorites = favorites;
        this.shopping_cart = shopping_cart;
    }

    @Override
    public String toString() {
        return "\n ID: " + this.getID()
                + "\n Name-Surname: " + this.getName_surname()
                + "\n Location: " + this.getLocation()
                + "\n Password: " + this.getPassword()
                + "\n Mail: " + this.getMail()
                + "\n Phone-Number: " + this.getPhone_number()
                + "\n Number of Item in Favorites: " + this.getFavorites().size()
                + "\n Number of Item in Shopping-Cart: " + this.getShopping_cart().size();
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
     * @return the name_surname
     */
    public String getName_surname() {
        return name_surname;
    }

    /**
     * @param name_surname the name_surname to set
     */
    public void setName_surname(String name_surname) {
        this.name_surname = name_surname;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return the phone_number
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * @param phone_number the phone_number to set
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    /**
     * @return the favorites
     */
    public List<Integer> getFavorites() {
        return favorites;
    }

    /**
     * @param favorites the favorites to set
     */
    public void setFavorites(List<Integer> favorites) {
        this.favorites = favorites;
    }

    /**
     * @return the shopping_cart
     */
    public List<Integer> getShopping_cart() {
        return shopping_cart;
    }

    /**
     * @param shopping_cart the shopping_cart to set
     */
    public void setShopping_cart(List<Integer> shopping_cart) {
        this.shopping_cart = shopping_cart;
    }

}
