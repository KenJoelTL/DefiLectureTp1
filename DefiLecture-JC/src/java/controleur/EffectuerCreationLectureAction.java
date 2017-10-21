/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jdbc.Config;
import jdbc.Connexion;
import modele.Lecture;
import modele.LectureDAO;

/**
 *
 * @author Charles
 */
public class EffectuerCreationLectureAction implements Action, RequestAware, SessionAware {
    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private LectureDAO dao;
    
    @Override
    public String execute() {
        
        System.out.println("Entrer dans l'action créer lecture");
        
        String  titre = request.getParameter("titre"),
                dateInscription = request.getParameter("dateInscription");
        int     dureeMinutes = Integer.parseInt(request.getParameter("dureeMinutes")),
                idParticipant = Integer.parseInt(request.getParameter("idParticipant"));
        
        String pilote = "com.mysql.jdbc.Driver";
        Lecture lecture;
        
        
        try{

            Class.forName(pilote);
            Connexion.setUrl(Config.URL);
            Connexion.setUser(Config.DB_USER);
            Connexion.setPassword(Config.DB_PWD);
            Connection cnx = Connexion.getInstance();
            dao = new LectureDAO(cnx);
            lecture = new Lecture();
            lecture.setIdParticipant(idParticipant);
            lecture.setDureeMinutes(dureeMinutes);
            lecture.setTitre(titre);
            lecture.setDateInscription(dateInscription);
            if(dao.create(lecture))
                System.out.println("Une lecture a été créée avec succès");
            else
                System.out.println("Problème de création de la lecture");
                
            request.setAttribute("vue", "accueil.jsp");
            return "/index.jsp";
        }
        catch(ClassNotFoundException e){
            System.out.println("Erreur dans le chargement du pilote :"+ e);
            request.setAttribute("vue", "lecture.jsp");
            return "/index.jsp";
        }
        
        
        
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