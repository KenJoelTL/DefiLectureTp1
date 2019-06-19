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
package com.defilecture.controleur;

import jdbc.Config;
import jdbc.Connexion;
import com.defilecture.modele.Compte;
import com.defilecture.modele.CompteDAO;
import com.util.Util;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel
 * @author Mikaël Nadeau
 */
public class EffectuerConnexionAction extends Action implements RequirePRGAction, DataSender {

    private HashMap data;

    @Override
    public String execute() {
        String action = "*.do?tache=afficherPageConnexion";
                            data.put("echecConnexion", "L'identifiant et/ou le mot de passe entré est invalide");

        if (session.getAttribute("connecte") == null
                && request.getParameter("identifiant") != null
                && request.getParameter("motPasse") != null) {
            String identifiant = request.getParameter("identifiant"),
                    motPasse = org.apache.commons.codec.digest.DigestUtils.sha1Hex(request.getParameter("motPasse"));
            try {
                CompteDAO dao
                        = new CompteDAO(Connexion.startConnection(Config.DB_USER, Config.DB_PWD, Config.URL, Config.DRIVER));
                Compte compte
                        = dao.findByIdentifiantMotPasse(identifiant, motPasse);

                // On vérifie s'il y a un résultat    
                if (compte != null) {
                    session = request.getSession(true);
                    session.setAttribute("connecte", compte.getIdCompte());
                    session.setAttribute("role", compte.getRole());
                    session.setAttribute("currentId", compte.getIdCompte());
                    action = "*.do?tache=afficherTableauScores";
                } else {
                    data.put("echecConnexion", "L'identifiant et/ou le mot de passe entré est invalide");
                    data.put("identifiant", Util.toUTF8(identifiant));
                }
            } catch (SQLException ex) {
                Logger.getLogger(EffectuerConnexionAction.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                Connexion.close();
            }
        }
        return action;
    }

    @Override
    public void setData(Map<String, Object> data) {
        this.data = (HashMap) data;
    }

}
