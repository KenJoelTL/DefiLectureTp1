/**
    This file is part of DefiLecture.

    DefiLecture is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    DefiLecture is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with DefiLecture.  If not, see <http://www.gnu.org/licenses/>.
*/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.defiLecture.controleur;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jdbc.Config;
import jdbc.Connexion;
import com.defiLecture.modele.Compte;
import com.defiLecture.modele.CompteDAO;
import com.defiLecture.modele.DemandeEquipe;
import com.defiLecture.modele.DemandeEquipeDAO;
import com.defiLecture.modele.Lecture;
import com.defiLecture.modele.LectureDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Charles
 */
public class EffectuerCreationLectureAction implements Action, RequestAware, SessionAware, RequirePRGAction {
    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    @Override
    public String execute() {
        
        System.out.println("Entrer dans l'action créer lecture");
        
        if(session.getAttribute("connecte") != null
        && session.getAttribute("role") != null
        && ( ((int)session.getAttribute("role") == Compte.PARTICIPANT)
            || ((int)session.getAttribute("role") == Compte.CAPITAINE) )
        && request.getParameter("titre")!=null
        && request.getParameter("dureeMinutes")!=null
        && request.getParameter("obligatoire")!=null){
     
            String  titre = request.getParameter("titre");                
            int     dureeMinutes = Integer.parseInt(request.getParameter("dureeMinutes")),
                    obligatoire = Integer.parseInt(request.getParameter("obligatoire")),
                    idCompte = (int)session.getAttribute("connecte");

            Lecture lecture;
               
        try{

            Connexion.reinit();
            Connection cnx = Connexion.startConnection(Config.DB_USER,Config.DB_PWD,Config.URL,Config.DRIVER);
            LectureDAO dao;
            dao = new LectureDAO(cnx);
            lecture = new Lecture();
            lecture.setIdCompte(idCompte);
            lecture.setDureeMinutes(dureeMinutes);
            lecture.setTitre(titre);
            lecture.setEstObligatoire(obligatoire);
             if(dao.create(lecture)){
                
                //Mise à jour des points du participant
                //Conversion du nombre de minutes de la lecture en points pour le Participant : 15mins = 1 point
                CompteDAO daoCompte = new CompteDAO(cnx);
                Compte compte = new Compte();
                compte = daoCompte.read(idCompte);
                if(lecture.getEstObligatoire() == Lecture.NON_OBLIGATOIRE)
                    dureeMinutes*=2;
                int pointLecture = (dureeMinutes + compte.getMinutesRestantes()) / 15;
                int pointCompte = compte.getPoint() + pointLecture;
                //Les minutes restantes sont gardées en mémoire ici
                int minutesRestantes = (dureeMinutes + compte.getMinutesRestantes()) % 15;
                compte.setPoint(pointCompte);
                compte.setMinutesRestantes(minutesRestantes);
                daoCompte.update(compte);
                //Mise à jour des points dans demande_equipe (pour calculer le total des points de l'équipe)
                if(compte.getIdEquipe() > 0){
                    DemandeEquipeDAO demandeDAO = new DemandeEquipeDAO(cnx);
                    DemandeEquipe demande = new DemandeEquipe();
                    demande = demandeDAO.findByIdCompteEquipe(idCompte, compte.getIdEquipe());
                    int pointDemandeEquipe = demande.getPoint() + pointLecture;
                    demande.setPoint(pointDemandeEquipe);
                    demandeDAO.update(demande);

                    System.out.println("Une lecture a été créée avec succès");

                }

                else
                    System.out.println("Problème de création de la lecture");
              }

            
            }
            catch(ClassNotFoundException e){
                System.out.println("Erreur dans le chargement du pilote :"+ e);
                //request.setAttribute("vue", "lecture.jsp");
                return "*.do?tache=afficherPageGestionLecture";
            } catch (SQLException ex) {
                Logger.getLogger(EffectuerCreationLectureAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }
        return "*.do?tache=afficherPageGestionLecture";
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
