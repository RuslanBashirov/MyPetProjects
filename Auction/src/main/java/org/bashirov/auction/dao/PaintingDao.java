package org.bashirov.auction.dao;

import org.bashirov.auction.entity.Painting;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.bashirov.auction.entity.Author;

import java.util.List;

@Component
public class PaintingDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public List<Painting> getListOfPaintings(){
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Painting> theQuery = currentSession.createQuery(
                "from Painting", Painting.class);

        List<Painting> paintings = theQuery.getResultList();
        return paintings;
    }

    @Transactional
    public Painting getPainting(Integer paintingId){
        Session currentSession = sessionFactory.getCurrentSession();

        Painting painting = currentSession.get(Painting.class, paintingId);
        return painting;
    }

    @Transactional
    public void save(Painting painting){
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(painting);
    }

    @Transactional
    public void update(int paintingId, Painting updatedPainting){
        Session currentSession = sessionFactory.getCurrentSession();

        Painting painting = currentSession.get(Painting.class, paintingId);

        painting.setTitle(updatedPainting.getTitle());
        painting.setInformation(updatedPainting.getInformation());

        currentSession.update("painting", painting);
    }

    @Transactional
    public void delete(int paintingId){
        Session currentSession = sessionFactory.getCurrentSession();
        Painting painting = this.getPainting(paintingId);
        currentSession.remove(painting);
    }

    @Transactional
    public Author getAuthor(int authorId){
        Session currentSession = sessionFactory.getCurrentSession();

        Author author = currentSession.get(Author.class, authorId);
        return author;
    }
}
