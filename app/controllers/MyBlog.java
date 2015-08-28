package controllers;

import java.util.Collections;
import java.util.List;

import models.Message;
import models.Post;
import models.User;
import models.Comment;
import play.Logger;
import play.mvc.Controller;
import utils.Helpers;

public class MyBlog extends Controller {

  public static void myBlogIndex() {
    User user = Helpers.getLoggedUser();
    Collections.reverse(user.posts);
    
    User userBlogs = user;
    
    render(user, userBlogs);
  }

  public static void myBlogNewPost(String title, String content) {
    User user = Helpers.getLoggedUser();

    if (!title.isEmpty() && !content.isEmpty()) {
      Post post = new Post(title, content);
      post.save();
      user.posts.add(post);
      user.save();

      Logger.info("title:" + title + " content:" + content);
    } else {
      Logger.info("Title or content was empty, not creating blog post");
    }
    myBlogIndex();
  }

  public static void myBlogDeletePost(Long postid) {
    User user = Helpers.getLoggedUser();

    Post post = Post.findById(postid);
    for (Comment comment:post.comments) {
      post.comments.remove(comment);
      comment.delete();
    }
    post.save();
    user.posts.remove(post);

    user.save();
    post.delete();

    myBlogIndex();
  }

  public static void myBlogShow(Long postid) {
    User user = Helpers.getLoggedUser();
    Post post = Post.findById(postid);
    
    if (post==null) {
      Logger.info("Blog post with this id "+ postid +" doesn't exists. ");
      Accounts.accountsIndex();
    } else {    
      render(user, post);
    }
  }

}