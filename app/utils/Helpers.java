package utils;

import models.User;
import play.Logger;
import controllers.Accounts;
import models.*;

import java.util.*;

import javax.mail.Session;

public class Helpers {

  public static long getLoggedUserId() {
    return (Long.parseLong(Accounts.getLoggedUserIdRaw()));
  }

  public static void timeoutCheck() {
    List<User> users = User.findAll();

    for (User user : users) {
      // timeout set for 1 hour
      if (user.online && ((user.lastActivity + (60L * 60L * 1000L)) < System.currentTimeMillis())) {
        user.online = false;
        user.save();
        Logger.info("User " + user.id + " (" + user.firstName + " " + user.lastName + ") got online timeout.");
      }
    }
  }

  public static User getLoggedUser() {
    String userId = Accounts.getLoggedUserIdRaw();
    if (userId != null) {
      User user = User.findById(Long.parseLong(userId));

      if (user.online) {
        // user.lastActivity = System.currentTimeMillis() / 1000L;
        user.lastActivity = System.currentTimeMillis();
        user.save();

        timeoutCheck();

        return (user);
      } else {
        /* here we can have 2 behaviours timeouted user be forced to relogin and redirected without any clue what is happening 
         * or have light timeout and heavy timeout, light timeout would consider you offline but when you get back you would
         * still be able to use the webpage and hard timeout which would signout you as well. 
         */
        /* 
         // this one will log you out every single time
        Logger.info("Timeout user "+user.id+" ( "+user.firstName+" "+user.lastName+" ) tried to use the webpage");
        Accounts.clearSessionAndShowIndex();
         */
        //this will allow you use it after light timeout and reactivate your status as online
        user.online=true;
        user.lastActivity = System.currentTimeMillis();
        user.save();

        timeoutCheck();
        return (user);
        
      }
    } else {
      Accounts.accountsLogin();
    }
    // just to avoid eclipse warrnings because some if statements won't return 
    return (new User());
  }

  public static boolean notLoggedIn() {
    String userId = Accounts.getLoggedUserIdRaw();
    if (userId == null) {
      return (true);
    } else {
      return (false);
    }
  }

  public static void redirectIfNotLoggedIn() {
    if (notLoggedIn()) Accounts.accountsLogin();
  }

  public static boolean fieldsCorrect(User user) {
    if (user.age < 0 || user.age > 200 || user.firstName.isEmpty() || user.lastName.isEmpty() || user.email.isEmpty()
        || !user.email.contains("@") || user.nationality.isEmpty() || user.password.isEmpty()) {
      return (false);
    } else {
      return (true);
    }
  }

  public static int convertAge(String age) {
    try {
      return (Integer.parseInt(age));
    } catch (NumberFormatException nfe) {
      return (-1);
    }
  }

  public static List<User> getUsersToAdd() {
    User user = Helpers.getLoggedUser();
    List<User> users = User.findAll();

    /* remove yourself */
    // users.remove((int)(Accounts.getLoggedUserId())-1);
    users.remove(user);

    /* remove all which you are following already */
    for (Friendship row : user.friendships) {
      User already = User.findById(row.targetUser.id);
      users.remove(already);
      /*
       * In case we would allow to delete accounts, we would need to have clean
       * up for the friendships, because if this tries to delete ID which is
       * gone it will crash. (and on other places it would make problems too)
       */
    }
    /*
     * for bigger website this approach seams to me wrong, if bigger load would
     * be expected lots of things would need to be redone.
     */
    return (users);
  }

  public static List<User> getPublicBlogs() {
    List<User> allUsers = User.findAll();
    List<User> blogs = new ArrayList<User>();

    for (User user : allUsers)
      if (user.posts.size() > 0 && user.showBlog) blogs.add(user);

    return (blogs);
  }

}
