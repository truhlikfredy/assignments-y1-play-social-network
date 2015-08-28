package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Comment extends Model {

  @OneToOne()
  public User   user;
  public String subject;
  @Lob
  public String content;
  public Long   time;

  public Comment(User user, String subject, String content) {
    this.user=user;
    this.subject = subject;
    this.content = content;
//    this.time=System.currentTimeMillis() / 1000L;
    this.time=System.currentTimeMillis();
//    Logger.info("Time "+this.time);
  }

  public String toString() {
    return content;
  }
}
