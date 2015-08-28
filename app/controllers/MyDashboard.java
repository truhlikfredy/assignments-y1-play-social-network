package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import utils.Helpers;

public class MyDashboard extends Controller {

  public static void myDashboardIndex() {
    User user = Helpers.getLoggedUser();
    
    List<User> users = User.findAll();
    users.remove(user);
    
    User userBlogs=user;

    List<User> usersToAdd = Helpers.getUsersToAdd();
    render(user, users, userBlogs, usersToAdd);    
  }

}