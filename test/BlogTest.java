import java.util.ArrayList;
import java.util.List;

import models.Comment;
import models.Message;
import models.Post;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BlogTest extends UnitTest {
  private User bob;
  private Post post1, post2;
  private Comment comment1, comment2, comment3;

  @BeforeClass
  public static void loadDB() {
    Fixtures.deleteAllModels();
  }

  @Before
  public void setup() {
    bob = new User("bob", "jones", "bob@jones.com", 20, "irish", "secret", true, true);
    post1 = new Post("Post Title 1", "This is the first post content");
    post2 = new Post("Post Title 2", "This is the second post content");
    bob.save();

    comment1 = new Comment(bob, "title", "First content");
    comment1.save();
    comment2 = new Comment(bob, "newest bob comment", "And it's most recent text");
    comment2.save();
    comment3 = new Comment(bob, "Last", "Your argument is invalid");
    comment3.save();

    post1.save();
    post2.save();
  }

  @After
  public void teardown() {
    comment1.delete();
    comment2.delete();
    comment3.delete();    
    bob.delete();
    post1.delete();
    post2.delete();
  }

  @Test
  public void testCreatePost() {
    bob.posts.add(post1);
    bob.save();

    User user = User.findByEmail("bob@jones.com");
    List<Post> posts = user.posts;
    assertEquals(1, posts.size());
    Post post = posts.get(0);
    assertEquals(post.title, "Post Title 1");
    assertEquals(post.content, "This is the first post content");
  }

  @Test
  public void testCreateMultiplePosts() {
    bob.posts.add(post1);
    bob.posts.add(post2);
    bob.save();

    User user = User.findByEmail("bob@jones.com");
    List<Post> posts = user.posts;
    assertEquals(2, posts.size());
    Post posta = posts.get(0);
    assertEquals(posta.title, "Post Title 1");
    assertEquals(posta.content, "This is the first post content");

    Post postb = posts.get(1);
    assertEquals(postb.title, "Post Title 2");
    assertEquals(postb.content, "This is the second post content");
  }

  @Test
  public void testCommentsOnPosts() {
    post1.comments.add(comment1);
    post1.comments.add(comment2);
    post1.comments.add(comment3);
    
    bob.posts.add(post1);
    bob.posts.add(post2);
    bob.save();
   
    User user = User.findByEmail("bob@jones.com");
    
    assertEquals(3, user.posts.get(0).comments.size());
    assertEquals(0, user.posts.get(1).comments.size());
  }

  @Test
  public void testTimestamps() {
    /*
     * or equal required, because the ms part of timestamp on many machines is not 
     * relaiable and even if it wasn't some fast machines could be executing it at 
     * same ms.  
     */

    assertTrue(post1.time<=System.currentTimeMillis());
    assertTrue(post1.time<=post2.time);
  }
  
  
  @Test
  public void testDeletePost() {
    Post post3 = new Post("Post Title 3", "This is the third post content");
    post3.comments.add(comment1);
    post3.save();
    bob.posts.add(post3);
    bob.save();

    User user = User.findByEmail("bob@jones.com");
    assertEquals(1, user.posts.size());
    Post post = user.posts.get(0);

    post.comments.remove(0);
    post.save();
    user.posts.remove(0);
    user.save();
    post.delete();

    User anotherUser = User.findByEmail("bob@jones.com");
    assertEquals(0, anotherUser.posts.size());
  }
}