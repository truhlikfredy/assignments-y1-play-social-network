package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import org.apache.log4j.pattern.LogEvent;

import models.*;
import utils.Helpers;

public class Blogs extends Controller {

  public static void blogsIndex() {
    User user;
    if (Helpers.notLoggedIn()) {
      user = null;
    } else {
      user = Helpers.getLoggedUser();
    }

    List<User> blogs = Helpers.getPublicBlogs();

    render(user, blogs);
  }

  public static void blogsAllFromUser(Long id) {
    User user;
    if (Helpers.notLoggedIn()) {
      user = null;
    } else {
      user = Helpers.getLoggedUser();
    }

    User userBlogs = User.findById(id);
    Collections.reverse(userBlogs.posts);

    render(user, userBlogs);
  }

  public static void blogsShowPost(Long id) {
    User user;
    if (Helpers.notLoggedIn()) {
      user = null;
    } else {
      user = Helpers.getLoggedUser();
    }

    Post post = Post.findById(id);
    if (post == null) {
      Logger.info("Blog post with this id " + id + " doesn't exists. ");
      Accounts.accountsIndex();
    } else {
      render(user, post);
    }
  }

  public static void blogsNewComment(Long postid, String subject, String content) {
    User user = Helpers.getLoggedUser();

    Post post = Post.findById(postid);
    if (post == null) {
      Logger.info("Blog post with this id " + postid + " doesn't exists. ");
      Accounts.accountsIndex();
    } else {
      Comment comment = new Comment(user,subject,content);
      comment.save();
      post.comments.add(comment);
      post.save();
      
      blogsShowPost(postid);
    }
  }
  
  public static void blogsDeleteComment(Long postId, Long commentId) {
    Helpers.redirectIfNotLoggedIn();
    Long userId = Helpers.getLoggedUserId();
    Comment comment = Comment.findById(commentId);
    
    if (comment.user.id == userId) {
      Logger.info("Deleting comment #"+commentId+" from post #"+postId);
      Post post = Post.findById(postId);
      post.comments.remove(comment);
      post.save();
      comment.delete();
    } else {
      Logger.info("User #"+userId+" is trying to delete comment #"+commentId+" from post #"+postId+" which he doesn't own!");
    }
    
    blogsShowPost(postId);
  }

}