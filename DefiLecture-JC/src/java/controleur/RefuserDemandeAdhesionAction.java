/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jdbc.Config;
import jdbc.Connexion;
import modele.Compte;
import modele.CompteDAO;
import modele.DemandeEquipe;
import modele.DemandeEquipeDAO;

/**
 *
 * @author Joel
 */
public class RefuserDemandeAdhesionAction implements Action, SessionAware, RequestAware, RequirePRGAction{
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;

    @Override
    public String execute() {
String action = ".do?tache=afficherPageAccueil";
        if(session.getAttribute("connecte") == null
            || session.getAttribute("role") == null
            || ((int)session.getAttribute("role") != 2)
            || request.getParameter("idDemandeEquipe") == null){
            action = ".do?tache=afficherPageAccueil";}
        else{
            try {
                String idDemandeEq = request.getParameter("idDemandeEquipe");
                Connection cnx = Connexion.startConnection(Config.DB_USER, Config.DB_PWD, Config.URL, Config.DRIVER);
                
                DemandeEquipeDAO deDao = new DemandeEquipeDAO(cnx);
                DemandeEquipe demandeEq = deDao.read(idDemandeEq);
                
                if(demandeEq == null)
                    action = "*.do?tache=afficherPageAccueil";
                else{
                    if(!deDao.delete(demandeEq))
                        action = "*.do?tache=afficherPageAccueil";
                    else
                        action = "refus.do?tache=afficherPageListeDemandesEquipe&ordre=recu";
                    
                }
                    
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AccepterDemandeAdhesionAction.class.getName()).log(Level.SEVERE, null, ex);
                action = "echec.do?tache=afficherPageAcceuil";
            }
            finally{Connexion.close();}
        }
        return action;
    }

    @Override
    public void setSession(HttpSession session) {
        this.response = response;
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
    
}