package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;
import play.db.jpa.Blob;
import utils.Helpers;

public class Profile extends Controller {

  public static void profileGetPicture(Long id) {
    User user = User.findById(id);
    Blob picture = user.profilePicture;
    if (picture.exists()) {
      response.setContentTypeIfNotSet(picture.type());
      renderBinary(picture.get());
    }
  }

  public static void profileGetThumbnail(Long id) {
    User user = User.findById(id);
    Blob picture = user.thumbnailPicture;
    if (picture.exists()) {
      response.setContentTypeIfNotSet(picture.type());
      renderBinary(picture.get());
    }
  }

  public static void profileShow(Long id) {
    Helpers.redirectIfNotLoggedIn();
    // User currentUser = Helpers.getLoggedUser();

    User user = User.findById(id);
    Logger.info("Just visiting the page for " + user.firstName + ' ' + user.lastName);
    render(user);
  }

  public static void profileSendMsg(Long id, String messageText) {
    User fromUser = Helpers.getLoggedUser();
    ;
    User toUser = User.findById(id);

    Logger.info("Message from user " + fromUser.firstName + ' ' + fromUser.lastName + " to " + toUser.firstName + ' ' + toUser.lastName
        + ": " + messageText);

    fromUser.sendMessage(toUser, messageText);
    profileShow(id);
  }
}