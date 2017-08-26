package com.amicabile.openingtrainer.servlet.webwork.user;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpSession;

public class LogoutAction extends ActionSupport {

   public String execute() {
      HttpSession session = ServletActionContext.getRequest().getSession();
      session.invalidate();
      return "success";
   }
}
