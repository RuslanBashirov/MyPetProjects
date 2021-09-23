package org.bashirov.auction.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "painting")
public class Painting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Size(min=1, max=30, message = "Title length should be from 1 to 30 symbols")
    @Column(name = "title")
    private String title;

    @Size(min=1, max=200, message = "Information length should be from 1 to 200 symbols")
    @Column(name = "information")
    private String information;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "painting", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(name = "image")
    private byte[] image;

    @Min(value=500, message = "min price has to be bigger than 500")
    @Column(name = "best_price")
    private  int bestPrice;

    @Email(message = "should have email form")
    @Column(name = "customer_email")
    private String customerEmail;

    public Painting() {
    }

    public Painting(String title, String information) {
        this.title = title;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        for (int i = 0; i < comments.size(); i++) {
            comments.get(i);
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(int bestPrice) {
        this.bestPrice = bestPrice;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void add(Comment comment){
        if (comments == null) comments = new ArrayList<>();
        comments.add(comment);
        comment.setPainting(this);
    }

    @Override
    public String toString() {
        return "Painting{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", information='" + information + '\'' +
                '}';
    }
}
