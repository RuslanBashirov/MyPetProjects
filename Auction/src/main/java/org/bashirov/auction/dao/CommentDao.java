package org.bashirov.auction.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.bashirov.auction.entity.Comment;
import org.bashirov.auction.entity.Painting;

import java.util.List;

@Component
public class CommentDao {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Comment> getListOfComments(){
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Comment> theQuery = currentSession.createQuery(
                "from Comment", Comment.class);

        List<Comment> comments = theQuery.getResultList();
        return comments;
    }

    public Comment getComment(int commentId){
        Session currentSession = sessionFactory.getCurrentSession();

        Comment comment = currentSession.get(Comment.class, commentId);
        return comment;
    }

    public void save(Comment comment){
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(comment);
    }

    public void update(int commentId ,Comment updatedComment){
        Session currentSession = sessionFactory.getCurrentSession();
        Comment comment = currentSession.get(Comment.class, commentId);

        comment.setText(updatedComment.getText());

        currentSession.update("comment", comment);
    }

    public void delete(int commentId){
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Comment> theQuery = currentSession.createQuery("delete from Comment " +
                "where id=:commentId");
        theQuery.setParameter("commentId",commentId);
        theQuery.executeUpdate();
    }

    public Painting getPainting(int paintingId){
        Session currentSession = sessionFactory.getCurrentSession();

        Painting painting = currentSession.get(Painting.class, paintingId);
        return painting;
    }
}
