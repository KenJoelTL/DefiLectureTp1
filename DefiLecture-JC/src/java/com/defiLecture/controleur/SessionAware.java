/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.defiLecture.controleur;

import javax.servlet.http.HttpSession;
/**
 *
 * @author Charles
 */
public interface SessionAware {
    public void setSession(HttpSession session);
}