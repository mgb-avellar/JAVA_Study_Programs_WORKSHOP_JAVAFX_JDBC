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

    private Integer id;
    private String name;
    private String email;
    private Date birthDate;
    private Double baseSalary;

    private Department department;

    public Seller() {

    }

    public Seller(Integer sellerID, String sellerName, String sellerEmail, Date sellerBirthDate, Double sellerBaseSalary, Department sellerDepartment) {
        this.id = sellerID;
        this.name = sellerName;
        this.email = sellerEmail;
        this.birthDate = sellerBirthDate;
        this.baseSalary = sellerBaseSalary;
        this.department = sellerDepartment;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return id.equals(seller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Seller [" +
                "sellerID: " + id +
                ", sellerName: '" + name + '\'' +
                ", sellerEmail: '" + email + '\'' +
                ", sellerBirthDate: " + birthDate +
                ", sellerBaseSalary: " + baseSalary +
                ", sellerDepartment: " + department +
                ']';
    }
}
