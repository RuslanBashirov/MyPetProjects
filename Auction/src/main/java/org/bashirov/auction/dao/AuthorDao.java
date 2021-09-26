package org.bashirov.auction.dao;

import org.bashirov.auction.entity.Author;
import org.bashirov.auction.entity.Painting;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AuthorDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public List<Author> getListOfAuthors(){
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Author> theQuery = currentSession.createQuery(
                "from Author ", Author.class);

        List<Author> authors = theQuery.getResultList();
        return authors;
    }

    @Transactional
    public Author getAuthor(Integer authorId){
        Session currentSession = sessionFactory.getCurrentSession();
        Author author = currentSession.get(Author.class, authorId);

        return author;
    }

    @Transactional
    public void save(Author author){
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(author);
    }

    @Transactional
    public void update(int authorId, Author updatedAuthor){
        Session currentSession = sessionFactory.getCurrentSession();

        Author author = currentSession.get(Author.class, authorId);

        author.setFirstName(updatedAuthor.getFirstName());
        author.setLastName(updatedAuthor.getLastName());
        author.setEmail(updatedAuthor.getEmail());

        currentSession.update("author", author);
    }

    @Transactional
    public void delete(int authorId){
        Session currentSession = sessionFactory.getCurrentSession();
        Author author = this.getAuthor(authorId);
        currentSession.remove(author);
    }
}
