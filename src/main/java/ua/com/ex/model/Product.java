package ua.com.ex.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "PRODUCTS")
public class Product implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id @Column(name = "ID")
	private int id;	

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "DISCOUNT")
    private int discount;		

    @Column(name = "PARENT_CATEGORY")
    private int categoryId;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "SIZE")
    private String size;

    @Transient
    private String imageBase64;

    @Transient
    @Override
    public int hashCode() {       
        return id + 31;
    }

    @Transient
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (categoryId != other.categoryId)
            return false;
        if (discount != other.discount)
            return false;
        if (id != other.id)
            return false;
        if (imageBase64 == null) {
            if (other.imageBase64 != null)
                return false;
        } else if (!imageBase64.equals(other.imageBase64))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (quantity != other.quantity)
            return false;
        return true;
    }

}
