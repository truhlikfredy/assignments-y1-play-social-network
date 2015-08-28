package controllers;

import play.*;
import play.db.jpa.Blob;
import play.mvc.*;
import java.util.*;
import models.*;
import utils.Helpers;

public class MyProfile extends Controller {

  public static void myProfileIndex() {
    User user = Helpers.getLoggedUser();
    render(user);
  }

  public static void myProfileChangeStatus(String statusText) {
    User user = Helpers.getLoggedUser();
    user.statusText = statusText;
    user.save();
    Logger.info("Status changed to " + statusText);
    myProfileIndex();
  }

  public static void myProfileUploadPicture(Long id, Blob picture) {
    User user = User.findById(id);
    user.profilePicture = picture;
    user.save();
    Logger.info("saving picture");
    myProfileIndex();
  }

  public static void myProfileUploadThumbnail(Long id, Blob picture) {
    User user = User.findById(id);
    user.thumbnailPicture = picture;
    user.save();
    myProfileIndex();
  }

  public static void myProfileEdit(boolean warn) {
    Helpers.redirectIfNotLoggedIn();
    User user = Helpers.getLoggedUser();
    render(warn, user);
  }

  public static void myProfileEditUpdate(String firstName, String lastName, String email, String age, String nationality,
      boolean showOnline, boolean showBlog) {
    int ageInt = Helpers.convertAge(age);

    Helpers.redirectIfNotLoggedIn();

    // email is case insensitive
    email = email.toLowerCase();

    Logger.info(firstName + " " + lastName + " " + email + " " + age + " " + nationality);

    User updatedUser = new User(firstName, lastName, email, ageInt, nationality, "just_keep_this_password_string_non_empty", showOnline,
        showBlog);

    User duplicate = User.findByEmail(email);
    User user = Helpers.getLoggedUser();
    // no duplicates by my own email is allowed to use again
    if (user.email.equals(email)) duplicate = null;

    if (Helpers.fieldsCorrect(updatedUser) && duplicate == null) {

      user.firstName = firstName;
      user.lastName = lastName;
      user.email = email;
      user.age = ageInt;
      user.nationality = nationality;
      user.showOnline=showOnline;
      user.showBlog=showBlog;
      user.save();

      Logger.info("Profile updated");
      MyHome.myHomeIndex();
    } else {
      Logger.info("Some other inputs are not filled in properly. Not updating.");

      myProfileEdit(true);
    }
  }

  /*
   * public static void change(String firstName, String lastName, int age,
   * String nationality, String email, String password, String password2) {
   * 
   * User user = Accounts.getLoggedUser(); user.firstName = firstName;
   * user.lastName = lastName; user.email = email; user.nationality =
   * nationality; user.age = age; user.password = password; user.save();
   * MyProfile.myProfileIndex(); }
   * 
   * public static void index() { User user = Accounts.getLoggedUser();
   * render(user); }
   */
}