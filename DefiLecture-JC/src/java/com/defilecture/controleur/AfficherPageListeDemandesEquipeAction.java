/**
 * This file is part of DefiLecture.
 *
 * <p>DefiLecture is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>DefiLecture is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with DefiLecture. If
 * not, see <http://www.gnu.org/licenses/>.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.defilecture.controleur;

/**
 * @author Joel
 * @author Mikaël Nadeau
 * @author Mikaël
 * @author Mikaël Nadeau
 */
public class AfficherPageListeDemandesEquipeAction extends Action {
  @Override
  public String execute() {
    if (session.getAttribute("connecte") == null) {
      request.setAttribute("vue", "pageConnexion");
    } else {
      request.setAttribute("vue", "pageListeDemandesEquipe.jsp");
    }
    return "/index.jsp";
  }
}
