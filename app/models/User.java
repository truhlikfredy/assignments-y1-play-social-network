package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import play.db.jpa.Model;
import play.db.jpa.Blob;
import play.Logger;

@Entity
public class User extends Model {
  public String           firstName;
  public String           lastName;
  public String           email;
  public String           password;
  public String           statusText;
  public Blob             profilePicture;
  public Blob             thumbnailPicture;
  public int              age;
  public String           nationality;
  public boolean          online;
  public boolean          showOnline;
  public Long             lastActivity;
  public boolean          showBlog;

  @OneToMany(mappedBy = "sourceUser")
  public List<Friendship> friendships = new ArrayList<Friendship>();

  @OneToMany(mappedBy = "to")
  public List<Message>    inbox       = new ArrayList<Message>();

  @OneToMany(mappedBy = "from")
  public List<Message>    outbox      = new ArrayList<Message>();

  @OneToMany
  public List<Post>       posts       = new ArrayList<Post>();

  public User(String firstName, String lastName, String email, int age, String nationality, String password,boolean showOnline, boolean showBlog) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.age = age;
    this.nationality = nationality;
    this.online = false;
    this.lastActivity = System.currentTimeMillis();
    this.showOnline = showOnline;
    this.showBlog = showBlog;
  }

  public User() {
  }

  public static User findByEmail(String email) {
    return find("email", email).first();
  }

  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }

  public void befriend(User friend) {
    Friendship friendship = new Friendship(this, friend);
    boolean duplicate = false;

    /*
     * friendships.contains(friendship) won't work because this friendship has
     * not yet id set properly, so it won't find the duplicate which is in the
     * database and already got the id. Had to use remote debugging to figure
     * this out.
     */

    for (Friendship fs : friendships) {
      if (friend.id == fs.targetUser.id) {
        duplicate = true;
        break;
      }
    }

    if (duplicate) {
      Logger.info("Using old link or breaking website. Trying to follow somebody who is followed already.");
    } else {
      friendships.add(friendship);
      friendship.save();
      save();
    }
  }

  public void unfriend(User friend) {
    Friendship thisFriendship = null;

    for (Friendship friendship : friendships) {
      if (friendship.targetUser == friend) {
        thisFriendship = friendship;
      }
    }
    friendships.remove(thisFriendship);
    thisFriendship.delete();
    save();
  }

  public void sendMessage(User to, String messageText) {
    Message message = new Message(this, to, messageText);
    outbox.add(message);
    to.inbox.add(message);
    message.save();
  }

}