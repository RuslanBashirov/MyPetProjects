package org.bashirov.auction.dao;

import org.bashirov.auction.entity.Painting;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.bashirov.auction.entity.Author;

import java.util.List;

@Component
public class PaintingDao {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Painting> getListOfPaintings(){
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Painting> theQuery = currentSession.createQuery(
                "from Painting", Painting.class);

        List<Painting> paintings = theQuery.getResultList();
        return paintings;
    }

    public Painting getPainting(Integer paintingId){
        Session currentSession = sessionFactory.getCurrentSession();

        Painting painting = currentSession.get(Painting.class, paintingId);
        return painting;
    }

    public void save(Painting painting){
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(painting);
    }

    public void update(int paintingId, Painting updatedPainting){
        Session currentSession = sessionFactory.getCurrentSession();

        Painting painting = currentSession.get(Painting.class, paintingId);

        painting.setTitle(updatedPainting.getTitle());
        painting.setInformation(updatedPainting.getInformation());

        currentSession.update("painting", painting);
    }

    public void delete(int paintingId){
        Session currentSession = sessionFactory.getCurrentSession();
        Painting painting = this.getPainting(paintingId);
        currentSession.remove(painting);
    }

    public Author getAuthor(int authorId){
        Session currentSession = sessionFactory.getCurrentSession();

        Author author = currentSession.get(Author.class, authorId);
        return author;
    }
}
