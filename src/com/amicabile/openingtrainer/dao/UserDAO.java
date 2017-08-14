package com.amicabile.openingtrainer.dao;

import com.amicabile.openingtrainer.config.ShowBoardRulePrototypes;
import com.amicabile.openingtrainer.dao.GenericDAO;
import com.amicabile.openingtrainer.model.dataobj.GameDataObj;
import com.amicabile.openingtrainer.model.dataobj.User;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.log4j.Logger;

public class UserDAO extends GenericDAO {

   private static Logger log = Logger.getLogger(UserDAO.class.getName());
   private static UserDAO userDAO = new UserDAO();

   public static UserDAO getInstance() {
      return userDAO;
   }

   private String retrieveSalt() throws IOException{


      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      InputStream input = classLoader.getResourceAsStream("chessella.properties");

      Properties properties = new Properties();
      properties.load(input);
      return properties.getProperty("saltvalue");
   }


   public void updateUser(User user) throws HibernateException {
      log.info("Updating user " + user);
      Session session = this.createSession();
      Transaction tx = null;

      try {
         tx = session.beginTransaction();
         session.update(user);
         tx.commit();
      } catch (Exception var8) {
         if(tx != null) {
            tx.rollback();
         }

         log.error("Exception updating user " + user, var8);
      } finally {
         session.close();
      }

   }

   public User createUser(String username, String password, String email) throws HibernateException, IOException {
      this.getUser(username);


      User user = new User();
      user.setUsername(username);

      String encPassword = getEncryptedPassword(password);

      user.setPassword(encPassword );
      user.setEmail(email);
      user.setShowBoardRule(ShowBoardRulePrototypes.DEFAULT_RULE);
      Session session = this.createSession();
      Transaction tx = null;

      try {
         tx = session.beginTransaction();
         session.save(user);
         log.info("Successfully created user " + username);
         tx.commit();
      } catch (Exception var12) {
         if(tx != null) {
            tx.rollback();
         }

         log.error("Exception creating user " + username, var12);
      } finally {
         session.close();
      }

      return user;
   }

   public String getEncryptedPassword(String md5) throws  IOException {
      try {
         String salt = retrieveSalt();
         String passwordSalted = salt + md5;
         MessageDigest md = MessageDigest.getInstance("MD5");
         byte[] array = md.digest(passwordSalted.getBytes());
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
         }
         return sb.toString();
      } catch (NoSuchAlgorithmException nsae) {
         log.error("This never happens "+nsae.getMessage());
         return md5;
      }
   }

   public User getUser(long id) throws HibernateException {
      Session session = this.createSession();

      User var6;
      try {
         User user = new User();
         session.load((Object)user, Long.valueOf(id));
         var6 = user;
      } finally {
         session.close();
      }

      return var6;
   }

   public List getAllUsers() throws HibernateException {
      Session session = this.createSession();

      List var5;
      try {
         Query query = session.getNamedQuery("com.amicabile.openingtrainer.model.dataobj.AllUsers");
         List querylist = query.list();
         var5 = querylist;
      } finally {
         session.close();
      }

      return var5;
   }

   public List getAllSubmitters() throws HibernateException {
      Session session = this.createSession();

      List var5;
      try {
         Query query = session.getNamedQuery("com.amicabile.openingtrainer.model.dataobj.AllSubmitters");
         List querylist = query.list();
         var5 = querylist;
      } finally {
         session.close();
      }

      return var5;
   }

   public User getUser(String username) throws HibernateException {
      Session session = this.createSession();

      User var8;
      try {
         User user = null;
         new GameDataObj();
         Query query = session.getNamedQuery("com.amicabile.openingtrainer.model.dataobj.UserByName");
         query.setString("username", username);
         List queryList = query.list();
         if(queryList != null && queryList.size() > 0) {
            user = (User)queryList.get(0);
         }

         log.debug("Successfully retrieved user " + username);
         var8 = user;
      } finally {
         session.close();
      }

      return var8;
   }
}
