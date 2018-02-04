/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.defiLecture.controleur;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jdbc.Config;
import jdbc.Connexion;
import com.defiLecture.modele.Compte;
import com.defiLecture.modele.CompteDAO;

/**
 *
 * @author Joel
 */
public class AfficherPageModificationCompteAction implements Action, RequestAware,SessionAware {
    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    @Override
    public String execute() {

        request.setAttribute("vue", "pageGestionListeCompte.jsp");        
        
        
        // On vérifie si l'utilisateur qui est selectionné est bien celui qui est connecté. 
        // Autrement, seuls les administrateurs et les modérateurs peuvent accéder à la page de configuration d'autrui
        if( session.getAttribute("connecte") != null && session.getAttribute("role") != null && request.getParameter("id")!=null) 
            if( ( !request.getParameter("id").equals(session.getAttribute("connecte")+"")
             && ( ((int)session.getAttribute("role") == Compte.ADMINISTRATEUR) || ((int)session.getAttribute("role") == Compte.MODERATEUR) ) ) 
             || (request.getParameter("id").equals(session.getAttribute("connecte")+"")) ){

            String idCompte = request.getParameter("id");
            try {
                Connection cnx = Connexion.startConnection
                       (Config.DB_USER, Config.DB_PWD, Config.URL, Config.DRIVER);

                CompteDAO dao = new CompteDAO(cnx);

                if(dao.read(idCompte)!=null)
                    request.setAttribute("vue", "pageModificationCompte.jsp");
                
            } 
            catch (ClassNotFoundException ex) {
                Logger.getLogger(AfficherPageModificationCompteAction
                                   .class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("vue", "pageGestionListeCompte.jsp");
                return "/index.jsp";
            }
            finally{
                Connexion.close();
            }
        }
        return "/index.jsp";
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void setSession(HttpSession session) {
        this.session = session;
    }
    
}