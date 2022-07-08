/*
 * Copyright (C) 2022 usuario
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jlgranda.appsventas.api.controller.app;

import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.List;
import org.jlgranda.appsventas.Api;
import org.jlgranda.appsventas.dto.UserData;
import org.jlgranda.appsventas.ux.TreeTableUX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.jlgranda.appsventas.ux.TreeUX;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author usuario
 */
@RestController()
@RequestMapping(path = "/tareas")
public class TareaController {

//    private TareaService tareaService;
//    private PerfilTareaService perfilTareaService;
    private final String ignoreProperties;

    @Autowired
    public TareaController(
            //            TareaService tareaService,
            //            PerfilTareaService perfilTareaService,
           @Value("${appsventas.persistence.ignore_properties}") String ignoreProperties
    ) {
//        this.tareaService = tareaService;
//        this.perfilTareaService = perfilTareaService;
        this.ignoreProperties = ignoreProperties;
    }
    
    @GetMapping("/raices")
    public ResponseEntity listarTodasRaices(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("tareas/raices/", user.getId());
        MenuItem menuItem = null;
        return ResponseEntity.ok(new MenuItem[]{menuItem});
    }

    /**
     * Lista de tareas asignadas por usuario en formato MenuItem para
     * renderizado del menú de usuario
     *
     * @param user
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping
    public ResponseEntity listarTodosPorUsuario(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        //Mapa de tareas y sus hijos
        List<MenuItem> menuItems = new ArrayList<>();
        Api.imprimirGetLogAuditoria("tareas/", user.getId());
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/modulos")
    public ResponseEntity listarRaices(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("tareas/modulos", user.getId());
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/arbolsimple")
    public ResponseEntity construirArbolSimple(
            @AuthenticationPrincipal UserData user
    ) {
        TreeUX arbol = new TreeUX();
        Api.imprimirGetLogAuditoria("tareas/arbolsimple/", user.getId());
        return ResponseEntity.ok(arbol);
    }

    @GetMapping("/todos")
    public ResponseEntity listarTodos(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("tareas/todos/", user.getId());
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/asignables")
    public ResponseEntity listarAsiginables(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        Api.imprimirGetLogAuditoria("tareas/todos/", user.getId());
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("padre/{id}")
    public ResponseEntity encontrarPorId(
            @AuthenticationPrincipal UserData user,
            @PathVariable("id") Long id
    ) {
        Api.imprimirGetLogAuditoria("tareas/padre/", user.getId());
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity encontrarPorUUID(
            @AuthenticationPrincipal UserData user,
            @PathVariable("uuid") String uuid
    ) {
        Api.imprimirGetLogAuditoria("tareas/uuid/", user.getId());
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/arbol")
    public ResponseEntity construirArbol(
            @AuthenticationPrincipal UserData user
    ) {
        TreeTableUX treeTable = new TreeTableUX();
        Api.imprimirGetLogAuditoria("tareas/arbol/", user.getId());
        return ResponseEntity.ok(treeTable);
    }

    /**
     * Lista de tareas asignadas por usuario como objetos Tarea con información
     * de permisos consultada por enlace, es la entrada para validar la
     * activación de botones.
     *
     * @param user
     * @param routerlink
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping("/routerlink/{routerlink}")
    public ResponseEntity listarTareasUsuarioRouterLink(
            @AuthenticationPrincipal UserData user,
            @PathVariable("routerlink") String routerlink,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        Api.imprimirGetLogAuditoria("tareas/routelink/", user.getId());

        routerlink = routerlink.replace(':', '/'); //cambiar caracteres
        routerlink = routerlink.replace("/fazil/", ""); //Quitar raíz /fazil/

        routerlink = routerlink + "/";
        String routerLinkP1 = net.tecnopro.util.Strings.extractBefore(routerlink, "/");
        String routerLinkP2 = routerlink.substring(routerLinkP1.length() + 1);
        routerLinkP2 = net.tecnopro.util.Strings.extractBefore(routerLinkP2, "/");
        if (routerLinkP2.length() >= 1) {
            routerlink = routerlink.substring(0, (routerLinkP1.length() + routerLinkP2.length() + 1));
        } else {
            routerlink = routerLinkP1;
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");
        System.out.println("routerlink: " + routerlink);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");
        
        return ResponseEntity.ok(new ArrayList<>());
    }

    /**
     * Lista de tareas asignadas por usuario como objetos Tarea con información
     * de permisos consultada por enlace, es la entrada para validar la
     * activación de botones.
     *
     * @param user
     * @param offset
     * @param limit
     * @return
     */
    @GetMapping("/permisos")
    public ResponseEntity listarTareasUsuarioPermisos(
            @AuthenticationPrincipal UserData user,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {

        Api.imprimirGetLogAuditoria("tareas/permisos", user.getId());
        return ResponseEntity.ok(new ArrayList<>());
    }

}
