package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.Helpers;

public class Members extends Controller {
  
  public static void membersIndex() {
    User user = Helpers.getLoggedUser();
    List<User> usersToAdd = Helpers.getUsersToAdd();
    
    render(user,usersToAdd);
  }

  public static void membersFollow(Long id) {
    User friend = User.findById(id);
    Helpers.getLoggedUser().befriend(friend);
    MyHome.myHomeIndex();
  }
}
