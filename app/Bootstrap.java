import java.util.List;

import play.*;
import play.jobs.*;
import play.test.*;
import play.db.DB;
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job 
{ 
  public void doJob()
  {
    if (User.count() == 0)
    {
      Fixtures.deleteDatabase(); 
      Fixtures.loadModels("data.yml");
      List<User> allItems = User.findAll();
      
      /*
       * For some reason cloudbeas will get it diffrent IDs and jumping a lot in numbers.
       * Maybe something in data.yml is causing it. 
       */
      int i=1;
      for (User a: allItems){
        DB.execute("UPDATE `User` SET profilePicture='picture_" + i + ".jpg|image/jpeg' WHERE id=" + a.getId());
        DB.execute("UPDATE `User` SET thumbnailPicture='thumb_" + i + ".png|image/png' WHERE id=" + a.getId());
        i++;
      }
      
      Post post1= new Post("First post","Lots of content");
      post1.save();
      Post post2= new Post("Last most recent post", "Lots of content Lots of contentLots of content");
      post2.save();
      User user = User.findByEmail("homer@simpson.com");
      user.posts.add(post1);
      user.posts.add(post2);
      user.save();
      
      User user2 = User.findByEmail("marge@simpson.com");
      Comment comment1=new Comment(user, "Why marge", "Whyyyyy?");
      comment1.save();
      Post post3= new Post("Why marge blogs", "Because, ok?");
      post3.comments.add(comment1);
      post3.save();
      user2.posts.add(post3);
      user2.save();
      
      User user3 = User.findByEmail("lisa@simpson.com");
      Post post4= new Post("Jazz", "Rulez");
      post4.save();
      user3.posts.add(post4);
      user3.save();

      //private blog
      User user4 = User.findByEmail("snowball2@simpson.com");
      Post post5= new Post("My private blog", "NOBODY can know this");
      post5.save();
      user4.posts.add(post5);
      user4.save();
      
    }
  }
}