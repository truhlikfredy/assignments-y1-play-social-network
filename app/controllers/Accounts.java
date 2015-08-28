package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.Helpers;

public class Accounts extends Controller {
  /* helpers */

  public static String getLoggedUserIdRaw() {
    return (session.get("logged_in_userid"));
  }
  
  public static void clearSessionAndShowIndex() {
    session.clear();
    accountsIndex();    
  }

  /* regular controller methods */
  
  /*
   * order of these 2 methods is important, when I swap them and then I call
   * AccountsSignup(true); it will go anyway to AccountsSignup();
   * 
   * Something is strange here. This happend multiple times on different methods
   * as well, play doesn't like overloading :-(. Even putting index before them 
   * can break it...
   */
  
  public static void accountsSignup() {
    boolean warn=false;
       
    Helpers.timeoutCheck();
    List<User> blogs = Helpers.getPublicBlogs();

    render(warn, blogs);
  }

  /* when i deployed it, it broke again, i'm creating new view, this is unrealiable*/
  /*
  public static void accountsSignup(boolean warn) {
    Helpers.timeoutCheck();
    List<User> blogs = Helpers.getPublicBlogs();
    
    render(warn, blogs);
  }
  */
  
  public static void accountsSignupWarn() {
    boolean warn=true;

    Helpers.timeoutCheck();
    List<User> blogs = Helpers.getPublicBlogs();

    render(warn, blogs);
  }
    
  public static void accountsIndex() {
    Helpers.timeoutCheck();
    List<User> blogs = Helpers.getPublicBlogs();

    render(blogs);
  }    

  public static void accountsLogin() {
    Helpers.timeoutCheck();
    List<User> blogs = Helpers.getPublicBlogs();

    render(blogs);
  }

  public static void accountsLogout() {
    User user = Helpers.getLoggedUser();
    user.online = false;
    user.save();

    clearSessionAndShowIndex();
  }
  
  public static void accountsRegister(String firstName, String lastName, String email, String age, String nationality, String password,
      boolean showOnline, boolean showBlog) {
    int ageInt = Helpers.convertAge(age);

    // email is case insensitive
    email = email.toLowerCase();

    Logger.info(firstName + " " + lastName + " " + email + " " + age + " " + nationality + " " + password);

    User user = new User(firstName, lastName, email, ageInt, nationality, password, showOnline, showBlog);
    User duplicate = User.findByEmail(email);
    // no duplicates by my own email is allowed to use again
    if (user.email.equals(email)) duplicate = null;

    // age 0 allowed as valid, but too old is not valid
    if (Helpers.fieldsCorrect(user) && duplicate == null) {
      user.save();
      Logger.info("User registered");

      accountsIndex();
    } else {
      Logger.info("Some other inputs are not filled in properly. Not registering.");

      accountsSignupWarn();
    }
  }

  public static void accountsAuthenticate(String email, String password) {
    // email is case insensitive
    email = email.toLowerCase();
    Logger.info("Attempting to authenticate with " + email + ":" + password);

    User user = User.findByEmail(email);
    if ((user != null) && (user.checkPassword(password) == true)) {
      Logger.info("Authentication successful");
      session.put("logged_in_userid", user.id);
      user.online = true;
      user.save();
      MyHome.myHomeIndex();
    } else {
      Logger.info("Authentication failed");
      accountsLogin();
    }
  }

}