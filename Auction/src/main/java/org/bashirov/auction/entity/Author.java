package org.bashirov.auction.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "first name shouldnt be empty")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "last name shouldnt be empty")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "should have email form")
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "author",
                cascade = CascadeType.ALL)
    private List<Painting> paintings;

    public Author() {
    }

    public Author(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<Painting> getPaintings() {
        for (int i = 0; i < paintings.size(); i++) paintings.get(i);
        return paintings;
    }

    public void setPaintings(List<Painting> paintings) {
        this.paintings = paintings;
    }

    public void add(Painting painting){
        if (paintings == null) paintings = new ArrayList<>();
        paintings.add(painting);
        painting.setAuthor(this);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
