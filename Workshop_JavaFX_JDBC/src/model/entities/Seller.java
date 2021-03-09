package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Seller implements Serializable {

    /*
    O serializale indica que nossos objetos podem ser transformados em sequências de bytes
    para que eles possam ser gravados em arquivos, trafegar em rede, etc.
     */

    private static final long serialVersionUID = 1L;

    private Integer sellerID;
    private String sellerName;
    private String sellerEmail;
    private Date sellerBirthDate;
    private Double sellerBaseSalary;

    private Department sellerDepartment;

    public Seller() {

    }

    public Seller(Integer sellerID, String sellerName, String sellerEmail, Date sellerBirthDate, Double sellerBaseSalary, Department sellerDepartment) {
        this.sellerID = sellerID;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.sellerBirthDate = sellerBirthDate;
        this.sellerBaseSalary = sellerBaseSalary;
        this.sellerDepartment = sellerDepartment;
    }

    public Integer getSellerID() {
        return sellerID;
    }

    public void setSellerID(Integer sellerID) {
        this.sellerID = sellerID;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public Date getSellerBirthDate() {
        return sellerBirthDate;
    }

    public void setSellerBirthDate(Date sellerBirthDate) {
        this.sellerBirthDate = sellerBirthDate;
    }

    public Double getSellerBaseSalary() {
        return sellerBaseSalary;
    }

    public void setSellerBaseSalary(Double sellerBaseSalary) {
        this.sellerBaseSalary = sellerBaseSalary;
    }

    public Department getSellerDepartment() {
        return sellerDepartment;
    }

    public void setSellerDepartment(Department sellerDepartment) {
        this.sellerDepartment = sellerDepartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return sellerID.equals(seller.sellerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerID);
    }

    @Override
    public String toString() {
        return "Seller [" +
                "sellerID: " + sellerID +
                ", sellerName: '" + sellerName + '\'' +
                ", sellerEmail: '" + sellerEmail + '\'' +
                ", sellerBirthDate: " + sellerBirthDate +
                ", sellerBaseSalary: " + sellerBaseSalary +
                ", sellerDepartment: " + sellerDepartment +
                ']';
    }
}
