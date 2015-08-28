import models.Post;
import models.User;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

//import antlr.collections.List;
import play.test.Fixtures;
import play.test.UnitTest;

public class PostTest extends UnitTest {
  private Post post1;
  private Post post2;

  @BeforeClass
  public static void loadDB() {
    Fixtures.deleteAllModels();
  }

  @Before
  public void setup() {
    post1 = new Post("title","content");
    post1.save();
    post2 = new Post("newest post","And it's most recent text");
    post2.save();
  }

  @After
  public void teardown() {
    post1.delete();
    post2.delete();
  }

  @Test
  public void testPostSize() {
    assertEquals(2, Post.count());
  }
  
  @Test
  public void testCreatePost() {
    List<Post> testPost = Post.findAll();    
    assertNotNull(testPost.get(0));
  }

  @Test
  public void testFindPost() {
    Post foundPost = Post.find("title", "newest post").first();
    assertNotNull(foundPost);
  }
}
