package com.amicabile.openingtrainer.dao;

import com.amicabile.openingtrainer.dao.GenericDAO;
import com.amicabile.openingtrainer.dao.UserDAO;
import com.amicabile.openingtrainer.model.dataobj.GameDataObj;
import com.amicabile.openingtrainer.model.dataobj.User;
import com.amicabile.openingtrainer.model.search.GameSearchCriteria;
import com.amicabile.openingtrainer.pgn.ChessGameGroup;
import ictk.boardgame.Game;
import ictk.boardgame.GameInfo;
import ictk.boardgame.Player;
import ictk.boardgame.chess.ChessGame;
import ictk.boardgame.chess.ChessGameInfo;
import ictk.boardgame.chess.ChessResult;
import ictk.boardgame.chess.io.PGNReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class GameDataObjDAO extends GenericDAO {

   private static final int MAX_MINE_RESULT = 500;
   private static final int MAX_LAST_RESULT = 500;
   private static final int MAX_SEARCH_RESULT = 500;
   private static Logger log = Logger.getLogger(GameDataObjDAO.class.getName());
   private static GameDataObjDAO gameDataObjDAO = new GameDataObjDAO();


   public static GameDataObjDAO getInstance() {
      return gameDataObjDAO;
   }

   public GameDataObj getGameDataObj(long var1) throws HibernateException {
      log.debug("getGameDataObjDAO(" + var1 + ")");
      Session var3 = this.createSession();

      GameDataObj var4;
      try {
         GameDataObj var5 = new GameDataObj();
         var3.load((Object)var5, Long.valueOf(var1));
         var4 = var5;
      } finally {
         var3.close();
      }

      return var4;
   }

   public List getGameDataObjForUsername(String var1) throws HibernateException {
      log.debug("getGameDataObjForUsername(" + var1 + ")");
      Session var2 = this.createSession();

      List var3;
      try {
         Query var4 = this.createQueryForUsername(var1, var2);
         var4.setMaxResults(500);
         List var5 = var4.list();
         if(var5 != null) {
            log.debug("returned size =" + var5.size());
         }

         var3 = var5;
      } finally {
         var2.close();
      }

      return var3;
   }

   public void deleteGamesDeleted(String var1) throws HibernateException {
      List var2 = this.getGameDataObjForUsernameDeleted(var1);
      Session var3 = this.createSession();
      Transaction var4 = null;

      try {
         var4 = var3.beginTransaction();
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            GameDataObj var6 = (GameDataObj)var5.next();
            var3.delete((Object)var6);
         }

         var4.commit();
      } catch (Exception var10) {
         if(var4 != null) {
            var4.rollback();
         }

         log.error("Exception while deleting games ", var10);
      } finally {
         var3.close();
      }

   }

   public List getGameDataObjForUsernameDeleted(String var1) throws HibernateException {
      log.debug("getGameDataObjForUsernameDelete(" + var1 + ")");
      Session var2 = this.createSession();

      List var3;
      try {
         Query var4 = this.createQueryForUsernameDeleted(var1, var2);
         List var5 = var4.list();
         if(var5 != null) {
            log.debug("returned size =" + var5.size());
         }

         var3 = var5;
      } finally {
         var2.close();
      }

      return var3;
   }

   private Query createQueryForUsername(String var1, Session var2) throws HibernateException {
      Query var3 = var2.getNamedQuery("com.amicabile.openingtrainer.model.dataobj.GameByUserName");
      var3.setString("username", var1);
      return var3;
   }

   private Query createQueryForUsernameDeleted(String var1, Session var2) throws HibernateException {
      Query var3 = var2.getNamedQuery("com.amicabile.openingtrainer.model.dataobj.GameByUserNameDeleted");
      var3.setString("username", var1);
      return var3;
   }

   public List getAllGames() throws HibernateException {
      Session var1 = this.createSession();
      Query var2 = var1.getNamedQuery("com.amicabile.openingtrainer.model.dataobj.AllGames");
      var2.setMaxResults(500);
      List var3 = var2.list();
      this.setAnnotationFlag(var3);
      this.clearPgnStrings(var3);
      var1.close();
      return var3;
   }

   public List getLastGames() throws HibernateException {
      Session var1 = this.createSession();

      List var2;
      try {
         Query var3 = var1.getNamedQuery("com.amicabile.openingtrainer.model.dataobj.LastGames");
         var3.setMaxResults(500);
         List var4 = var3.list();
         this.setAnnotationFlag(var4);
         this.clearPgnStrings(var4);

         var2 = var4;
      } finally {
         var1.close();
      }

      return var2;
   }

   public int getNumGameDataObjForSearchCriteria(GameSearchCriteria var1) throws HibernateException {
      Session var2 = this.createSession();

      int var3;
      try {
         Query var4 = this.createQueryForSearch(var1, var2);
         int var5 = ((Integer)var4.iterate().next()).intValue();
         var3 = var5;
      } finally {
         var2.close();
      }

      return var3;
   }

   public List getGameDataObjForSearchCriteria(GameSearchCriteria var1) throws HibernateException {
      Session var2 = this.createSession();

      List var3;
      try {
         Query var4 = this.createQueryForSearch(var1, var2);
         var4.setMaxResults(500);
         List var5 = var4.list();
         this.setAnnotationFlag(var5);
         this.clearPgnStrings(var5);
         var3 = var5;
      } finally {
         var2.close();
      }

      return var3;
   }

   private Query createQueryForSearch(GameSearchCriteria var1, Session var2) throws HibernateException {
      StringBuffer var3 = new StringBuffer();
      boolean var4 = false;
      if(var1.isIgnoreColor()) {
         var3.append("  ( (lower(white) like lower(:white) or lower(black) like lower(:white)) and ( lower(white) like lower(:black) or lower(black) like lower(:black) )  ) ");
      } else {
         var3.append(" lower(white) like lower(:white) ");
         var3.append(" and lower(black) like lower(:black) ");
      }

      if(var1.getAfterDate() != null) {
         var3.append(" and date >= :afterDate ");
      }

      if(var1.getBeforeDate() != null) {
         var3.append(" and date <= :beforeDate ");
      }

      if(StringUtils.isNotEmpty(var1.getAfterECO()) && StringUtils.isNotEmpty(var1.getBeforeECO())) {
         var3.append(" and eco >= :afterECO ");
         var3.append(" and eco <= :beforeECO ");
      }

      if(var1.getBeforeDate() != null) {
         var3.append(" and date <= :beforeDate ");
      }

      if(StringUtils.isNotEmpty(var1.getSubmitter())) {
         var3.append(" and  gameDataObj.user.username like :username ");
      }

      if(StringUtils.isNotEmpty(var1.getResult())) {
         var3.append(" and  gameDataObj.result = :result ");
      }

      if(StringUtils.isNotEmpty(var1.getEvent())) {
         var3.append(" and  lower(gameDataObj.event) like lower(:event) ");
      }

      if(StringUtils.isNotEmpty(var1.getSite())) {
         var3.append(" and  lower(gameDataObj.site) like  lower(:site)");
      }

      if(StringUtils.isNotEmpty(var1.getTags())) {
         var3.append(" and  lower(gameDataObj.tags) like  lower(:tags)");
      }

      if(!StringUtils.isNotEmpty(var1.getLoggedinUser())) {
         var3.append(" and gameDataObj.publicgame = 1");
      }

      var3.append(" and gameDataObj.deleted = 0");
      var3.append(" order by GAME_ID desc");


       String var5 = "from com.amicabile.openingtrainer.model.dataobj.GameDataObj gameDataObj where ";
      var3.insert(0, var5);

      Query var6 = var2.createQuery(var3.toString());
      var6.setString("white", '%' + var1.getWhite() + '%');
      if(var1.isIgnoreColor()) {
         var6.setString("white", '%' + var1.getWhite() + '%');
      }

      var6.setString("black", '%' + var1.getBlack() + '%');
      if(var1.isIgnoreColor()) {
         var6.setString("black", '%' + var1.getBlack() + '%');
      }

      if(var1.getAfterDate() != null) {
         var6.setDate("afterDate", var1.getAfterDate());
      }

      if(var1.getBeforeDate() != null) {
         var6.setDate("beforeDate", var1.getBeforeDate());
      }

      if(StringUtils.isNotEmpty(var1.getAfterECO()) && StringUtils.isNotEmpty(var1.getBeforeECO())) {
         var6.setString("afterECO", var1.getAfterECO());
         var6.setString("beforeECO", var1.getBeforeECO());
      }

      if(StringUtils.isNotEmpty(var1.getSubmitter())) {
         var6.setString("username", "%" + var1.getSubmitter() + "%");
      }

      if(StringUtils.isNotEmpty(var1.getResult())) {
         var6.setString("result", var1.getResult());
      }

      if(StringUtils.isNotEmpty(var1.getEvent())) {
         var6.setString("event", "%" + var1.getEvent() + "%");
      }

      if(StringUtils.isNotEmpty(var1.getSite())) {
         var6.setString("site", "%" + var1.getSite() + "%");
      }

      if(StringUtils.isNotEmpty(var1.getTags())) {
         var6.setString("tags", "%" + var1.getTags() + "%");
      }

      return var6;
   }

   private void clearListFromGames(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         GameDataObj var3 = (GameDataObj)var2.next();
         var3.setPgnstring((String)null);
      }

   }

   private void setAnnotationFlag(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         GameDataObj var3 = (GameDataObj)var2.next();
         if(StringUtils.isEmpty(var3.getAnnotator()) && !StringUtils.isEmpty(var3.getPgnstring())) {
            var3.setAnnotator(var3.getPgnstring().contains("{")?"Yes":"");
         }
      }

   }

   private void clearPgnStrings (List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         GameDataObj var3 = (GameDataObj)var2.next();
         var3.setPgnstring(null);
      }

   }

   private void fillGameDataObj(GameDataObj var1, Game var2) {
      GameInfo var3 = var2.getGameInfo();
      if(var3 != null) {
         Player[] var4 = var3.getPlayers();
         if(var4 != null) {
            String var5;
            if(var4[0] != null) {
               var5 = var4[0].getName();
               var1.setWhite(var5);
            }

            if(var4[1] != null) {
               var5 = var4[1].getName();
               var1.setBlack(var5);
            }
         }

         Calendar var7 = var3.getDate();
         if(var7 != null) {
            var1.setDate(var7.getTime());
         }

         var1.setEvent(var3.getEvent());
         var1.setAnnotator(var3.get("Annotator"));
         var1.setEco(((ChessGameInfo)var3).getECO());
         var1.setSite(var3.getSite());
         if(var3.getResult() != null) {
            ChessResult var6 = (ChessResult)var3.getResult();
            var1.setResult(var6.toString());
         }

         var1.setRound(var3.getRound());
         String var8 = "";
      }

   }

   public Game getGame(GameDataObj var1) {
      Game var2 = null;

      try {
         String var3 = var1.getPgnstring();
         StringReader var4 = new StringReader(var3);
         PGNReader var5 = new PGNReader(var4);
         var2 = var5.readGame();
         var2.getGameInfo().getAuxilleryProperties().setProperty("GameId", String.valueOf(var1.getId()));
      } catch (IOException var6) {
         log.error("IOException in getGame", var6);
         var6.printStackTrace();
      } catch (Exception var7) {
         log.error("Exception in getGame", var7);
         var7.printStackTrace();
      }

      return var2;
   }

   public ChessGameGroup getGameGroup(String var1) throws HibernateException {
      ChessGameGroup var2 = new ChessGameGroup();
      List var3 = this.getGameDataObjForUsername(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         GameDataObj var5 = (GameDataObj)var4.next();
         var2.addChessGame((ChessGame)this.getGame(var5));
      }

      return var2;
   }

   public ChessGameGroup getGameGroup(GameSearchCriteria var1) throws HibernateException {
      ChessGameGroup var2 = new ChessGameGroup();
      List var3 = this.getGameDataObjForSearchCriteria(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         GameDataObj var5 = (GameDataObj)var4.next();
         var2.addChessGame((ChessGame)this.getGame(var5));
      }

      return var2;
   }

   public boolean switchPublicStateGame(String var1, long var2) throws HibernateException {
      GameDataObj var4 = this.getGameDataObj(var2);
      Session var5 = this.createSession();
      Transaction var6 = null;

      try {
         var6 = var5.beginTransaction();
         var4.setPublicgame(!var4.isPublicgame());
         if(var4.getUser().getUsername().equals(var1)) {
            log.info("About to switch stat" + var2);
            var5.update(var4);
            log.info("Game " + var2 + " is now " + var4.isPublicgame());
         } else {
            log.warn("Game " + var2 + " cannot be modified by user " + var1);
         }

         var6.commit();
         boolean var7 = var4.isPublicgame();
         boolean var8 = var7;
         return var8;
      } catch (Exception var12) {
         if(var6 != null) {
            var6.rollback();
         }

         log.error("Exception while deleting game " + var2, var12);
      } finally {
         var5.close();
      }

      return false;
   }

   public boolean switchDeleteGame(String var1, long var2) throws HibernateException {
      GameDataObj var4 = this.getGameDataObj(var2);
      Session var5 = this.createSession();
      Transaction var6 = null;

      try {
         var6 = var5.beginTransaction();
         var4.setDeleted(!var4.isDeleted());
         if(var4.getUser().getUsername().equals(var1)) {
            log.info("About to switch stat" + var2);
            var5.update(var4);
            log.info("Game " + var2 + " is now " + var4.isPublicgame());
         } else {
            log.warn("Game " + var2 + " cannot be modified by user " + var1);
         }

         var6.commit();
         boolean var7 = var4.isDeleted();
         boolean var8 = var7;
         return var8;
      } catch (Exception var12) {
         if(var6 != null) {
            var6.rollback();
         }

         log.error("Exception while deleting game " + var2, var12);
      } finally {
         var5.close();
      }

      return false;
   }

   public void deleteGame(String var1, long var2) throws HibernateException {
      Session var4 = this.createSession();
      Transaction var5 = null;

      try {
         var5 = var4.beginTransaction();
         GameDataObj var6 = this.getGameDataObj(var2);
         if(var6.getUser().getUsername().equals(var1)) {
            log.info("About to delete " + var2);
            var4.delete((Object)var6);
            log.info("Game " + var2 + " was successfully deleted");
         } else {
            log.warn("Game " + var2 + " cannot be deleted by user " + var1);
         }

         var5.commit();
      } catch (Exception var10) {
         if(var5 != null) {
            var5.rollback();
         }

         log.error("Exception while deleting game " + var2, var10);
      } finally {
         var4.close();
      }

   }

   public int getCountGames() throws HibernateException {
      Session var1 = this.createSession();

      int var2;
      try {
         Query var3 = var1.getNamedQuery("com.amicabile.openingtrainer.model.dataobj.CountGames");
         int var4 = ((Integer)var3.uniqueResult()).intValue();
         var2 = var4;
      } finally {
         var1.close();
      }

      return var2;
   }

   public GameDataObj updateGameDataObj(GameDataObj var1) throws HibernateException {
      Session var2 = this.createSession();
      Transaction var3 = null;

      try {
         var3 = var2.beginTransaction();
         var2.saveOrUpdate(var1);
         var3.commit();
      } catch (Exception var8) {
         if(var3 != null) {
            var3.rollback();
         }
      } finally {
         var2.close();
      }

      return var1;
   }

   public GameDataObj createGameDataObj(String var1, String var2) throws HibernateException {
      return this.createGameDataObj(var1, var2, "", true);
   }

   public GameDataObj createGameDataObj(String var1, String var2, String var3, boolean var4) throws HibernateException {
      GameDataObj var5 = null;
      UserDAO var6 = UserDAO.getInstance();
      User var7 = var6.getUser(var1);
      var5 = new GameDataObj();
      var5.setPgnstring(var2);
      var5.setUser(var7);
      var5.setTags(var3);
      var5.setPublicgame(var4);
      this.updateAndFillGameDataObj(var5);
      return var5;
   }

   public void updateAndFillGameDataObj(GameDataObj var1) throws HibernateException {
      Game var2 = this.getGame(var1);
      this.fillGameDataObj(var1, var2);
      this.updateGameDataObj(var1);
   }

}
