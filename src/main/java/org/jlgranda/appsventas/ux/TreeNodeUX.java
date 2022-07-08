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
package org.jlgranda.appsventas.ux;

import com.fasterxml.jackson.core.TreeNode;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author usuario
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class TreeNodeUX {
    
    private String label;
    private Object data;
    private String icon;
    private String expandedIcon = "pi pi-folder-open";
    private String collapsedIcon = "pi pi-folder";
    private List<TreeNode> children;
    private Boolean leaf = Boolean.TRUE;
    private Boolean expanded = Boolean.TRUE;
    private String type;
    private TreeNode parent;
    private Boolean partialSelected = Boolean.TRUE;
    private String styleClass;
    private Boolean draggable = Boolean.TRUE;
    private Boolean droppable = Boolean.TRUE;
    private Boolean selectable = Boolean.TRUE;
    private String key;
    
    public void addChildren(TreeNode node) {
        if (children == null){
            this.children = new ArrayList<>(); //Sólo si es necesario
        }
        this.children.add(node); //Agrega un item a la lista de items
    }
    
}