package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;
import utils.Helpers;

public class MyHome extends Controller {

  public static void myHomeIndex() {
    User user = Helpers.getLoggedUser();
    render(user);
  }

  public static void myHomeDrop(Long id) {
    User user = Helpers.getLoggedUser();
    User friend = User.findById(id);
    user.unfriend(friend);
    Logger.info("Dropping " + friend.email);
    myHomeIndex();
  }
}