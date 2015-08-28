import models.Comment;
import models.User;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.db.jpa.GenericModel.JPAQuery;
//import antlr.collections.List;
import play.test.Fixtures;
import play.test.UnitTest;

public class CommentTest extends UnitTest {
  private User bob, john;
  private Comment comment1, comment2, comment3, comment4, comment5;

  @BeforeClass
  public static void loadDB() {
    Fixtures.deleteAllModels();
  }

  @Before
  public void setup() {
    bob = new User("bob", "jones", "bob@jones.com", 20, "irish", "secret", true, true);
    bob.save();

    john = new User("john", "jay", "john@jay.ie", 25, "irish", "secret", true, true);
    john.save();
    
    comment1 = new Comment(bob,"title","First content");
    comment1.save();
    comment2 = new Comment(bob,"newest bob comment","And it's most recent text");
    comment2.save();
    comment3 = new Comment(bob,"Last","Your argument is invalid");
    comment3.save();
    
    comment4 = new Comment(john, "Are you", "You must be always first");
    comment4.save();
    
    comment5 = new Comment(john, "Spam", "And keep replaying to everything.");
    comment5.save();
    
  }

  @After
  public void teardown() {
    comment1.delete();
    comment2.delete();
    comment3.delete();
    bob.delete();
  }

  @Test
  public void testCommentSize() {
    assertEquals(5, Comment.count());
  }
  
  @Test
  public void testCommentCreate() {    
    assertNotNull(Comment.findAll().get(0));
  }

  @Test
  public void testFindComment() {
//    List<Comment> foundComment = Comment.find("subject", "Last").fetch();
//    assertNotNull(foundComment.get(0));
    Comment foundComment = Comment.find("subject", "Last").first();
    assertNotNull(foundComment);
  }
  
  @Test
  public void testFindCommentByUser() {
    assertEquals(3, Comment.find("user",bob).fetch().size());

    assertEquals(2, Comment.find("user",john).fetch().size());
  }
  
  @Test
  public void testTimestamps() {
    /*
     * or equal required, because the ms part of timestamp on many machines is not 
     * relaiable and even if it wasn't some fast machines could be executing it at 
     * same ms.  
     */
    assertTrue(comment1.time<=System.currentTimeMillis());
    assertTrue(comment1.time<=comment2.time);
  }

  
}
